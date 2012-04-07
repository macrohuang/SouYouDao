$(function(){
	//全网搜索分页
	$("#web-search-read-more").css("cursor","pointer");
	$("#web-search-loading").fadeOut();
	$('#full-web-results').scrollPagination({
		'contentPage': "/WebSearch/search",
		'contentData': {keywords:$("#keywords").val(),page:2},
		'clickButton': "web-search-read-more",
		'beforeLoad': function(){
			$('#web-search-loading').fadeIn();
		},
		'afterLoad': function(data){
			if(data!=null && data.length > 0){
				this.contentData.page = this.contentData.page + 1;
			}else{
				$("#web-search-read-more").fadeOut();
			}
			$('#web-search-loading').fadeOut();
		}
	});
	$.post("/WebSearch/search",{keywords:$("#keywords").val(),page:1},function(data){
		$("#full-web-results").append(data);
	});
	
});