$(function () {
    $("#setWonderful").click(wonderful);
    $("#setTop").click(setTop);
    $("#deletePost").click(deletePost);
});

function wonderful() {
    $.post(
        "/post/setWonderful",
        {"postId":$("#postId").val(), "targetStatus":$("#setWonderful").val()},
        function(data) {
            data=$.parseJSON(data);
            if(data.code==1) {
                window.location.reload();
            }else {
                alert("操作失败，请稍后重试");
            }
        });
}

function setTop() {
    $.post(
        "/post/setTop",
        {"postId":$("#postId").val(), "targetType":$("#setTop").val()},
        function(data) {
            data=$.parseJSON(data);
            if(data.code==1) {
                window.location.reload();
            }else {
                alert("操作失败，请稍后重试");
            }
        });
}

function deletePost() {
    $.post(
        "/post/setDelete",
        {"postId":$("#postId").val()},
        function(data) {
            data=$.parseJSON(data);
            if(data.code==1) {
                var curWwwPath = window.location.href;
                var pathName = window.location.pathname;
                var redirectTo = curWwwPath.substring(0,curWwwPath.indexOf(pathName))+"/index";
                window.location.replace(redirectTo);
            }else {
                alert("操作失败，请稍后重试");
            }
        });
}
