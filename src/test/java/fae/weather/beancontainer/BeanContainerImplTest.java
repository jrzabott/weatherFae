package fae.weather.beancontainer;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeanContainerImplTest {

    private BeanContainer beanContainer;

    @BeforeEach
    void setUp() {
        beanContainer = new BeanContainerImpl();
    }

    @Test
    void addBean() {
        Object bean = new Object();
        beanContainer.addBean(bean);
        assertSame(bean, beanContainer.getBean(Object.class));
    }

    @Test
    void addBeanTwice() {
        Object bean = new Object();
        beanContainer.addBean(bean);
        beanContainer.addBean(bean);
        assertSame(bean, beanContainer.getBean(Object.class));
    }

    @Test
    void addBeanNull() {
        Assertions.assertThatThrownBy(() -> beanContainer.addBean(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void addTwoBeansOfSameType() {
        Object bean1 = new Object();
        Object bean2 = new Object();
        beanContainer.addBean(bean1);
        beanContainer.addBean(bean2);
        assertNotEquals(bean1, bean2);
        assertSame(bean1, beanContainer.getBean(Object.class));

        // BeanContainerImpl will hold only one bean of a given type.
        assertNotSame(bean2, beanContainer.getBean(Object.class));
    }
    
    @Test
    void addTwoBeansOfDifferentTypes() {
        Object bean1 = new Object();
        String bean2 = StringUtils.EMPTY;
        beanContainer.addBean(bean1);
        beanContainer.addBean(bean2);
        assertNotEquals(bean1, bean2);
        assertSame(bean1, beanContainer.getBean(bean1.getClass()));
        assertSame(bean2, beanContainer.getBean(bean2.getClass()));
    }

    @Test
    void removeBean() {
        Object bean = new Object();
        beanContainer.addBean(bean);
        beanContainer.removeBean(bean);
        assertNull(beanContainer.getBean(Object.class));
    }

    @Test
    void removeBeanNull() {
        Assertions.assertThatThrownBy(() -> beanContainer.removeBean(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void removeBeanNotInContainer() {
        Object bean = new Object();
        beanContainer.removeBean(bean);
        assertNull(beanContainer.getBean(Object.class));
    }

    @Test
    void removeBeanTwice() {
        Object bean = new Object();
        beanContainer.addBean(bean);
        beanContainer.removeBean(bean);
        beanContainer.removeBean(bean);
        assertNull(beanContainer.getBean(Object.class));
    }

    @Test
    void removeTwoBeansOfSameType() {
        Object bean1 = new Object();
        Object bean2 = new Object();
        beanContainer.addBean(bean1);
        beanContainer.addBean(bean2);
        beanContainer.removeBean(bean1);
        assertNull(beanContainer.getBean(bean1.getClass()));
        assertNull(beanContainer.getBean(bean2.getClass()));
    }
    
    @Test
    void removeTwoBeansOfDifferentTypes() {
        Object bean1 = new Object();
        String bean2 = StringUtils.EMPTY;
        beanContainer.addBean(bean1);
        beanContainer.addBean(bean2);
        beanContainer.removeBean(bean1);
        assertNull(beanContainer.getBean(bean1.getClass()));
        assertSame(bean2, beanContainer.getBean(bean2.getClass()));
    }

    @Test
    void getBean() {
        Object bean = new Object();
        beanContainer.addBean(bean);
        assertSame(bean, beanContainer.getBean(Object.class));
    }
    
    @Test
    void getBeanNull() {
        final Class<Object> nullClass = null;
        Assertions.assertThatThrownBy(() -> beanContainer.getBean(nullClass))
                .isInstanceOf(NullPointerException.class);
    }
    
    @Test
    void getBeanNotInContainer() {
        assertNull(beanContainer.getBean(Object.class));
    }
    
    @Test
    void getBeanTwice() {
        Object bean = new Object();
        beanContainer.addBean(bean);
        assertSame(bean, beanContainer.getBean(Object.class));
        assertSame(bean, beanContainer.getBean(Object.class));
    }
    
    @Test
    void getBeanTwoBeans() {
        Object bean1 = new Object();
        Object bean2 = new Object();
        beanContainer.addBean(bean1);
        beanContainer.addBean(bean2);
        assertNotEquals(bean1, bean2);
        assertSame(bean1, beanContainer.getBean(bean1.getClass()));
        
        // BeanContainerImpl will hold only one bean of a given type, it also works as FIFO. The first bean added will 
        // be the stored bean.
        assertSame(bean1, beanContainer.getBean(bean2.getClass()));
    }

    @Test
    void getBeanByName() {
        Object bean = new Object();
        beanContainer.addBean(bean);
        assertSame(bean, beanContainer.getBean(Object.class.getName()));
    }
    @Test
    void getBeanByNameNull() {
        assertNull(beanContainer.getBean((String) null));
    }
    @Test
    void getBeanByNameNotInContainer() {
        assertNull(beanContainer.getBean(Object.class.getName()));
    }
    
    @Test
    void getBeanByNameTwice() {
        Object bean = new Object();
        beanContainer.addBean(bean);
        assertSame(bean, beanContainer.getBean(Object.class.getName()));
    }
    
    @Test
    void getBeanByNameTwoBeans() {
        Object bean1 = new Object();
        Object bean2 = new Object();
        beanContainer.addBean(bean1);
        beanContainer.addBean(bean2);
        assertNotEquals(bean1, bean2);
        assertSame(bean1, beanContainer.getBean(bean1.getClass().getName()));
        
        // BeanContainerImpl will hold only one bean of a given type, it also works as FIFO. The first bean added will 
        // be the stored bean.
        assertSame(bean1, beanContainer.getBean(bean2.getClass().getName()));
    }
    
    @Test  
    void getAllBeans() {
        Object bean1 = new Object();
        Object bean2 = new Object();
        beanContainer.addBean(bean1);
        beanContainer.addBean(bean2);
        Object[] beans = beanContainer.getAllBeans();
        assertEquals(1, beans.length);
        assertSame(bean1, beans[0]);
    }
    
    @Test
    void getAllBeansEmpty() {
        Object[] beans = beanContainer.getAllBeans();
        assertEquals(0, beans.length);
    }
    
    @Test
    void getAllBeansTwice() {
        Object bean1 = new Object();
        Object bean2 = new Object();
        beanContainer.addBean(bean1);
        beanContainer.addBean(bean2);
        Object[] beans = beanContainer.getAllBeans();
        assertEquals(1, beans.length);
        assertSame(bean1, beans[0]);
        beans = beanContainer.getAllBeans();
        assertEquals(1, beans.length);
        assertSame(bean1, beans[0]);
    }
    
    @Test
    void getAllBeansTwoBeansWithSameType() {
        Object bean1 = new Object();
        Object bean2 = new Object();
        beanContainer.addBean(bean1);
        beanContainer.addBean(bean2);
        Object[] beans = beanContainer.getAllBeans();
        assertEquals(1, beans.length);
        assertSame(bean1, beans[0]);
        beans = beanContainer.getAllBeans();
        assertEquals(1, beans.length);
        assertSame(bean1, beans[0]);
    }

    @Test
    void getAllBeanTwoBeansWithDifferentTypes() {
        Object bean1 = new Object();
        String bean2 = StringUtils.EMPTY;
        beanContainer.addBean(bean1);
        beanContainer.addBean(bean2);
        Object[] beans = beanContainer.getAllBeans();
        assertEquals(2, beans.length);
        assertSame(bean1, beans[0]);
        assertSame(bean2, beans[1]);
        beans = beanContainer.getAllBeans();
        assertEquals(2, beans.length);
        assertSame(bean1, beans[0]);
        assertSame(bean2, beans[1]);
    }
    
    @Test
    void getAllBeansTwoBeansRemoveOne() {
        Object bean1 = new Object();
        String bean2 = StringUtils.EMPTY;
        beanContainer.addBean(bean1);
        beanContainer.addBean(bean2);
        beanContainer.removeBean(bean1);
        Object[] beans = beanContainer.getAllBeans();
        assertEquals(1, beans.length);
        assertSame(bean2, beans[0]);
    }
    
    @Test
    void getAllBeansTwoBeansRemoveTwo() {
        Object bean1 = new Object();
        String bean2 = StringUtils.EMPTY;
        beanContainer.addBean(bean1);
        beanContainer.addBean(bean2);
        beanContainer.removeBean(bean1);
        beanContainer.removeBean(bean2);
        Object[] beans = beanContainer.getAllBeans();
        assertEquals(0, beans.length);
    }
    
    @Test
    void getAllBeansTwoBeansRemoveTwoAddOne() {
        Object bean1 = new Object();
        String bean2 = StringUtils.EMPTY;
        beanContainer.addBean(bean1);
        beanContainer.addBean(bean2);
        beanContainer.removeBean(bean1);
        beanContainer.removeBean(bean2);
        beanContainer.addBean(bean1);
        Object[] beans = beanContainer.getAllBeans();
        assertEquals(1, beans.length);
        assertSame(bean1, beans[0]);
    }

    @Test
    void getAllBeansTwoBeansWithDifferentTypeRemoveTwoAddTwo() {
        Object bean1 = new Object();
        String bean2 = StringUtils.EMPTY;
        beanContainer.addBean(bean1);
        beanContainer.addBean(bean2);
        beanContainer.removeBean(bean1);
        beanContainer.removeBean(bean2);
        beanContainer.addBean(bean1);
        beanContainer.addBean(bean2);
        Object[] beans = beanContainer.getAllBeans();
        assertEquals(2, beans.length);
        assertSame(bean1, beans[0]);
        assertSame(bean2, beans[1]);
    }

    @Test
    void testAddBeanMultipleThreadsWillNotThrowException() {
        assertDoesNotThrow(() -> {
            Thread thread1 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    beanContainer.addBean(new Object());
                }
            });
            Thread thread2 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    beanContainer.addBean(new Object());
                }
            });
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
        });
    }

    @Test
    void testRemoveBeanMultipleThreadsWillNotThrowException() {
        assertDoesNotThrow(() -> {
            Object bean = new Object();
            beanContainer.addBean(bean);
            Thread thread1 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    beanContainer.removeBean(bean);
                }
            });
            Thread thread2 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    beanContainer.removeBean(bean);
                }
            });
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
        });
    }

    @Test
    void testGetBeanMultipleThreadsWillNotThrowException() {
        assertDoesNotThrow(() -> {
            Object bean = new Object();
            beanContainer.addBean(bean);
            Thread thread1 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    beanContainer.getBean(Object.class);
                }
            });
            Thread thread2 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    beanContainer.getBean(Object.class);
                }
            });
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
        });
    }

    @Test
    void testAddBeanMultipleThreadsWillAddTheFirstBeanOnly() throws InterruptedException {
        Object bean1 = new Object();
        Object bean2 = new Object();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                beanContainer.addBean(bean1);
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                beanContainer.addBean(bean2);
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        assertSame(bean1, beanContainer.getBean(Object.class));
    }

    @Test
    void testRemoveBeanMultipleThreadsWillRemoveTheBeanOnly() throws InterruptedException {
        Object bean = new Object();
        beanContainer.addBean(bean);
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                beanContainer.removeBean(bean);
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                beanContainer.removeBean(bean);
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        assertNull(beanContainer.getBean(Object.class));
    }

    @Test
    void testGetBeanMultipleThreadsWillReturnTheBeanOnly() throws InterruptedException {
        Object bean = new Object();
        beanContainer.addBean(bean);
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                beanContainer.getBean(Object.class);
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                beanContainer.getBean(Object.class);
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        assertSame(bean, beanContainer.getBean(Object.class));
    }

    @RepeatedTest(100)
    void addMultipleBeansWithSameTypeMultipleThreadsWillAddTheFirstBeanOnly() throws InterruptedException {
        Object bean1 = new Object();
        Object bean2 = new Object();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                beanContainer.addBean(bean1);
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                beanContainer.addBean(bean2);
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        assertSame(bean1, beanContainer.getBean(Object.class));
    }
}