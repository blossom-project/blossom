<#import "/spring.ftl" as spring>

<#macro renderPagination page mode="url" handler="">
    <#assign current = page.number/>
    <#assign total = page.totalPages/>
    <#assign isFirstPage = page.first />
    <#assign isLastPage = page.last />

    <#if total gt 1 >
        <#if total gt 5 >
            <#if current lte 3>
                <#assign pageNumberToShow = 0..4 />
            <#elseif current gt (total - 3)>
                <#assign pageNumberToShow = total-5..total-1 />
            <#else>
                <#assign pageNumberToShow = current-2..current+2 />
            </#if>
        <#else>
            <#assign pageNumberToShow = 0..total-1 />
        </#if>

    <ul class="pagination pagination-sm m-t-none m-b-none">
        <li class="<#if isFirstPage >disabled</#if>">
            <a
                <#if mode == "url"><#if !isFirstPage >class="clickable"
                        onclick="window.location.href = $.updateQueryStringParameter(window.location.href,'page',${current-1})"</#if></#if>
                <#if mode == "custom"><#if !isFirstPage >class="clickable"
                        onclick="${handler}(${current-1})"</#if></#if>
            >
                <span><i class="fa fa-chevron-left"></i></span>
            </a>
        </li>

        <#list pageNumberToShow as pageNumber>
            <li class="<#if pageNumber == current >active</#if>">
                <a
                    <#if mode == "url"><#if pageNumber != current>class="clickable"
                            onClick="window.location.href = $.updateQueryStringParameter(window.location.href,'page',${pageNumber})"</#if></#if>
                    <#if mode == "custom"><#if pageNumber != current>class="clickable"
                            onclick="${handler}(${pageNumber})"</#if></#if>
                >
                ${pageNumber+1}
                </a>
            </li>
        </#list>

        <li class="<#if isLastPage >disabled</#if>">
            <a id="nextPage"
                <#if mode == "url">
                    <#if !isLastPage >
               class="clickable"
               onclick="window.location.href = $.updateQueryStringParameter(window.location.href,'page',${current+1})"
                    <#else>
               disabled
                    </#if>
                </#if>
                <#if mode == "custom"><#if !isLastPage >class="clickable" onclick="${handler}(${current+1})"</#if></#if>
            >
                <span><i class="fa fa-chevron-right"></i></span>
            </a>
        </li>
    </ul>
    </#if>
</#macro>



<#macro renderPosition page label>
    <#assign index = (page.number * page.size) >
    <#assign indexMin = index + 1>
    <#assign indexMax = index + page.numberOfElements>
    <#assign totalElements = page.totalElements>
    <#assign paginationArgs = [index + 1, index + page.numberOfElements, page.totalElements]>
<small class="text-muted inline m-t-sm m-b-sm"><@spring.messageText label label /> <@spring.messageText "list.pagination.detail.label" paginationArgs /></small>
</#macro>


<#macro pageSize page>
    <#if page??>
    <select onChange="window.location.href = $.updateQueryStringParameter(window.location.href,'size',this.value);">
        <option value="10" <#if page.size == 10>selected="selected"</#if>>10</option>
        <option value="25" <#if page.size == 25>selected="selected"</#if>>25</option>
        <option value="50" <#if page.size == 50>selected="selected"</#if>>50</option>
        <option value="75" <#if page.size == 75>selected="selected"</#if>>75</option>
        <option value="100" <#if page.size == 100>selected="selected"</#if>>100</option>
    </select>
        <@spring.message "list.pagination.element.label" />
    </#if>
</#macro>
