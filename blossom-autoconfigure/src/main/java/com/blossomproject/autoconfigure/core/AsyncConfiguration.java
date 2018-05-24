package com.blossomproject.autoconfigure.core;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration("BlossomAsyncConfiguration")
public class AsyncConfiguration {

  @Configuration("BlossomAsyncProperties")
  @ConfigurationProperties("blossom.async.thread-pool")
  public class AsyncProperties {
    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Integer keepAliveSeconds;
    private Integer queueCapacity;
    private Boolean allowCoreThreadTimeOut;

    public Integer getCorePoolSize() {
      return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
      this.corePoolSize = corePoolSize;
    }

    public Integer getMaxPoolSize() {
      return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
      this.maxPoolSize = maxPoolSize;
    }

    public Integer getKeepAliveSeconds() {
      return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(Integer keepAliveSeconds) {
      this.keepAliveSeconds = keepAliveSeconds;
    }

    public Integer getQueueCapacity() {
      return queueCapacity;
    }

    public void setQueueCapacity(Integer queueCapacity) {
      this.queueCapacity = queueCapacity;
    }

    public Boolean getAllowCoreThreadTimeOut() {
      return allowCoreThreadTimeOut;
    }

    public void setAllowCoreThreadTimeOut(Boolean allowCoreThreadTimeOut) {
      this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }
  }

  @Bean
  @ConditionalOnMissingBean(value = TaskExecutor.class, name = "taskExecutor")
  public TaskExecutor taskExecutor(@Qualifier("BlossomAsyncProperties") AsyncProperties asyncProperties) {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    if (asyncProperties.getCorePoolSize() != null) {
      taskExecutor.setCorePoolSize(asyncProperties.getCorePoolSize());
    }
    if (asyncProperties.getMaxPoolSize() != null) {
      taskExecutor.setMaxPoolSize(asyncProperties.getMaxPoolSize());
    }
    if (asyncProperties.getKeepAliveSeconds() != null) {
      taskExecutor.setKeepAliveSeconds(asyncProperties.getKeepAliveSeconds());
    }
    if (asyncProperties.getQueueCapacity() != null) {
      taskExecutor.setQueueCapacity(asyncProperties.getQueueCapacity());
    }
    if (asyncProperties.getAllowCoreThreadTimeOut() != null) {
      taskExecutor.setAllowCoreThreadTimeOut(asyncProperties.getAllowCoreThreadTimeOut());
    }
    return taskExecutor;
  }
}
