var ChatApp = function (_rootDivId, eventBus, Events) {

    var registrationFormId = _rootDivId + "_registrationFormId";
    var loginFormId = _rootDivId + "_loginFormId";
    var chatIdFrame = _rootDivId + "_chatId";
    var messageIdFrame = _rootDivId + "_messageId";
    var _init = function () {
        _render();
    };

    // Inner class:
    function RegistrationComponent(containerId) {

        var _init = function () {
            _createRegistrationContainer();

            eventBus.subscribe(Events.REGISTRATION_FAIL, function (evt) {
                _showFail(evt.eventMessage);
            });
            eventBus.subscribe(Events.REGISTRATION_SUCCESS, function (evt) {
                _showSuccess(evt.eventMessage);
            });

            _renderForm();
        };

        function _renderForm() {
            var nicknameId = containerId + "_nicknameId";
            var passwordId = containerId + "_passwordId";
            var verifyPasswordId = containerId + "_verifyPasswordId";
            var registerButtonId = containerId + "_registerButtonId";

            $('#' + containerId)
                .text('Registration form.').append($('<br>'))
                .append('Please fill in next fields:').append($('<p>'))
                .append($('<label>').attr('for', nicknameId).text('Nickname:')).append($('<br>'))
                .append($('<input>').attr({'id': nicknameId, 'type': 'text'}))
                .append($('<br>'))
                .append($('<label>').attr('for', passwordId).text('Password:')).append($('<br>'))
                .append($('<input>').attr({'id': passwordId, 'type': 'password'}))
                .append($('<br>'))
                .append($('<label>').attr('for', verifyPasswordId).text('Verify password:')).append($('<br>'))
                .append($('<input>').attr({'id': verifyPasswordId, 'type': 'password'}))
                .append($('<br>'))
                .append($('<button>').attr({'id': registerButtonId, 'class': 'btn btn-success'}).text('Register'))
                .append($('<div>').attr('id', messageIdFrame));

            $('#' + registerButtonId).click(function () {

                console.log('Registration form. Data input:');
                var nickname = $('#' + nicknameId).val();
                console.log('nickname: ' + nickname);
                var password = $('#' + passwordId).val();
                console.log('password: ' + password);
                var verifyPassword = $('#' + verifyPasswordId).val();
                console.log('verifyPassword: ' + verifyPassword);

                var evt = {
                    "nickname": nickname,
                    "password": password,
                    "verifyPassword": verifyPassword
                };

                eventBus.post(Events.REGISTRATION_REQUEST, evt);
            });
        }


        return {"init": _init};
    }

    //Inner class:
    function LoginComponent(containerId, eventBus) {
        function _init() {
            eventBus.subscribe(Events.REGISTRATION_SUCCESS, function () {
                _renderButton();
            });
            eventBus.subscribe(Events.LOGIN_FAIL, function (evt) {
                _showFail(evt.eventMessage);
            });

        }

        function _renderButton() {
            _createLoginContainer();

            var loginButtonId = containerId + '_loginButtonId';
            var $rootForm = $('#' + loginFormId);
            $rootForm.empty();
            $rootForm.append($('<button>)').attr({
                'id': loginButtonId,
                'class': 'btn btn-success'
            }).text('Start login'));

            $('#' + loginButtonId).click(function () {
                $('#' + registrationFormId).remove();
                _renderForm()
            })
        }

        function _renderForm() {

            var nicknameId = loginFormId + '_nicknameId';
            var passwordId = loginFormId + '_passwordId';
            var loginButtonId = loginFormId + '_loginButtonId';

            $('#' + loginFormId)
                .text('Login form.').append($('<br>'))
                .append('Please fill in next fields:').append($('<p>'))
                .append($('<label>').attr('for', nicknameId).text('Nickname:')).append($('<br>'))
                .append($('<input>').attr({'id': nicknameId, 'type': 'text'})).append($('<br>'))
                .append($('<label>').attr('for', passwordId).text('Password:')).append($('<br>'))
                .append($('<input>').attr({'id': passwordId, 'type': 'password'})).append($('<br>'))
                .append($('<button>').attr({'id': loginButtonId, 'class': 'btn btn-success'}).text('Login'))
                .append($('<div>').attr('id', messageIdFrame));

            $('#' + loginButtonId).click(function () {

                console.log('Login form. Data input:');
                var nickname = $('#' + nicknameId).val();
                console.log('nickname: ' + nickname);
                var password = $('#' + passwordId).val();
                console.log('password: ' + password);

                var evt = {
                    'nickname': nickname,
                    'password': password
                };

                eventBus.post(Events.LOGIN_DATA_REQUEST, evt)
            })

        }


        return {"init": _init};
    }

    //Inner class:
    function ChatComponent(chatId, eventBus) {
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
                var allChats = $.map(JSON.parse(chatList), function (element) {
                    return element;
                });

                _render(allChats);
            });
            eventBus.subscribe(Events.CHAT_CREATION_FAIL, function (evt) {
                _showFail(evt.eventMessage)
            });
            eventBus.subscribe(Events.CHAT_CONNECTION_SUCCESS, function (evt) {
                var chatName = evt.chatName;
                var $messageList = $.map(JSON.parse(evt.messages), function (element) {
                    return element;
                });

                new Chat(chatId, chatName, eventBus).init($messageList);
                _clearMessage();
            });
            eventBus.subscribe(Events.CHAT_CONNECTION_FAIL, function (evt) {
                _showFail(evt.eventMessage);
            });
        }

        function _render(chatList) {

            var addNewChatButtonId = chatId + '_addNewChatButtonId';
            var chatInputId = chatId + '_chatInputId';
            var chatSelectorId = chatId + '_chatSelectorId';
            var joinChatButtonId = chatId + '_joinChatButtonId';

            $('#' + loginFormId).remove();
            $('#' + chatId).text('Welcome: ' + serverCredentials.ownerNickname).append($('<p>'))
                .append($('<input>').attr({'id': chatInputId, 'placeholder': 'Enter new chat name'}))
                .append($('<button>').attr({'class': 'btn btn-success', 'id': addNewChatButtonId}).text('Add new chat'))
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

        function Chat(chatId, chatName, eventBus) {

            var messageListId;
            var messageInputId;

            function _init(messages) {
                eventBus.subscribe(Events.POST_MESSAGE_SUCCESS, function (evt) {
                    if (evt.chatName === chatName) {
                        var messageList = $.map(JSON.parse(evt.messages), function (argument) {
                            return argument;
                        });
                        _renderMessages(messageList);
                        _clearMessageInput();
                    }
                });
                eventBus.subscribe(Events.POST_MESSAGE_FAIL, function (evt) {
                    if (evt.chatName === chatName) {
                        $('#' + messageInputId).attr('placeholder', evt.eventMessage);
                    }
                });

                _renderChat(chatId, chatName, messages);
            }

            function _renderChat(chatId, chatName, messages) {
                var currentChatId = chatId + '_chat-' + (++chatCounter);
                var closeChatButtonId = currentChatId + '_closeChatButtonId';
                var sendMessageButtonId = currentChatId + '_sendMessageButtonId';
                messageListId = currentChatId + '_messageListId';
                messageInputId = currentChatId + '_inputId';

                $('#' + _rootDivId)
                    .append($('<div>').attr({'id': currentChatId, 'class': 'box'})
                        .append($('<label>').text('Chat Name: ' + chatName))
                        .append($('<button>').attr('id', closeChatButtonId)
                            .append($('<img>').attr({'src': 'image/close_btn.gif', 'alt': 'close chat'})))
                        .append($('<ul>', {'id': messageListId, class: 'chat-block'}))
                        .append($('<textarea>').attr('id', messageInputId).attr('placeholder', 'Type message here'))
                        .append($('<button>').attr({'class': 'btn btn-success','id': sendMessageButtonId}).text('Send'))
                        .append($('<div>').attr('id', messageIdFrame)));

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

    function _showSuccess(message) {
        _clearMessage();
        $('#' + messageIdFrame)
            .append($('<div>')
                .attr('class', 'message')
                .attr('class', 'success')
                .text(message));
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
        var registrationForm = new RegistrationComponent(registrationFormId);
        registrationForm.init();

        var loginComponent = new LoginComponent(loginFormId, eventBus);
        loginComponent.init();

        var chatComponent = new ChatComponent(chatIdFrame, eventBus);
        chatComponent.init();
    }

    function _createRegistrationContainer() {
        $('#' + _rootDivId)
            .append($('<div>')
                .attr('id', registrationFormId)
                .attr('class', 'registration-form box')
                .text('registration-form '));
    }

    function _createLoginContainer() {
        $('#' + _rootDivId)
            .append($('<div>')
                .attr('id', loginFormId)
                .attr('class', 'login-form box'));
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
    return ChatApp;
});