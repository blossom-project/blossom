<#import "master.ftl" as master/>

<@master.default>
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

<section id="documentation" class="container">
  <div class="row">
    <div class="col-lg-12">
      <div class="navy-line"></div>
      <h2>Quick start</h2>
      <p>Sadly, there is no quick start yet :'-(</p>
      <p>You must clone the repository, build, install it manually, then you can use the project
        generator to start your first project with Blossom.</p>

      <ol>
        <li>git clone https://github.com/blossom-project/blossom.git</li>
        <li>cd blossom</li>
        <li>mvn clean install</li>
        <li>java -jar
          blossom-tools/blossom-tools-initializr/target/blossom-tools-initializr-0.0.1-SNAPSHOT.jar
        </li>
        <li>Go to localhost:8090</li>
        <li>Choose your modules and generate your project !</li>
      </ol>
    </div>
  </div>


  <div class="row">
    <div class="col-lg-12">
      <div class="navy-line"></div>
      <h2>Features</h2>
      <div class="row">
        <div class="col-lg-12">
          <h3>UIs</h3>
          <div class="row">
            <div class="col-lg-12">
              <h4>Back-office web interface</h4>
            </div>
            <div class="col-lg-12">
              <h4>Back-office API</h4>
            </div>
          </div>
        </div>
        <div class="col-lg-12">
          <h3>Security</h3>
        </div>
        <div class="col-lg-12">
          <h3>Caching</h3>
        </div>
        <div class="col-lg-12">
          <h3>Scheduling</h3>
        </div>
        <div class="col-lg-12">
          <h3>Application Events</h3>
        </div>
        <div class="col-lg-12">
          <h3>Plugin system</h3>
        </div>
        <div class="col-lg-12">
          <h3>Indexation and Search</h3>
          <p>Blossom provides utility classes to deal with indexation and search powered by Elasticsearch.</p>
          <p>The embedded elasticsearch version currently is <strong>2.4.6</strong></p>
        </div>
        <div class="col-lg-12">
          <h3>Bean Validation</h3>
        </div>
        <div class="col-lg-12">
          <h3>Exception handling</h3>
        </div>
        <div class="col-lg-12">
          <h3>Action tokens</h3>
        </div>
        <div class="col-lg-12">
          <h3>Rendering & templating</h3>
        </div>
        <div class="col-lg-12">
          <h3>Database source control</h3>
        </div>
        <div class="col-lg-12">
          <h3>Internationalisation</h3>
        </div>
        <div class="col-lg-12">
          <h3>Mailing</h3>
        </div>
        <div class="col-lg-12">
          <h3>Monitoring</h3>
        </div>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-lg-12">
      <div class="navy-line"></div>
      <h2>How-tos</h2>

      <div class="row">
        <div class="col-lg-12">
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
          <h3>Manage the menu</h3>
          <div class="row">
            <div class="col-lg-12">
              <h4>Add a new menu entry</h4>
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
              <h4>Add a new menu subentry</h4>
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
              <h4>Open a specific menu on a page</h4>
            </div>
            <div class="col-lg-12">
              <h4>Ordering menus</h4>
            </div>
          </div>
        </div>

        <div class="col-lg-12">
          <h3>Schedule tasks</h3>
          <div class="row">
            <div class="col-lg-12">
              <h4>Configuring the scheduler</h4>
            </div>
            <div class="col-lg-12">
              <h4>Add a scheduled task</h4>
              <div class="row">
                <div class="col-lg-12">
                  <h5>Creating a job</h5>
                </div>
                <div class="col-lg-12">
                  <h5>Creating a job detail factory</h5>
                </div>
                <div class="col-lg-12">
                  <h5>Creating triggers</h5>
                </div>
              </div>
            </div>
            <div class="col-lg-12">
              <h4>Add a scheduled task</h4>
            </div>
          </div>
        </div>

        <div class="col-lg-12">
          <h3>Adding custom entities</h3>
          <div class="row">
            <div class="col-lg-12">
              <h4>Manually create an entity</h4>
            </div>
            <div class="col-lg-12">
              <h4>Using the generator </h4>
            </div>
          </div>
        </div>

        <div class="col-lg-12">
          <h3>Index and search entities</h3>
          <div class="row">
            <div class="col-lg-12">
              <h4>Creating an indexation engine</h4>
            </div>
            <div class="col-lg-12">
              <h4>Indexation lifecycle</h4>
            </div>
            <div class="col-lg-12">
              <h4>Creating a search engine</h4>
            </div>
          </div>
        </div>

        <div class="col-lg-12">
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
      console.log(index, value, $(this));
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
