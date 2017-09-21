EYApp.controller('complianceMgmtCtrl', ['$scope', '$state', 'ngDialog', function($scope, $state, ngDialog)
{	
	$scope.addTopic = function(size,parentSelector)
	{
		ngDialog.open
		({ 
			template: 'views/PopUps/addTopic.html', 
			controller : 'addTopicCtrl',
			closeByDocument: false,
			className: 'ngdialog-theme-default' 
		 });
	}
	
	$scope.manageGeo = function()
	{
		$state.go('home.manageGeo');
	}
	
	$scope.manageQuestions = function(size)
	{
		$state.go('home.manageQuestions');
	}
	
}]);