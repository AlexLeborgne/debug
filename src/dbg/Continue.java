package dbg;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.Event;

public class Continue extends Command {

    VirtualMachine vm;

    public Continue(VirtualMachine vm) {this.vm = vm;}

    @Override
    public void run() {
        getEvent().request().disable();
    }

    @Override
    public boolean isLocked() {
        return false;
    }
}
