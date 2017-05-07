<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>
<#import "/utils/table.ftl" as table>

<@table.pagetable
  page=groups
  label='groups.label'
  iconPath='fa fa-group'
  columns= {
  "name": {"label":"groups.group.properties.name", "sortable":true, "link":"/blossom/administration/groups/{id}"},
  "dateModification": {"label":"list.modification.date.head", "sortable":true, "type":"datetime"}
  }
  filters=[]
  searchable=true
  q=q
/>
