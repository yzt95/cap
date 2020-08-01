
function like(btn,entityType,entityId,authorId) {
    $.post(
        "/like",
        {"entityType":entityType,"entityId":entityId,"authorId":authorId},
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