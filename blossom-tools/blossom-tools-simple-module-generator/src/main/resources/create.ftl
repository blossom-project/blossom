<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>
<#import "/blossom/utils/civility.ftl" as gender>
<#import "/blossom/utils/buttons.ftl" as buttons>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2><i class="fa fa-user"></i> <@spring.message "%%ENTITY_NAME_PLURAL%%.creation"/></h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
        <a href="/blossom/modules"><@spring.message "menu.modules"/></a>
      </li>
      <li>
        <a href="/blossom/administration/users"><@spring.message "%%ENTITY_NAME_PLURAL%%.title"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "%%ENTITY_NAME_PLURAL%%.creation"/></strong>
      </li>
    </ol>
  </div>
</div>
<div class="wrapper wrapper-content">
  <form id="%%CREATE_FORM%%" class="form form-horizontal" novalidate method="POST">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <div class="ibox">
      <div class="ibox-content">

          %%FIELD_FORM%%
        <@spring.bind "%%CREATE_FORM%%.%%FIELD_NAME%%"/>
        <div class="form-group <#if spring.status.error>has-error</#if>">
          <label class="col-sm-2 control-label"><@spring.message "%%FIELD_LABEL%%"/></label>
          <div class="col-sm-10">
            <input type="text" name="%%FIELD_NAME%%" class="form-control" value="${%%CREATE_FORM%%.%%FIELD_NAME%%}"
                   placeholder="<@spring.message "%%FIELD_LABEL%%"/>">
            <#list spring.status.errorMessages as error>
              <span class="help-block text-danger m-b-none">${error}</span>
            </#list>
          </div>
        </div>
          %%/FIELD_FORM%%

      </div>
      <div class="ibox-footer">
        <div class="text-right">
          <button type="submit" class="btn btn-primary"><i class="fa fa-save"></i> <@spring.message "save"/></button>
          <a href="/blossom/modules/%%ENTITY_NAME_PLURAL%%" class="btn btn-default btn-sm"><i class="fa fa-remove"></i> <@spring.message "cancel"/></a>
        </div>
      </div>
    </div>
  </form>
</div>
</@master.default>
