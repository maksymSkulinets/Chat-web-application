var EventBus = function () {

    var _callBacksMap = {};

    var _post = function (eventType, evt) {
        var _subscribersSameType = _callBacksMap[eventType];

        if (typeof (_subscribersSameType) === "undefined") {
            return;
        }

        for (var callbackIdx = 0; callbackIdx < _subscribersSameType.length; callbackIdx++) {
            var _asyncExec = function (index) {
                setTimeout(function () {
                    var currentCbk = _subscribersSameType[index];
                    currentCbk(evt);
                }, 0);
            };
            _asyncExec(callbackIdx);
        }
    };

    var _subscribe = function (eventType, callback) {
        if (typeof(_callBacksMap[eventType]) === "undefined") {
            _callBacksMap[eventType] = [];
        }
        _callBacksMap[eventType].push(callback);
    };

    return {
        "post": _post,
        "subscribe": _subscribe
    };
};


if (typeof define !== 'function') {
    var define = require('amdefine')(module)
}

define(function () {
    return EventBus;
});
