EYApp.controller('homePageCtrl', ['$scope', '$location' ,'$window','ngDialog', '$state', '$http',
    function($scope, $location, $window, ngDialog, $state, $http)
	{
		$scope.tabList = [
			{
			"title":'Subscriber Management',
			"state" : 'home.subscriberMgmt'
			},
			{
			"title":'Report',
			"state" : 'home.report'
			},
			{
			"title":'Compliance DB Management',
			"state" : 'home.complianceMgmt'
			},
			{
			"title":'Chatbot',
			"state" : 'home.chatbot'
			}
		];
		$scope.selectedTab = getSelectedState();
		function getSelectedState()
		{
			for(var i=0;i<=$scope.tabList.length;i++)
			{
				if($state.current.name.includes("manageGeo") || $state.current.name.includes("manageQuestions"))
				{
					return "home.complienceMgmt";
				}
				else if($state.current.name.includes($scope.tabList[i].state))
				{
					return $scope.tabList[i];
				}
			}
		}
		
		$scope.selectTab = function(index,tab)
		{
			$scope.selectedTab = $scope.tabList[index];
			$state.go(tab.state);
		}

			 
}]);