var ChatService = function (eventBus, events, baseUrl) {

    
    function _init() {
        eventBus.subscribe(events.CHAT_CREATION_REQUEST, function (evt) {
            _createChat(evt.chatName, evt.userId, evt.tokenId);
        });
      /*  eventBus.subscribe(events.CHAT_CONNECTION_REQUEST, function (evt) {
            _connectChat(evt.name);
        });
        eventBus.subscribe(events.CHAT_MESSAGE_CREATION_REQUEST, function (evt) {
            _addMessage(evt.chatName, evt.userNickname, evt.message);
        });*/
    }


    function _createChat(chatName, userId, tokenId) {
        var chatDto = {
            'chatName': chatName,
            'userId': userId,
            'tokenId': tokenId
        };

        $.post(baseUrl + '/chat/login',
            chatDto,
            function (xhr) {
                var data = eval('(' + xhr + ')');
                data.chatName = chatName;
                eventBus.post(events.CHAT_CREATION_SUCCESS, data);
                console.log(data);
            }, 'text')
            .fail(function (xhr) {
                var data = eval('(' + xhr.responseText + ')');
                eventBus.post(events.CHAT_CREATION_FAIL, data);
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
    return ChatService;
});
