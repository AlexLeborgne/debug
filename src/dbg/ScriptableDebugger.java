package dbg;

import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.*;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.StepRequest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;

public class ScriptableDebugger {

    private Class debugClass;
    private Class powerClass;
    private List<Class> classes = new ArrayList<>();
    private VirtualMachine vm;
    Map<String, Command> commands = new HashMap<>();
    Boolean prepare = true;
    Command command;

    public VirtualMachine connectAndLaunchVM() throws IOException, IllegalConnectorArgumentsException, VMStartException {
        LaunchingConnector launchingConnector = Bootstrap.virtualMachineManager().defaultConnector();
        Map<String, Connector.Argument> arguments = launchingConnector.defaultArguments();
        arguments.get("main").setValue(JDISimpleDebuggee.class.getName());
        VirtualMachine vm = launchingConnector.launch(arguments);

        return vm;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    public void attachTo(List<Class> classes) {
        //this.debugClass = classes;

        try {
            vm = connectAndLaunchVM();
            enableClassPrepareRequest(vm);

            this.debugClass = classes.get(0);
            this.powerClass = classes.get(1);

            startCommand();
            startDebugger();



        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalConnectorArgumentsException e) {
            e.printStackTrace();
        } catch (VMStartException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        } catch (VMDisconnectedException e) {
            System.out.println("Virtual Machine is disconnected: " + e.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enableClassPrepareRequest(VirtualMachine vm) {
        ClassPrepareRequest classPrepareRequest = vm.eventRequestManager().createClassPrepareRequest();
        classPrepareRequest.addClassFilter(JDISimpleDebuggee.class.getName());
        classPrepareRequest.enable();
        System.out.println("class : " + JDISimpleDebuggee.class.getName());
        ClassPrepareRequest classPrepareRequest1 = vm.eventRequestManager().createClassPrepareRequest();
        classPrepareRequest1.addClassFilter(Power.class.getName());
        classPrepareRequest1.enable();
        System.out.println("class : " + Power.class.getName());

        classes.add(JDISimpleDebuggee.class);
        classes.add(Power.class);
        setClasses(classes);
    }

    public void setBreakPoint(String className, int lineNumber) throws AbsentInformationException {
        for (ReferenceType targetClass : vm.allClasses()) {
            if (targetClass.name().equals(className)) {
                Location location = targetClass.locationsOfLine(lineNumber).get(0);
                BreakpointRequest bpReq = vm.eventRequestManager().createBreakpointRequest(location);
                bpReq.enable();
            }
        }
    }

    public void enableStepRequest(LocatableEvent event){
        StepRequest stepRequest = vm.eventRequestManager().createStepRequest(event.thread(),
                StepRequest.STEP_LINE,
                StepRequest.STEP_OVER);
        stepRequest.disable();
        stepRequest.enable();
    }

    public void startCommand(){
        commands.put("step",  new Step(vm));
        commands.put("step-over", new StepOver(vm));
        commands.put("frame", new Frame(vm));
        commands.put("stack", new Stack(vm));
        commands.put("method", new Method(vm));
        commands.put("argument", new Argument(vm));
        commands.put("print-var", new PrintVar(vm));
        commands.put("continue", new Continue(vm));
        commands.put("temporaries", new Temporaries(vm));
        commands.put("receiver", new Receiver(vm));
        commands.put("break", new Break(vm));
    }

    public void read(Event event, ArrayList<String> parameterList){
        parameterList = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        System.out.print("> ");

        String str = sc.nextLine();
        String arr[] = str.split(" ", 3);
        String commandStr = arr[0];
        command = commands.get(commandStr);
        command.setEvent(event);
        String firstParameter = null;
        String secondParameter = null;

        try {
            firstParameter = arr[1];
        } catch (ArrayIndexOutOfBoundsException e) {

        }
        try {
            secondParameter = arr[2];
        } catch (ArrayIndexOutOfBoundsException e) {

        }

        parameterList.add(0, firstParameter);
        parameterList.add(1, secondParameter);
        command.setParameterList(parameterList);
        System.out.println(command);
        if (command != null){
            command.run();

            if ( command.isLocked() && !commandStr.equals("break")){
                read(event, parameterList);
            } else {
                return;
            }
        }
    }

    public Location getLocation(Event event){
        Location location = null;
        for (ReferenceType targetClass : vm.allClasses()) {
            if (targetClass.name().equals(command.getParameterList().get(0))) { // command null
                try {
                    location = targetClass.locationsOfLine(Integer.parseInt(command.getParameterList().get(1))).get(0);
                } catch (AbsentInformationException e) {
                    e.printStackTrace();
                }
            } else if (command.getParameterList().get(0).equals("dbg.Power")){
                try {
                    ReferenceType target = vm.classesByName(command.getParameterList().get(0)).get(0);
                    location = target.locationsOfLine(Integer.parseInt(command.getParameterList().get(1))).get(0);
                } catch (AbsentInformationException e) {
                    e.printStackTrace();
                }
            }
        }
        return location;
    }

    public void startDebugger() throws Exception {
        EventSet eventSet = null;
        while ((eventSet = vm.eventQueue().remove()) != null) {
            for (Event event : eventSet) {
                // Disconnection even interception
                if (event instanceof VMDisconnectEvent) {

                    // Get debuggee VM's input stream
                    System.out.println("Debuggee output ===");
                    InputStreamReader reader = new InputStreamReader(vm.process().getInputStream());
                    OutputStreamWriter writer = new OutputStreamWriter (System.out);

                    // Transfer debuggee VM's data to console
                    char[] buf = new char[vm.process().getInputStream().available()];
                    reader.read(buf);
                    writer.write(buf);
                    writer.flush();

                    System.out.println(" === End of program.");
                    return;
                }

                switch (event) {
                    case ClassPrepareEvent cpe -> {
                        //setBreakPoint(debugClass.getName(), 6);

                        read(cpe, null);

                        Location location = getLocation(cpe);
                        BreakpointRequest bpReq = vm.eventRequestManager().createBreakpointRequest(location);
                        bpReq.enable();
                        System.out.println("BreakPoint created : " + location.lineNumber());

                        //setBreakPoint(debugClass.getName(),10);
                        //setBreakPoint(debugClass.getName(),10);
                    }
                    case BreakpointEvent bpe -> {
                        enableStepRequest((BreakpointEvent) event);
                        read(bpe, null);

                    }
                    case StepEvent se -> {
                        read(se, null);
                    }
                    case LocatableEvent sr -> {
                        read(sr, null);
                    }
                    default -> {

                    }
                }

                System.out.println(event.toString());
                vm.resume();

            }
        }
    }

}
