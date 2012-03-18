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
//Ajax三级省市区联动菜单，只需传递三个select的id即可
function locationSelection(province_id,city_id,region_id,province,city,region){
	//先读取省数据
	$.get("/Utils/province", function(json){
		for(var i =0;i<json.length;i++){
			$("#"+province_id).append("<option value='"+json[i].id+"'>"+json[i].name+"</option>");
		}
		if(province!=null){
			$("#"+province_id+" option[value='"+province+"']").attr("selected","selected")
		}
		$("#"+province_id).change();
	});
	//选择省联动事件(载入当前省的城市)
	$("#"+province_id).change(function(){
		//清空当前省的城市
		$("#"+city_id).empty();
		//载入当前省的城市
		$.get("/Utils/city",{province_id:$(this).val()}, function(json){
			$("#"+city_id).append("<option value=''>选择城市</option>");
			for(var i =0;i<json.length;i++){
				$("#"+city_id).append("<option value='"+json[i].id+"'>"+json[i].name+"</option>");
			}
			if(city!=null){
				$("#"+city_id+" option[value='"+city+"']").attr("selected","selected")
			}
			$("#"+city_id).change();
		});
	});
	//选择城市联动事件(载入当前城市的县区)
	$("#"+city_id).change(function(){
		//清空当前县区
		$("#"+region_id).empty();
		//载入当前城市的县区
		$.get("/Utils/region",{city_id:$(this).val()}, function(json){
			$("#"+region_id).append("<option value=''>选择县区</option>");
			for(var i =0;i<json.length;i++){
				$("#"+region_id).append("<option value='"+json[i].id+"'>"+json[i].name+"</option>");
			}
			if(region!=null){
				$("#"+region_id+" option[value='"+region+"']").attr("selected","selected")
			}
		});
	});
}