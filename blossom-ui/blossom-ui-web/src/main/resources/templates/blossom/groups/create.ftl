<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
     <div class="col-sm-8">
        <h2><i class="fa fa-key"></i> <@spring.message "groups.title"/></h2>
        <ol class="breadcrumb">
            <li>
                <a href="/blossom"><@spring.message "menu.home"/></a>
            </li>
            <li>
                <@spring.message "menu.administration"/>
            </li>
            <li class="active">
                <strong><@spring.message "groups.title"/></strong>
            </li>
        </ol>
    </div>
</div>
<div class="wrapper wrapper-content">
  <form id="groupCreateForm" class="form form-horizontal" novalidate method="POST">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <div class="ibox">
      <div class="ibox-content">

        <@spring.bind "groupCreateForm"/>
        <#if spring.status.error>
          <p class="alert alert-danger">
            <#list spring.status.errorMessages as error>
            ${error}<#if !error?is_last><br/></#if>
            </#list>
          </p>
        </#if>

        <@spring.bind "groupCreateForm.name"/>
        <div class="form-group <#if spring.status.error>has-error</#if>">
          <label class="col-sm-2 control-label"><@spring.message "groups.group.properties.name"/></label>
          <div class="col-sm-10">
            <input type="text" name="name" class="form-control" value="${groupCreateForm.name}"
                   placeholder="<@spring.message "groups.group.properties.name"/>">
            <#list spring.status.errorMessages as error>
              <span class="help-block text-danger m-b-none">${error}</span>
            </#list>
          </div>
        </div>

        <@spring.bind "groupCreateForm.description"/>
        <div class="form-group <#if spring.status.error>has-error</#if>">
          <label class="col-sm-2 control-label"><@spring.message "groups.group.properties.description"/></label>
          <div class="col-sm-10">
            <input type="text" name="description" class="form-control" value="${groupCreateForm.description}"
                   placeholder="<@spring.message "groups.group.properties.description"/>">
            <#list spring.status.errorMessages as error>
              <span class="help-block text-danger m-b-none">${error}</span>
            </#list>
          </div>
        </div>
      </div>
      <div class="ibox-footer">
        <div class="text-right">
          <button type="submit" class="btn btn-primary"><i class="fa fa-save"></i> <@spring.message "save"/></button>
          <a href="." class="btn btn-default btn-sm"><i class="fa fa-remove"></i> <@spring.message "cancel"/></a>
        </div>
      </div>
    </div>
  </form>
</div>
</@master.default>
