package dbg;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;

public class PrintVar extends Command {

    private VirtualMachine vm;


    public PrintVar(VirtualMachine vm) {
        this.vm = vm;
    }

    @Override
    public void run() {
        StackFrame frame = null;
        Value variable = null;
        try {
            frame = ((LocatableEvent) getEvent()).thread().frame(0);
            variable = frame.getValue(frame.visibleVariableByName(this.getParameterList().get(0)));
            System.out.println("value : " + variable);
        } catch (IncompatibleThreadStateException e) {
            e.printStackTrace();
        } catch (AbsentInformationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isLocked() {
        return true;
    }
}
