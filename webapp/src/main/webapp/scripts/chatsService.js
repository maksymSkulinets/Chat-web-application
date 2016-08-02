var ChatService = function (eventBus, events, storage) {
    var type = 'chat';

    function _init() {
        eventBus.subscribe(events.CHAT_CREATION_REQUEST, function (evt) {
            _addChat(evt.name, evt.owner);
        });
        eventBus.subscribe(events.CHAT_MESSAGE_CREATION_REQUEST, function (evt) {
            _addMessage(evt.chatName, evt.userNickname, evt.message);
        });
        eventBus.subscribe(events.CHAT_CONNECTION_REQUEST, function (evt) {
            _connectChat(evt.name);
        });
    }

    //Inner class
    function Chat(name, owner) {
        return {
            'name': name,
            'owner': owner,
            'messages': []
        };
    }

    //Inner class
    function Message(nickname, message) {
        return {
            ownerName: nickname,
            message: message
        };
    }

    /*
     TODO can be used for additional loading, for example on init
     function _loadChats() {
     eventBus.post(events.CHAT_LIST_UPDATED, {chatList: storage.getEntitiesByType(type)});
     }*/

    function _addChat(name, owner) {
        var trimmedName = name.trim();

        if (trimmedName === '') {
            eventBus.post(events.CHAT_CREATION_FAIL, {message: "Chat name must be filled."});
            return;
        }
        if (storage.findEntityByValue(type, 'name', trimmedName)) {
            eventBus.post(events.CHAT_CREATION_FAIL, {message: "Chat name must be unique."});
            return;
        }

        var newChat = new Chat(name, owner);
        storage.addEntity(type, newChat);
        eventBus.post(events.CHAT_CREATION_SUCCESS, {chatList: storage.getEntitiesByType(type)});
    }

    function _connectChat(name, ownerNickname) {
        if (!name) {
            eventBus.post(events.CHAT_CONNECTION_FAIL, {message: "Chat name must be specified."});
            return;
        }

        var trimmedName = name.trim();

        var chat = storage.findEntityByValue(type, 'name', trimmedName);
        if (!chat) {
            eventBus.post(events.CHAT_CONNECTION_FAIL, {message: "Chat with such name doesn't exist."});
            return;
        }
        var user = storage.findEntityByValue('user', 'name', ownerNickname);
        if (user.chats.indexOf(chat) !== -1) {
            eventBus.post(events.CHAT_CONNECTION_FAIL, {message: "Already connected."});
            return;
        }

        user.chats.push(chat);
        eventBus.post(events.CHAT_CONNECTION_SUCCESS, {chat: chat});
    }

    function _addMessage(chatName, userNickname, message) {
        var trimmedMessage = message.trim();
        if (trimmedMessage === '') {
            eventBus.post(events.CHAT_MESSAGE_CREATION_FAIL, {
                target: chatName,
                message: "Empty message can't be posted."
            });
            return;
        }

        var newMessage = new Message(userNickname, message);
        var currentChat = storage.findEntityByValue(type, 'name', chatName);
        currentChat.messages.push(newMessage);

        /*TODO: to save chat to server need something like this:
         storage.updateEntity(type, currentChat);
         */

        eventBus.post(events.CHAT_MESSAGE_CREATION_SUCCESS, {chat: currentChat});
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
