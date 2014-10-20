package com.okm_android.main.Utils.AddObserver;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by hu on 14/9/29.
 */
public class NotificationCenter {
    private HashMap<String,Observer> keyPaths;



    private static class NotificationCenterHolder{
        public static NotificationCenter holder = init();

        private static NotificationCenter init(){
            NotificationCenter notificationCenter = new NotificationCenter();
            notificationCenter.keyPaths = new HashMap<String, Observer>();
            return notificationCenter;
        }
    }

    public static NotificationCenter getInstance(){
        return NotificationCenterHolder.holder;
    }

    private class Observer{
        public Object observerObject;
        public String methodName;

        public Observer(Object theObserverObject, String theMethodName){
            this.observerObject = theObserverObject;
            this.methodName = theMethodName;
        }
    }

    public void postNotification(String keyPath){
        this.postNotification(keyPath, null);
    }

    public void postNotification(String keyPath, Object object){
        Observer observer = keyPaths.get(keyPath);
        if(observer == null){
            return;
        }
        Object observerObject = observer.observerObject;
        String methodName = observer.methodName;
        try {
            if (object == null) {
                Method method = observerObject.getClass().getMethod(methodName
                       );
                method.invoke(observerObject);
            }else{
                Method method = observerObject.getClass().getMethod(methodName,  object.getClass());
                method.invoke(observerObject, object);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void addObserver(String keyPath, Object observerObject, String methodName){
        keyPaths.put(keyPath, new Observer(observerObject,methodName));
    }


    public void removeObserver(String keyPath){
        keyPaths.remove(keyPath);
    }

}

