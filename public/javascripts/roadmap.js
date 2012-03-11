$(function() {
		//人均费用
		$( "#slider-range-price" ).slider({
			range: true,
			min: 0,
			max: 20000,
			values: [ 1000, 5000 ],
			slide: function( event, ui ) {
				$( "#price-amount" ).val( "￥" + ui.values[ 0 ] + " - ￥" + ui.values[ 1 ] );
			}
		});
		$( "#price-amount" ).val( "￥" + $( "#slider-range-price" ).slider( "values", 0 ) +
			" - ￥" + $( "#slider-range-price" ).slider( "values", 1 ) );
		//出行时间规划
		$( "#slider-range-date" ).slider({
			range: true,
			min: 1,
			max: 30,
			values: [ 3, 7 ],
			slide: function( event, ui ) {
				$( "#date-amount" ).val( ui.values[ 0 ] + " 天 - " + ui.values[ 1 ] + " 天" );
			}
		});
		$( "#date-amount" ).val( $( "#slider-range-date" ).slider( "values", 0 ) + " 天" +
			" -" + $( "#slider-range-date" ).slider( "values", 1 ) + " 天");
		//出行方式
		$( "#radio" ).buttonset();
		//游玩风格
		$( "#format" ).buttonset();
	});