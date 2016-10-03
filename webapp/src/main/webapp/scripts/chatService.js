var ChatService = function (eventBus, events, baseUrl) {


    function _init() {
        eventBus.subscribe(events.CHAT_CREATION_REQUEST, function (evt) {
            _createChat(evt.chatName, evt.userId, evt.tokenId);
        });

        eventBus.subscribe(events.CHAT_CONNECTION_REQUEST, function (evt) {
            _joinChat(evt.chatName, evt.userId, evt.tokenId);
        });

        eventBus.subscribe(events.CHAT_LEAVE_REQUEST, function (evt) {
            _leaveChat(evt.chatName, evt.userId, evt.tokenId);
        });

        eventBus.subscribe(events.POST_MESSAGE_REQUEST, function (evt) {
            _postMessage(evt.chatName, evt.nickname, evt.message, evt.userId, evt.tokenId);
        })
    }

    function _createChat(chatName, userId, tokenId) {
        console.log('Attempt to create chat.');

        var chatDto = {
            'chatName': chatName,
            'userId': userId,
            'tokenId': tokenId
        };

        $.post(baseUrl + '/chat/chat-creation',
            chatDto,
            function (xhr) {
                var data = eval('(' + xhr + ')');
                eventBus.post(events.CHAT_CREATION_SUCCESS, data);
                console.log('Chat creation success.');
                console.log(data);
            }, 'text')
            .fail(function (xhr) {
                var data = eval('(' + xhr.responseText + ')');
                eventBus.post(events.CHAT_CREATION_FAIL, data);
                console.log('Chat creation fail.');
                console.log(xhr);
            })
    }

    function _joinChat(chatName, userId, tokenId) {
        console.log('Attempt to join chat.');

        var chatDto = {
            'chatName': chatName,
            'userId': userId,
            'tokenId': tokenId
        };

        $.post(baseUrl + '/chat/join-chat',
            chatDto,
            function (xhr) {
                var data = eval('(' + xhr + ')');
                eventBus.post(events.CHAT_CONNECTION_SUCCESS, data);
                console.log('Joining chat success.');
                console.log(data);
            }, 'text')
            .fail(function (xhr) {
                var data = eval('(' + xhr.responseText + ')');
                eventBus.post(events.CHAT_CONNECTION_FAIL, data);
                console.log('Joining chat fail.');
                console.log(xhr);
            })
    }

    function _leaveChat(chatName, userId, tokenId) {
        console.log('Attempt to leave chat.');

        var chatDto = {
            'chatName': chatName,
            'userId': userId,
            'tokenId': tokenId
        };

        $.post(baseUrl + '/chat/leave-chat',
            chatDto,
            function () {
                console.log('Leaving chat success.');
            }, 'text')
            .fail(function (xhr) {
                console.log('Leaving chat fail.');
                console.log(xhr);
            })
    }

    function _postMessage(chatName, nickname, message, userId, tokenId) {
        console.log('Attempt to post message.');

        var messageDto = {
            'chatName': chatName,
            'nickname': nickname,
            'message': message,
            'tokenId': tokenId,
            'userId': userId
        };

        $.post(baseUrl + '/chat/post-message',
            messageDto,
            function (xhr) {
                var data = eval('(' + xhr + ')');
                eventBus.post(events.POST_MESSAGE_SUCCESS, data);
                console.log('Post message success.');
                console.log(data);
            }, 'text')
            .fail(function (xhr) {
                var data = eval('(' + xhr.responseText + ')');
                data.chatName = chatName;
                eventBus.post(events.POST_MESSAGE_FAIL, data);
                console.log('Post message fail.');
                console.log(xhr);
            });
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
