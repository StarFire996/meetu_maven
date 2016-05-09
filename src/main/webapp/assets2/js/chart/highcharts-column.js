
$(function () {
    
    var colors = Highcharts.getOptions().colors,
        categoriesTotal = ['累计受理', '正在办理', '累计办结'],
        categoriesToday = ['今日受理', '正在办理', '今日办结'],
        name = 'Brands',
        dataTotal = [{
                y: 2570,
                color: colors[0],
                
            }, {
                y: 248,
                color: colors[3],
                
            }, {
                y: 1876,
                color: colors[2],
                
            }],
        
        dataToday = [{
                y: 43,
                color: colors[0],
                
            }, {
                y: 12,
                color: colors[3],
                
            }, {
                y: 35,
                color: colors[2],
                
            }];

    function setChart(name, categories, data, color) {
	chart.xAxis[0].setCategories(categories, false);
	chart.series[0].remove(false);
	chart.addSeries({
		name: name,
		data: data,
		color: color || 'white'
	}, false);
	chart.redraw();
    }

    
    var chartTotal = $('#js-column-total').highcharts({
        chart: { type: 'column', height: '200' },
        title: { text: false },
        subtitle: { text: false },
        legend:{ enabled: false },
        credits:{ enabled:false },
        exporting: { enabled: false },
        plotOptions: {
            column: {
                dataLabels: {
                    enabled: true,
                    color: colors[1],
                    style: {
                        fontWeight: 'bold',
                        fontSize: '12px'
                    },
                    formatter: function() {
                        return this.y;
                    }
                }
            }
        },
        tooltip: {
            formatter: function() {
                var point = this.point,
                    s = this.x +':<b>'+ this.y + '件';
                return s;
            }
        },
        
        yAxis: {
            title: { text: false }
        },
        xAxis: {
            categories: categoriesTotal
        },
        
        series: [{
            name: name,
            data: dataTotal,
            color: 'white'
        }]
        
    })
    
    
    
    var chartToday = $('#js-column-today').highcharts({
        chart: {
            type: 'column',
            height: '200'
        },
        title: { text: false },
        subtitle: { text: false },
        legend: { enabled: false },
        credits:{ enabled:false },
        exporting: { enabled: false },
        plotOptions: {
            column: {
                dataLabels: {
                    enabled: true,
                    color: colors[1],
                    style: {
                        fontWeight: 'bold',
                        fontSize: '12px'
                    },
                    formatter: function() {
                        return this.y;
                    }
                }
            }
        },
        tooltip: {
            formatter: function() {
                var point = this.point,
                    s = this.x +':<b>'+ this.y + '件';
                return s;
            }
        },
        yAxis: {
            title: { text: false }
        },
        
        xAxis: { categories: categoriesToday },
        series: [{
            name: name,
            data: dataToday,
            color: 'white'
        }]
        
    })
    .highcharts();
});	
