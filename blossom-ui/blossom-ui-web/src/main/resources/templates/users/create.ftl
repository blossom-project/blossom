<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>
<#import "/utils/civility.ftl" as civility>


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
  <div class="ibox">
    <div class="ibox-title">
      <h3><@spring.message "users.creation"/></h3>
    </div>
    <div class="ibox-content">
      <form class="form form-horizontal" method="POST">

        <div class="form-group">
          <label class="col-sm-2 control-label"><@spring.message "users.user.properties.firstname"/></label>

          <div class="col-sm-10">
            <input type="text" name="firstname" class="form-control" value="${userCreateForm.firstname}">
          </div>
        </div>

        <div class="form-group">
          <label class="col-sm-2 control-label"><@spring.message "users.user.properties.lastname"/></label>
          <div class="col-sm-10">
            <input type="text" name="lastname" class="form-control" value="${userCreateForm.lastname}">
          </div>
        </div>

        <div class="form-group">
          <label class="col-sm-2 control-label"><@spring.message "users.user.properties.civility"/></label>
          <div class="col-sm-10">
            <#list civilities as civility>
              <div class="radio radio-inline">
                <input type="radio" value="${civility}" name="civility" <#if userCreateForm.civility==civility>checked</#if>>
                <label><@civility.icon civility=civility/> <@civility.label civility=civility/></label>
              </div>
            </#list>
          </div>
        </div>

        <div class="hr-line-dashed"></div>

        <div class="form-group">
          <label class="col-sm-2 control-label"><@spring.message "users.user.properties.identifier"/></label>
          <div class="col-sm-10">
            <input type="text" name="identifier" class="form-control" value="${userCreateForm.identifier}">
          </div>
        </div>

        <div class="hr-line-dashed"></div>


        <div class="form-group">
          <label class="col-sm-2 control-label"><@spring.message "users.user.properties.email"/></label>
          <div class="col-sm-10">
            <input type="text" name="email" class="form-control" value="${userCreateForm.email}">
          </div>
        </div>
      </form>
    </div>
  </div>
</div>
</@master.default>
