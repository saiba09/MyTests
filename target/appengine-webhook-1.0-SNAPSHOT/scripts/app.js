var env = {};

// Import variables if present (from env.js)
if(window){
    Object.assign(env, window.__env);
}


var EYApp = angular.module('EYApp', ['ui.router','ngSanitize','ui.bootstrap','LocalStorageModule', 'toaster', 'ngIdle','ngDialog']);

EYApp.run(['$rootScope', '$location', '$http', '$state', '$window', 'toaster', function($rootScope, $location, $http, $state, $window, toaster){

}]);

EYApp.config(['$stateProvider', '$locationProvider', '$urlRouterProvider','$httpProvider', function($stateProvider, $locationProvider, $urlRouterProvider, $httpProvider){
    	
    $stateProvider
		.state('home', {
            abstract:true,
			url: '/home',
			templateUrl: 'views/homePage.html',
			controller : 'homePageCtrl'
        })
		
		.state('home.subscriberMgmt', {
		url: '/subscriberMgmt',
		templateUrl: 'views/subscriberMgmt.html',
		controller: 'subscriberMgmtCtrl',
		})	
		
		.state('home.complianceMgmt', {
		url: '/complianceMgmt',
		templateUrl: 'views/complianceMgmt.html',
		controller: 'complianceMgmtCtrl',
		})
		
		.state('home.manageGeo', {
		url: '/manageGeo',
		templateUrl: 'views/manageGeo.html',
		controller: 'manageGeoCtrl',
		})
		
		.state('home.manageQuestions', {
		url: '/manageQuestions',
		templateUrl: 'views/manageQuestions.html',
		controller: 'manageQuestionsCtrl',
		})
		
		.state('home.report', {
		url: '/reports',
		templateUrl: 'views/reports.html',
		controller: 'reportCtrl',
		})
		
		.state('home.chatbot', {
		url: '/chatbot',
		templateUrl: 'views/chatbot.html',
		controller: 'chatbotCtrl',
		})
		
		$urlRouterProvider.otherwise('/home/subscriberMgmt');
}]);

EYApp.constant('__env', env);