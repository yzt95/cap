$(function(){
	$(".follow-btn").click(follow);
});

function follow() {
	var btn = this;
	var followedId = $("#followedId").val();
	var isFollow = !$(btn).hasClass("btn-info");
	var followerCount = $("#followerCount");
	$.post(
		"/follow",
		{"followedId":followedId,"isFollow":isFollow},
		function (data) {
			data = $.parseJSON(data);
			alert(data.msg);
			if(data.code==1) {
				if($(btn).hasClass("btn-info")) {
					// 关注TA
					$(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
					followerCount.text(parseInt(followerCount.text())+1);
				} else {
					// 取消关注
					$(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
					followerCount.text(parseInt(followerCount.text())-1);
				}
			}
		}
	);
}