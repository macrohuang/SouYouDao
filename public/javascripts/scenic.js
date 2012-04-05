$(function() {
	$(".scenic-image-thumb").each(function(){
		$(this).hover(function(){
			$(".scenic-image-first:first").attr("src","/data/scenic/images/"+$(this).attr("image"));
		});
	});
	//分页
	$('#scenic-thumb-recommend').scrollPagination({
		'contentPage': "/Scenics/query", // the page where you are searching for results
		'contentData': {keywords:$("#keywords").val(),page:2},
		'scrollTarget': $(window),
		'heightOffset': 20, 
		'beforeLoad': function(){
//			$('#loading').fadeIn();
		},
		'afterLoad': function(elementsLoaded){
			if(elementsLoaded.length > 0){
				this.contentData.page = this.contentData.page + 1;
			}
//			 $('#loading').fadeOut();
			 var i = 0;
			 $(elementsLoaded).fadeInWithDelay();
		}
	});
	
	// code for fade in element by element with delay
	$.fn.fadeInWithDelay = function(){
		var delay = 0;
		return this.each(function(){
			$(this).delay(delay).animate({opacity:1}, 200);
			delay += 100;
		});
	};
});