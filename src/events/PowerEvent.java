
package events;


public class PowerEvent {
    private final boolean state;
    
    public PowerEvent(boolean state)
    {
        this.state = state;
    }
    
    public boolean getState() {
        return state;
    }
}
