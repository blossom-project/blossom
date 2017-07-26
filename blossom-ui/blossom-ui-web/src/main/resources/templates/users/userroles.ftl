<#import "/spring.ftl" as spring>
<#import "/utils/table.ftl" as table>

<@table.table
  page=roles
  iconPath='fa fa-group'
  columns= {
  "name": {"label":"roles.role.properties.name", "sortable":true, "link":"/blossom/administration/roles/{id}"},
  "dateModification": {"label":"list.modification.date.head", "sortable":true, "type":"datetime"}
  }
/>
