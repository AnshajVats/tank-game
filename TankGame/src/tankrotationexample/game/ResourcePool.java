package tankrotationexample.game;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import tankrotationexample.ResourceManager;

public class ResourcePool<G extends Poolable> {

    private static final int INIT_CAPACITY = 100;
    private final String resourceName;
    public final Class<G> resourceClass;
    private final ArrayList<G> resources;

    public ResourcePool(String resourceName, Class<G> resourceClass) {
        this.resourceName = resourceName;
        this.resourceClass = resourceClass;
        this.resources = new ArrayList<>(INIT_CAPACITY);
    }

    public ResourcePool(String resourceName, Class<G> resourceClass, int initCapacity) {
        this.resourceName = resourceName;
        this.resourceClass = resourceClass;
        this.resources = new ArrayList<>(initCapacity);
    }

    public G removeFromPool() {
        if (resources.isEmpty()) {
            this.refillPool();
        }
        return resources.removeLast();
    }

    public void addToPool(G resource) {
        resources.addLast(resource);
    }

    public void refillPool() {
        this.fillPool(INIT_CAPACITY);
    }

    public ResourcePool<G> fillPool(int size) {
        BufferedImage img = ResourceManager.getSprite(this.resourceName);
        for (int i = 0; i < size; i++) {
            try {
                var g = this.resourceClass.getDeclaredConstructor(BufferedImage.class).newInstance(img);
                this.addToPool(g);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }
}
