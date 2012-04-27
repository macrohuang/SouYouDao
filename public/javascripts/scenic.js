$(function() {
	$(".scenic-image-thumb").each(function(){
		$(this).hover(function(){
			$(".scenic-image-first:first").attr("src","/data/scenic/images/"+$(this).attr("image"));
		});
	});
	//分页
	$("#loading").fadeOut();
	$('#scenic-thumb-recommend').scrollPagination({
		'contentPage': "/Scenics/query",
		'contentData': {keywords:$("#keywords").val(),page:2},
		'clickButton': "loadMore",
		'beforeLoad': function(){
			$('#loading').fadeIn();
		},
		'afterLoad': function(data){
			if(data!=null && data.length > 0){
				this.contentData.page = this.contentData.page + 1;
			}else{
				$("#loadMore").fadeOut();
			}
			$('#loading').fadeOut();
		}
	});
});
function saveScenicName(nodeId,scenicId){
	$.post("/Scenics/saveName",{name:$("#"+nodeId).val(),id:scenicId},function(){
		$(".modal").modal('hide');
	});
}