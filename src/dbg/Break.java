package dbg;

import com.sun.jdi.*;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.BreakpointRequest;

import java.util.ArrayList;
import java.util.List;

public class Break extends Command {

    VirtualMachine vm;
    ArrayList<String> parameterList;
    ReferenceType target;

    public Break(VirtualMachine vm) {
        this.vm = vm;
        this.parameterList = new ArrayList<>();
    }

    @Override
    public void run() {
        /*for (ReferenceType targetClass : vm.allClasses()) {
            if (targetClass.name().equals(this.parameterList.get(0))) {
                try {
                    Location location = targetClass.locationsOfLine(Integer.parseInt(this.parameterList.get(1))).get(0);
                    BreakpointRequest bpReq = vm.eventRequestManager().createBreakpointRequest(location);
                    bpReq.enable();
                    System.out.println("BreakPoint created : "+ location.lineNumber());
                } catch (AbsentInformationException e) {
                    e.printStackTrace();
                }
            } else if (this.parameterList.get(0).equals("dbg.Power")){
                try {
                    target = vm.classesByName(this.parameterList.get(0)).get(0);
                    Location location = target.locationsOfLine(Integer.parseInt(this.parameterList.get(1))).get(0);
                    BreakpointRequest bpReq = vm.eventRequestManager().createBreakpointRequest(location); // a voir ou le mettre
                    bpReq.enable();
                    System.out.println("BreakPoint created : "+ location.lineNumber());
                } catch (AbsentInformationException e) {
                    e.printStackTrace();
                }
            }
        }*/

    }

    @Override
    public boolean isLocked() {
        return true;
    }

    @Override
    public void setParameterList(ArrayList<String> parameterList){
        this.parameterList = parameterList;
    }

}
