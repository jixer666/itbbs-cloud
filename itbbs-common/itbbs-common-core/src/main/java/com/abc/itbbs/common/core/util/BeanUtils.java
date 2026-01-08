package com.abc.itbbs.common.core.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LiJunXi
 * @date 2026/1/8
 */
public class BeanUtils {

    public static Map<String, String> convertToMap(Object object) {
        Map<String, String> result = new HashMap<>();
        BeanWrapper wrapper = new BeanWrapperImpl(object);

        for (PropertyDescriptor pd : wrapper.getPropertyDescriptors()) {
            String propertyName = pd.getName();
            if (!"class".equals(propertyName)) {
                Object value = wrapper.getPropertyValue(propertyName);
                if (value != null) {
                    result.put(propertyName, String.valueOf(value));
                }
            }
        }
        return result;
    }

}
