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

  <#if  groups?size != 0>
  <div class="row">
      <form id="associateUserGroupForm" class="form form-horizontal" novalidate method="POST">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
          <input type="hidden" name="userId" value="${user.id?c}" />
          <div class="col-xs-10">
              <select class="form-control m-b" name="groupId">
                  <#list groups as group>
                      <option value="${group.id?c}">${group.name}</option>
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
    page=associatedGroups
    iconPath='fa fa-group'
    columns= {
    "name": {"label":"groups.group.properties.name", "sortable":true, "link":"/blossom/administration/groups/{id}"},
    "dateModification": {"label":"list.modification.date.head", "sortable":true, "type":"datetime"}
    }
  />
  </div>
</div>

<script>


$("#associateUserGroupForm").submit(function(e){
    e.preventDefault();
    var form = this;
     $.post("/blossom/administration/memberships",$(form).serialize(), function () {
          goToGroupsTab();
    });

});

</script>
