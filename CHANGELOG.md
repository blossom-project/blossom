# Changelog

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

### No issue
- Create CONTRIBUTING.md
- Removing the sample generator (see [blossom-samples](https://github.com/blossom-project/blossom-samples/pull/2))
- Use BulkProcessor for http traces (Elasticsearch)
- Moving impersonation freemarker to utils
- Facultative aggregation registry
- Update bcprov & bcmail versions
- Update README.md

## 1.0.0
Initial release