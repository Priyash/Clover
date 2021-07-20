package com.clover.data.utility;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class CopyFields {

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> fieldNames = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            String pdName = pd.getName();
            String typeName = pd.getPropertyType().getTypeName();
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                fieldNames.add(pd.getName());

            if(pdName.equals("id") && typeName.equals("java.lang.Long")) {
                fieldNames.add(pd.getName());
            }

            if(pdName.equals("created_at") && typeName.equals("java.lang.String")){
                fieldNames.add(pd.getName());
            }

            if(pdName.equals("updated_at") && typeName.equals("java.lang.String")){
                fieldNames.add(pd.getName());
            }
        }
        String[] result = new String[fieldNames.size()];
        return fieldNames.toArray(result);
    }
}
