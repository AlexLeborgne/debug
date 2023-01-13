package dbg;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;

import java.util.List;
import java.util.Map;

public class Receiver extends Command{

    private VirtualMachine vm;

    public Receiver(VirtualMachine vm) {
        this.vm = vm;
    }

    @Override
    public void run() {
        ObjectReference objectReference = null;
        try {
            objectReference = ((LocatableEvent) getEvent()).thread().frame(0).thisObject();
            System.out.println("receiver : " + objectReference);
        } catch (IncompatibleThreadStateException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean isLocked() {
        return true;
    }
}
