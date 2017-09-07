<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>
<#import "/utils/civility.ftl" as gender>
<#import "/utils/buttons.ftl" as buttons>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2><i class="fa fa-user"></i> <@spring.message "users.creation"/></h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
        <a href="/blossom/administration"><@spring.message "menu.administration"/></a>
      </li>
      <li>
        <a href="/blossom/administration/users"><@spring.message "users.title"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "users.creation"/></strong>
      </li>
    </ol>
  </div>
</div>
<div class="wrapper wrapper-content">
  <form id="userCreateForm" class="form form-horizontal" novalidate method="POST">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <div class="ibox">
      <div class="ibox-content">

        <@spring.bind "userCreateForm.firstname"/>
        <div class="form-group <#if spring.status.error>has-error</#if>">
          <label class="col-sm-2 control-label"><@spring.message "users.user.properties.firstname"/></label>
          <div class="col-sm-10">
            <input type="text" name="firstname" class="form-control" value="${userCreateForm.firstname}"
                   placeholder="<@spring.message "users.user.properties.firstname"/>">
            <#list spring.status.errorMessages as error>
              <span class="help-block text-danger m-b-none">${error}</span>
            </#list>
          </div>
        </div>

        <@spring.bind "userCreateForm.lastname"/>
        <div class="form-group <#if spring.status.error>has-error</#if>">
          <label class="col-sm-2 control-label"><@spring.message "users.user.properties.lastname"/></label>
          <div class="col-sm-10">
            <input type="text" name="lastname" class="form-control" value="${userCreateForm.lastname}"
                   placeholder="<@spring.message "users.user.properties.lastname"/>">
            <#list spring.status.errorMessages as error>
              <span class="help-block text-danger m-b-none">${error}</span>
            </#list>
          </div>
        </div>

        <@spring.bind "userCreateForm.civility"/>
        <div class="form-group <#if spring.status.error>has-error</#if>">
          <label class="col-sm-2 control-label"><@spring.message "users.user.properties.civility"/></label>
          <div class="col-sm-10">
            <#list civilities as civility>
              <div class="radio radio-success radio-inline">
                <input type="radio" class="radio" value="${civility}" id="gender_${civility}" name="civility" <#if userCreateForm.civility == civility>checked</#if>>
                <label for="gender_${civility}"> <@gender.icon civility=civility/> <@gender.label civility=civility/> </label>
              </div>
            </#list>

            <#list spring.status.errorMessages as error>
              <span class="help-block text-danger m-b-none">${error}</span>
            </#list>
          </div>
        </div>

        <@spring.bind "userCreateForm.locale"/>
        <div class="form-group <#if spring.status.error>has-error</#if>">
          <label class="col-sm-2 control-label"><@spring.message "users.user.properties.locale"/></label>
          <div class="col-sm-10">
            <select class="form-control m-b" name="locale">
              <#list locales as locale>
                <option value="${locale}" <#if locale == userCreateForm.locale>selected</#if> >
                  ${locale.getDisplayLanguage(currentLocale)}<#if locale.getDisplayCountry(currentLocale)?has_content> / ${locale.getDisplayCountry(currentLocale)}</#if>
                </option>
              </#list>
            </select>
            <#list spring.status.errorMessages as error>
              <span class="help-block text-danger m-b-none">${error}</span>
            </#list>
          </div>
        </div>

        <div class="hr-line-dashed"></div>

        <@spring.bind "userCreateForm.identifier"/>
        <div class="form-group <#if spring.status.error>has-error</#if>">
          <label class="col-sm-2 control-label"><@spring.message "users.user.properties.identifier"/></label>
          <div class="col-sm-10">
            <input type="text" name="identifier" class="form-control" value="${userCreateForm.identifier}" placeholder="<@spring.message "users.user.properties.identifier"/>">

            <#list spring.status.errorMessages as error>
              <span class="help-block text-danger m-b-none">${error}</span>
            </#list>
          </div>
        </div>

        <div class="hr-line-dashed"></div>

        <@spring.bind "userCreateForm.email"/>
        <div class="form-group <#if spring.status.error>has-error</#if>">
          <label class="col-sm-2 control-label"><@spring.message "users.user.properties.email"/></label>
          <div class="col-sm-10">
            <input type="email" name="email" class="form-control" value="${userCreateForm.email}"
                   placeholder="<@spring.message "users.user.properties.email"/>">
            <#list spring.status.errorMessages as error>
              <span class="help-block text-danger m-b-none">${error}</span>
            </#list>
          </div>
        </div>
      </div>
      <div class="ibox-footer">
        <div class="text-right">
          <button type="submit" class="btn btn-primary"><i class="fa fa-save"></i> <@spring.message "save"/></button>
          <a href=".." class="btn btn-default btn-sm"><i class="fa fa-remove"></i> <@spring.message "cancel"/></a>
        </div>
      </div>
    </div>
  </form>
</div>
</@master.default>
