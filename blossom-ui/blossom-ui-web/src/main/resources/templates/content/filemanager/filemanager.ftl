<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>
<#import "/utils/table.ftl" as table>


<#macro displayFolder folder>
<li>
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
              <button type="button" class="btn btn-primary">+git add</button>
            <div class="hr-line-dashed"></div>

            <h5>Folders</h5>
            <div id="fs-tree">

              <ul>
                <@displayFolder folder=folder/>
              </ul>
            </div>

            <h5 class="tag-title">Tags</h5>
            <div>
              <button class="btn btn-primary btn-xs" type="button">New</button>
              <button class="btn btn-primary btn-xs" type="button">Laughter</button>
              <button class="btn btn-primary btn-xs" type="button">Old</button>
              <button class="btn btn-default btn-xs" type="button">Film</button>
              <button class="btn btn-primary btn-xs" type="button">Photography</button>
              <button class="btn btn-primary btn-xs" type="button">Music</button>
              <button class="btn btn-primary btn-xs" type="button">Holidays</button>
              <button class="btn btn-primary btn-xs" type="button">Children</button>
              <button class="btn btn-primary btn-xs" type="button">Home</button>
              <button class="btn btn-primary btn-xs" type="button">Work</button>
            </div>
            <div class="clearfix"></div>
          </div>
        </div>
      </div>
    </div>
    <div class="col-lg-9 animated fadeInRight">
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
    });


    $('.file-box').each(function () {
      animationHover(this, 'pulse');
    });
  });

</script>
</@master.default>
