package cn.rectcircle.learn.use;

/**
 * @author rectcircle
 */
public class MyThreadEvent {
    private MyThreadEventType type;

    public MyThreadEvent(MyThreadEventType type) {
        this.setType(type);
    }

    public MyThreadEventType getType() {
        return type;
    }

    public void setType(MyThreadEventType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MyThreadEvent [type=" + type + "]";
    }
}