//全局的省-市-县映射图，仅在加载首页时初始化一次
var location_map={};
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
    initLocationMap();
});
function initLocationMap(){
	//$.get("/Utils/locationMap", function(json){
	//	location_map = json;
	//});
	$.ajax({
			"url":"/Utils/locationMap",
			"async":false,
			"dataType":"json",
			"success":function(data,status){
				location_map=data;
			}
	});
}
//Ajax三级省市区联动菜单，只需传递三个select的id即可
function locationSelection(province_id,city_id,region_id,province,city,region){
	//先读取省数据
	for (var pid in location_map){
		$("#"+province_id).append("<option value='"+pid+"'>"+ location_map[pid].name+"</option>");
	}
	if(province!=null && province!=undefined){
		$("#"+province_id+" option[value='"+province+"']").attr("selected","selected")
	}
	
	$("#"+city_id).change(function(pid){
		$("#"+region_id).empty();
		for (var rid in location_map[pid].cities[$(this).val()].region){
			$("#"+region_id).append("<option value='"+rid+"'>"+location_map[pid].cities[cid].region[rid]+"</option>");
		}
		if(region!=null && region != undefined){
			$("#"+region_id+" option[value='"+region+"']").attr("selected","selected")
		}
	});
	
	$("#" + province_id).change(function(){
		$("#"+city_id).empty();
		for (var cid in location_map[$(this).val()].cities){
			$("#"+city_id).append("<option value='"+cid+"'>"+location_map[pid].cities[cid].name+"</option>");
		}
		if(city!=null && city!=undefined){
			$("#"+city_id+" option[value='"+city+"']").attr("selected","selected")
		}
		$("#"+city_id).change('$(this).val()');
	});
	
	$("#"+province_id).change();
}