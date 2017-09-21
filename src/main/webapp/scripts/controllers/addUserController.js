EYApp.controller('addUserCtrl', ['$scope', '$state', 'ngDialog', function($scope, $state, ngDialog)
{
	$scope.cancel = function()
	{
		ngDialog.close();
	}
}]);