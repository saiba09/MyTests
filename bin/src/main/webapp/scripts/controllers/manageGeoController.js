EYApp.controller('manageGeoCtrl', ['$scope', '$state', 'ngDialog', function($scope, $state, ngDialog)
{
	$scope.addGeo = function()
	{
		ngDialog.open
		({ 
			template: 'views/PopUps/addGeo.html', 
			controller : 'addGeoCtrl',
			closeByDocument: false,
			className: 'ngdialog-theme-default' 
		 });
	}
}]);