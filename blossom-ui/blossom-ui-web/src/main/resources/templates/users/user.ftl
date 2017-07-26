<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>
<#import "/utils/tabulation.ftl" as tabulation>


<@master.default currentUser=currentUser>

<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
    <h2><i class="fa fa-user"></i> ${user.firstname +' '+ user.lastname}</h2>
    <ol class="breadcrumb">
      <li>
        <a href="/blossom"><@spring.message "menu.home"/></a>
      </li>
      <li>
        <a href="/blossom/administration"><@spring.message "menu.administration"/></a>
      </li>
      <li>
        <a href="/blossom/administration/users"><@spring.message "users.title"/></a>
      </li>
      <li class="active">
        <strong><@spring.message "users.user.title"/></strong>
      </li>
    </ol>
  </div>
</div>

<div class="wrapper wrapper-content">
<@tabulation.tabulation
    tabnavlist=[
        {
            "isActive": true,
            "href": "#userContent",
            "onClick": "goToMainTab();",
            "linkLabel": "panel.information"
        },
        {
            "isActive": false,
            "href": "#userContent",
            "onClick": "goToGroupsTab();",
            "linkLabel": "users.user.groups"
        },
        {
            "isActive": false,
            "href": "#userContent",
            "onClick": "goToRolesTab();",
            "linkLabel": "users.user.roles"
        }
    ]
    idContent="userContent"
    idPanelContent="userPanelContent"
   
/>
</div>

<script>
    var goToGroupsTab =  function(){
         $.get("/blossom/administration/users/${user.id?c}/_groups", function (data) {
          $("#userPanelContent").html(data);
        });
    };
    
   var goToRolesTab =  function (){
        $.get("/blossom/administration/users/${user.id?c}/_roles", function (data) {
          $("#userPanelContent").html(data);
        });
    };
    
    var goToMainTab =  function(){
        $.get("/blossom/administration/users/${user.id?c}/_informations", function (data) {
          $("#userPanelContent").html(data);
        });
    };
    
    $(document).ready(function(){
        goToMainTab();
    });
</script>

</@master.default>
