$(function(){
	if($("#planNameForm").length > 0){
		$("#planNameForm").validate();
	}
	//date-pickers
	if($(".datepick").length > 0){
		$(".datepick").datepicker().on('changeDate', function(ev){
			datepickerCallback(ev,$(this));
		});
	}
	//time-pickers
	if($(".timepick").length > 0){
		$('.timepick').ptTimeSelect({onClose:timepickerCallback});
	}
	//PlanTime content
	if($(".day-time-content textarea").length > 0){
		$(".day-time-content textarea").each(function(){
			$(this).focus(function(){
				$(this).css("background","#FFF");
			});
			$(this).focusout(function(){
				$(this).css("background","transparent");
				//保存当前content到数据库
				$.post("/Plans/savePlanTimeContent",{planTimeId:$(this).attr("planTimeId"),content:$(this).val()},function(){});
			});
		});
	}
});
//保存计划名称
function savePlanName(_id){
	$.post("/Plans/savePlanName",{planId:_id,planName:$("#planName").val()},function(data){
		$("#planName").addClass("plan-name-saved");
		$("#savePlanNameBtn").attr("disabled","disabled");
	});
}
//重新编辑计划名称
function editPlanName(){
	$("#planName").removeClass("plan-name-saved");
	$("#savePlanNameBtn").removeAttr("disabled");
}
//添加一天计划
function addDayPlan(){
	var d = new Date();
	var date = dateFormat(d)
	$.post("/Plans/savePlanDay",{planDayId:0,planId:$("#planId").val(),date:date},function(planDayId){
		var html = '<div class="day-plan" planDayId="'+planDayId+'">' +
				'<div class="day-label">' +
						'<div>' +
							'<input class="btn btn-info datepick input-small" data-date-format="yyyy-mm-dd" data-date="'+date+'" value="'+date+'"/>' +
							'<a class="btn btn-success" onclick="addDayTime(this)">添加时间段</a>' +
							'<a class="btn btn-success" onclick="showImgModal('+planDayId+')">添加图片</a>' +
					        '<a class="btn btn-danger" onclick="deletePlanDay(this)"><i class="icon-trash icon-white"></i></a>' +
					'</div>' +
				'</div>'+
				'<div class="day-plan-maps"></div>' + 
				'<div class="day-plan-images"></div>' + 
				'</div>';
		$("#days-plan-container").append(html);
		$(".datepick").datepicker().on('changeDate', function(ev){
			datepickerCallback(ev,$(this));
		});
		//TODO CHROME下JQUERY添加CSS后效果不理想，刷新页面可解决问题(原因未知).
		window.location.reload();
	});
	
}
function deletePlanDay(_node){
	var planDayId = $(_node).closest(".day-plan").attr("planDayId");
	$.post("/Plans/deletePlanDay",{planDayId:planDayId},function(){
		$(_node).closest(".day-plan").remove();
	})
}
//添加一天内的时间段,_node是出发按钮节点
function addDayTime(_node){
	var dayId = $(_node).closest(".day-plan").attr("planDayId");
	$.post("/Plans/savePlanTime",
			{planDayId:dayId,planTimeId:0,time:'',isStartTime:null},
			function(planTimeId){
		var html = '<div class="day-content" planDayId="0">' + 
				'<div class="day-time-plan">' + 
					'<div class="day-time form-inline well">' +
					'<input type="text" planTimeId="'+planTimeId+'" name="startTime" class="timepick input-small" placeholder="开始时间"/> ~ ' + 
					'<input type="text" planTimeId="'+planTimeId+'" name="endTime" class="timepick input-small" placeholder="结束时间"/>' +
					'<a class="btn btn-danger" onclick="deleteDayTime(this,'+planTimeId+')"><i class="icon-trash icon-white"></i></a>' +
					'<div class="day-time-content"><textarea rows="2" planTimeId="'+planTimeId+'"></textarea></div>' +
					'</div>' + 
				'</div>' + 
			'</div>';
		$(_node).closest(".day-plan").append(html);
		// 添加新的HTML元素后重新设置时间选择事件
		$('.timepick').ptTimeSelect({onClose:timepickerCallback});
		//添加新元素后重新设置textarea编辑事件
		$(".day-time-content textarea").each(function(){
			$(this).focus(function(){
				$(this).css("background","#FFF");
			});
			$(this).focusout(function(){
				$(this).css("background","transparent");
				//保存当前content到数据库
				$.post("/Plans/savePlanTimeContent",{planTimeId:$(this).attr("planTimeId"),content:$(this).val()},function(){});
			});
		});
	});
}
function deleteDayTime(_node,planTimeId){
	$.post("/Plans/deletePlanTime",{planTimeId:planTimeId},function(){
		$(_node).closest(".day-time-plan").remove();
	});
}
//date-picker 回调函数,_node为选择日期的按钮
function datepickerCallback(ev,_node){
	var d = ev.date;
	var date = dateFormat(ev.date);
	var dayId = $(_node).closest(".day-plan").attr("planDayId");
	//显示新日期
	$(_node).html(date);
	//持久化 PlanDay
	$.post("/Plans/savePlanDay",{planDayId:dayId,planId:$("#planId").val(),date:date},function(planDayId){
		$(_node).closest(".day-plan").attr("planDayId",planDayId);
	});
}
//time-picker 回调函数,_node是时间选择框的JQuery对象
function timepickerCallback(_node){
	var planDayId = $(_node).closest(".day-plan").attr("planDayId");
	var time = $(_node).val();
	var timeId = $(_node).attr("planTimeId");
	var isStartTime = false;
	if($(_node).attr("name")=='startTime'){
		isStartTime = true;
	}
	$.post("/Plans/savePlanTime",
			{planDayId:planDayId,planTimeId:timeId,time:time,isStartTime:isStartTime},
			function(planTimeId){
		$(_node).attr("planTimeId",planTimeId);
	});
}
// 2012-02-03,zero prefix format
function dateFormat(date){
	var val = {
		dd: date.getDate(),
		mm: date.getMonth() + 1,
		yyyy: date.getFullYear()
	};
	
	val.dd = (val.dd < 10 ? '0' : '') + val.dd;
	val.mm = (val.mm < 10 ? '0' : '') + val.mm;
	var date = [val.yyyy,val.mm,val.dd];
	console.log(date.join("-"));
	return date.join("-");
}
function preUploadImg(_node){
	var imgSrc = $(_node).val();
	if(imgSrc.toLowerCase().indexOf("http") > -1){
		$("#imgPreview").attr("src",imgSrc);
	}
}
//显示上传图片modal
function showImgModal(planDayId){
	$("#newImg").modal('toggle');
	$("#planDayId").val(planDayId);
}
//切换图片源
function changeImg(_type){
	if(_type=='local'){
		$("#imgSrc").val('local');//本地图片
	}else{
		$("#imgSrc").val('web');//网络图片
	}
}
function deletePlanDayImg(_id){
	$.post("/Plans/deletePlanDayImg",{imageId:_id});
}