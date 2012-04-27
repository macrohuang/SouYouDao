$(function(){
	//Search页面全网搜索并分页
	if($("#web-search-read-more").length > 0){
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
	}
	if($("#search-box").length > 0){
		$("#search-box").focus();
	}
	//search-box的slider
	if($(".search-option").length > 0){
		//天数
		$( "#slider-days" ).slider({
			range: true,
			min: 1,
			max: 15,
			values: [ 2, 5 ],
			slide: function( event, ui ) {
				$( "#amount-days" ).val( ui.values[ 0 ] + "天 - " + ui.values[ 1 ] + "天" );
			}
		});
		$( "#amount-days" ).val($( "#slider-days" ).slider( "values", 0 ) +
			"天 - " + $( "#slider-days" ).slider( "values", 1 ) + "天" );
		//人均消费
		$( "#slider-consume" ).slider({
			range: true,
			min: 0,
			max: 2000,
			values: [ 200, 500 ],
			slide: function( event, ui ) {
				var step = 100;
				ui.values[0] = ui.values[0] - (ui.values[0]+step) % step;
				ui.values[1] = ui.values[1] - (ui.values[1]+step) % step;
				$( "#amount-consume" ).val("￥" + ui.values[0] + " - ￥" + ui.values[1]);
			}
		});
		$( "#amount-consume" ).val("￥" + $( "#slider-consume" ).slider( "values", 0 ) +
			" - ￥" + $( "#slider-consume" ).slider( "values", 1 ) );
	}
});
//检查搜索框是否为空
function checkSearhBox(){
	if($("#search-box").val()==''){
		$("#search-box").focus();
		return false;
	}
}