<#import "master.ftl" as master/>

<@master.default>
<style>
  .CodeMirror{
    margin-top:10px;
    margin-bottom:10px;
  }
</style>
<section id="headline" class="container">
  <div class="row">
    <div class="col-lg-12 text-center">
      <div class="navy-line"></div>
      <h1>Blossom</h1>
      <p>Blossom is a Java framework base on Spring-Boot which enables you to realize your projects
        quickly and smoothly.</p>
      <p>It comes with several modules and tools made to ease and speed up your development
        process.</p>
    </div>
  </div>
</section>

<section id="quickstart" class="container documentation">
  <div class="row">
    <div class="col-lg-12">
      <div class="navy-line"></div>
      <h2>Quick start</h2>
      <p>Sadly, there is no quick start yet :'-(</p>
      <p>You must clone the repository, build, install it manually, then you can use the project
        generator to start your first project with Blossom.</p>

      <ol>
        <li>
          Get the jars into your local maven repository
          <ol>
            <li><code>git clone https://github.com/blossom-project/blossom.git</code></li>
            <li><code>cd blossom</code></li>
            <li><code>mvn clean install</code></li>
          </ol>
        </li>
        <li>
          Generate your application
          <ol>
            <li>Go to <a href="http://www.blossom-project.com/initializr" target="_blank">the initializr</a></li>
            <li>Choose the modules you want and generate your app' !</li>
          </ol>
        </li>
      </ol>
    </div>
  </div>
</section>


<section id="features" class="container documentation">
  <div class="row">
    <div class="col-lg-12">
      <div class="navy-line"></div>
      <h2>Features</h2>
      <div class="row">
        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>User Interfaces</h3>
          <div class="row">
            <div class="col-lg-12">
              <h4 class="m-t-lg">Back-office web interface</h4>
              <p>Blossom comes with a back-office interface (module : blossom-starter-ui-web) which can be accessed on <code>/blossom</code>.</p>
            </div>
            <div class="col-lg-12">
              <h4 class="m-t-lg">Back-office API</h4>
              <p>Blossom comes with an API (module : blossom-starter-ui-api) which can be accessed on <code>/blossom</code>, secured with OAuth2.</p>
              <p>The API is a level-2 API on the Richardson Maturity Model.</p>
            </div>
          </div>
        </div>
        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Security</h3>
        </div>
        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Caching</h3>
        </div>
        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Scheduling</h3>
        </div>
        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Generic implementations</h3>
        </div>
        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Application Events</h3>

          <div class="row">
            <div class="col-lg-12">
              <h4 class="m-t-lg">Events</h4>
              <div class="row">
                <div class="col-lg-12">
                  <p>Services extending <code>fr.blossom.core.common.service.GenericCrudServiceImpl</code> or <code>fr.blossom.core.common.service.GenericAssociationServiceImpl</code> benefit from automatic event broadcasting.</p>
                  <p>Theses event types depends on the method called, and contains informations about the DTO being created, modified, deleted, associated, etc. </p>
                  <p>Some events are sent before and after the operation.</p>
                  <div class="b-r-xl p-xs bg-warning m-l-md m-r-md m-t-md m-b-md">
                      <span class="text-white">
                        <i class="fa fa-warning"></i> Be careful when overriding existing generic method on theses classes, as if you don't call super, application event broadcasting will be lost.<br/>
                        Use the constructor provided <code>ApplicationEventPublisher</code> locally to send your own events.
                      </span>
                  </div>
                  <p>The event all extend <code>fr.blossom.core.common.event.Event</code> and blossom provides predefined event for handling with DTO lifecycle.</p>
                  <p>Application specific events can be created and handled following. <a href="https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html#context-functionality-events" target="_blank">See more on Spring documentation here</a>.</p>
                </div>
              </div>
              <div class="row">

                <div class="col-lg-12">
                  <h5 class="m-t-md">CreatedEvent</h5>
                  <p>This event is sent right <strong>after</strong> an <code>AbstractDTO</code> has been created.</p>
                  <p>It contains the newly created DTO which can be accessed using the <code>getDTO()</code> method.</p>
                </div>

                <div class="col-lg-12">
                  <h5 class="m-t-md">UpdatedEvent</h5>
                  <p>This event is sent right <strong>after</strong> an <code>AbstractDTO</code> has been updated.</p>
                  <p>It contains the updated DTO which can be accessed using the <code>getDTO()</code> method.</p>
                </div>

                <div class="col-lg-12">
                  <h5 class="m-t-md">BeforeDeletedEvent</h5>
                  <p>This event is sent right <strong>before</strong> an <code>AbstractDTO</code> has been deleted.</p>
                  <p>It contains the soon-to-be deleted DTO which can be accessed using the <code>getDTO()</code> method.</p>
                </div>

                <div class="col-lg-12">
                  <h5 class="m-t-md">DeletedEvent</h5>
                  <p>This event is sent right <strong>after</strong> an <code>AbstractDTO</code> has been deleted.</p>
                  <p>It contains the deleted DTO which can be accessed using the <code>getDTO()</code> method.</p>
                </div>

                <div class="col-lg-12">
                  <h5 class="m-t-md">AssociatedEvent</h5>
                  <p>This event is sent right <strong>after</strong> an <code>AbstractAssociationDTO</code> between two <code>AbstractDTO</code>s has been created.</p>
                  <p>It contains the deleted DTO which can be accessed using the <code>getDTO()</code> method.</p>
                </div>

                <div class="col-lg-12">
                  <h5 class="m-t-md">BeforeDissociatedEvent</h5>
                  <p>This event is sent right <strong>before</strong> an <code>AbstractAssociationDTO</code> between two <code>AbstractDTO</code>s has been deleted.</p>
                  <p>It contains the soon-to-be DTO which can be accessed using the <code>getDTO()</code> method.</p>
                </div>

                <div class="col-lg-12">
                  <h5 class="m-t-md">AfterDissociatedEvent</h5>
                  <p>This event is sent right <strong>after</strong> an <code>AbstractAssociationDTO</code> between two <code>AbstractDTO</code>s has been deleted.</p>
                  <p>It contains the DTO which can be accessed using the <code>getDTO()</code> method.</p>
                </div>
              </div>
            </div>

            <div class="col-lg-12">
              <h4 class="m-t-lg">Handling</h4>
              <div class="row">
                <div class="col-lg-12">
                  <p>Blossom use generic event handling for dealing with the DTO lifecycle.</p>
                  <p>Theses handlers are used for two</p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Plugin system</h3>
        </div>

        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Indexation and Search</h3>
          <p>Blossom provides utility interfaces and implementations to deal with indexation and search powered by Elasticsearch. (<code>IndexationEngine</code> and <code>SearchEngine</code>)</p>
          <p>The embedded elasticsearch version currently is <strong>2.4.6</strong></p>
          <p>By default, an embedded node will be started, and a client on that node provided.</p>
          <p>You can connect to a standalone Elasticsearch installation by setting the properties below in your application.properties file.</p>
          <textarea class="code" data-cm-mode="properties">
# ELASTICSEARCH (ElasticsearchProperties)
blossom.elasticsearch.cluster-name=elasticsearch # Elasticsearch cluster name.
blossom.elasticsearch.cluster-nodes= # Comma-separated list of cluster node addresses.
blossom.elasticsearch.properties.*= # Additional properties used as the client Settings.</textarea>
        </div>

        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Bean Validation</h3>
          <p>Blossom relies on the default validation capabilities and approaches of Spring.</p>
          <p>Internally, the project prefers to rely on the "JSR-303 Bean Validation API" and provides the message source for error messages.</p>
          <p><a href="https://docs.spring.io/spring/docs/4.1.x/spring-framework-reference/html/validation.html#validation" target="_blank"> See here for more documentation about spring bean validation</a></p>
        </div>

        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Exception handling</h3>
          <p>Blossom provides a generic <code>ErrorControllerAdvice</code> handling various exceptions (<code>Throwable</code>,<code>HttpRequestMethodNotSupportedException</code>,<code>NoHandlerFoundException</code>,<code>NoSuchElementException</code>,<code>AccessDeniedException</code>,) happening inside a <code>@BlossomController</code>.</p>
          <p>For front-end errors and unmanaged exceptions, we provides a <code>BlossomErrorViewResolver</code> which choose between multiple error view locations :</p>
          <ul>
            <li>If the current path starts with <code>/blossom</code>, search the view in <code>src/resources/blossom/error/blossom/</code></li>
            <li>Else search the error view in <code>src/resources/blossom/error/</code></li>
            <li>If a specific template is not found, search <code>src/resources/error/blossom/default.ftl</code> or <code>src/resources/error/default.ftl</code></li>
          </ul>
        </div>

        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Action tokens</h3>
          <p>Blossom provides a <code>ActionTokenService</code> which can be used to encrypt <code>String</code> tokens and decrypt with an expiration date.</p>
          <p>These can be used for temporarily-valid tokens sent by e-mail as an example, but you can use it for any other purpose.</p>
          <p>An <code>ActionToken</code> contains : </p>
          <ul>
            <li>a user id (<code>Long</code>)</li>
            <li>an action (<code>String</code>)</li>
            <li>an expiration date (<code>String</code>)</li>
            <li>a map of additional parameters (<code>Map&lt;String,String&gt;</code></li>
          </ul>
        </div>

        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Rendering & templating</h3>
          <p>Blossom uses <a href="https://freemarker.apache.org/" target="_blank">Freemarker</a> as the template engine for the back-office.</p>
          <p>Any template contained in Blossom can be overriden in your application. Just add a template bearing the same name in the same location and it'll have precedence over the provided one.</p>
          <p>De facto, you can use it for front-end rendering, but you can choose to create an API with</p>
        </div>

        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Database source control</h3>
          <p><a href="http://www.liquibase.org/" target="_blank">Liquibase</a> is used as a Database source control.</p>
          <p>A Blossom project must contains a root changelog containing the following includes.</p>
          <textarea class="code" data-cm-mode="yaml">
databaseChangeLog:
    - includeAll:
        path: classpath*:/db/changelog/blossom
    - includeAll:
        path: classpath*:/db/changelog/blossom/modules
        errorIfMissingOrEmpty: false
    - includeAll:
        path: classpath:/db/changelog/generated
        errorIfMissingOrEmpty: false
          </textarea>
          <p>The <code>classpath*:</code> will look for changesets files in all jars of the classpath, while <code>classpath:</code> prefixed includes will only look for changeset inside your application.</p>
        </div>

        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Internationalisation</h3>
        </div>

        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Mailing</h3>
        </div>

        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Monitoring</h3>
        </div>
      </div>
    </div>
  </div>
</section>

<section id="howtos" class="container documentation">
  <div class="row">
    <div class="col-lg-12">
      <div class="navy-line"></div>
      <h2>How-tos</h2>

      <div class="row">
        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Log in a default Blossom application</h3>
          <p>Blossom comes with a default admin account with identifier <strong>system</strong> and
            password <strong>system</strong>.</p>
          <p>This default configuration can be configured with the following properties</p>
          <div class="row m-t-xs m-b-xs">
            <div class="col-lg-12">
                <textarea class="code" data-cm-mode="properties">
blossom.security.default.account.identifier=system
blossom.security.default.account.password=system</textarea>
            </div>
          </div>
          <p>This default account can be disabled using this property :</p>
          <div class="row m-t-xs m-b-xs">
            <div class="col-lg-12">
<textarea class="code" data-cm-mode="properties">blossom.security.default.account.enabled=false</textarea>
            </div>
          </div>
        </div>

        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Manage the menu</h3>
          <div class="row">
            <div class="col-lg-12">
              <h4 class="m-t-lg">Add a new menu entry</h4>
              <p>To add a new menu entry, juste add a <code>MenuItem</code> bean to your <code>ApplicationContext</code>.</p>
              <p>You can inject in your <code>@Bean</code> annotated method the provided prototype-scoped <code>MenuItemBuilder</code> to simplifies it's creation.</p>

              <ul class="list-unstyled">
                <li><code>key()</code> is mandatory and represents the id of the menu.</li>
                <li><code>label()</code> is the i18n key or not static text</li>
                <li><code>icon()</code> is the css class that will be applied on the icon</li>
                <li><code>link()</code> is the relative url of the page the menu should lead to.</li>
                <li><code>privilege()</code> set the right the logged user should have to see the menu</li>
                <li><code>order()</code> allow to sort the menu entries. (values ranging from Integer.MIN_VALUE to Integer.MAX_VALUE)</li>
                <li><code>leaf()</code> indicates that the menu is a leaf in the hierarchy. If a non-leaf menu has no submenu, it'll not be displayed.</li>
              </ul>
              <div class="row m-t-xs m-b-xs">
                <div class="col-lg-12">
                 <p>The snippet below shows an example <code>MenutItem</code> declaration.</p>
<textarea class="code" data-cm-mode="text/x-java">
@Bean
public MenuItem exampleMenu(MenuItemBuilder builder) {
    return builder
            .key("exampleMenu")
            .label("menu.example.label.key")
            .icon("fa fa-photo")
            .link("/blossom")
            .build();
}</textarea>
              </div>
              </div>

            </div>
            <div class="col-lg-12">
              <h4 class="m-t-lg">Add a new menu subentry</h4>
              <p>To add a submenu entry, use the same process than to add a menu, but set the <code>parent()</code> property by injecting the parent <code>MenuItem</code> using the <code>@Qualifier</code> annotation.</p>
              <div class="row m-t-xs m-b-xs">
                <div class="col-lg-12">
                  <p>The snippet below shows an example of a child <code>MenuItem</code> declaration.</p>
<textarea class="code" data-cm-mode="text/x-java">
@Bean
public MenuItem exampleSubmenu(MenuItemBuilder builder, @Qualifier("exampleMenu") MenuItem testMenuItem) {
    return builder
              .key("exampleSubmenu")
              .label("menu.example.label.key.submenu")
              .link("/blossom")
              .icon("fa fa-picture")
              .parent(testMenuItem) // Setting the parent MenuItem
              .build();
}</textarea>
                </div>
              </div>
            </div>
            <div class="col-lg-12">
              <h4 class="m-t-lg">Open a specific menu on a page</h4>
              <p>Opening a specific menu on a back-office page is easy : add an <code>@OpenedMenu("menuKey")</code> annotation on the <code>@BlossomController</code> class containing the handler or <code>@RequestMapping</code> annotated method.</p>
            </div>
            <div class="col-lg-12">
              <h4 class="m-t-lg">Ordering menus</h4>
              <p>Menus and sub-menus can be ordered relatively to their siblings with the <code>order()</code> method on the MenuItemBuilder</code>.</p>
              <p>Possible values are the whole Integer values, from <code>Integer.MIN_VALUE</code> (highest precedence) to <code>Integer.MAX_VALUE</code> (lowest precedence)</p>
            </div>
          </div>
        </div>

        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Schedule tasks</h3>
          <div class="row">
            <div class="col-lg-12">
              <h4 class="m-t-lg">Configuring the scheduler</h4>
              <p>Blossom uses Quartz Scheduler by default.</p>
              <p>A file name `quartz.properties` can be added to your project to configure it further.</p>
              <p>See all available configurations <a href="http://www.quartz-scheduler.org/documentation/quartz-2.2.x/configuration/" target="_blank">here</a></p>
            </div>
            <div class="col-lg-12">
              <h4 class="m-t-lg">Add a scheduled task</h4>
              <div class="row">
                <div class="col-lg-12">
                  <h5 class="m-t-md">Creating a job</h5>
                  <p>To create a new job, do the following :</p>
                  <ol>
                    <li>Create a class implementing the `org.quartz.Job` interface.</li>
                    <li>Add annotations if necessary (see the example)</li>
                    <li>Autowire existing beans if they are needed by the job</li>
                  </ol>

                  <p>An example is provided below.</p>
                  <textarea class="code" data-cm-mode="text/x-java">
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
                  </textarea>
                </div>
                <div class="col-lg-12">
                  <h5 class="m-t-md">Creating a job detail factory</h5>
                  <p>The job definition and execution being created, you know need to reference it with Quartz in order to schedule it.</p
                  <p>It requires the creation of a `JobDetailFactoryBean` in your application context.</p>
                  <p>This factory doesn't schedule anything, it's a definition of your job.</p>
                  <p>You can configure it by setting :</p>
                  <ul>
                    <li>a group name to regroup jobs that are related (e.g : Indexation).</li>
                    <li>a job name</li>
                    <li>a job description</li>
                    <li>a durability (should the job definition be kept even if it's not scheduled ?)</li>
                  </ul>
                  <p>An example is provided below.</p>

                  <textarea class="code" data-cm-mode="text/x-java">
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
                  </textarea>
                </div>
                <div class="col-lg-12">
                  <h5 class="m-t-md">Creating triggers</h5>
                  <p>Trigger are what really schedule the jobs at given times.</p>
                  <p>They can be named and described.</p>
                  <p>Multiple triggers of different types can be configured for each job detail.</p>

                  <p class="m-t-lg">The <code>SimpleTriggerFactoryBean</code> allows you to define a repeat interval in milliseconds, and a repeat count (possibly indefinitely).</p>
                  <p>This trigger can also be used as a "fire once" trigger with a repeat count of zero.</p>
                  <p>(Note: the "fire once" trigger will be stored and not triggered again when the application restarts. See below for on-application-start trigger)</p>
                  <textarea class="code" data-cm-mode="text/x-java">
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
                  </textarea>

                  <p>The <code>CronTriggerFactoryBean</code> allows you to define cron expression to schedule the job.</p>
                  <textarea class="code" data-cm-mode="text/x-java">
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
                  </textarea>

                </div>
              </div>
            </div>
            <div class="col-lg-12">
              <h4 class="m-t-lg">Start a job dynamically</h4>
              <p>Use the blossom provided ScheduledJobService bean to immediately start a job.</p>

              <textarea class="code" data-cm-mode="text/x-java">
public void startJob(@Qualifier("sampleJobDetail") JobDetail sampleJobDetail) {
      scheduledJobService.execute(sampleJobDetail.getKey());
}
              </textarea>
            </div>
            <div class="col-lg-12">
              <h4 class="m-t-lg">Start a job on application start</h4>
              <p>Use a CommandLineRunner to execute some arbitrary code right after the ApplicationContext has been initialized.</p>
              <textarea class="code" data-cm-mode="text/x-java">
@Bean
public CommandLineRunner startJobOnStart(ScheduledJobService service,
                                  @Qualifier("sampleJabDetail") JobDetail sampleJobDetail) {
  return args -> {
    service.execute(sampleJobDetail.getKey());
  };
}
              </textarea>
            </div>
          </div>
        </div>

        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Adding custom entities</h3>
          <div class="row">
            <div class="col-lg-12">
              <h4 class="m-t-lg">Manually create an entity</h4>
            </div>
            <div class="col-lg-12">
              <h4 class="m-t-lg">Using the generator </h4>
            </div>
          </div>
        </div>

        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Index and search entities</h3>
          <div class="row">
            <div class="col-lg-12">
              <h4 class="m-t-lg">Creating an indexation engine</h4>
            </div>
            <div class="col-lg-12">
              <h4 class="m-t-lg">Indexation lifecycle</h4>
            </div>
            <div class="col-lg-12">
              <h4 class="m-t-lg">Creating a search engine</h4>
            </div>
          </div>
        </div>

        <div class="col-lg-12">
          <div class="navy-line-light"></div>
          <h3>Deploy Blossom as a war</h3>
          <p>The Blossom Initializr provides the possibility to create and initialize a project with WAR packaging instead of JAR.</p>
          <p>In case you want to change an already created Blossom application from jar to war packaging, the Spring-Boot team provides <a target="_blank" href="https://docs.spring.io/spring-boot/docs/current/reference/html/howto-traditional-deployment.html">a handy documentation for that</a></p>
          <p>Don't forget this quote : </p>
          <blockquote>
            <p>Make jar, not war !</p>
            <small><strong><a href="https://twitter.com/starbuxman" target="_blank">Josh Long</a></strong></small>
          </blockquote>
        </div>
      </div>
    </div>
  </div>
</section>

<script>
  $(document).ready(function () {

    $("textarea.code").each(function (index, value) {
      CodeMirror.fromTextArea(value, {
        mode: $(this).data("cm-mode"),
        lineNumbers: true,
        matchBrackets: true,
        readOnly: true
      });
    });
  });
</script>

</@master.default>
