<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>
<#import "/utils/table.ftl" as table>

<@table.pagetable
  page=users
  label='users.label'
  iconPath='fa fa-user'
  columns= {
  "firstname": {"label":"users.user.properties.firstname", "sortable":true, "link":"/blossom/administration/users/{id}"},
  "lastname": {"label":"users.user.properties.lastname", "sortable":true, "link":"/blossom/administration/users/{id}"},
  "identifier": {"label":"users.user.properties.identifier", "sortable":true},
  "function": {"label":"users.user.properties.function", "sortable":true},
  "company": {"label":"users.user.properties.company", "sortable":true},
  "email": {"label":"users.user.properties.email", "sortable":true},
  "phone": {"label":"users.user.properties.phone", "sortable":false},
  "lastConnection": {"label":"users.user.properties.lastConnection", "sortable":true, "type":"datetime"},
  "dateModification": {"label":"list.modification.date.head", "sortable":true, "type":"datetime"}
  }
  filters=[]
  searchable=true
  q=q
/>
