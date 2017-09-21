EYApp.controller('addGeoCtrl', ['$scope', '$state', 'ngDialog', function($scope, $state, ngDialog)
{
	$scope.cancel = function()
	{
		ngDialog.close();
	}
}]);