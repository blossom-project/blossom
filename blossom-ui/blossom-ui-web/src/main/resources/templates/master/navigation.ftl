<#import "/spring.ftl" as spring>


<#macro drawer menu currentUser>
<nav class="navbar-default navbar-static-side" role="navigation">
  <div class="sidebar-collapse">
    <ul class="nav metismenu" id="side-menu">
      <li class="nav-header">
        <div class="dropdown profile-element"> <span>
                            <img alt="image" class="img-circle" src="/img/profile_small.jpg">
                             </span>
          <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                            <span class="clear"> <span class="block m-t-xs"> <strong
                              class="font-bold">${currentUser.user.firstname +' '+currentUser.user.lastname}</strong>
                             </span> <span class="text-muted text-xs block">${currentUser.user.function!''}<b class="caret"></b></span> </span> </a>
          <ul class="dropdown-menu animated fadeInRight m-t-xs">
            <li><a href="profile.html">Profile</a></li>
          </ul>
        </div>
        <div class="logo-element">
          IN+
        </div>
      </li>
      <#list menu.items() as menuItem>
        <li>
          <#if menuItem.link()??><a href="${menuItem.link()}"></#if>
          <#if menuItem.icon()??><i class="${menuItem.icon()}"></i></#if>
          <span class="nav-label"><@spring.messageText menuItem.label() menuItem.label()/></span>
          <#if menuItem.items()?size gt 0>
            <span class="fa arrow"></span>
          </#if>
          <#if menuItem.link??></a></#if>
          <#if menuItem.items()?size gt 0>
            <ul class="nav nav-second-level collapse">
              <#list menuItem.items() as subMenuItem>
                <li>
                  <#if subMenuItem.link()??><a href="${subMenuItem.link()}"></#if>
                  <#if subMenuItem.icon()??><i class="${subMenuItem.icon()}"></i></#if>
                  <span class="nav-label"><@spring.messageText subMenuItem.label() subMenuItem.label()/></span>
                  <#if subMenuItem.link??></a></#if>
                </li>
              </#list>
            </ul>
          </#if>
        </li>
      </#list>
    </ul>
  </div>
</nav>
</#macro>

<#macro top currentUser>
<div class="row border-bottom">
  <nav class="navbar navbar-static-top  " role="navigation" style="margin-bottom: 0">
    <div class="navbar-header">
      <a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>
      <form role="search" class="navbar-form-custom" action="search_results.html">
        <div class="form-group">
          <input type="text" placeholder="Search for something..." class="form-control" name="top-search"
                 id="top-search">
        </div>
      </form>
    </div>
    <ul class="nav navbar-top-links navbar-right">
      <li class="dropdown">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
        ${currentLocale.getDisplayLanguage(currentLocale)}<#if currentLocale.getDisplayCountry(currentLocale)?has_content> / ${currentLocale.getDisplayCountry(currentLocale)}</#if>
        </a>
        <ul class="dropdown-menu dropdown-alerts">
          <#list locales as locale>
            <#if locale != currentLocale>
              <li>
                <a onclick="window.location.href = $.updateQueryStringParameter(window.location.href,'lang','${locale}');">
                  <div>
                    ${locale.getDisplayLanguage(currentLocale)}<#if locale.getDisplayCountry(currentLocale)?has_content> / ${locale.getDisplayCountry(currentLocale)}</#if>
                    <span class="pull-right text-muted small">( ${locale.getDisplayLanguage(locale)}<#if locale.getDisplayCountry(locale)?has_content> / ${locale.getDisplayCountry(locale)}</#if>)</span>
                  </div>
                </a>
              </li>
            </#if>
          </#list>
        </ul>
      </li>
      <li>
        <a href="/blossom/logout">
          <i class="fa fa-sign-out"></i> Log out
        </a>
      </li>
    </ul>

  </nav>
</div>
</#macro>
