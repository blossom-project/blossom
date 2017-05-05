<#import "/spring.ftl" as spring>
<#import "/utils/pagination.ftl" as pagination />
<#import "/utils/sortable.ftl" as sortable/>
<#import "/utils/icon.ftl" as icon/>

<#macro table page columns iconPath="" tableId="">
<table class="table table-striped" <#if tableId?has_content>id="${tableId}"</#if>>
  <thead>
    <#list columns as name, properties>
    <th>
      <#if properties.sortable?? && properties.sortable>
        <#assign sortOrder = (page.sort??)?then((page.sort.getOrderFor(name)??)?then(page.sort.getOrderFor(name).getDirection().name(),""),"")/>
        <@sortable.icon name=name order=sortOrder/>
      </#if>
	    		<@spring.messageText properties.label properties.label />
    </th>
    </#list>
  </thead>
  <tbody>
    <#if page.content?size == 0>
    <tr>
      <td colspan="${columns?size}">
        <div class="alert alert-default text-center"><@spring.message "list.no.element.found.label" /></div>
      </td>
    </tr>
    <#else>
      <#list page.content as item>
      <tr id="item_${item.id?c}">
        <#list columns as name, properties>
          <td class="${name}_property">
            <#if properties.link??>
            <a id="item_${item.id}_link"
              <#if item.code??>
               name="item_code_${item.code}_link"
              </#if>
               class="text-primary clickable" href="<@spring.url relativeUrl=properties.link id=item.id />">
            <strong>
            </#if>

            <!-- Icon -->
            <#if name?index == 0>
              <#if iconPath?has_content && iconPath?is_sequence>
                <#assign result = item/>
                <#list iconPath as pathItem>
                  <#assign result = result[pathItem]/>
                </#list>
                <@icon.default icon=result/>
              </#if>
              <#if iconPath?has_content &&  iconPath?is_string>
                <@icon.default icon=iconPath/>
              </#if>
            </#if>

            <!-- Value -->
            <#if properties.type??>
              <#if properties.type == "localdate">
                <#if item[name]??>${item[name]?date("yyyy-MM-dd")}</#if>
              </#if>
              <#if properties.type == "date">
                <#if item[name]??>${item[name]?date}</#if>
              </#if>
              <#if properties.type == "time">
                <#if item[name]??>${item[name]?time}</#if>
              </#if>
              <#if properties.type == "datetime">
                <#if item[name]??>${item[name]?datetime}</#if>
              </#if>
              <#if properties.type == "boolean">
                <#if item[name]??>${item[name]?string("oui","non")}</#if>
              </#if>
              <#if properties.type == "enum">
                <#assign enumLabel = "${properties.label}.${item[name].toString()}.label" />
                <@spring.messageText enumLabel, item[name].toString() />
              </#if>
              <#if properties.type == "list">
                <ul>
                  <#list item[name] as value>
                    <li>${value}</li>
                  </#list>
                </ul>
              </#if>
            <#else>
            ${item[name]!""}
            </#if>

            <#if properties.link??>
            </strong></a>
            </#if>
          </td>
        </#list>
      </tr>
      </#list>
    </#if>
  </tbody>
</table>
</#macro>

<#macro pagetable page columns label filters=[] iconPath="" searchable=false q="" tableId="">
<div class="ibox float-e-margins">
  <div class="ibox-title">
    <h5>Basic Table</h5>
    <div class="ibox-tools">
      <a class="collapse-link">
        <i class="fa fa-chevron-up"></i>
      </a>
    </div>
  </div>

  <div class="ibox-content">
    <@table page=page columns=columns iconPath=iconPath tableId=tableId/>
  </div>

  <footer class="ibox-footer">
    <div class="row">
      <div class="col-sm-4 hidden-xs">
      </div>
      <div class="col-sm-4 text-center">
        <@pagination.renderPosition page=page label=label/>
      </div>
      <div class="col-sm-4 text-right text-center-xs">
        <@pagination.renderPagination page=page/>
      </div>
    </div>
  </footer>
</div>
</#macro>
