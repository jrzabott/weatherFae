package fae.weather.beanmanager;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class BeanManagerImplTest {

    private BeanManager beanManager;

    @BeforeEach
    void setUp() {
        beanManager = new BeanManagerImpl();
    }

    @Test
    void addBean() {
        Object bean = new Object();
        beanManager.addBean(bean);
        assertSame(bean, beanManager.getBean(Object.class));
    }

    @Test
    void addBeanTwice() {
        Object bean = new Object();
        beanManager.addBean(bean);
        beanManager.addBean(bean);
        assertSame(bean, beanManager.getBean(Object.class));
    }

    @Test
    void addBeanNull() {
        assertDoesNotThrow(() -> beanManager.addBean(null));
    }

    @Test
    void addBeanNullDoesNotAffectedExistingBeans() {
        Object bean = new Object();
        beanManager.addBean(bean);
        beanManager.addBean(null);
        assertEquals(1, beanManager.getAllBeans().length);
        assertSame(bean, beanManager.getBean(Object.class));
    }

    @Test
    void addTwoBeansOfSameType() {
        Object bean1 = new Object();
        Object bean2 = new Object();
        beanManager.addBean(bean1);
        beanManager.addBean(bean2);
        assertNotEquals(bean1, bean2);
        assertSame(bean1, beanManager.getBean(Object.class));

        // BeanContainerImpl will hold only one bean of a given type.
        assertNotSame(bean2, beanManager.getBean(Object.class));
    }
    
    @Test
    void addTwoBeansOfDifferentTypes() {
        Object bean1 = new Object();
        String bean2 = StringUtils.EMPTY;
        beanManager.addBean(bean1);
        beanManager.addBean(bean2);
        assertNotEquals(bean1, bean2);
        assertSame(bean1, beanManager.getBean(bean1.getClass()));
        assertSame(bean2, beanManager.getBean(bean2.getClass()));
    }

    @Test
    void removeBean() {
        Object bean = new Object();
        beanManager.addBean(bean);
        beanManager.removeBean(bean);
        assertNull(beanManager.getBean(Object.class));
    }

    @Test
    void removeBeanNull() {
        assertDoesNotThrow(() -> beanManager.removeBean(null));
    }

    @Test
    void removeBeanNullDoesNotAffectExistingBeans() {
        Object bean = new Object();
        beanManager.addBean(bean);
        beanManager.removeBean(null);
        assertEquals(1, beanManager.getAllBeans().length);
        assertSame(bean, beanManager.getBean(Object.class));
    }

    @Test
    void removeBeanNotInContainer() {
        Object bean = new Object();
        beanManager.removeBean(bean);
        assertNull(beanManager.getBean(Object.class));
    }

    @Test
    void removeBeanTwice() {
        Object bean = new Object();
        beanManager.addBean(bean);
        beanManager.removeBean(bean);
        beanManager.removeBean(bean);
        assertNull(beanManager.getBean(Object.class));
    }

    @Test
    void removeTwoBeansOfSameType() {
        Object bean1 = new Object();
        Object bean2 = new Object();
        beanManager.addBean(bean1);
        beanManager.addBean(bean2);
        beanManager.removeBean(bean1);
        assertNull(beanManager.getBean(bean1.getClass()));
        assertNull(beanManager.getBean(bean2.getClass()));
    }
    
    @Test
    void removeTwoBeansOfDifferentTypes() {
        Object bean1 = new Object();
        String bean2 = StringUtils.EMPTY;
        beanManager.addBean(bean1);
        beanManager.addBean(bean2);
        beanManager.removeBean(bean1);
        assertNull(beanManager.getBean(bean1.getClass()));
        assertSame(bean2, beanManager.getBean(bean2.getClass()));
    }

    @Test
    void getBean() {
        Object bean = new Object();
        beanManager.addBean(bean);
        assertSame(bean, beanManager.getBean(Object.class));
    }
    
    @Test
    void getBeanNull() {
        final Class<Object> nullClass = null;
        Assertions.assertThatThrownBy(() -> beanManager.getBean(nullClass))
                .isInstanceOf(NullPointerException.class);
    }
    
    @Test
    void getBeanNotInContainer() {
        assertNull(beanManager.getBean(Object.class));
    }
    
    @Test
    void getBeanTwice() {
        Object bean = new Object();
        beanManager.addBean(bean);
        assertSame(bean, beanManager.getBean(Object.class));
        assertSame(bean, beanManager.getBean(Object.class));
    }
    
    @Test
    void getBeanTwoBeans() {
        Object bean1 = new Object();
        Object bean2 = new Object();
        beanManager.addBean(bean1);
        beanManager.addBean(bean2);
        assertNotEquals(bean1, bean2);
        assertSame(bean1, beanManager.getBean(bean1.getClass()));
        
        // BeanContainerImpl will hold only one bean of a given type, it also works as FIFO. The first bean added will 
        // be the stored bean.
        assertSame(bean1, beanManager.getBean(bean2.getClass()));
    }

    @Test
    void getBeanByName() {
        Object bean = new Object();
        beanManager.addBean(bean);
        assertSame(bean, beanManager.getBean(Object.class.getName()));
    }
    @Test
    void getBeanByNameNull() {
        assertNull(beanManager.getBean((String) null));
    }
    @Test
    void getBeanByNameNotInContainer() {
        assertNull(beanManager.getBean(Object.class.getName()));
    }
    
    @Test
    void getBeanByNameTwice() {
        Object bean = new Object();
        beanManager.addBean(bean);
        assertSame(bean, beanManager.getBean(Object.class.getName()));
    }
    
    @Test
    void getBeanByNameTwoBeans() {
        Object bean1 = new Object();
        Object bean2 = new Object();
        beanManager.addBean(bean1);
        beanManager.addBean(bean2);
        assertNotEquals(bean1, bean2);
        assertSame(bean1, beanManager.getBean(bean1.getClass().getName()));
        
        // BeanContainerImpl will hold only one bean of a given type, it also works as FIFO. The first bean added will 
        // be the stored bean.
        assertSame(bean1, beanManager.getBean(bean2.getClass().getName()));
    }
    
    @Test  
    void getAllBeans() {
        Object bean1 = new Object();
        Object bean2 = new Object();
        beanManager.addBean(bean1);
        beanManager.addBean(bean2);
        Object[] beans = beanManager.getAllBeans();
        assertEquals(1, beans.length);
        assertSame(bean1, beans[0]);
    }
    
    @Test
    void getAllBeansEmpty() {
        Object[] beans = beanManager.getAllBeans();
        assertEquals(0, beans.length);
    }
    
    @Test
    void getAllBeansTwice() {
        Object bean1 = new Object();
        Object bean2 = new Object();
        beanManager.addBean(bean1);
        beanManager.addBean(bean2);
        Object[] beans = beanManager.getAllBeans();
        assertEquals(1, beans.length);
        assertSame(bean1, beans[0]);
        beans = beanManager.getAllBeans();
        assertEquals(1, beans.length);
        assertSame(bean1, beans[0]);
    }
    
    @Test
    void getAllBeansTwoBeansWithSameType() {
        Object bean1 = new Object();
        Object bean2 = new Object();
        beanManager.addBean(bean1);
        beanManager.addBean(bean2);
        Object[] beans = beanManager.getAllBeans();
        assertEquals(1, beans.length);
        assertSame(bean1, beans[0]);
        beans = beanManager.getAllBeans();
        assertEquals(1, beans.length);
        assertSame(bean1, beans[0]);
    }

    @Test
    void getAllBeanTwoBeansWithDifferentTypes() {
        Object bean1 = new Object();
        String bean2 = StringUtils.EMPTY;
        beanManager.addBean(bean1);
        beanManager.addBean(bean2);
        Object[] beans = beanManager.getAllBeans();
        assertEquals(2, beans.length);
        assertSame(bean1, beans[0]);
        assertSame(bean2, beans[1]);
        beans = beanManager.getAllBeans();
        assertEquals(2, beans.length);
        assertSame(bean1, beans[0]);
        assertSame(bean2, beans[1]);
    }
    
    @Test
    void getAllBeansTwoBeansRemoveOne() {
        Object bean1 = new Object();
        String bean2 = StringUtils.EMPTY;
        beanManager.addBean(bean1);
        beanManager.addBean(bean2);
        beanManager.removeBean(bean1);
        Object[] beans = beanManager.getAllBeans();
        assertEquals(1, beans.length);
        assertSame(bean2, beans[0]);
    }
    
    @Test
    void getAllBeansTwoBeansRemoveTwo() {
        Object bean1 = new Object();
        String bean2 = StringUtils.EMPTY;
        beanManager.addBean(bean1);
        beanManager.addBean(bean2);
        beanManager.removeBean(bean1);
        beanManager.removeBean(bean2);
        Object[] beans = beanManager.getAllBeans();
        assertEquals(0, beans.length);
    }
    
    @Test
    void getAllBeansTwoBeansRemoveTwoAddOne() {
        Object bean1 = new Object();
        String bean2 = StringUtils.EMPTY;
        beanManager.addBean(bean1);
        beanManager.addBean(bean2);
        beanManager.removeBean(bean1);
        beanManager.removeBean(bean2);
        beanManager.addBean(bean1);
        Object[] beans = beanManager.getAllBeans();
        assertEquals(1, beans.length);
        assertSame(bean1, beans[0]);
    }

    @Test
    void getAllBeansTwoBeansWithDifferentTypeRemoveTwoAddTwo() {
        Object bean1 = new Object();
        String bean2 = StringUtils.EMPTY;
        beanManager.addBean(bean1);
        beanManager.addBean(bean2);
        beanManager.removeBean(bean1);
        beanManager.removeBean(bean2);
        beanManager.addBean(bean1);
        beanManager.addBean(bean2);
        Object[] beans = beanManager.getAllBeans();
        assertEquals(2, beans.length);
        assertSame(bean1, beans[0]);
        assertSame(bean2, beans[1]);
    }

    @Test
    void testAddBeanMultipleThreadsWillNotThrowException() {
        assertDoesNotThrow(() -> {
            Thread thread1 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    beanManager.addBean(new Object());
                }
            });
            Thread thread2 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    beanManager.addBean(new Object());
                }
            });

            CompletableFuture.runAsync(thread1).thenRunAsync(thread2).join();
        });
    }

    @Test
    void testRemoveBeanMultipleThreadsWillNotThrowException() {
        assertDoesNotThrow(() -> {
            Object bean = new Object();
            beanManager.addBean(bean);
            Thread thread1 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    beanManager.removeBean(bean);
                }
            });
            Thread thread2 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    beanManager.removeBean(bean);
                }
            });

            CompletableFuture.runAsync(thread1).thenRunAsync(thread2).join();
        });
    }

    @Test
    void testGetBeanMultipleThreadsWillNotThrowException() {
        assertDoesNotThrow(() -> {
            Object bean = new Object();
            beanManager.addBean(bean);
            Thread thread1 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    beanManager.getBean(Object.class);
                }
            });
            Thread thread2 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    beanManager.getBean(Object.class);
                }
            });

            CompletableFuture.runAsync(thread1).thenRunAsync(thread2).join();
        });
    }

    @Test
    void testAddBeanMultipleThreadsWillAddTheFirstBeanOnly() {
        Object bean1 = new Object();
        Object bean2 = new Object();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                beanManager.addBean(bean1);
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                beanManager.addBean(bean2);
            }
        });

        CompletableFuture.runAsync(thread1).thenRunAsync(thread2).join();
        assertSame(bean1, beanManager.getBean(Object.class));
    }

    @Test
    void testRemoveBeanMultipleThreadsWillRemoveTheBeanOnly() {
        Object bean = new Object();
        beanManager.addBean(bean);
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                beanManager.removeBean(bean);
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                beanManager.removeBean(bean);
            }
        });

        CompletableFuture.runAsync(thread1).thenRunAsync(thread2).join();
        assertNull(beanManager.getBean(Object.class));
    }

    @Test
    void testGetBeanMultipleThreadsWillReturnTheBeanOnly() {
        Object bean = new Object();
        beanManager.addBean(bean);
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                beanManager.getBean(Object.class);
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                beanManager.getBean(Object.class);
            }
        });

        CompletableFuture.runAsync(thread1).thenRunAsync(thread2).join();
        assertSame(bean, beanManager.getBean(Object.class));
    }

    @RepeatedTest(100)
    void addMultipleBeansWithSameTypeMultipleThreadsWillAddTheFirstBeanOnly() {
        Object bean1 = new Object();
        Object bean2 = new Object();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                beanManager.addBean(bean1);
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                beanManager.addBean(bean2);
            }
        });
        CompletableFuture.runAsync(thread1).thenRunAsync(thread2).join();
        assertSame(bean1, beanManager.getBean(Object.class));
    }
}