<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>
<#import "/utils/table.ftl" as table>

<@table.pagetable
  page=roles
  label='roles.label'
  iconPath='fa fa-key'
  columns= {
  "name": {"label":"roles.role.properties.name", "sortable":true, "link":"/blossom/administration/roles/{id}"},
  "modificationDate": {"label":"list.modification.date.head", "sortable":true, "type":"datetime"}
  }
  filters=[]
  searchable=true
  q=q
/>
