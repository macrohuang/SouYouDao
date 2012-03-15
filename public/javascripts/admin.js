$(function() {
	if($("#errors").html().indexOf("div") > 0){
		$("#errors").dialog({
		    modal: true,
		    width : 350,
		    buttons: {
		      Ok: function() {
		        $( this ).dialog( "close" );
		      }
		    }
		 });
	}
});

// 保存Scenic前台验证
function saveScenic(_form){
	
}