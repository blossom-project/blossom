<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>
<#import "/blossom/utils/table.ftl" as table>

<@table.pagetable
  page=articles
  label='articles.label.capitalized'
  iconPath='fa fa-pencil'
  columns= {
  "name": { "label":"articles.article.properties.name", "sortable":true, "link":"/blossom/content/articles/{id}"},
  "modificationDate": {"label":"list.modification.date.head", "sortable":true, "type":"datetime"}
  }
  filters=[]
  searchable=true
  q=q
/>
