<#import "/spring.ftl" as spring>
<#import "/utils/table.ftl" as table>

<@table.table
  page=groups
  iconPath='fa fa-group'
  columns= {
  "name": {"label":"groups.group.properties.name", "sortable":true, "link":"/blossom/administration/groups/{id}"},
  "dateModification": {"label":"list.modification.date.head", "sortable":true, "type":"datetime"}
  }
/>
