<#import "/spring.ftl" as spring>
<#import "/master/master.ftl" as master>
<#import "/utils/table.ftl" as table>
<#import "/utils/privilege.ftl" as privilege>

<@master.default currentUser=currentUser>
<link href="/css/plugins/dropzone/dropzone.css" rel="stylesheet">
<style>
  .dropzone .dz-preview .dz-image {
    width: 100px;
    height: 100px;
  }
</style>

<div class="row wrapper border-bottom white-bg page-heading">
  <div class="col-sm-8">
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
    <@privilege.has currentUser=currentUser privilege="content:filemanager:create">
      <div class="col-lg-3">
        <div class="ibox float-e-margins">
          <div class="ibox-content">
            <div class="file-manager">
              <h5><@spring.message "filemanager.search"/></h5>
              <div class="input-group m-b-md">
                <input type="text"
                       placeholder="<@spring.message "filemanager.search.placeholder"/>"
                       class="file-search input-sm form-control"
                       onkeyup="var which = event.which || event.keyCode;if(which === 13) {$(this).closest('.input-group').find('button.file-search').first().click();}">

                <span class="input-group-btn">
                  <button type="button" class="btn btn-sm btn-primary file-search"
                          onclick="var value = $(this).closest('.input-group').children('input.file-search').first().val();context.q=value;updateFilelist();">
                      <i class="fa fa-search"></i>
                  </button>
                </span>
              </div>

              <div class="hr-line-dashed"></div>

              <form id="filemanagerDropzone" action="filemanager/files" method="POST" class="dropzone"
                    enctype="multipart/form-data">
                <div class="fallback">
                  <input name="file" type="file" multiple="false"/>
                </div>
              </form>
              <div class="hr-line-dashed"></div>

            </div>
          </div>
        </div>
      </div>
    </@privilege.has>

    <div class="<#if privilege.hasOne(currentUser,"content:filemanager:create")>col-lg-9<#else>col-lg-12</#if>">
      <div class="row">
        <div id="filelist" class="col-xs-12">
        </div>
      </div>
    </div>
  </div>
</div>

<!-- Dropzone.js-->
<script src="/js/plugins/dropzone/dropzone.js"></script>
<script>
  var context = {
    page: 0,
    q: '',
    filters:[]
  };

  var updateFilelist = function () {
    context.page = 0;
    var params = "?"
    params+="q="+context.q;

    $.get("filemanager/files" + params, function (data) {
      $("#filelist").html(data);
    });
  };
  updateFilelist();

  var appendFilelist = function () {
    context.page += 1;
    $.get("filemanager/files?page=" + context.page + "&q=" + context.q, function (data) {
      $("#filelist").append(data);
    });
  };

  var defaultHeaders = {};
  defaultHeaders[$('meta[name="_csrf_header"]').attr('content')] = $('meta[name="_csrf"]').attr('content');

  Dropzone.options.filemanagerDropzone = {
    headers: defaultHeaders,
    parallelUploads: 1,
    init: function () {
      this.on("complete", function (file) {
        if (this.getUploadingFiles().length === 0 && this.getQueuedFiles().length === 0) {
          console.log("Removing all ! ")
          this.removeAllFiles(true);
          updateFilelist();
        }
      });
    }
  };
</script>
</@master.default>
