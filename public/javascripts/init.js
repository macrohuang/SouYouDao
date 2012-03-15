$(function() {
	//所以的search按钮初始化
	$(".search-btn").each(function(){
		$(this).hover(function(){
			$(this).css("background",'url("/public/images/search_btn.png") no-repeat scroll 0 -30px transparent');
		},function(){
			$(this).css("background",'url("/public/images/search_btn.png") no-repeat scroll 0 0 transparent');
		});
	});
    // 景点或酒店等详细介绍选项卡
    $(".tab-btn").each(function(){
        $(this).hover(function(){
            $(this).attr("class","tab-btn-on");
        }, function(){
            $(this).attr("class","tab-btn");
        }); 
    });
});
//错误提示框初始化
$(function() {
	if($("#errors").html().indexOf("div") > 0){
		$("#errors").dialog({
		    modal: true,
		    buttons: {
		      Ok: function() {
		        $( this ).dialog( "close" );
		      }
		    }
		 });
	}
});

