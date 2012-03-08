$(function() {
		//酒店类型
		$("#hotel_type").buttonset();
		//日期选择
		$.datepicker.regional['zh-CN'] = {  
        closeText: '关闭', 
        closeStatus: '不改变当前选择', 
        prevText: '<上月', 
        prevStatus: '显示上月', 
        prevBigText: '<<', 
        prevBigStatus: '显示上一年', 
        nextText: '下月>', 
        nextStatus: '显示下月', 
        nextBigText: '>>', 
        nextBigStatus: '显示下一年', 
        currentText: '今天', 
        currentStatus: '显示本月', 
        monthNames: ['一月','二月','三月','四月','五月','六月', '七月','八月','九月','十月','十一月','十二月'], 
        monthNamesShort: ['一','二','三','四','五','六', '七','八','九','十','十一','十二'], 
        monthStatus: '选择月份', 
        yearStatus: '选择年份', 
        weekHeader: '周', 
        weekStatus: '年内周次', 
        dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'], 
        dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'], 
        dayNamesMin: ['日','一','二','三','四','五','六'],  
        dateFormat: 'yy-mm-dd', 
        firstDay: 1, 
        isRTL: false
        }; 
        $.datepicker.setDefaults($.datepicker.regional['zh-CN']); 
		$("#begin_date" ).datepicker({
			numberOfMonths: 3,
			showButtonPanel: true,
			changeYear: true,
			changeMonth: true
		});
		$("#end_date" ).datepicker({
			numberOfMonths: 3,
			showButtonPanel: true,
			changeYear: true,
			changeMonth: true
		});
	});