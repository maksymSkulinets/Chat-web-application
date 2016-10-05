var UserService = function (eventBus, events, baseUrl) {

    function _init() {
        eventBus.subscribe(events.REGISTRATION_REQUEST, function (evt) {
            _register(evt.nickname, evt.password, evt.verifyPassword);
        });

        eventBus.subscribe(events.LOGIN_DATA_REQUEST, function (evt) {
            _login(evt.nickname, evt.password);
        });
    }

    function _register(nickname, password, verifyPassword) {

        var userData = {
            'nickname': nickname,
            'password': password,
            'verifyPassword': verifyPassword
        };

        $.post(baseUrl + '/chat/registration',
            userData)
            .done(function (xhr) {
                var data = JSON.parse(xhr);
                data.eventMessage = 'Registration success';
                eventBus.post(events.REGISTRATION_SUCCESS, data);
                console.log('Registration success.');
                console.log(data);
            }, 'text')
            .fail(function (xhr) {
                var data = eval('(' + xhr.responseText + ')');
                eventBus.post(events.REGISTRATION_FAIL, data);
                console.log('Registration fail.');
                console.log(xhr);
            })
    }

    function _login(nickname, password) {

        var userData = {
            'nickname': nickname,
            'password': password
        };

        $.post(baseUrl + '/chat/login',
            userData)
            .done(function (xhr) {
                var data = eval('(' + xhr + ')');
                data.eventMessage = 'Login success';
                data.nickname = nickname;
                eventBus.post(events.LOGIN_SUCCESS, data);
                console.log('Login success.');
                console.log(data);
            }, 'text')
            .fail(function (xhr) {
                var data = eval('(' + xhr.responseText + ')');
                eventBus.post(events.LOGIN_FAIL, data);
                console.log('Login fail.');
                console.log(xhr);
            })
    }

    return {
        "init": _init
    };

};

if (typeof define !== 'function') {
    var define = require('amdefine')(module)
}

define(function () {
    return UserService;
});
