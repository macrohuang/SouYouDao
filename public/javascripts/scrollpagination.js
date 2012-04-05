/**
 * ROY GUO(royguo1988@gmail.com)
 * You can use this plugin anywhere you want.
 */
(function($){
 $.fn.scrollPagination = function(options) {
		var opts = $.extend($.fn.scrollPagination.defaults, options);  
		return this.each(function() {
		  $.fn.scrollPagination.init($(this), opts);
		});
  };
  $.fn.scrollPagination.init = function(obj, opts){
	 $("#"+opts.clickButton).click(function(){
		 opts.beforeLoad();
		 $.ajax({
			  type: 'POST',
			  url: opts.contentPage,
			  data: opts.contentData,
			  success: function(data){
				$(obj).append(data); 
				var objectsRendered = $(obj).children('[rel!=loaded]');
				if (opts.afterLoad != null){
					opts.afterLoad(objectsRendered);	
				}
			  },
			  dataType: 'html'
		 });
	 });
  }
 $.fn.scrollPagination.defaults = {
      	 'contentPage' : null,	//data source URL
     	 'contentData' : {},	//post request parameters
		 'beforeLoad': null,
		 'afterLoad': null,
		 'clickButton': null	  
 };
})( jQuery );