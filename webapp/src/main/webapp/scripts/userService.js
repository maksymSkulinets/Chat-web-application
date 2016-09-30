var UserService = function (eventBus, events, storage) {
    var type = 'user';

    function _init() {
        eventBus.subscribe(events.REGISTRATION_REQUEST, function (evt) {
            _register(evt.nickname, evt.password, evt.verifyPassword);
        });

        eventBus.subscribe(events.LOGIN_DATA_REQUEST, function (evt) {
            _login(evt.nickname, evt.password);
        });

        eventBus.subscribe(events.CHAT_LEAVE_REQUEST, function (evt) {
            _leaveChat(evt.nickname, evt.chatName);
        });

    }

    //Inner class
    function User(nickname, password) {
        return {
            'nickname': nickname,
            'password': password,
            'chats': []
        };
    }

    function _register(nickname, password, verifyPassword) {
        var trimmedNickname = nickname.trim();

        if (!trimmedNickname || !password || !verifyPassword) {
            eventBus.post(events.REGISTRATION_FAIL, {eventMessage: 'All fields must be filled.'});
            return;
        }

        if (storage.findEntityByValue(type, "nickname", trimmedNickname)) {
            eventBus.post(events.REGISTRATION_FAIL, {eventMessage: 'Nickname must be unique.'});
            return;
        }

        if (password !== verifyPassword) {
            eventBus.post(events.REGISTRATION_FAIL, {eventMessage: 'Passwords must be unique.'});
            return;
        }

        var user = new User(nickname, password);
        storage.addEntity(type, user);
        eventBus.post(events.REGISTRATION_SUCCESS, {eventMessage: 'Registration was successful!'});
    }

    function _login(nickname, password) {
        var trimmedNickname = nickname.trim();

        if (!trimmedNickname || !password) {
            eventBus.post(events.LOGIN_FAIL, {eventMessage: 'All fields must be filled.'});
            return;
        }

        var user = storage.findEntityByValue(type, "nickname", trimmedNickname);
        if (!user || user.password !== password) {
            eventBus.post(events.LOGIN_FAIL, {eventMessage: 'Such user must register before.'});
            return;
        }

        eventBus.post(events.LOGIN_SUCCESS, {nickname: trimmedNickname});
    }

    function _leaveChat(nickname, chatName) {
        var user = storage.findEntityByValue(type, 'nickname', nickname);
        for (var i = 0; i < user.chats.length; i++) {
            if (user.chats[i].name === chatName) {
                var evt = {message: 'Chat: ' + chatName + 'was leaved by user' + nickname};
                user.chats.splice(i, 1);
                eventBus.post(Event.CHAT_LEAVE_REQUEST, evt);
                return;
            }
        }
    }


    return {
        "init": _init,
        "register": _register,
        "login": _login,
        'leaveChat': _leaveChat
    };
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module)
}

define(function () {
    return UserService;
});
