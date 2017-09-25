<#import "/spring.ftl" as spring>
<#import "/utils/table.ftl" as table>

<div class="ibox-content">
  <div class="sk-spinner sk-spinner-wave">
    <div class="sk-rect1"></div>
    <div class="sk-rect2"></div>
    <div class="sk-rect3"></div>
    <div class="sk-rect4"></div>
    <div class="sk-rect5"></div>
  </div>

  <#if  roles?size != 0>
  <div class="row">
      <form id="associateUserRoleForm"  class="form form-horizontal" novalidate method="POST">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
          <input type="hidden" name="userId" value="${user.id?c}" />
          <div class="col-xs-10">
              <select class="form-control m-b" name="roleId">
                  <#list roles as role>
                      <option value="${role.id?c}">${role.name}</option>
                  </#list>
              </select>
          </div>
          <div class="col-xs-2">
              <div class="tex-center">
                 <button type="submit" class="btn btn-primary"><i class="fa fa-plus"></i></button>
              </div>
          </div>
      </form>
  </div>
  </#if>

  <div class="row">
  <@table.table
    page=associatedRoles
    iconPath='fa fa-group'
    columns= {
    "name": {"label":"roles.role.properties.name", "sortable":true, "link":"/blossom/administration/roles/{id}"},
    "dateModification": {"label":"list.modification.date.head", "sortable":true, "type":"datetime"}
    }
  />
  </div>
</div>

<script>


$("#associateUserRoleForm").submit(function(e){
    e.preventDefault();
    var form = this;
     $.post("/blossom/administration/responsability",$(form).serialize(), function () {
          goToRolesTab();
    });

});

</script>
