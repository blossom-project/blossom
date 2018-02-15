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
    <div class="col-sm-4">
        <div class="title-action">
            <a href="https://blossom-project.com/documentation"
               class="btn btn-primary"><@spring.message "home.documentation"/></a>
        </div>
    </div>
</div>
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-lg-12">
            <div class="ibox float-e-margins">
                <div class="ibox-content text-center p-md">

                    <h2><span
                            class="text-navy"><@spring.message "home.content.title.span"/></span> <@spring.message "home.content.title"/>
                    </h2>

                    <p>
                        <@spring.message "home.content.subtitle.1"/><br>
                        <@spring.message "home.content.subtitle.2"/>
                    </p>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <#assign colors = ["navy-bg", "yellow-bg", "lazur-bg", "blue-bg"]>
        <#list menu.filteredItems(currentUser) as menuItem>
            <#if menuItem.filteredItems(currentUser)?size gt 0>
                <#list menuItem.items() as subMenuItem>
                    <#if !(subMenuItem.privilege()?has_content) || privilege.hasOne(currentUser, subMenuItem.privilege())>
                        <div class="col-xs-6 col-md-4 col-lg-2">
                            <#if subMenuItem.link()??><a href="${subMenuItem.link()}"></#if>
                            <div class="widget <#if menuItem?index<=4>${colors[(menuItem?index)-1]}<#else>${colors[0]}</#if> p-lg text-center"
                                 style="height: 150px;">
                                <#if subMenuItem.icon()??><i class="${subMenuItem.icon()} fa-4x"></i></#if>
                                <h3 class="font-bold" style="margin-top:15px;">
                                    <@spring.messageText subMenuItem.label() subMenuItem.label()/>
                                </h3>
                            </div>
                        </a>
                        </div>
                    </#if>
                </#list>
            </#if>
        </#list>
    </div>
</div>
</@master.default>
