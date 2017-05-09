<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-4">
    <h2>Tâches planifiées</h2>
    <ol class="breadcrumb">
        <li>
            <a href="/blossom"><@spring.message "menu.home"/></a>
        </li>
        <li>
            <a href="/blossom/system"><@spring.message "menu.system"/></a>
        </li>
        <li>
            <a href="/blossom/system/scheduler"><@spring.message "menu.system.scheduler"/></a>
        </li>
        <li class="active">
            <strong>${jobInfo.key.group} - ${jobInfo.key.name}</strong>
        </li>
    </ol>
  </div>
</div>
<div class="wrapper wrapper-content">
</div>
</@master.default>
