$(document).ready(function () {
    $("#getbugs_btn").click(function () {
        var linkJQ = $("#link_input"),
            url = linkJQ.val(),
            getBugsBtnQ = $(this);

        var verification_code;

        if (url.length == 0) {
            showLinkError(ERROR.URL.EMPTY, true);
            return;
        }

        if (!re_weburl.test(url)) {
            showLinkError(ERROR.URL.INVALID, true);
            return;
        }

        
        var jqxhr = $.get("/jreport/oauth?url=" + extractDomain(decodeURIComponent(url)), function (jsonRes) {
                if (jsonRes.data) {
                    window.location.href = jsonRes.data;
                }

                else if (jsonRes.errorMessage) {
                    alert(jsonRes.errorMessage);
                }
                linkJQ.val("");
            })
            .done(function () {
                getBugsBtnQ.button("reset");
                linkJQ.removeAttr("disabled");
            })
            .fail(function () {
                alert("Error");
            });

    });

    function extractDomain(url) {
        var domain;
        //find & remove protocol (http, ftp, etc.) and get domain
        if (url.indexOf("://") > -1) {
            domain = url.split('/')[2];
        }
        else {
            domain = url.split('/')[0];
        }

        return domain;
    }

    $("#link_input").keypress(function () {
        clearUrlErrors();
        clearServerMessage();
    });

    function showLinkError(error, clearOther) {
        var message = null;
        switch (error) {
            case ERROR.URL.EMPTY:
                message = "Please enter your link first!";
                break;
            case ERROR.URL.INVALID:
                message = "Please enter a valid link!";
                break;
            case ERROR.PROJKEY.EMPTY:
                message = "Please enter your jira project key first!";
                break;
            case ERROR.PROJKEY.INVALID:
                message = "Please enter a valid project key link!";
                break;
            default:
                message = null;
                break;
        }
        var errorContainer = $("#urlerrorcontainer");
        if (clearOther) {
            clearUrlErrors();
        }
        if (null != message) {
            errorContainer.append(
                '<div role="alert" class="alert alert-danger alert-dismissible fade in">' +
                '<button aria-label="Close" data-dismiss="alert" class="close" type="button"><span aria-hidden="true">×</span></button>' +
                '<strong>Link error. </strong> ' + message +
                '</div>');
        }
    }

    function clearUrlErrors() {
        var errorContainer = $("#urlerrorcontainer");
        errorContainer.html("");
    }

    function clearServerMessage() {
        $("#servermessagescontainer").html("");
    }

});

var ERROR = {};
ERROR.URL = {},
    ERROR.PROJKEY = {},
    ERROR.URL.EMPTY = 0;
ERROR.URL.INVALID = 1;
ERROR.PROJKEY.EMPTY = 2;
ERROR.PROJKEY.INVALID = 3;
ERROR.SERVER = {};
ERROR.SERVER.DUPLICATE_URL = {};
ERROR.SERVER.DUPLICATE_URL = "E_BUILDING_DUPLICATE_URL";

var re_weburl = new RegExp("^(http:\/\/|https:\/\/)?(www.)?([a-zA-Z0-9]+):?([0-9]*)?.?([a-zA-Z0-9]+)?");