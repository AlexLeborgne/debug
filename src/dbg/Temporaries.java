package dbg;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;

import java.util.Map;

public class Temporaries extends Command {

    VirtualMachine vm;

    public Temporaries(VirtualMachine vm) {this.vm = vm;}

    @Override
    public void run() {
        StackFrame frame = null;
        Map<LocalVariable, Value> variables = null;
        try {
            frame = ((LocatableEvent) getEvent()).thread().frame(0);
            variables = frame.getValues(frame.visibleVariables());
        } catch (IncompatibleThreadStateException e) {
            e.printStackTrace();
        } catch (AbsentInformationException e) {
            e.printStackTrace();
        }
        for(Map.Entry mp: variables.entrySet()){
            System.out.println("\n"+ mp.getKey()+ " ---> "+mp.getValue());
        }
    }
}
