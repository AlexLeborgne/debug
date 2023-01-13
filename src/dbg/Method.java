package dbg;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.LocatableEvent;

public class Method extends Command{

    private VirtualMachine vm;

    public Method(VirtualMachine vm) {
        this.vm = vm;
    }

    @Override
    public void run() {
        com.sun.jdi.Method frame = null;
        try {
            frame = ((LocatableEvent) getEvent()).thread().frame(0).location().method();
            System.out.println("method :" + frame);
        } catch (IncompatibleThreadStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isLocked() {
        return true;
    }
}
