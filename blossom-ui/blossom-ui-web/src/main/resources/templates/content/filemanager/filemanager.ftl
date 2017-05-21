<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>
<#import "/utils/table.ftl" as table>


<#macro displayFolder folder>
<li data-folder-path="${folder.getPath()}">
${folder.getName()}
    <#if folder.getChildren()?size gt 0>
        <ul>
            <#list folder.getChildren() as subFolder>
                <@displayFolder folder=subFolder/>
            </#list>
        </ul>
    </#if>
</li>
</#macro>

<@master.default currentUser=currentUser>

<style>
    .jstree-open > .jstree-anchor > .fa-folder:before {
        content: "\f07c";
    }

    .jstree-default .jstree-icon.none {
        width: 0;
    }
</style>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-sm-4">
        <h2><i class="fa fa-photo"></i> <@spring.message "menu.content.filemanager"/></h2>
        <ol class="breadcrumb">
            <li>
                <a href="/blossom"><@spring.message "menu.home"/></a>
            </li>
            <li>
                <a href="/blossom/content"><@spring.message "menu.content"/></a>
            </li>
            <li class="active">
                <strong><@spring.message "menu.content.filemanager"/></strong>
            </li>
        </ol>
    </div>
</div>
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-lg-3">
            <div class="ibox float-e-margins">
                <div class="ibox-content">
                    <div class="file-manager">
                        <div id="fs-tree">
                            <ul>
                                <@displayFolder folder=folder/>
                            </ul>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                </div>
            </div>
        </div>
        <div id="filelist" class="col-lg-9 animated fadeInRight">
        </div>
    </div>
</div>

<script src="/js/plugin/jsTree/jstree.min.js"></script>

<script>

    $(document).ready(function () {
        $('#fs-tree').jstree({
            'core': {
                'check_callback': true
            },
            'plugins': ['types', 'dnd'],
            'types': {
                'default': {
                    'icon': 'fa fa-folder'
                }
            }
        })
        .on("select_node.jstree", function (e, data) {
            console.log("node_id: " , data);
            var path = data.node.data.folderPath;
            $.get("filemanager/files?path="+encodeURIComponent(path),function(data){
               $("#filelist").html(data);
            });
        });

        $('.file-box').each(function () {
            animationHover(this, 'pulse');
        });
    });

</script>
</@master.default>
