(function (factory) {
    /* Global define */
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define(['jquery'], factory);
    } else if (typeof module === 'object' && module.exports) {
        // Node/CommonJS
        module.exports = factory(require('jquery'));
    } else {
        // Browser globals
        factory(window.jQuery);
    }
}(function ($) {

    $.extend($.summernote.plugins,{
        'customLinkDialog':function (a) {
            var b=this,
                c=$.summernote.ui,
                d=a.layoutInfo.editor,
                e=a.options,
                f=e.langInfo;
            a.memo('button.customlink', function () { return c.button({
                contents: c.icon(e.icons.link),
                tooltip: f.link.link,
                click: a.createInvokeHandler("customLinkDialog.show")
            }).render()})
            this.initialize = function() {
                var a = e.dialogsInBody ? $(document.body) : d,
                    b = '<div class="form-group">' +
                        '<label>' + f.link.textToDisplay + '</label>' +
                        '<input class="note-link-text form-control" type="text" />' +
                        '</div>' +
                        '<div class="form-group">' +
                        '<label>' + f.link.url + '</label>' +
                        '<input class="note-link-url form-control" type="text" value="http://" /></div>'
                        + (e.disableLinkTarget ? "" : '<div class="checkbox"><input type="checkbox" checked><label> ' + f.link.openInNewWindow + "</label></div>"),
                    g = '<button href="#" class="btn btn-primary note-link-btn disabled" disabled>' + f.link.insert + "</button>";
                this.$dialog = c.dialog({
                    className: "link-dialog",
                    title: f.link.insert,
                    fade: e.dialogsFade,
                    body: b,
                    footer: g
                }).render().appendTo(a)
            }, this.destroy = function() {
                c.hideDialog(this.$dialog), this.$dialog.remove()
            }, this.bindEnterKey = function(a, b) {
                a.on("keypress", function(a) {
                    a.keyCode === key.code.ENTER && b.trigger("click")
                })
            }, this.toggleLinkBtn = function(a, b, d) {
                c.toggleBtn(a, b.val() && d.val())
            }, this.showLinkDialog = function(d) {
                return $.Deferred(function(e) {
                    var f = b.$dialog.find(".note-link-text"),
                        g = b.$dialog.find(".note-link-url"),
                        h = b.$dialog.find(".note-link-btn"),
                        i = b.$dialog.find("input[type=checkbox]");
                    c.onDialogShown(b.$dialog, function() {
                        a.triggerEvent("dialog.shown"), d.url || (d.url = d.text), f.val(d.text);
                        var c = function() {
                            b.toggleLinkBtn(h, f, g), d.text = f.val()
                        };
                        f.on("input", c).on("paste", function() {
                            setTimeout(c, 0)
                        });
                        var j = function() {
                            b.toggleLinkBtn(h, f, g), d.text || f.val(g.val())
                        };
                        g.on("input", j).on("paste", function() {
                            setTimeout(j, 0)
                        }).val(d.url).trigger("focus"), b.toggleLinkBtn(h, f, g), b.bindEnterKey(g, h), b.bindEnterKey(f, h), i.prop("checked", d.isNewWindow), h.one("click", function(a) {
                            a.preventDefault(), e.resolve({
                                range: d.range,
                                url: g.val(),
                                text: f.val(),
                                isNewWindow: i.is(":checked")
                            }), b.$dialog.modal("hide")
                        })
                    }), c.onDialogHidden(b.$dialog, function() {
                        f.off("input paste keypress"), g.off("input paste keypress"), h.off("click"), "pending" === e.state() && e.reject()
                    }), c.showDialog(b.$dialog)
                }).promise()
            }, this.show = function() {
                var b = a.invoke("editor.getLinkInfo");
                a.invoke("editor.saveRange"), this.showLinkDialog(b).then(function(b) {
                    a.invoke("editor.restoreRange"), a.invoke("editor.createLink", b)
                }).fail(function() {
                    a.invoke("editor.restoreRange")
                })
            }, a.memo("help.CustomLinkDialog.show", e.langInfo.help["linkDialog.show"])


        }

    })

}));