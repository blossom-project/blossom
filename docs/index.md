# Blossom

Blossom is a Java framework base on Spring-Boot which enables you to realize your projects quickly and smoothly.
It comes with several modules and tools made to ease and speed up your development process.

## Quick start

Sadly, there is no quick start yet :'-(

You must clone the repository, build, install it manually, then you can use the project generator to start your first project with Blossom.

1. git clone https://github.com/blossom-project/blossom.git
2. cd blossom
3. mvn clean install
4. java -jar blossom-tools/blossom-tools-initializr/target/blossom-tools-initializr-0.0.1-SNAPSHOT.jar
5. Go to localhost:8090
6. Choose your modules and generate your project !

## How-tos

### Deploy Blossom as a war
Spring boot has a pretty straigthforward documentation for that :
[https://docs.spring.io/spring-boot/docs/current/reference/html/howto-traditional-deployment.html](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-traditional-deployment.html)

As for blossom, there is the possibility to initialize a project with jar or war packaging.
Juste choose a packaging mode in the select of the initalization page blossom will do the rest.

### Menu
#### Add a new menu entry
To add a new menu entry, juste add a MenuItem bean.
You can use the capabilities of an injected MenuItemBuilder to simplifies it's creation.

key() is mandatory and represents the id of the menu.
label() is the i18n key or not static text
icon() is the css class that will be applied on the icon
link() is the relative url of the page the menu should lead to.
privilege() set the right the logged user should have to see the menu
order() allow to sort the menu entries. (values ranging from Integer.MIN_VALUE to Integer.MAX_VALUE)
leaf() indicates that the menu is a leaf in the hierarchy. If a non-leaf menu has no submenu, it'll not be displayed.

```java
@Bean
public MenuItem testMenuItem(MenuItemBuilder builder) {
    return builder.key("testMenu").label("menu.test", true).icon("fa fa-photo").link("/blossom/test").build();
}
```

#### Add a new menu subentry
To add a submenu entry, use the same process than to add a menu, but set the parent property by injecting the parent MenuItem using the @Qualifier annotation.

```java
@Bean
public MenuItem testSubMenuItem(MenuItemBuilder builder, @Qualifier("testMenuItem") MenuItem testMenuItem) {
    return builder.key("testSubMenu").label("menu.test.submenu", true).link("/blossom/test/submenu").icon("fa fa-photo").parent(testMenuItem).build();
}
```

#### Open a specific menu on a page
#### Ordering menus

### Scheduling
#### Configuring the scheduler
Blossom uses Quartz Scheduler by default.
A file name `quartz.properties` can be added to your project to configure it further.

See all available configurations here : [http://www.quartz-scheduler.org/documentation/quartz-2.2.x/configuration/](http://www.quartz-scheduler.org/documentation/quartz-2.2.x/configuration/)

#### Add a scheduled task

##### Creating a job
Creating a new job is quite simple :
1. Create a class implementing the `org.quartz.Job` interface.
2. Add annotations if necessary (see the example)
3. Autowire existing beans if they are needed by the job

```java
  import org.quartz.Job;

  @DisallowConcurrentExecution  
  @PersistJobDataAfterExecution
  public class SampleJob implements Job {
    private final Logger LOGGER = LoggerFactory.getLogger(SampleJob.class);

    @Autowired // Jobs can be Autowired with beans from context
    private ApplicationContext applicationContext;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
          // Do something here ...
    }
  }
```

##### Creating a job detail factory
The job definition and execution being created, you know need to reference it with Quartz in order to schedule it.
It requires the creation of a `JobDetailFactoryBean` in your application context.
This factory doesn't schedule anything, it's a definition of your job.
You can configure it by setting :
1. a group name to regroup jobs that are related (e.g : Indexation).
2. a job name
3. a job description
4. a durability (should the job definition be kept even if it's not scheduled ?)


```java
  @Bean
  public JobDetailFactoryBean sampleJobDetail() {
    JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
    factoryBean.setJobClass(SampleJob.class);
    factoryBean.setGroup("Sample");
    factoryBean.setName("Sample Job");
    factoryBean.setDescription("Sample job for demonstration purpose");
    factoryBean.setDurability(true);
    return factoryBean;
  }
```

##### Creating triggers
Trigger are what really schedule the jobs at given times.
They can be named and described.
Multiple triggers of different types can be configured for each job detail.

The `SimpleTriggerFactoryBean` allows you to define a repeat interval in milliseconds, and a repeat count (possibly indefinitely).
This trigger can also be used as a "fire once" trigger with a repeat count of zero. 
(Note: the "fire once" trigger will be stored and not triggered again when the 
application restarts. See below for on-application-start trigger)

```java
  @Bean
  public SimpleTriggerFactoryBean sampleJobCronTrigger(@Qualifier("sampleJobDetail") JobDetail sampleJobDetail) {
    SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
    factoryBean.setName("Cron trigger");
    factoryBean.setDescription("This is a cron trigger for demonstration purpose");
    factoryBean.setJobDetail(sampleJobDetail);
    factoryBean.setStartDelay((long) 30 * 1000);
    factoryBean.setRepeatInterval(1 * 60 * 60 * 1000);
    factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
    return factoryBean;
  }
```


The `CronTriggerFactoryBean` allows you to define cron expression to schedule the job.

```java
  @Bean
  public CronTriggerFactoryBean sampleJobSimpleTrigger(@Qualifier("sampleJobDetail") JobDetail sampleJobDetail) {
    CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
    factoryBean.setName("Simple trigger");
    factoryBean.setDescription("This is a simple trigger for demonstration purpose");
    factoryBean.setJobDetail(sampleJobDetail);
    factoryBean.setStartDelay(10L);
    factoryBean.setCronExpression("0/30 * * * * ?");
    factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY);
    return factoryBean;
  }
```

To run a job on application start:

```java
  @Bean
  public CommandLineRunner initJobs(ScheduledJobService service, 
                                    @Qualifier("sampleJabDetail") JobDetail sampleJobDetail) {
    return args -> {
      service.execute(sampleJobDetail.getKey());
    };
  }
```

### Adding an entity
#### Manually create an entity
#### Index and search an entity
#### Generate a (simple) entity

### Adding an association between two entities
#### Manually create an association

## Features

### Back-office
Blossom comes with a back-office interface (module : blossom-ui-web) which can be accessed on http://localhost:8080/blossom.
You can then connect with the default user and password : system / system.

This default user can be configured with the following properties :

```ini
blossom.security.default.account.enabled=true
blossom.security.default.account.identifier=system
blossom.security.default.account.password=system
```

### Security

### Caching

### Application Events

### Plugin system

### Indexation and Search
Blossom provides utility classes to deal with indexation and search powered by Elasticsearch.
The elasticsearch version currently is 2.4.6.

#### Elasticsearch Client configuration
Blossom relies on Elasticsearch to index and search entities.
By default, an embedded node will be started, and a client on that node provided.
You can connect to a standalone Elasticsearch installation by setting the properties below in your application.properties file.

```ini
# ELASTICSEARCH (ElasticsearchProperties)
blossom.elasticsearch.cluster-name=elasticsearch # Elasticsearch cluster name.
blossom.elasticsearch.cluster-nodes= # Comma-separated list of cluster node addresses.
blossom.elasticsearch.properties.*= # Additional properties used to configure the client.
```

#### Creating an indexation engine
Add a Bean of type `IndexationEngine` in your context.
The default implementation is `IndexationEngineImpl`. It depends on the ES `Client`, a `ReadOnlyService`, a `BulkProcessor`, a jackson `ObjectMapper` and a `IndexationEngineConfiguration`

```java
    @Bean
    public IndexationEngineImpl<ExampleDTO> exampleIndexationEngine(
      Client client, // Provided by Blossom
      UserService userService, 
      BulkProcessor bulkProcessor, // Provided by Blossom
      ObjectMapper objectMapper,// Provided by Blossom
      IndexationEngineConfiguration<ExampleDTO> exampleIndexationEngineConfiguration
      ) {
      return new IndexationEngineImpl<>(client, userService, bulkProcessor, objectMapper,
        exampleIndexationEngineConfiguration);
    }
    
```

#### Indexation lifecycle


#### Creating a search engine



### Bean Validation

### Exception Handling

### Action tokens

### Rendering

### Liquibase

### Scheduling

### I18n

### Mails




