<#import "master.ftl" as master/>

<@master.default>

  <section id="generator" class="container">
    <div class="row">
      <div class="col-lg-12 text-center">
        <div class="navy-line"></div>
        <h1>Generate your project</h1>
        <p>Name it, select any modules and start your own Blossom-based project !</p>
      </div>
    </div>

    <form method="post" action="/initializr" class="form-horizontal">
      <div class="row m-t-xl">
        <div class="col-md-6">
          <div class="form-group">
            <label class="col-sm-2 control-label">Blossom version</label>
            <div class="col-sm-10">
              <select class="form-control"  name="version"><#list initializr.versions as version><option value="${version.blossom}" <#if version?is_first>selected="selected"</#if>>${version.blossom}</option></#list></select>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">Group</label>
            <div class="col-sm-10">
              <input class="form-control" type="text" name="groupId" value="${project.groupId}"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">ArtifactId</label>
            <div class="col-sm-10">
              <input class="form-control" type="text" name="artifactId" value="${project.artifactId}"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">Packaging mode</label>
            <div class="col-sm-10">
              <select class="form-control" name="packagingMode">
                <#list packagingModes as packagingMode>
                  <option value="${packagingMode}">${packagingMode}</option>
                </#list>
              </select>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group">
            <label class="col-sm-2 control-label">Name</label>
            <div class="col-sm-10">
              <input class="form-control" type="text" name="name" value="${project.name}"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">Description</label>
            <div class="col-sm-10">
              <input class="form-control" type="text" name="description" value="${project.description}"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">Package name</label>
            <div class="col-sm-10">
              <input class="form-control" type="text" name="packageName" value="${project.packageName}"/>
            </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">Sources language</label>
            <div class="col-sm-10">
              <select class="form-control" name="sourceLanguage">
                <#list sourceLanguages as sourceLanguage>
                  <option value="${sourceLanguage.name()}" <#if (sourceLanguage.default)!false>selected="selected"</#if>>${sourceLanguage.displayName}</option>
                </#list>
              </select>
            </div>
          </div>
        </div>
      </div>

    <div class="row">
      <#list initializr.groups as group>
        <div class="panel col-md-6">
          <h2>${group.name}</h2>
          <p>${group.description}</p>

          <div>
            <#list group.dependencies as dependency>
              <div class="form-group">
                <div class="col-sm-12">
                  <input type="checkbox" value="${dependency.id}" name="dependencies" <#if project.dependencies?seq_contains(dependency.id)>checked="checked"</#if>/>
                  ${dependency.name}
                  <p class="text-muted text-normal">${dependency.description}</p>
                </div>
              </div>
            </#list>
          </div>
        </div>
      </#list>
    </div>

    <div class="row text-center">
      <button class="btn btn-lg btn-primary" type="submit">
        Generate project
      </button>
    </div>

    </form>
  </section>

</@master.default>
