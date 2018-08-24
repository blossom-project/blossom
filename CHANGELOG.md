# Changelog

## 1.1.0.M3
- Fix filter for InternetAddress with personnal  [#189](https://github.com/blossom-project/blossom/pull/189)
- Upgrade spring boot to 2.0.4-RELEASE [#190](https://github.com/blossom-project/blossom/pull/190)
- Rename "function" column to "job_title" to avoid reserved keywords on SQL Server and MySQL 8 [#187](https://github.com/blossom-project/blossom/pull/187)
- Module generator mysql [#183](https://github.com/blossom-project/blossom/pull/183)
- Force latin1 encoding for job names and groups in trigger history [#186](https://github.com/blossom-project/blossom/pull/186)
- Randomize system account password and by default deactivate it when another account exists [#184](https://github.com/blossom-project/blossom/pull/184)
- Do not throw any exception in GlobalTriggerListener [#182](https://github.com/blossom-project/blossom/pull/182)
- Allow the use of InternetAddress instead of String for emails [#159](https://github.com/blossom-project/blossom/issues/159)  
- Automated changelog [#160](https://github.com/blossom-project/blossom/pull/160)  
- LongFieldBuilder JDBC type &quot;long&quot; badly translate to mediumtext on MySQL [#164](https://github.com/blossom-project/blossom/pull/164)  
- Display db migration in reverse order so last changes appears first [#168](https://github.com/blossom-project/blossom/pull/168)  
- If there is no page, remove pagiation instead of displaying 1-0 of 0 [#169](https://github.com/blossom-project/blossom/pull/169)  
- make cache search case insensitive [#170](https://github.com/blossom-project/blossom/pull/170)  
- AdditionnalScss not taken into account in Theme [#173](https://github.com/blossom-project/blossom/issues/173)  
- Fixes #173 [#174](https://github.com/blossom-project/blossom/pull/174)  
- fix date filter initialization [#176](https://github.com/blossom-project/blossom/pull/176)  
- In the log, display only the actual recipients after filtering is applied on a mail [#177](https://github.com/blossom-project/blossom/pull/177)  
- Add ability to configure session length and max session per user [#188](https://github.com/blossom-project/blossom/pull/188)  

### No issue
- Create CONTRIBUTING.md


## 1.1.0.M1
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
- Removing the sample generator
- Use BulkProcessor for http traces
- Using bulk to index httptraces
- Moving impersonation freemarker to utils
- Facultative aggregation registry
- Update bcprov & bcmail versions
- Update property default value
- Update README.md
- Fix typo
- Prepare next version


## 1.0.0
- getAll() cache (again) [#125](https://github.com/blossom-project/blossom/issues/125)  

### No issue
- Pom refinement
- Avatar update fix
- Two minor corrections (avatar update + scheduler display on small devices)
- Prepare release


## 1.0.0.M2
- Back-office homepage with submenu items as cards [#101](https://github.com/blossom-project/blossom/pull/101)  
- UserApiController avatar tests + Administration Web tests [#102](https://github.com/blossom-project/blossom/pull/102)  
- Generator UI Back-office create, display, edit and delete functions [#103](https://github.com/blossom-project/blossom/pull/103)  
- Add spring-boot-configuration-processor to autoconfigure [#104](https://github.com/blossom-project/blossom/pull/104)  
- Generator : Enum Type + Indexation Elasticsearch [#105](https://github.com/blossom-project/blossom/pull/105)  
- Generator : Textarea input for @Lob String [#106](https://github.com/blossom-project/blossom/pull/106)  
- Back-office homepage with panel for each menuItem. [#107](https://github.com/blossom-project/blossom/pull/107)  
- Generator : ApiController generation [#108](https://github.com/blossom-project/blossom/pull/108)  
- Generator : Fix changelog Path + Fix IndexationJob Qualifier [#109](https://github.com/blossom-project/blossom/pull/109)  
- getAll() cache [#113](https://github.com/blossom-project/blossom/issues/113)  
- Manual cache usage and cache disabling [#114](https://github.com/blossom-project/blossom/issues/114)  
- Add link of the documentation website in the read me file [#117](https://github.com/blossom-project/blossom/pull/117)  
- Fix in askForForgottenPassword POST Controller. [#118](https://github.com/blossom-project/blossom/pull/118)  
- Tests on Article module [#119](https://github.com/blossom-project/blossom/pull/119)  
- Create CODE_OF_CONDUCT.md [#120](https://github.com/blossom-project/blossom/pull/120)  
- Unit Testing (ActivationController, StatusController, OmnisearchController, BlossomErrorViewResolver) [#121](https://github.com/blossom-project/blossom/pull/121)  
- HikariCP-java6 exclusion [#122](https://github.com/blossom-project/blossom/pull/122)  
- Add functionalities to the article module [#123](https://github.com/blossom-project/blossom/pull/123)  
- Unit Testing [#126](https://github.com/blossom-project/blossom/pull/126)  
- Simple changes to article module [#127](https://github.com/blossom-project/blossom/pull/127)  
- Cleaning of Unit Testing  [#128](https://github.com/blossom-project/blossom/pull/128)  
- correction: title and icon on article create page [#129](https://github.com/blossom-project/blossom/pull/129)  
- Bad performances of messagesource if launched from jar [#13](https://github.com/blossom-project/blossom/issues/13)    *bug*  *enhancement*  *performance*  
- Quartz configuration [#130](https://github.com/blossom-project/blossom/issues/130)  
- GenericCrudServiceImpl + GenericReadOnlyServiceImpl Unit Testing [#131](https://github.com/blossom-project/blossom/pull/131)  
- Home page and login personalisation [#132](https://github.com/blossom-project/blossom/issues/132)  
- Fix Issue : Generator conflit message properties file [#133](https://github.com/blossom-project/blossom/pull/133)  
- RestrictedSessionLocaleResolver + MenuItemImpl + MenuInterceptor + FileController Unit Testing [#134](https://github.com/blossom-project/blossom/pull/134)  
- Creation of a customized insert link plugin for summernote [#135](https://github.com/blossom-project/blossom/pull/135)  
- Theme configuration using Scss [#136](https://github.com/blossom-project/blossom/issues/136)  
- correction: text input in module article [#137](https://github.com/blossom-project/blossom/pull/137)  
- Manage message source file name and overwritting of them [#22](https://github.com/blossom-project/blossom/issues/22)    *bug*  *enhancement*  
- Add system menu for liquibase actuator endpoint [#40](https://github.com/blossom-project/blossom/issues/40)  
- Implements Blossom REST API [#41](https://github.com/blossom-project/blossom/issues/41)    *enhancement*  
- Secure Blossom REST API [#42](https://github.com/blossom-project/blossom/issues/42)    *enhancement*  
- Upgrade to Spring-Boot 2.0.0 [#45](https://github.com/blossom-project/blossom/issues/45)    *dependency*  *enhancement*  
- Cleaning and styling of the project Initializr [#47](https://github.com/blossom-project/blossom/issues/47)    *enhancement*  
- Activation mail [#53](https://github.com/blossom-project/blossom/pull/53)  
- Enabling attached files for emails [#54](https://github.com/blossom-project/blossom/pull/54)  
- Adding the possibility to initialize project as war deployment [#56](https://github.com/blossom-project/blossom/pull/56)  
- External monitoring [#57](https://github.com/blossom-project/blossom/issues/57)  
- Protect brute-force attemps on BO users [#58](https://github.com/blossom-project/blossom/issues/58)  
-  Bootsrap js after jquery-ui js for tooltip conflict  [#59](https://github.com/blossom-project/blossom/pull/59)  
- Cache improvement [#60](https://github.com/blossom-project/blossom/pull/60)  
- Add run job on application start doc &amp; minor doc changes [#64](https://github.com/blossom-project/blossom/pull/64)  
- Blossom 404 overriding [#65](https://github.com/blossom-project/blossom/issues/65)  
- Login / Forgot your password - Default language [#66](https://github.com/blossom-project/blossom/issues/66)  
- Design search bar  [#67](https://github.com/blossom-project/blossom/issues/67)  
- Wrong password + wrong identifier [#68](https://github.com/blossom-project/blossom/issues/68)  
- Heading title [#69](https://github.com/blossom-project/blossom/issues/69)  
- French translation [#70](https://github.com/blossom-project/blossom/issues/70)  
- Scheduler information &quot;forgets&quot; executions [#71](https://github.com/blossom-project/blossom/issues/71)  
- Crypto module [#72](https://github.com/blossom-project/blossom/pull/72)  
- French translation [#73](https://github.com/blossom-project/blossom/issues/73)  
- Groups with the same name [#74](https://github.com/blossom-project/blossom/issues/74)  
- Menu - I can&#39;t see anything [#75](https://github.com/blossom-project/blossom/issues/75)  
- Fix TriggerHistory loading from DB [#76](https://github.com/blossom-project/blossom/pull/76)  
- Fix reset password sending to activation link instead of change_password link [#77](https://github.com/blossom-project/blossom/pull/77)  
- User/roles association save [#78](https://github.com/blossom-project/blossom/issues/78)  
- Remove double reset password token submit [#79](https://github.com/blossom-project/blossom/pull/79)  
- Entity deletion [#80](https://github.com/blossom-project/blossom/issues/80)  
- Action token dates &amp; activation/password reset tokens invalidation [#81](https://github.com/blossom-project/blossom/pull/81)  
- Add source &amp; javadoc jars when packaging [#82](https://github.com/blossom-project/blossom/pull/82)  
- Change version [#83](https://github.com/blossom-project/blossom/pull/83)  
- Ajout de la génération du controlleur [#84](https://github.com/blossom-project/blossom/pull/84)  
- Set the not blank param in EntityField [#85](https://github.com/blossom-project/blossom/pull/85)  
- External monitoring: filtering and extra information [#87](https://github.com/blossom-project/blossom/issues/87)  
- Disabling default account causes NullPointerException on login fail [#89](https://github.com/blossom-project/blossom/issues/89)  
- Role privilege management [#90](https://github.com/blossom-project/blossom/issues/90)  
- Liquibase changelogs [#91](https://github.com/blossom-project/blossom/pull/91)  
- Add kotlin source language for project initializr [#92](https://github.com/blossom-project/blossom/pull/92)  
- Upgrade boot 2.0.0 [#93](https://github.com/blossom-project/blossom/pull/93)  
- Renaming all packages [#94](https://github.com/blossom-project/blossom/pull/94)  
- Password restrictions on account création [#95](https://github.com/blossom-project/blossom/issues/95)    *enhancement*  
- Override properties values [#96](https://github.com/blossom-project/blossom/issues/96)    *enhancement*  
- Regression (?) HTML in title [#97](https://github.com/blossom-project/blossom/issues/97)    *bug*  
- &quot;404 not found&quot; on item where I can click [#98](https://github.com/blossom-project/blossom/issues/98)    *enhancement*  
- Disconnect doesn&#39;t properly redirect to login. [#99](https://github.com/blossom-project/blossom/issues/99)  
-  utf-8  

