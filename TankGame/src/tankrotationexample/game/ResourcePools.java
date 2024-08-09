package tankrotationexample.game;

import java.util.HashMap;
import java.util.Map;

public class ResourcePools {

    private static final Map<String, ResourcePool<? extends Poolable>> pools = new HashMap<>();


    public static void addPool(String name, ResourcePool<? extends Poolable> pool) {
        pools.put(name, pool);
    }

    public static Poolable getPool(String name) {
        return ResourcePools.pools.get(name).removeFromPool();
    }
}
