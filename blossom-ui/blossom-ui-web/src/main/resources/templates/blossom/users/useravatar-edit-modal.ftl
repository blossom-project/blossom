<#import "/spring.ftl" as spring>
<#import "/blossom/utils/table.ftl" as table>
<#import "/blossom/utils/notification.ftl" as notification>

<div class="avatar_modal">
<div class="modal-header">
  <button type="button" class="close" data-dismiss="modal">
    <span>&times;</span><span class="sr-only">Close</span>
  </button>

  <h4 class="modal-title"><@spring.message "users.update.avatar"/></h4>
</div>
<div class="modal-body">

  <div class="row">
    <div class="col-md-6">
      <div class="image-crop" style="width:100%">
        <img src="/blossom/administration/users/${user.id?c}/avatar" style="width:100%;max-width: 100%;">
      </div>
    </div>
    <div class="col-md-6">
      <h4>Preview image</h4>
      <div class="img-preview img-circle"></div>
    </div>
  </div>

  <div class="row m-t-md">
    <div class="col-md-6 text-center">

      <div class="btn-group">
        <button class="btn btn-white" id="zoomIn" type="button"><i class="fa fa-search-plus"></i></button>
        <button class="btn btn-white" id="zoomOut" type="button"><i class=" fa fa-search-minus"></i></button>
        <button class="btn btn-white" id="rotateLeft" type="button"><i class=" fa fa-rotate-left"></i></button>
        <button class="btn btn-white" id="rotateRight" type="button"><i class=" fa fa-rotate-right"></i></button>
        <label title="Upload image file" for="inputAvatar" class="btn btn-primary">
          <input type="file" accept="image/*" name="file" id="inputAvatar" class="hide">
          <@spring.message "load"/>
        </label>
      </div>
    </div>
  </div>
</div>


</div>

<div class="modal-footer">
    <button id="cancel_avatar" class="btn btn-default">
      <@spring.message "cancel"/>
    </button>
    <button id="download_avatar" class="btn btn-primary">
      <@spring.message "save"/>
    </button>
</div>
</div>

<script>
  $(document).ready(function(){

    var $image = $(".image-crop > img")
    $($image).cropper({
      autoCropArea:1,
      aspectRatio: 1,
      preview: ".img-preview"
    });

    var $inputImage = $("#inputAvatar");
    if (window.FileReader) {
      $inputImage.change(function () {
        var fileReader = new FileReader(),
          files = this.files,
          file;

        if (!files.length) {
          return;
        }

        file = files[0];

        if (/^image\/\w+$/.test(file.type)) {
          fileReader.readAsDataURL(file);
          fileReader.onload = function () {
            $inputImage.val("");
            $image.cropper("reset", true).cropper("replace", this.result);
          };
        }
      });
    } else {
      $inputImage.addClass("hide");
    }

    $("#cancel_avatar").click(function () {
      var $this = $(this);
      $this.closest(".modal").modal('hide');
    });

    $("#download_avatar").click(function () {
      var $this = $(this);
      var dataURL = $image.cropper("getDataURL", {
        width: 200,
        height: 200
      });

      $.dataURItoBlob(dataURL, function (blob) {
        var formData = new FormData();
        formData.append('avatar', blob);

        // Use `jQuery.ajax` method
        $.ajax('/blossom/administration/users/${user.id?c}/_avatar/_edit', {
          method: "POST",
          data: formData,
          processData: false,
          contentType: false,
          success: function () {
            $this.closest(".modal").modal('hide');
            var date = new Date();
            $.each($(document).find(".profile-image img"),function(index, image){
              var $image = $(image);
              $image.attr("src",$image.attr("src")+"?" +date.getTime())
            });
          <@notification.success/>
          },
          error: function () {
          <@notification.error/>
          }
        });
      });
    });

    $("#zoomIn").click(function () {
      $image.cropper("zoom", 0.1);
    });

    $("#zoomOut").click(function () {
      $image.cropper("zoom", -0.1);
    });

    $("#rotateLeft").click(function () {
      $image.cropper("rotate", 45);
    });

    $("#rotateRight").click(function () {
      $image.cropper("rotate", -45);
    });
  });
</script>
