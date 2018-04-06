<#import "/spring.ftl" as spring>
<#import "/blossom/master/master.ftl" as master>
<#import "/blossom/utils/privilege.ftl" as privilege>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-sm-8">
        <h2><i class="fa fa-home"></i> <@spring.message "home.title"/></h2>
        <ol class="breadcrumb">
            <li>
                <a href="/blossom"><@spring.message "menu.home"/></a>
            </li>
        </ol>
    </div>
</div>
<div class="wrapper wrapper-content">
    <div class="row">
        <#assign colorsWidget = ["navy-bg", "yellow-bg", "lazur-bg", "blue-bg"]>
        <#assign colorsPanel = ["panel-primary", "panel-warning", "panel-info", "panel-success"]>
        <#list menu.filteredItems(currentUser) as menuItem>
            <#if menuItem.filteredItems(currentUser)?size gt 0>
                <div class="col-sm-12 col-md-6">
                    <div class="panel <#if menuItem?index<=4>${colorsPanel[(menuItem?index)-1]}<#else>${colorsPanel[0]}</#if>">
                        <div class="panel-heading">
                            <@spring.messageText menuItem.label() menuItem.label()/>
                        </div>
                        <div class="panel-body gray-bg">
                            <#list menuItem.items() as subMenuItem>
                                <#if !(subMenuItem.privilege()?has_content) || privilege.hasOne(currentUser, subMenuItem.privilege())>
                                    <div class="col-sm-12 col-md-6 col-lg-3">
                                        <#if subMenuItem.link()??><a href="${subMenuItem.link()}"></#if>
                                        <div class="widget <#if menuItem?index<=4>${colorsWidget[(menuItem?index)-1]}<#else>${colorsWidget[0]}</#if> p-lg text-center"
                                             style="height: 150px;">
                                            <#if subMenuItem.icon()??><i class="${subMenuItem.icon()} fa-4x"></i></#if>
                                            <h3 class="font-bold" style="margin-top:15px;">
                                                <@spring.messageText subMenuItem.label() subMenuItem.label()/>
                                            </h3>
                                        </div>
                                        <#if subMenuItem.link()??></a></#if>
                                    </div>
                                </#if>
                            </#list>
                        </div>
                    </div>
                </div>
            </#if>
        </#list>
    </div>
</div>
</@master.default>
