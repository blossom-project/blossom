package fr.mgargadennec.blossom.core.common.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public final class BlossomAutowireHelper implements ApplicationContextAware {

  private static final BlossomAutowireHelper INSTANCE = new BlossomAutowireHelper();
  private static ApplicationContext applicationContext;

  private BlossomAutowireHelper() {
  }

  public static void autowire(Object classToAutowire, Object... beansToAutowireInClass) {
    for (Object bean : beansToAutowireInClass) {
      if (bean == null) {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(classToAutowire);
      }
    }
  }

  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    BlossomAutowireHelper.applicationContext = applicationContext;
  }

  /**
   * @return the singleton instance.
   */
  public static BlossomAutowireHelper getInstance() {
    return INSTANCE;
  }

}