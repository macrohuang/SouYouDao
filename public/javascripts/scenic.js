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
		'heightOffset': 140, 
		'beforeLoad': function(){
			console.log("before:" + this.contentData.page);
//			$('#loading').fadeIn();
		},
		'afterLoad': function(elementsLoaded){
			console.log("after1:" + this.contentData.page);
			console.log("after2:" + elementsLoaded.length);
			if(elementsLoaded.length > 0){
				this.contentData.page = this.contentData.page + 1;
			}
//			 $('#loading').fadeOut();
			 var i = 0;
			 $(elementsLoaded).fadeInWithDelay();
			 if ($('#scenic-thumb-recommend').children().size() > 100){
//			 	$('#nomoreresults').fadeIn();
				$('#scenic-thumb-recommend').stopScrollPagination();
			 }
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