var Storage = function () {
    var container = {};

    function _getEntitiesByType(type) {
        return container[type];
    }

    function _findEntityByValue(type, key, value) {
        var collection = _getEntitiesByType(type);
        if (!collection) {
            return null;
        }

        var object = null;
        for (var i = 0; i < collection.length; i++) {
            if (collection[i][key] === value) {
                object = collection[i];
                break;
            }
        }
        return object;
    }

    function _addEntity(type, entity) {
        if (!container[type]) {
            container[type] = [];
        }
        container[type].push(entity);
    }

    return {
        "getEntitiesByType": _getEntitiesByType,
        "findEntityByValue": _findEntityByValue,
        "addEntity": _addEntity
    };
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function () {
    return Storage;
});
