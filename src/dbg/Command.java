package dbg;

import com.sun.jdi.event.Event;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    private com.sun.jdi.event.Event event;

    private ArrayList<String> parameterList;

    public Command() {
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public ArrayList<String> getParameterList() {
        return parameterList;
    }

    public void setParameterList(ArrayList<String> parameterList) {
        this.parameterList = parameterList;
    }

    public void run() {}

    public boolean isLocked() {
        return false;
    }


}
