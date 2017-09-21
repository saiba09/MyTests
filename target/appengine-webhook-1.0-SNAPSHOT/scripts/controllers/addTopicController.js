EYApp.controller('addTopicCtrl', ['$scope', '$state', 'ngDialog', 'topicService', function($scope, $state, ngDialog, topicService){
	
	$scope.close = function()
	{
		ngDialog.close();
	}
	
	$scope.save = function()
	{
		var topicData = 
		{
			topic : $scope.topic,
			subTopic : $scope.subtopic
		};
		var p = topicService.createTopic(topicData);
		p.then(function(response)
		{
			//ngDialog.close();
			ngDialog.open({
				scope: $scope,
				showClose : true,
				template: 'topicAdded',
				controller : ['$scope', 'ngDialog', function ($scope, ngDialog) {
						$scope.ngDialog = ngDialog;
						$scope.message = "Topic Added Successfully!";
					}]
			});
		},
		function(error){
			//ngDialog.close();
			ngDialog.open({
				scope: $scope,
				showClose : true,
				template: 'topicAdded',
				controller : ['$scope', 'ngDialog', function ($scope, ngDialog) {
						$scope.ngDialog = ngDialog;
						$scope.message = "Error occurred while adding topic!";
					}]
			});
		});
	}	
	
}]);