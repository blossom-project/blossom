package com.blossomproject.autoconfigure.core;

import com.blossomproject.core.common.utils.mail.*;
import com.google.common.collect.Iterables;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Configuration
@ConditionalOnMissingBean(MailSender.class)
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
public class MailAutoConfiguration {

    @Configuration
    @ConfigurationProperties("blossom.mail")
    @PropertySource({"classpath:/mailsender.properties"})
    public static class MailsenderProperties {
        private String url;
        private String from;
        private final Set<String> filters = new HashSet<>();


        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public Set<String> getFilters() {
            return filters;
        }

    }

    @Bean
    @ConditionalOnBean(JavaMailSender.class)
    @ConditionalOnMissingBean(MailSender.class)
    public MailSender blossomMailSender(JavaMailSender javaMailSender,
                                        MessageSource messageSource,
                                        freemarker.template.Configuration configuration,
                                        Set<Locale> availableLocales,
                                        MailsenderProperties properties, MailFilter mailFilter) {
        return new MailSenderImpl(javaMailSender,
                configuration,
                messageSource,
                properties.getUrl(),
                Iterables.getFirst(availableLocales, Locale.ENGLISH), mailFilter);
    }

    @Bean
    @ConditionalOnMissingBean(MailFilter.class)
    public MailFilter blossomDefaultMailFilter(MailsenderProperties properties) {
        return new MailFilterImpl(properties.getFilters(), properties.getFrom());
    }

    @Bean
    @ConditionalOnMissingBean(JavaMailSender.class)
    public MailSender blossomNoopMailSender() {
        return new NoopMailSenderImpl();
    }

}
