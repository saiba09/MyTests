EYApp.controller('subscriberMgmtCtrl', ['$scope', '$state', 'ngDialog', function($scope, $state, ngDialog){

	$scope.deleteUser = function()
	{
	}
	
	$scope.addUser = function()
	{
		ngDialog.open
		({ 
			template: 'views/PopUps/addUser.html', 
			controller : 'addUserCtrl',
			closeByDocument: false,
			className: 'ngdialog-theme-default' 
		 });
	}
	
}]);