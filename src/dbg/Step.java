package dbg;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.StepEvent;

import java.util.Scanner;

public class Step extends Command {

    VirtualMachine vm;

    public Step(VirtualMachine vm) {
        this.vm = vm;
    }

    @Override
    public void run() {
        return;
        //getEvent().request().disable();

    }
}
