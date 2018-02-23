<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>
<#import "/blossom/utils/civility.ftl" as gender>
<#import "/blossom/utils/buttons.ftl" as buttons>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2><i class="fa fa-question"></i> <@spring.message "%%ENTITY_NAME_PLURAL%%.title"/></h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
       <@spring.message "menu.modules"/>
      </li>
      <li>
        <a href="/blossom/modules/%%ENTITY_NAME_PLURAL%%"><@spring.message "%%ENTITY_NAME_PLURAL%%.title"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "%%ENTITY_NAME_PLURAL%%.%%ENTITY_NAME%%.label"/></strong>
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
          %%FIELD_FORM_INPUT%%
        <@spring.bind "%%CREATE_FORM%%.%%FIELD_NAME%%"/>
        <div class="form-group <#if spring.status.error>has-error</#if>">
          <label class="col-sm-2 control-label">%%FIELD_LABEL%%</label>
          <div class="col-sm-10">
            <input type="%%FIELD_TYPE%%" name="%%FIELD_NAME%%" class="form-control" value="<#if %%CREATE_FORM%%.%%FIELD_NAME%%??>${%%CREATE_FORM%%.%%FIELD_NAME%%%%FIELD_CAST%%}</#if>"
                   placeholder="%%FIELD_LABEL%%">
            <#list spring.status.errorMessages as error>
              <span class="help-block text-danger m-b-none">${error}</span>
            </#list>
          </div>
        </div>
          %%/FIELD_FORM_INPUT%%

          %%FIELD_FORM_BOOLEAN%%
          <@spring.bind "%%CREATE_FORM%%.%%FIELD_NAME%%"/>
          <div class="form-group">
              <label class="col-sm-2 control-label">%%FIELD_LABEL%%</label>
              <div class="col-sm-10">
                  <#if %%CREATE_FORM%%.%%FIELD_NAME%%??>
                      <@buttons.switch checked=%%CREATE_FORM%%.%%FIELD_NAME%% name="%%FIELD_NAME%%"/>
                  <#else>
                      <@buttons.switch checked=false name="%%FIELD_NAME%%"/>
                  </#if>

              </div>
          </div>
          %%/FIELD_FORM_BOOLEAN%%

          %%FIELD_FORM_SELECT%%
          <@spring.bind "%%CREATE_FORM%%.%%FIELD_NAME%%"/>
          <div class="form-group">
              <label class="col-sm-2 control-label">%%FIELD_LABEL%%</label>
              <div class="col-sm-10">
                <select class="form-control" name="%%FIELD_NAME%%">
                    %%FIELD_FORM_SELECT_OPTION%%
                        <option value="%%OPTION_VALUE%%" <#if "%%OPTION_VALUE%%"==%%CREATE_FORM%%.%%FIELD_NAME%%!"">selected</#if>>%%OPTION_LABEL%%</option>
                    %%/FIELD_FORM_SELECT_OPTION%%
                </select>
                  <#list spring.status.errorMessages as error>
                      <span class="help-block text-danger m-b-none">${error}</span>
                  </#list>
              </div>
          </div>
          %%/FIELD_FORM_SELECT%%

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
