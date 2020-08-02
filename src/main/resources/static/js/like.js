
function like(btn,entityType,entityId,authorId,postId) {
    $.post(
        "/like",
        {"entityType":entityType,"entityId":entityId,"authorId":authorId,"postId":postId},
        function (data) {
            data = $.parseJSON(data);
            if(data.code==0) {
                alert(data.msg);
            }else {
                $(btn).children("i").text(data.likeCount);
                $(btn).children("b").text(data.likeStatus==1?'已赞':'赞');
            }
        }
    );
}