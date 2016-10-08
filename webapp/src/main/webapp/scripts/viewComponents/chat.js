var Chat = function (_rootDivId, eventBus, Events) {

    var chatIdFrame = _rootDivId + "_chatId";
    var messageIdFrame = _rootDivId + "_messageId";
    
    var _init = function () {
        _render();
    };

    //Inner class:
    function ChatComponent(chatIdFrame, eventBus) {
        var serverCredentials = {};
        var chatCounter = 0;

        function _init() {

            eventBus.subscribe(Events.LOGIN_SUCCESS, function (evt) {
                _createChatContainer();
                serverCredentials.ownerNickname = evt.nickname;
                serverCredentials.tokenId = evt.tokenId;
                serverCredentials.userId = evt.userId;
                _render();
            });

            eventBus.subscribe(Events.CHAT_CREATION_SUCCESS, function (evt) {
                var chatList = evt.chatList;
                var allChats = JSON.parse(chatList);
                $('#' + chatIdFrame).empty();
                _render(allChats);
            });

            eventBus.subscribe(Events.CHAT_CREATION_FAIL, function (evt) {
                _showFail(evt.eventMessage)
            });

            eventBus.subscribe(Events.CHAT_CONNECTION_SUCCESS, function (evt) {
                var chatName = evt.chatName;
                var $messageList = JSON.parse(evt.messages);

                new Chat(chatIdFrame, chatName, eventBus).init($messageList);
                _clearMessage();
            });

            eventBus.subscribe(Events.CHAT_CONNECTION_FAIL, function (evt) {
                _showFail(evt.eventMessage);
            });
        }

        function _render(chatList) {

            var addNewChatButtonId = chatIdFrame + '_addNewChatButtonId';
            var chatInputId = chatIdFrame + '_chatInputId';
            var chatSelectorId = chatIdFrame + '_chatSelectorId';
            var joinChatButtonId = chatIdFrame + '_joinChatButtonId';

            $('#' + chatIdFrame)
                .append($('<h3>').text('Welcome: ' + serverCredentials.ownerNickname)).append($('<hr>'))
                .append($('<input>').attr({'id': chatInputId, 'placeholder': 'Enter new chat name'}))
                .append($('<button>').attr({
                    'class': 'btn btn-success',
                    'id': addNewChatButtonId
                }).text('Add new chat')).append($('<br>'))
                .append($('<select>').attr('id', chatSelectorId))
                .append($('<button>').attr({'class': 'btn btn-success', 'id': joinChatButtonId}).text('Join chat'))
                .append($('<div>').attr('id', messageIdFrame));


            $('#' + addNewChatButtonId).click(function () {
                var chatName = $('#' + chatInputId).val();
                eventBus.post(Events.CHAT_CREATION_REQUEST, {
                    chatName: chatName,
                    userId: serverCredentials.userId,
                    tokenId: serverCredentials.tokenId
                });

            });

            if (chatList) {
                for (var i = 0; i < chatList.length; i++) {
                    $('#' + chatSelectorId).append($('<option>').text(chatList[i]));
                }
            }

            $('#' + joinChatButtonId).click(function () {
                var chatName = $('#' + chatSelectorId).val();
                eventBus.post(Events.CHAT_CONNECTION_REQUEST, {
                    chatName: chatName,
                    userId: serverCredentials.userId,
                    tokenId: serverCredentials.tokenId
                });
            });
        }

        function Chat(chatIdFrame, chatName, eventBus) {

            var messageListId;
            var messageInputId;

            function _init(messages) {
                eventBus.subscribe(Events.POST_MESSAGE_SUCCESS, function (evt) {
                    if (evt.chatName === chatName) {
                        var messageList = JSON.parse(evt.messages);
                        _renderMessages(messageList);
                        _clearMessageInput();
                    }
                });
                eventBus.subscribe(Events.POST_MESSAGE_FAIL, function (evt) {
                    if (evt.chatName === chatName) {
                        $('#' + messageInputId).attr('placeholder', evt.eventMessage);
                    }
                });

                _renderChat(chatIdFrame, chatName, messages);
            }

            function _renderChat(chatIdFrame, chatName, messages) {
                var currentChatId = chatIdFrame + '_chat-' + (++chatCounter);
                var closeChatButtonId = currentChatId + '_closeChatButtonId';
                var sendMessageButtonId = currentChatId + '_sendMessageButtonId';
                messageListId = currentChatId + '_messageListId';
                messageInputId = currentChatId + '_inputId';

                $('#' + _rootDivId)
                    .append($('<div>').attr({'id': currentChatId, 'class': 'box'})
                        .append($('<label>').text('Chat Name: ' + chatName))
                        .append($('<button>').attr({'id': closeChatButtonId,'class':'close-chat-button'})
                            .append($('<span>', {'class': 'glyphicon glyphicon-remove'})))
                        .append($('<ul>', {'id': messageListId, class: 'chat-block'}))
                        .append($('<textarea>').attr('id', messageInputId).attr('placeholder', 'Type message here'))
                        .append($('<button>').attr({
                            'class': 'send-message-button',
                            'id': sendMessageButtonId
                        }).text('Send'))
                        .append($('<div>').attr('id', messageIdFrame))
                    );

                _renderMessages(messages);

                $('#' + sendMessageButtonId).click(function () {
                    var message = $('#' + messageInputId).val();
                    eventBus.post(Events.POST_MESSAGE_REQUEST, {
                        chatName: chatName,
                        message: message,
                        nickname: serverCredentials.ownerNickname,
                        tokenId: serverCredentials.tokenId,
                        userId: serverCredentials.userId
                    });
                });

                $('#' + closeChatButtonId).click(function () {
                    var evt = {
                        tokenId: serverCredentials.tokenId,
                        userId: serverCredentials.userId,
                        chatName: chatName
                    };
                    eventBus.post(Events.CHAT_LEAVE_REQUEST, evt);
                    $('#' + currentChatId).remove();

                })
            }

            function _renderMessages(messages) {
                if (messages) {
                    var $messageList = $('#' + messageListId);
                    $messageList.empty();
                    for (var i = 0; i < messages.length; i++) {
                        var msg = messages[i];
                        $messageList.append($('<li>').text(msg));
                    }
                }
            }

            function _clearMessageInput() {
                $('#' + messageInputId).val('');
            }

            return {
                init: _init
            }
        }

        return {"init": _init};
    }

    function _showFail(message) {
        _clearMessage();
        $('#' + messageIdFrame)
            .append($('<div>')
                .attr('class', 'message')
                .attr('class', 'warning')
                .text(message));
    }

    function _clearMessage() {
        $('#' + messageIdFrame).empty();
    }

    function _render() {
        var chatComponent = new ChatComponent(chatIdFrame, eventBus);
        chatComponent.init();
    }

    function _createChatContainer() {
        $('#' + _rootDivId)
            .append($('<div>')
                .attr('id', chatIdFrame)
                .attr('class', 'chat box'));

    }

    return {"init": _init};

};


if (typeof define !== 'function') {
    var define = require('amdefine')(module)
}

define(function () {
    return Chat;
});