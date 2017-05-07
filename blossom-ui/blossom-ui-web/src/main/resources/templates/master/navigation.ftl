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
            <li class="divider"></li>
            <li><a href="/blossom/logout">Logout</a></li>
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
      <li>
        <span class="m-r-sm text-muted welcome-message">Welcome to Blossom Back-Office.</span>
      </li>
      <li class="dropdown">
        <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
          <i class="fa fa-envelope"></i> <span class="label label-warning">16</span>
        </a>
        <ul class="dropdown-menu dropdown-messages">
          <li>
            <div class="dropdown-messages-box">
              <a href="profile.html" class="pull-left">
                <img alt="image" class="img-circle" src="img/a7.jpg">
              </a>
              <div class="media-body">
                <small class="pull-right">46h ago</small>
                <strong>Mike Loreipsum</strong> started following <strong>Monica Smith</strong>. <br>
                <small class="text-muted">3 days ago at 7:58 pm - 10.06.2014</small>
              </div>
            </div>
          </li>
          <li class="divider"></li>
          <li>
            <div class="dropdown-messages-box">
              <a href="profile.html" class="pull-left">
                <img alt="image" class="img-circle" src="img/a4.jpg">
              </a>
              <div class="media-body ">
                <small class="pull-right text-navy">5h ago</small>
                <strong>Chris Johnatan Overtunk</strong> started following <strong>Monica Smith</strong>. <br>
                <small class="text-muted">Yesterday 1:21 pm - 11.06.2014</small>
              </div>
            </div>
          </li>
          <li class="divider"></li>
          <li>
            <div class="dropdown-messages-box">
              <a href="profile.html" class="pull-left">
                <img alt="image" class="img-circle" src="img/profile.jpg">
              </a>
              <div class="media-body ">
                <small class="pull-right">23h ago</small>
                <strong>Monica Smith</strong> love <strong>Kim Smith</strong>. <br>
                <small class="text-muted">2 days ago at 2:30 am - 11.06.2014</small>
              </div>
            </div>
          </li>
          <li class="divider"></li>
          <li>
            <div class="text-center link-block">
              <a href="mailbox.html">
                <i class="fa fa-envelope"></i> <strong>Read All Messages</strong>
              </a>
            </div>
          </li>
        </ul>
      </li>
      <li class="dropdown">
        <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
          <i class="fa fa-bell"></i> <span class="label label-primary">8</span>
        </a>
        <ul class="dropdown-menu dropdown-alerts">
          <li>
            <a href="mailbox.html">
              <div>
                <i class="fa fa-envelope fa-fw"></i> You have 16 messages
                <span class="pull-right text-muted small">4 minutes ago</span>
              </div>
            </a>
          </li>
          <li class="divider"></li>
          <li>
            <a href="profile.html">
              <div>
                <i class="fa fa-twitter fa-fw"></i> 3 New Followers
                <span class="pull-right text-muted small">12 minutes ago</span>
              </div>
            </a>
          </li>
          <li class="divider"></li>
          <li>
            <a href="grid_options.html">
              <div>
                <i class="fa fa-upload fa-fw"></i> Server Rebooted
                <span class="pull-right text-muted small">4 minutes ago</span>
              </div>
            </a>
          </li>
          <li class="divider"></li>
          <li>
            <div class="text-center link-block">
              <a href="notifications.html">
                <strong>See All Alerts</strong>
                <i class="fa fa-angle-right"></i>
              </a>
            </div>
          </li>
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
