package fr.mgargadennec.blossom.autoconfigure;

import org.springframework.boot.autoconfigure.EnableAutoConfigurationImportSelector;

public class EnableBlossomWebImportSelector extends EnableAutoConfigurationImportSelector{
	@Override
	protected Class<?> getSpringFactoriesLoaderFactoryClass() {
		return EnableBlossomWeb.class;
	}
	@Override
	protected Class<?> getAnnotationClass() {
		return EnableBlossomWeb.class;
	}
}
