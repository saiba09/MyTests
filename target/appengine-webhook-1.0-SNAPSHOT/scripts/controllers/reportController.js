EYApp.controller('reportCtrl', ['$scope', '$state', function($scope,$state){
	
	$scope.reportTabList = ['Table View','Chart View'];
	$scope.selectedReportTab = $scope.reportTabList[0];
		
	$scope.selectReportTab = function(tab)
	{			
		$scope.selectedReportTab = tab;
	}

	var dataSet = [
    {label: "Asia", data: 4119630000, color: "#005CDE" },
    { label: "Latin America", data: 590950000, color: "#00A36A" },
    { label: "Africa", data: 1012960000, color: "#7D0096" },
    { label: "Oceania", data: 35100000, color: "#992B00" },
    { label: "Europe", data: 727080000, color: "#DE000F" },
    { label: "North America", data: 344120000, color: "#ED7B00" }    
	];

	var options = {

    	series: {
        pie: {
            show: true,
            radius: 1,
            label: {
                show: true,
                radius: 2/3,
                formatter:  function (label, series) {
                return '<span style="font-size:10pt;text-align:left;padding-left:10px;color:white;">' +
                +
                Math.round(series.percent) +
                '%</span>';
            },
                background: {
                    opacity: 0.5
                }
            }
        }
    },
    legend: {
        show: false
    }
         };
	$(document).ready(function () {
		$.plot($("#chart"), dataSet, options);
		$.plot($("#chart1"), dataSet, options);
	});

}]);