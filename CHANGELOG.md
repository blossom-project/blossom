# Changelog

## 1.3.0

- Upgrade to spring boot 2.1.9 [#224](https://github.com/blossom-project/blossom/pull/224)
- Extract UI Web Security Configuration [#222](https://github.com/blossom-project/blossom/pull/222)
- Build with OpenJDK 8 [#221](https://github.com/blossom-project/blossom/pull/221)
- Extract supervision controller to a standalone module [#220](https://github.com/blossom-project/blossom/pull/220)
- Upgrade Apache Tika to 1.22 [#219](https://github.com/blossom-project/blossom/pull/219)
- Do not configure Elasticsearch client & node if not on the classpath [#218](https://github.com/blossom-project/blossom/pull/218)
- Do not configure web unless blossom-ui-common is present [#218](https://github.com/blossom-project/blossom/pull/218)
- Do not wrap results for cache storage when bypassing cache altogether [#217](https://github.com/blossom-project/blossom/pull/217)
- Upgrade dependencies [#215](https://github.com/blossom-project/blossom/pull/215)

## 1.2.1
- Fix trigger history [#211](https://github.com/blossom-project/blossom/pull/211)  
- Fix loggers page security [#213](https://github.com/blossom-project/blossom/pull/213)  
- Upgrade spring-boot 2.1.4 & spring-security-oauth 2.3.5 [#214](https://github.com/blossom-project/blossom/pull/214)

## 1.2.0
- Mailsender interface rework [#204](https://github.com/blossom-project/blossom/pull/204)  
- Toggle unuseful log to debug level [#207](https://github.com/blossom-project/blossom/pull/207)  
- Java 11 compatibility & Spring boot 2.1 [#205](https://github.com/blossom-project/blossom/pull/205)
- Check for logger manager authority for LoggerManagerController [#203](https://github.com/blossom-project/blossom/issues/201)

## 1.1.3
- Upgrade spring boot to 2.0.6 [#197](https://github.com/blossom-project/blossom/pull/197)
- Upgrade Apache Tika to 1.19.1 [#198](https://github.com/blossom-project/blossom/pull/198)
- MailSender allow BCC-only and multiple recipients & MailSender fixes [#196](https://github.com/blossom-project/blossom/pull/196)
- Upgrade Spring Security OAuth to 2.2.3

## 1.1.0
- Mail: better default [#180](https://github.com/blossom-project/blossom/issues/180)  
- Generation improvements [#191](https://github.com/blossom-project/blossom/pull/191)  
- Fix filter for InternetAddress with personnal  [#189](https://github.com/blossom-project/blossom/pull/189)
- Upgrade spring boot to 2.0.4-RELEASE [#190](https://github.com/blossom-project/blossom/pull/190)
- Rename "function" column to "job_title" to avoid reserved keywords on SQL Server and MySQL 8 [#187](https://github.com/blossom-project/blossom/pull/187)
- Module generator mysql [#183](https://github.com/blossom-project/blossom/pull/183)
- Force latin1 encoding for job names and groups in trigger history [#186](https://github.com/blossom-project/blossom/pull/186)
- Randomize system account password and by default deactivate it when another account exists [#184](https://github.com/blossom-project/blossom/pull/184)
- Do not throw any exception in GlobalTriggerListener [#182](https://github.com/blossom-project/blossom/pull/182)
- Allow the use of InternetAddress instead of String for emails [#159](https://github.com/blossom-project/blossom/issues/159)  
- Automated changelog [#160](https://github.com/blossom-project/blossom/pull/160)  
- LongFieldBuilder JDBC type `long` badly translate to mediumtext on MySQL [#164](https://github.com/blossom-project/blossom/pull/164)  
- Display db migration in reverse order so last changes appears first [#168](https://github.com/blossom-project/blossom/pull/168)  
- If there is no page, remove pagiation instead of displaying 1-0 of 0 [#169](https://github.com/blossom-project/blossom/pull/169)  
- Make cache search case insensitive [#170](https://github.com/blossom-project/blossom/pull/170)  
- AdditionnalScss not taken into account in Theme [#173](https://github.com/blossom-project/blossom/issues/173)  
- Fix date filter initialization [#176](https://github.com/blossom-project/blossom/pull/176)  
- In the log, display only the actual recipients after filtering is applied on a mail [#177](https://github.com/blossom-project/blossom/pull/177)  
- Add ability to configure session length and max session per user [#188](https://github.com/blossom-project/blossom/pull/188)  
- Add the missing import spring in master.ftl [#139](https://github.com/blossom-project/blossom/pull/139)  
- User profile page [#142](https://github.com/blossom-project/blossom/issues/142)  
- User impersonation [#143](https://github.com/blossom-project/blossom/issues/143)  
- Fix mail filters [#144](https://github.com/blossom-project/blossom/pull/144)  
- Search filters [#145](https://github.com/blossom-project/blossom/pull/145)  
- Revert the change from `blossom.mail.url` to `blossom.mail.base-url` [#148](https://github.com/blossom-project/blossom/pull/148)  
- Provide a copy constructor for AbstractDTO [#149](https://github.com/blossom-project/blossom/pull/149)  
- Add Cc and Bcc recipients to mail sender and allow customisation of an email parameter by adding a mailfilter [#150](https://github.com/blossom-project/blossom/pull/150)  
- Add filter on request/response headers indexed in Elasticsearch [#152](https://github.com/blossom-project/blossom/pull/152)  
- Supervision status page improvements [#156](https://github.com/blossom-project/blossom/pull/156)  
- Fix profile page for users with empty values [#157](https://github.com/blossom-project/blossom/pull/157)  
- Add job supervision to detect when jobs stop firing [#158](https://github.com/blossom-project/blossom/pull/158)  
- Async mail sender [#161](https://github.com/blossom-project/blossom/pull/161)  
