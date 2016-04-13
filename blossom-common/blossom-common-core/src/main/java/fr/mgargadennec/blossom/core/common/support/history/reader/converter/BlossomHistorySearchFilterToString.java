/**
 *
 */
package fr.mgargadennec.blossom.core.common.support.history.reader.converter;

import static fr.mgargadennec.blossom.core.common.support.history.reader.BlossomHistorySearchFilter.ALLOCATION;
import static fr.mgargadennec.blossom.core.common.support.history.reader.BlossomHistorySearchFilter.INNER_SEPARATOR;
import static fr.mgargadennec.blossom.core.common.support.history.reader.BlossomHistorySearchFilter.SEPARATOR;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import fr.mgargadennec.blossom.core.common.support.history.reader.BlossomHistorySearchFilter;

public class BlossomHistorySearchFilterToString implements Converter<BlossomHistorySearchFilter, String> {

  public String convert(BlossomHistorySearchFilter source) {

    PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(BlossomHistorySearchFilter.class);
    List<String> keyValueList = new ArrayList<String>();
    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
      Method readMethod = propertyDescriptor.getReadMethod();

      if (readMethod.getReturnType().equals(String.class)) {
        Object value;

        try {
          value = readMethod.invoke(source);
        } catch (IllegalAccessException e) {
          throw new IllegalArgumentException(e);
        } catch (IllegalArgumentException e) {
          throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
          throw new IllegalArgumentException(e);
        }

        String serializedValue = null;
        if (readMethod.getReturnType().isArray()) {
          // array of String
          if (value != null) {
            String[] valueArray = (String[]) value;
            if (valueArray.length > 0) {
              serializedValue = org.apache.commons.lang3.StringUtils.join(valueArray, INNER_SEPARATOR);
            }
          }
        } else {
          // simple String
          serializedValue = (String) value;
        }

        if (!StringUtils.isEmpty(serializedValue)) {
          keyValueList.add(propertyDescriptor.getName() + ALLOCATION + serializedValue);
        }
      }

    }

    return StringUtils.collectionToDelimitedString(keyValueList, SEPARATOR);
  }

}
