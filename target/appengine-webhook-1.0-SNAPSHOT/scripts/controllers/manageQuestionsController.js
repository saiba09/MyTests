EYApp.controller('manageQuestionsCtrl', ['$scope', '$state', 'ngDialog', function($scope, $state, ngDialog)
{
	$scope.close = function()
	{
		ngDialog.close();
	}
	
	$scope.save = function()
	{
		ngDialog.close();
	}	
}]);