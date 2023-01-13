package dbg;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.StackFrame;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;

public class Stack extends Command {

    private VirtualMachine vm;

    public Stack(VirtualMachine vm) {
        this.vm = vm;
    }

    @Override
    public void run() {
        StackFrame frame = null;
        try {

            frame = ((LocatableEvent) getEvent()).thread().frame(0);
            for (StackFrame sframe : frame.thread().frames()) {
                System.out.println("stack :" + sframe.location().method());
            }
        } catch (IncompatibleThreadStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isLocked() {
        return true;
    }
}
