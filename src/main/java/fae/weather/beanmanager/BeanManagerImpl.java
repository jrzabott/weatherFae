package fae.weather.beanmanager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * An implementation of BeanContainer.
 * It uses a map to hold the beans.
 * The underlying map is synchronized to allow concurrent access.
 *
 * @see BeanManager
 */
public class BeanManagerImpl implements BeanManager {
    private static final Logger LOGGER = LogManager.getLogger(BeanManagerImpl.class);
    /**
     * The map of beans.
     * This map is synchronized to allow concurrent access.
     */
    private final Map<String, Object> beans = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void addBean(Object bean) {
        if (Objects.isNull(bean)) {
            LOGGER.warn("Trying to add a null bean to the container.");
            return;
        }
        beans.putIfAbsent(bean.getClass().getName(), bean);
    }

    @Override
    public void removeBean(Object bean) {
        if (Objects.isNull(bean)) {
            LOGGER.warn("Trying to remove a null bean from the container.");
            return;
        }
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

    /**
     * Technically, in this implementation references are removed from the underlying container storage.
     */
    @Override
    public void invalidateAllBeans() {
        String beanCount = String.valueOf(beans.size());
        LOGGER.debug("Invalidating all <"+  beanCount + "> beans.");
        beans.clear();
        LOGGER.debug("All <" + beanCount + "> beans invalidated.");
    }
}
