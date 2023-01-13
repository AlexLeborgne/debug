package dbg;

import com.sun.jdi.*;
import com.sun.jdi.event.LocatableEvent;

import java.util.List;

public class Argument extends Command{

    private VirtualMachine vm;

    public Argument(VirtualMachine vm) {
        this.vm = vm;
    }

    @Override
    public void run() {
        List<LocalVariable> frame = null;
        try {
            frame = ((LocatableEvent) getEvent()).thread().frame(0).location().method().arguments();
            for (LocalVariable argument : frame ) {
                System.out.println("argument : " + argument.name() + " --> " + argument.type());
            }
        } catch (IncompatibleThreadStateException e) {
            e.printStackTrace();
        } catch (AbsentInformationException e) {
            e.printStackTrace();
        } catch (ClassNotLoadedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isLocked() {
        return true;
    }
}
