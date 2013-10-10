package utils;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Date;

public class BeanComparator implements Comparator<Object> {

    private String properName;// 根据此关键字属性排序

    private boolean flag;// 为true的时候是升序，为false的时候是倒序

    public BeanComparator(String properName, boolean flag) {
        super();
        this.properName=properName;
        this.flag=flag;
    }

    public String getProperName() {
        return properName;
    }

    public void setProperName(String properName) {
        this.properName=properName;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag=flag;
    }

    public int compare(Object o1, Object o2) {
        Class clazz=o1.getClass();
        try {
            Method method=clazz.getMethod("get" + properName.substring(0, 1).toUpperCase() + properName.substring(1), new Class[] {});
            Object complareValue1=method.invoke(o1);
            Object complareValue2=method.invoke(o2);
            long result=0;
            if(complareValue1 instanceof String) {
                result=
                    flag ? ((String)complareValue1).compareTo((String)complareValue2) : ((String)complareValue2)
                        .compareTo((String)complareValue1);
            }

            if(complareValue1 instanceof Integer) {
                result=flag ? (Integer)complareValue1 - (Integer)complareValue2 : (Integer)complareValue2 - (Integer)complareValue1;
            }

            if(complareValue1 instanceof Long) {
                result=flag ? (Long)complareValue1 - (Long)complareValue2 : (Long)complareValue2 - (Long)complareValue1;
            }

            if(complareValue1 instanceof Date) {
                boolean b=
                    flag ? ((Date)complareValue1).after((Date)complareValue2) : ((Date)complareValue1).before((Date)complareValue2);
                result=b ? 1 : -1;
            }

            if(result > 0){
                return 1;
            }
            if(result < 0){
                return -1;
            }
            return 0;
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
