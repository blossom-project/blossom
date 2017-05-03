package fr.mgargadennec.blossom.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;

public class EnableBlossomImportSelector extends AutoConfigurationImportSelector {
  @Override
  protected Class<?> getSpringFactoriesLoaderFactoryClass() {
    return EnableBlossom.class;
  }

  @Override
  protected Class<?> getAnnotationClass() {
    return EnableBlossom.class;
  }

}
