EYApp.provider('Constants', function () {
    var constants = {};

    var getConstant = function (constant)
	{
        return constants[constant];
    };
	
    var addConstant = function (str, constant) 
	{
        constants[str] = constant;
    };
	
    var getConstants = function () 
	{
        return constants;
    };

    this.$get = function (__env) 
	{
        angular.forEach(__env, function(val,key){
            addConstant(key, val);
        });

		return {
			getConstant: getConstant,
			addConstant: addConstant,
			getConstants: getConstants
		};	
    }
	
});