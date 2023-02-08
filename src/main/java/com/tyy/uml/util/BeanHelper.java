package com.tyy.uml.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import com.alibaba.fastjson2.JSON;

/**
 * Bean代理,被代理的bean拥有添加观察者的能力
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
public class BeanHelper {

    public static boolean isEnhancer(Class<?> type) {
        return Enhancer.isEnhanced(type);
    }

    public static <T> T proxy(Class<T> clazz, String... ingoreFields) {
        return (T) Enhancer.create(clazz, new Class[] {BeanObservale.class}, new BeanCallback(ingoreFields));
    }

    public static <T> T proxy(T t, String... ingoreFields) {
        Object proxy = proxy(t.getClass(), ingoreFields);
        BeanUtils.copyProperties(t, proxy);
        return (T) proxy;
    }

    public static <T> T proxy(String content, Class<T> clazz, String... ingoreFields) {
        return proxy(JSON.parseObject(content, clazz), ingoreFields);
    }

    public static interface BeanObservale {

        public void addObserver(Observer o);

        public void deleteObserver(Observer o);

        public void deleteObservers();

        public int countObservers();

    }

    public static interface BeanObserver extends Observer {

        @Override
        default void update(Observable o, Object arg) {
            ValueChange vc = (ValueChange) arg;

            this.update(o, vc.getSource(), vc.getKey(), vc.getOldValue(), vc.getNewValue());
        }

        void update(Observable o, Object source, String prop, Object oldValue, Object newValue);
        
        default boolean isNullOrEq(String prop, String e) {
            return prop == null || e.equals(prop);
        }
        
    }

    public static class BeanCallback extends Observable implements BeanObservale, MethodInterceptor {

        public static final Map<Class<?>, Map<String, PropertyDescriptor>> listenReadMethodcache = new HashMap<>();

        public static final Map<Class<?>, Set<String>> listenCache = new HashMap<>();

        public static final Set<Method> observaleMethods = new HashSet<>();
        static {
            for (Method method : BeanObservale.class.getDeclaredMethods()) {
                observaleMethods.add(method);
            }
        }

        public Set<String> ignoreFieldSet = new HashSet<String>();

        public BeanCallback(String[] ignoreFields) {
            if (ignoreFields != null) {
                for (String string : ignoreFields) {
                    ignoreFieldSet.add(string);
                }
            }
        }

        public synchronized void initBean(Class<?> type) {
            // key set方法的方法名
            Map<String, PropertyDescriptor> setRead = new HashMap<>();
            Set<String> listen = new HashSet<>();

            PropertyDescriptor[] beanProperties = ReflectUtils.getBeanProperties(type);
            for (PropertyDescriptor propertyDescriptor : beanProperties) {
                Method readMethod = propertyDescriptor.getReadMethod();
                Method writeMethod = propertyDescriptor.getWriteMethod();
                if (readMethod != null && writeMethod != null && !ignoreFieldSet.contains(propertyDescriptor.getName())) {
                    if (readMethod.getReturnType().getCanonicalName().startsWith("org.springframework.cglib.proxy")) {
                        continue;
                    }
                    setRead.put(writeMethod.getName(), propertyDescriptor);
                    listen.add(writeMethod.getName());
                }
            }
            listenReadMethodcache.put(type, setRead);
            listenCache.put(type, listen);
        }

        private boolean isWriterMethod(Class<?> objType, Method sourceMethod) {
            if (!listenCache.containsKey(objType)) {
                initBean(objType);
            }
            return listenCache.get(objType).contains(sourceMethod.getName());
        }

        private String getPropName(Class<?> objType, Method sourceMethod) {
            return listenReadMethodcache.get(objType).get(sourceMethod.getName()).getName();
        }

        private Object getOldValue(Object obj, Method sourceMethod) {
            PropertyDescriptor propertyDescriptor = listenReadMethodcache.get(obj.getClass()).get(sourceMethod.getName());
            Method readMethod = propertyDescriptor.getReadMethod();
            try {
                return readMethod.invoke(obj);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                return null;
            }
        }

        @Override
        public Object intercept(Object obj, Method sourceMethod, Object[] args, MethodProxy methodProxy) throws Throwable {
            if (observaleMethods.contains(sourceMethod)) { return sourceMethod.invoke(this, args); }

            if (isWriterMethod(obj.getClass(), sourceMethod)) {
                Object oldValue = getOldValue(obj, sourceMethod);
                Object invokeSuper = methodProxy.invokeSuper(obj, args);
                Object newValue = getOldValue(obj, sourceMethod);
                if (!Objects.equals(oldValue, newValue)) {
                    setChanged();
                    notifyObservers(new ValueChange(obj, getPropName(obj.getClass(), sourceMethod), oldValue, newValue));
                }
                return invokeSuper;
            }
            return methodProxy.invokeSuper(obj, args);
        }

    }

    public static class ValueChange {

        private Object source;

        private String key;

        private Object oldValue;

        private Object newValue;

        public ValueChange(Object source, String key, Object oldValue, Object newValue) {
            super();
            this.source = source;
            this.key = key;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public Object getSource() {
            return source;
        }

        public String getKey() {
            return key;
        }

        public Object getOldValue() {
            return oldValue;
        }

        public Object getNewValue() {
            return newValue;
        }

        @Override
        public String toString() {
            return "ValueChange [key=" + key + ", oldValue=" + oldValue + ", newValue=" + newValue + "]";
        }

    }

}
