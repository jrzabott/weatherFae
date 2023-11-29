package fae.weather.beancontainer;

/**
 * Will be used to hold all the beans.
 * Beans shoudl be added to this container at startup.
 * Beans will likely be unique, so we will use a map to hold them.
 */
public interface BeanContainer {
    /**
     * Add a bean to the container.
     *
     * @param bean - the bean to add
     */
    void addBean(Object bean);

    /**
     * Remove a bean from the container.
     *
     * @param bean - the bean to remove
     */
    void removeBean(Object bean);

    /**
     * Get a bean from the container.
     *
     * @param beanClass - the class of the bean to get
     * @return the bean
     */
    <T> T getBean(Class<T> beanClass);

    /**
     * Get a bean from the container.
     *
     * @param beanName - the name of the bean to get
     * @return the bean
     */
    Object getBean(String beanName);

    /**
     * Get all beans from the container.
     *
     * @return all beans
     */
    Object[] getAllBeans();

    /**
     * Invalidate all beans' references.
     * This will allow the garbage collector to collect them.
     */
    void invalidateAllBeans();
}
