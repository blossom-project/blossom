<#import "/spring.ftl" as spring>
<#import "/utils/pagination.ftl" as pagination />
<#import "/utils/sortable.ftl" as sortable/>
<#import "/utils/icon.ftl" as icon/>

<#macro table page columns iconPath="" tableId="">
<div class="table-responsive">
  <table class="table table-striped items" <#if tableId?has_content>id="${tableId}"</#if>>
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
          <div
            class="alert alert-default text-center"><@spring.message "list.no.element.found.label" /></div>
        </td>
      </tr>
      <#else>
        <#list page.content as item>
        <tr id="item_${item.id?c}">
          <#list columns as name, properties>
            <td class="${name}_property">
              <#if properties.link??>
              <a id="item_${item.id?c}_link" class="text-primary clickable"
                <#if item.code??>
                 name="item_code_${item.code}_link"
                </#if>

                 <#assign itemId = item.id/>
                 <#if properties.nestedIdPath??>
                   <#assign resultId = item/>
                   <#list properties.nestedIdPath as pathItem>
                     <#assign resultId = resultId[pathItem]/>
                   </#list>
                   <#assign itemId = resultId/>
                 </#if>
                 href="<@spring.url relativeUrl=properties.link id=itemId />">
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
              <#if properties.nestedPath??>
                <#if properties.nestedPath?has_content && properties.nestedPath?is_sequence>
                  <#assign result = item/>
                  <#list properties.nestedPath as pathItem>
                    <#if result[pathItem]??>
                      <#assign result = result[pathItem]/>
                    </#if>
                  </#list>

                  <@displayProperty value=result type=properties.type label=properties.label/>
                </#if>

                <#if properties.nestedPath?has_content && properties.nestedPath?is_string>
                  <@displayProperty value=item[properties.nestedPath] type=properties.type label=properties.label/>
                </#if>
              <#else>
                <@displayProperty value=item[name] type=properties.type label=properties.label/>
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
</div>
</#macro>

<#macro pagetable page columns label filters=[] iconPath="" searchable=false q="" tableId="">
<div class="ibox float-e-margins">
  <div class="ibox-title">
    <h5><@spring.messageText label label/></h5>
    <div class="ibox-tools">
      <a class="collapse-link">
        <i class="fa fa-chevron-up"></i>
      </a>
    </div>
  </div>

  <div class="ibox-content">
    <div class="row">
      <div class="col-sm-9 m-b-xs">
      </div>
      <div class="col-sm-3">
        <#if searchable>
          <div class="input-group">
            <input type="text"
                   placeholder="<@spring.message "list.searchbar.placeholder"/>"
                   class="table-search input-sm form-control"
                   onkeyup="var which = event.which || event.keyCode;if(which === 13) {$(this).closest('.input-group').find('button.table-search').first().click();}"
              <#if q?has_content> value="${q}"</#if>
            />

            <span class="input-group-btn">
                        <button type="button"
                                class="btn btn-sm btn-primary table-search"
                                onclick="var value = $(this).closest('.input-group').children('input.table-search').first().val();var resetPage = $.updateQueryStringParameter(window.location.href,'page',0);window.location.href = $.updateQueryStringParameter(resetPage,'q',value);">
                            <i class="fa fa-search"></i>
                        </button>
                      </span>
          </div>
        </#if>

      </div>
    </div>
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


<#macro displayProperty value="" label="" type="" >
  <#if type?has_content>
    <#if type == "localdate">
      <#if value?? && !(value?is_string)>${value?date("yyyy-MM-dd")}</#if>
    </#if>
    <#if type == "date">
      <#if value?? && !(value?is_string)>${value?date}</#if>
    </#if>
    <#if type == "time">
      <#if value?? && !(value?is_string)>${value?time}</#if>
    </#if>
    <#if type == "datetime">
      <#if value?? && !(value?is_string)>${value?datetime}</#if>
    </#if>
    <#if type == "boolean">
      <#if value?? && !(value?is_string)><@spring.message value?string('yes','no')/></#if>
    </#if>
    <#if type == "enum">
      <#assign enumLabel = "${label}.${value.toString()}.label" />
      <@spring.messageText enumLabel, value.toString() />
    </#if>
    <#if type == "list">
    <ul>
      <#list value as val>
        <li>${val}</li>
      </#list>
    </ul>
    </#if>
  <#else>
    ${value!""}
  </#if>
</#macro>
