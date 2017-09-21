EYApp.service('chatService', ['$http', function ($http) {
    
    return {
        
        postChat: function(sessionId, message){
            var req = {
                url: '/ai?sessionId=' + sessionId + '&message=' + message,
                method: 'POST'
            };
            return $http(req);
        }
    }
}]);

EYApp.service('topicService', ['$http', function ($http) {
    
    return {
        
        createTopic: function(topicData){
            var req = {
                url: '/addTopic',
                method: 'POST',
                data : topicData
            };
            return $http(req);
        }
    }
}]);