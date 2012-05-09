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
	$("#scenic-province-dropdown").empty();
	for (var pid in location_map){
		$("#scenic-province-dropdown").append("<option value='"+pid+"'>" + location_map[pid].name +"</option>");
	}
	initDropdown();
	tinyMCE.init({
		mode : "textareas",
		theme : "advanced",
		theme_advanced_toolbar_location : "top",
        theme_advanced_toolbar_align : "left",
        theme_advanced_statusbar_location : "bottom",
        theme_advanced_resizing : true
	});
	//bindEvent();
});

function initDropdown(){
	pid=$("#scenic-province-span-id").attr("value");
	if (pid !=null && pid!=undefined){
		$("#scenic-province-dropdown option[value='"+pid+"']").attr("selected","selected")
		changeProvince();
	}
	cid=$("#scenic-city-span-id").attr("value");
	if (cid !=null && cid!=undefined){
		$("#scenic-city-dropdown option[value='"+cid+"']").attr("selected","selected")
		changeCity();
	}
	rid=$("#scenic-region-span-id").attr("value");
	if (rid !=null && rid!=undefined){
		$("#scenic-region-dropdown option[value='"+rid+"']").attr("selected","selected")
	}
}
function updateField(cmpId,disSpan,field){
	nval =$("#" + cmpId + " select").attr("value");
	if (!nval){
		nval=$("#"+cmpId+" input").attr("value");
	}
	
	$("#"+cmpId).modal('hide');
	$.post("/Scenics/updateByField",
			{
				"scenicId":$("#scenicId").attr("value"),
				"field":field,
				"value":nval
			},
			function(data){
				if (data.status == 0){
					$("#" + disSpan).text(nval);
					changeProvince();
					alert("更新成功！");
				}else{
					alert("更新失败：" + data.msg);
				}
			}
		);
}
function updateLocationField(cmpId,disSpan,field){
	$("#"+cmpId).modal('hide');
	$.post("/Scenics/updateByField",
		{
			"scenicId":$("#scenicId").attr("value"),
			"field":field,
			"value":$("#" + cmpId + " select option:selected").attr("value")
		},
		function(data){
			if (data.status == 0){
				$("#" + disSpan).text($("#" + cmpId + " select option:selected").text());
				$("#" + disSpan+"-id").attr("value",$("#" + cmpId + " select option:selected").attr("value"));
				initDropdown();
			}else{
				alert("更新失败：" + data.msg);
			}
		}
	);
}

function changeProvince(){
	var pid=$("#scenic-province-dropdown").attr("value");
	$("#scenic-city-dropdown").empty();
	for (var cid in location_map[pid].cities){
		$("#scenic-city-dropdown").append("<option value='"+cid+"'>"+location_map[pid].cities[cid].name+"</option>");
	}
}

function changeCity(){
	var pid=$("#scenic-province-dropdown").attr("value");
	var cid=$("#scenic-city-dropdown").attr("value");
	$("#scenic-region-dropdown").empty();
	for (var rid in location_map[pid].cities[cid].region){
		$("#scenic-region-dropdown").append("<option value='"+rid+"'>"+location_map[pid].cities[cid].region[rid]+"</option>");
	}
}

function saveScenicName(nodeId,scenicId){
	$.post("/Scenics/saveName",{name:$("#"+nodeId).val(),id:scenicId},function(){
		$(".modal").modal('hide');
	});
}
function bindEvent(){
	$("div.scenic-editable").unbind();
	$("div.textEditor").bind("click",function(){
		var val=$(this).html();
		$(this).html("<input type='text' onblur='saveEdit(this)' size='10' value='"+val+"' >");//点击单元格时，动态插入一个input输入框，获取当前单元格的值，当输入框失去焦点时保存值
		$(this).children("input").select();
		$(this).unbind();//取消当前单元格事件，不然一会点击输入框时还会再触发该事件，会有让你意想不到的崩溃效果
	});
}
function saveEdit(ctl){
	var pnt=$(ctl).parent();
	$(pnt).html($(ctl).attr("value"));//将值放回原单元格
	$(pnt).attr("orig",$(ctl).attr("value"));//将当前值保存到orig属性中
	$(ctl).remove();//移除编辑组件，很重要
	bindEvent();//重新绑定事件，其实可以重新绑定当前单元格就可以，但是这样的话我现在给的绑定方法要修改，懒得改了，呵呵
}
//更改图片上传的类型
function changeImgType(_type){
	if(_type=='local'){
		document.onpaste = function(event){}
		$("#imgSrc").val('local');//本地图片
	}else if(_type=='web'){
		document.onpaste = function(event){}
		$("#imgSrc").val('web');//网络图片
	}else if(_type=='paste'){
		$("#imgSrc").val('paste');//直接粘贴
		//直接粘贴需要覆写浏览器粘贴事件
		document.onpaste = function(event){
			console.log("on paste");
			var items = event.clipboardData.items;
			var blob = items[0].getAsFile();
			var reader = new FileReader();
			reader.onload = function(event){
				$("#imgPreview").attr("src",event.target.result);
				$("#pasteImg").val(event.target.result);
			};
			//读取剪贴板中的文件，读取完成后调用onload
			reader.readAsDataURL(blob);
		}
	}
}
function openGallery(_imageId){
	$.post("/Scenics/getImage",{imageId:_imageId},function(data){
		$("#scenic-image-gallery").modal("show");
		$("#scenic-image").attr("src","/data/scenic/images/"+data.imageName);
	});
}