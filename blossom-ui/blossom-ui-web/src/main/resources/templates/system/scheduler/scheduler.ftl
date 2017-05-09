<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-4">
    <h2>Tâches planifiées</h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom">Accueil</a>
      </li>
      <li class="active">
        <strong>Tâches planifiées</strong>
      </li>
    </ol>
  </div>
</div>
<div class="wrapper wrapper-content">
  <div class="wrapper wrapper-content animated fadeInUp">

    <div class="col-lg-12">
      <div class="tabs-container">
        <div class="tabs-left">
          <ul class="nav nav-tabs">
            <#list jobGroupNames as jobGroupName>
              <li class="active"><a data-toggle="tab" href="#${jobGroupName}" aria-expanded="true">${jobGroupName}</a></li>
            </#list>
          </ul>
          <div class="tab-content">
            <#list jobGroupNames as jobGroupName>
              <div id="${jobGroupName}" class="tab-pane active">
                <div class="panel-body">
                      <div class="project-list">

                        <table class="table table-hover">
                          <tbody>
                          <#list jobKeysByGroupNames[jobGroupName] as jobKey>
                            <tr>
                              <td class="project-status">
                                <span class="label label-primary">Active</span>
                              </td>
                              <td class="project-title">
                                <a href="project_detail.html">${jobKey.name}</a>
                                <br>
                                <small>Created 14.08.2014</small>
                              </td>
                              <td class="project-completion">
                                <small>Completion with: 48%</small>
                                <div class="progress progress-mini">
                                  <div style="width: 48%;" class="progress-bar"></div>
                                </div>
                              </td>
                              <td class="project-people">
                                <a href=""><img alt="image" class="img-circle" src="/img/a3.jpg"></a>
                                <a href=""><img alt="image" class="img-circle" src="/img/a1.jpg"></a>
                                <a href=""><img alt="image" class="img-circle" src="/img/a2.jpg"></a>
                                <a href=""><img alt="image" class="img-circle" src="/img/a4.jpg"></a>
                                <a href=""><img alt="image" class="img-circle" src="/img/a5.jpg"></a>
                              </td>
                              <td class="project-actions">
                                <a href="#" class="btn btn-white btn-sm"><i class="fa fa-folder"></i> View </a>
                                <a href="#" class="btn btn-white btn-sm"><i class="fa fa-pencil"></i> Edit </a>
                              </td>
                            </tr>
                          </#list>
                          </tbody>
                        </table>
                      </div>
                </div>
              </div>
            </#list>
          </div>

        </div>

      </div>
    </div>

  </div>
</div>
</@master.default>
