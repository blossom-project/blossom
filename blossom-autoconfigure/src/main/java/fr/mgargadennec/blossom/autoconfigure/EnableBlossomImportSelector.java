package fr.mgargadennec.blossom.autoconfigure;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfigurationImportSelector;

public class EnableBlossomImportSelector extends EnableAutoConfigurationImportSelector{
	@Override
	protected Class<?> getSpringFactoriesLoaderFactoryClass() {
		return EnableBlossom.class;
	}
	@Override
	protected Class<?> getAnnotationClass() {
		return EnableBlossom.class;
	}

}
