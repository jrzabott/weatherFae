package fae.weather.beancontainer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An implementation of BeanContainer.
 * It uses a map to hold the beans.
 * The underlying map is synchronized to allow concurrent access.
 *
 * @see BeanContainer
 */
public class BeanContainerImpl implements BeanContainer {
    /**
     * The map of beans.
     * This map is synchronized to allow concurrent access.
     */
    private final Map<String, Object> beans = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void addBean(Object bean) {
        beans.putIfAbsent(bean.getClass().getName(), bean);
    }

    @Override
    public void removeBean(Object bean) {
        beans.remove(bean.getClass().getName());
    }

    @Override
    public <T> T getBean(Class<T> beanClass) {
        return Optional.ofNullable(beans.get(beanClass.getName()))
                .map(beanClass::cast)
                .orElse(null);
    }

    @Override
    public Object getBean(String beanName) {
        return beans.get(beanName);
    }

    @Override
    public Object[] getAllBeans() {
        return beans.values().toArray();
    }
}
