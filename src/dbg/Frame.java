package dbg;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.StackFrame;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;
import com.sun.jdi.request.StepRequest;

public class Frame extends Command{

    private VirtualMachine vm;

    public Frame(VirtualMachine vm) {
        this.vm = vm;
    }

    @Override
    public void run() {
        StackFrame frame = null;
        try
        {
            frame = ((LocatableEvent) getEvent()).thread().frame(0);
        } catch (IncompatibleThreadStateException e) {
            e.printStackTrace();
        }
        System.out.println("frame: " + frame);

    }

    @Override
    public boolean isLocked() {
        return true;
    }
}
