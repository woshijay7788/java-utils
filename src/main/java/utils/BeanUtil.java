package utils;

import com.google.common.collect.Lists;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * @Description bean拷贝类
 * @author chibei
 */
public class BeanUtil {

    private static final DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();

    public static void copy(Object source, Object target) {

        if(source == null || target == null) {
            return;
        }

        dozerBeanMapper.map(source, target);
    }

    public static <T> T map(Object source, Class<T> clazz) {

        if(source == null || clazz == null) {
            return null;
        }

        return dozerBeanMapper.map(source, clazz);
    }

    public static <T> List<T> copyList(List source, Class<T> target) {

        if(target.isInterface() || source == null || source.isEmpty()) {
            return Lists.newArrayList();
        }

        List<T> targetList = Lists.newArrayList();

        source.forEach(a -> {
            T targetObject = null;
            if (a != null) {
                try {
                    targetObject = target.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                copy(a, targetObject);
            }
            targetList.add(targetObject);
        });

        return targetList;
    }

    public static <T> List<T> mapList(Collection source, Class<T> target) {

        if(target.isInterface() || source == null || source.isEmpty()) {
            return Lists.newArrayList();
        }

        List<T> targetList = Lists.newArrayList();

        source.forEach(a -> targetList.add(map(a, target)));

        return targetList;
    }

    /**
     * 复制原有Spring BeanUtils.copyProperties代码
     * 增加逻辑: 如果source中值为空则跳过不copy
     *
     * @param source
     * @param target
     * @throws BeansException
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target, List<String> ignoreProperties) throws BeansException {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreProperties == null || !ignoreProperties.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null &&
                        ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            // 值为空则跳过copy
                            if (!notNull(value)) {
                               continue;
                            }
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        }
                        catch (Throwable ex) {
                            throw new FatalBeanException(
                                "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }

    public static boolean notNull(Object... objs){

        if (objs == null || objs.length == 0){
            return false;
        }

        for (Object obj : objs){
            if (!notNull(obj)){
                return false;
            }
        }

        return true;
    }
}
