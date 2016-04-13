package fr.mgargadennec.blossom.core.common.support.history.reader.converter;

import static fr.mgargadennec.blossom.core.common.support.history.reader.BlossomHistorySearchFilter.ALLOCATION;
import static fr.mgargadennec.blossom.core.common.support.history.reader.BlossomHistorySearchFilter.INNER_SEPARATOR;
import static fr.mgargadennec.blossom.core.common.support.history.reader.BlossomHistorySearchFilter.SEPARATOR;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import fr.mgargadennec.blossom.core.common.support.history.reader.BlossomHistorySearchFilter;

public class BlossomStringToHistorySearchFilter implements Converter<String, BlossomHistorySearchFilter> {

	public BlossomHistorySearchFilter convert(String source) {
		if (StringUtils.isEmpty(source)) {
			return null;
		}

		BlossomHistorySearchFilter historySearchFilter = new BlossomHistorySearchFilter();
		String[] filterEntryArray = source.split(SEPARATOR);
		for (String filterEntry : filterEntryArray) {
			String[] filterKeyValue = filterEntry.split(ALLOCATION, 2);
			if (filterKeyValue.length != 2) {
				throw new IllegalArgumentException(
						"History filter entry '" + filterEntry + "' is not formatted as 'property:value'");
			}
			String propertyName = filterKeyValue[0];
			String value = filterKeyValue[1];

			PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(BlossomHistorySearchFilter.class,
					propertyName);

			Object deserializedValue;
			if (propertyDescriptor.getPropertyType().isArray()) {
				// array of String
				deserializedValue = value.split(INNER_SEPARATOR.replaceAll("\\|", "\\\\|"));
			} else {
				// simple String
				deserializedValue = value;
			}

			try {
				propertyDescriptor.getWriteMethod().invoke(historySearchFilter, deserializedValue);
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException(e);
			} catch (InvocationTargetException e) {
				throw new IllegalArgumentException(e);
			}
		}

		return historySearchFilter;
	}

}
