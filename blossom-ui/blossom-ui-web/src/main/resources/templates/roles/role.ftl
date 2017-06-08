<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>


<@master.default currentUser=currentUser>
<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-sm-8">
        <h2><i class="fa fa-key"></i> ${role.name}</h2>
        <ol class="breadcrumb">
            <li>
                <a href="/blossom"><@spring.message "menu.home"/></a>
            </li>
            <li>
                <a href="/blossom/administration"><@spring.message "menu.administration"/></a>
            </li>
            <li>
                <a href="/blossom/administration/groups"><@spring.message "roles.title"/></a>
            </li>
            <li class="active">
                <strong><@spring.message "roles.role.title"/></strong>
            </li>
        </ol>
    </div>
</div>

<div class="wrapper wrapper-content">


    <div class="row m-b-lg m-t-lg">
        <div class="col-md-6">

            <div class="profile-image">
                <img src="/img/a4.jpg" class="img-circle circle-border m-b-md" alt="profile">
            </div>
            <div class="profile-info">
                <div class="">
                    <div>
                        <h2 class="no-margins">
                        ${role.name}
                        </h2>
                        <small>
                        ${role.description!''}
                        </small>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</@master.default>
