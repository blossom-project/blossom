<#ftl output_format="HTML">
<#import "/spring.ftl" as spring>
<#import "/blossom/utils/buttons.ftl" as buttons>
<#import "/blossom/utils/notification.ftl" as notification>
<#import "/blossom/utils/status.ftl" as state>


<form id="articleUpdateForm" class="form form-horizontal" onsubmit="submit_articleinformations(this);return false;">
    <div class="ibox-content">
        <div class="sk-spinner sk-spinner-wave">
            <div class="sk-rect1"></div>
            <div class="sk-rect2"></div>
            <div class="sk-rect3"></div>
            <div class="sk-rect4"></div>
            <div class="sk-rect5"></div>
        </div>


      <@spring.bind "articleUpdateForm.name"/>
        <div class="form-group <#if spring.status.error>has-error</#if>">
            <label class="col-sm-2 control-label"><@spring.message "articles.article.properties.name"/></label>
            <div class="col-sm-10">
                <input type="text" name="name" class="form-control" value="${articleUpdateForm.name!''}"
                       placeholder="<@spring.message "articles.article.properties.name"/>">
        <#list spring.status.errorMessages as error>
          <span class="help-block text-danger m-b-none">${error}</span>
        </#list>
            </div>
        </div>


      <@spring.bind "articleUpdateForm.summary"/>
        <div class="form-group <#if spring.status.error>has-error</#if>">
            <label class="col-sm-2 control-label"><@spring.message"articles.article.properties.summary"></@spring.message></label>
            <div class="col-sm-10">
                <textarea name="summary" class="form-control">${articleUpdateForm.summary!''}</textarea>
        <#list spring.status.errorMessages as error>
          <span class="help-block text-danger m-b-none">${error}</span>
        </#list>
            </div>
        </div>

        <div class="hr-line-dashed"></div>

        <@spring.bind "articleUpdateForm.status"/>
        <div class="form-group">
            <label class="col-sm-2 control-label"><@spring.message"articles.article.properties.status"/></label>
            <div class="col-sm-10">
        <#list statuslist as status>
          <div class="radio radio-success radio-inline">
              <input type="radio" class="radio" value="${status}" id="state_${status}" name="status" <#if articleUpdateForm.status == status>checked</#if>>
              <label for="state_${status}"> <@state.label status=status/> </label>
          </div>
        </#list>
        <#list spring.status.errorMessages as error>
          <span class="help-block text-danger m-b-none">${error}</span>
        </#list>
            </div>
        </div>

         <@spring.bind "articleUpdateForm.content"/>
        <div class="form-group <#if spring.status.error>has-error</#if>">
            <label class="col-sm-2 control-label"><@spring.message"articles.article.properties.content"></@spring.message></label>
            <div class="col-sm-10">
                <textarea id="summernote" name="content" class="form-control">${articleUpdateForm.content!''}</textarea>
        <#list spring.status.errorMessages as error>
          <span class="help-block text-danger m-b-none">${error}</span>
        </#list>
            </div>
        </div>


    </div>


    <div class="ibox-footer">
        <div class="text-right">
            <button class="btn btn-default btn-view" type="button" onclick="cancel_articleinformations(this);">
      <@spring.message "cancel"/>
            </button>

            <button class="btn btn-primary btn-view" type="submit">
      <@spring.message "save"/>
            </button>
        </div>
    </div>
</form>


<script>
  var submit_articleinformations = function (button) {
    var targetSelector = '#'+$(button).closest(".tab-pane").attr('id');
    $(targetSelector + ' > .ibox-content').addClass("sk-loading");
    var edit = $(targetSelector).data("edit");
    var form = $(targetSelector + ' > form');

    $.post(edit, form.serialize()).done(function (responseText, textStatus, jqXHR) {
      $(targetSelector).html(responseText);
      <@notification.success message="updated"/>
      $(targetSelector+ ' > .ibox-content').removeClass("sk-loading");
    });
  };

  var cancel_articleinformations = function (button) {
    var targetSelector = '#'+$(button).closest(".tab-pane").attr('id');
    $(targetSelector + ' > .ibox-content').addClass("sk-loading");
    var view = $(targetSelector).data("view");
    $.get(view).done(function (responseText, textStatus, jqXHR) {
      $(targetSelector).html(responseText);
      $(targetSelector).removeClass("sk-loading");
    });
  };


  $(document).ready(function() {
      $('#summernote').summernote({
          lang:'${translate}',
          popover:{
              link:[
                  ["link", ["customlink", "unlink"]]
              ],image: [
                  ["imagesize", ["imageSize100", "imageSize50", "imageSize25"]],
                  ["float", ["floatLeft", "floatRight", "floatNone"]],
                  ["remove", ["removeMedia"]]
              ], air: [
                  ["color", ["color"]],
                  ["font", ["bold", "underline", "clear"]],
                  ["para", ["ul", "paragraph"]],
                  ["table", ["table"]],
                  ["insert", ["link", "picture"]]
              ]
          },
          toolbar: [
              // [groupName, [list of button]]
              ['style', ['bold', 'italic', 'underline', 'clear']],
              ['font', ['strikethrough', 'superscript', 'subscript']],
              ['fontstyle', ['fontsize','fontname']],
              ['color', ['color']],
              ['para', ['ul', 'ol', 'paragraph','style']],
              ['height', ['height']],
              ['insert',['customlink','table','picture','hr']],
              ['code',['codeview']],
              ['help',['help']]
          ]
      });
  });
</script>
