package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author haopengujn@gmail.com
 */
public class BeanUtil {

    /**
     * 把Bean转换为字符串
     * @param obj
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String toString(Object obj) {
        if(obj == null) {
            return "null";
        }
        Class cl=obj.getClass();
        if(obj.getClass().isPrimitive() || obj instanceof String || obj instanceof Integer || obj instanceof Long
            || obj instanceof Byte || obj instanceof Character || obj instanceof Boolean || obj instanceof Short
            || obj instanceof Float || obj instanceof Double || obj instanceof Date) {
            return String.valueOf(obj);
        } else if(obj instanceof Enum) {
            return ((Enum)obj).name();
        } else if(cl.isArray()) {
            String r="[";
            for(int i=0; i < Array.getLength(obj); i++) {
                if(i > 0) {
                    r+=",";
                }
                Object val=Array.get(obj, i);
                if(null == val) {
                    r+="null";
                } else if(val.getClass().isPrimitive()) {
                    r+=val;
                } else {
                    r+=toString(val);
                }
            }
            return r + "]";
        } else if(obj instanceof List) {
            List tempList=(List)obj;
            String r="[";
            for(int i=0; i < tempList.size(); i++) {
                if(i > 0) {
                    r+=",";
                }
                Object val=tempList.get(i);
                if(null == val) {
                    r+="null";
                } else if(val.getClass().isPrimitive()) {
                    r+=val;
                } else {
                    r+=toString(val);
                }
            }
            return r + "]";
        } else if(obj instanceof Map) {
            Map tempMap=(Map)obj;
            String r="{";
            Iterator it=tempMap.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry entry=(Entry)it.next();
                if(it.hasNext()) {
                    r+=",";
                }
                Object key=entry.getKey();
                if(null == key) {
                    r+="null";
                } else if(key.getClass().isPrimitive()) {
                    r+=key;
                } else {
                    r+=toString(key);
                }
                r+="=";
                Object val=entry.getValue();
                if(null == val) {
                    r+="null";
                } else if(val.getClass().isPrimitive()) {
                    r+=val;
                } else {
                    r+=toString(val);
                }
            }
            return r + "}";
        }
        String r=cl.getName();
        do {
            Field[] fields=cl.getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);
            if(null == fields || fields.length == 0) {
                cl=cl.getSuperclass();
                continue;
            }
            r+="[";
            // get the names and values of all fields
            for(Field f: fields) {
                if(!Modifier.isStatic(f.getModifiers())) {
                    r+=f.getName() + "=";
                    try {
                        Class t=f.getType();
                        Object val=f.get(obj);
                        if(t.isPrimitive()) {
                            r+=val;
                        } else {
                            r+=toString(val);
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    r+=",";
                }
            }
            if(r.endsWith(",")) {
                r=r.substring(0, r.length() - 1);
            }
            r+="]";
            cl=cl.getSuperclass();
        } while(cl != null);
        return r;
    }

    @SuppressWarnings("rawtypes")
    public static String getBeanproperty(Class clazz){
        String r="";
        do {
            Field[] fields=clazz.getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);
            if(null == fields || fields.length == 0) {
                clazz=clazz.getSuperclass();
                continue;
            }
            r += "[";
            for(Field f: fields) {
                if(!Modifier.isStatic(f.getModifiers())) {
                    r+=f.getName();
                    r+=f.getName() + ":";
                    try {
                        Class t=f.getType();
                        r += t.getName();
                        if(!t.isPrimitive()) {
                            r+=getBeanproperty(t);
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    r+=",";
                }
            }
            if(r.endsWith(",")) {
                r=r.substring(0, r.length() - 1);
            }
            r+="]";
            clazz=clazz.getSuperclass();
        } while(clazz != null);
        return r;
    }
    
    @SuppressWarnings("rawtypes")
    public static String getGetMethods(Class clazz){
        Method[] methods=clazz.getDeclaredMethods();
        String s="[";
        for(Method method : methods){
            if(method.getName().startsWith("get")){
                s += method.getName()+",";
            }
            if(method.getName().startsWith("is")){
                s += method.getName()+",";
            }
        }
        if(s.endsWith(",")){
            s=s.substring(0, s.length() - 1);
        }
        s += "]";
        return s;
    }
    
    /**
     * 通过序列化进行深度复�?
     * @param obj
     * @return
     * @throws Exception
     */
    public static Object deepClone(Object obj) throws Exception {
        if(null == obj) {
            return null;
        }
        // 将对象写到流�?
        ByteArrayOutputStream bo=new ByteArrayOutputStream();
        ObjectOutputStream oo=new ObjectOutputStream(bo);
        oo.writeObject(obj);
        // 从流里读出来
        ByteArrayInputStream bi=new ByteArrayInputStream(bo.toByteArray());
        ObjectInputStream oi=new ObjectInputStream(bi);
        return(oi.readObject());
    }
}
