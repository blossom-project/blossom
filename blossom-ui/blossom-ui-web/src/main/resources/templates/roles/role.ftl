<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-sm-8">
        <h2><i class="fa fa-key"></i> ${role.name}</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/blossom"><@spring.message "menu.home"/></a>
            </li>
            <li>
                <a href="/blossom/administration"><@spring.message "menu.administration"/></a>
            </li>
            <li>
                <a href="/blossom/administration/roles"><@spring.message "roles.title"/></a>
            </li>
            <li class="active">
                <strong><@spring.message "roles.role.title"/></strong>
            </li>
        </ol>
    </div>
</div>

<div class="wrapper wrapper-content">


    <div class="row">
    <div class="col-sm-12">
      <div class="ibox">
        <div class="ibox-title">
          <h3><@spring.message "panel.information"/></h3>
        </div>
         <div class="ibox-content">
            <form class="form form-horizontal">
                <div class="form-group">
                  <label class="col-sm-2 control-label"><@spring.message "roles.role.properties.name"/></label>
                  <div class="col-sm-10">
                    <p class="form-control-static">${role.name}</p>
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 control-label"><@spring.message "roles.role.properties.description"/></label>
                  <div class="col-sm-10">
                    <p class="form-control-static"> ${role.description!''}</p>
                  </div>
                </div>
            </form>
         </div>
         <div class="ibox-footer">
            <div class="text-right">
             <form id="deleteRole" novalidate method="POST"  action="/blossom/administration/roles/${role.id?c}/_delete">
                <a href="/blossom/administration/roles/${role.id?c}/_edit" class="btn btn-primary"><i class="fa fa-edit"></i> <@spring.message "roles.update"/></a>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <button type="submit" class="btn btn-danger pull-left"><i class="fa fa-trash"></i> <@spring.message "roles.delete"/></a>
              </form>
            </div>
        </div>
      </div>
    </div>
    </div>
</div>
</@master.default>
