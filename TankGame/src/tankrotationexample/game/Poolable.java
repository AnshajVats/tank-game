package tankrotationexample.game;

public interface Poolable {

    public void initObject(float x, float y);

    public void initObject(float x, float y, float angle);

    public void resetObject();
}
