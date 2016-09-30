var ChatApp = function (_rootDivId, eventBus, Events) {

    var registrationFormId = _rootDivId + "_registrationFormId";
    var loginFormId = _rootDivId + "_loginFormId";
    /*TODO rename variables to chatIdFrame and messageIdFrame to prevent duplication in naming*/
    var chatId = _rootDivId + "_chatId";
    var messageId = _rootDivId + "_messageId";
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
                .append($('<input>').attr('id', nicknameId).attr('type', 'text'))
                .append($('<br>'))
                .append($('<label>').attr('for', passwordId).text('Password:')).append($('<br>'))
                .append($('<input>').attr('id', passwordId).attr('type', 'password'))
                .append($('<br>'))
                .append($('<label>').attr('for', verifyPasswordId).text('Verify password:')).append($('<br>'))
                .append($('<input>').attr('id', verifyPasswordId).attr('type', 'password'))
                .append($('<br>'))
                .append($('<button>').attr('class', 'btn btn-success').attr('id', registerButtonId).text('Register'))
                .append($('<div>').attr('id', messageId));

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
            $rootForm.append($('<button>)').attr('id', loginButtonId).attr('class', 'btn btn-success').text('Start login'));

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
                .append($('<input>').attr('id', nicknameId).attr('type', 'text')).append($('<br>'))
                .append($('<label>').attr('for', passwordId).text('Password:')).append($('<br>'))
                .append($('<input>').attr('id', passwordId).attr('type', 'password')).append($('<br>'))
                .append($('<button>').attr('id', loginButtonId).attr('class', 'btn btn-success').text('Login'))
                .append($('<div>').attr('id', messageId));

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

            /*
             TODO can be used to init chat list before rendering
             eventBus.subscribe(Events.CHAT_LIST_UPDATED, function (evt) {
             chatList = evt.chatList;
             if (rendered) {
             _render(chatList);
             }
             });*/

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
                var chatName = evt.chat.name;
                new Chat(chatId, chatName, eventBus).init(evt.chat.messages);
                _clearMessage();
            });
            eventBus.subscribe(Events.CHAT_CONNECTION_FAIL, function (evt) {
                _showFail(evt.message);
            });
        }

        function _render(chatList) {

            var addNewChatButtonId = chatId + '_addNewChatButtonId';
            var inputId = chatId + '_inputId';
            var selectId = chatId + '_selectorId';
            var joinChatButtonId = chatId + '_joinChatButtonId';

            $('#' + loginFormId).remove();
            $('#' + chatId).text('Welcome: ' + serverCredentials.ownerNickname).append($('<p>'))
                .append($('<input>').attr('id', inputId).attr('placeholder', 'Enter new chat name'))
                .append($('<button>').attr('class', 'btn btn-success').attr('id', addNewChatButtonId).text('Add new chat'))
                .append($('<select>').attr('id', selectId))
                .append($('<button>').attr('class', 'btn btn-success').attr('id', joinChatButtonId).text('Join chat'))
                .append($('<div>').attr('id', messageId));


            $('#' + addNewChatButtonId).click(function () {
                var chatName = $('#' + inputId).val();
                eventBus.post(Events.CHAT_CREATION_REQUEST, {
                    chatName: chatName,
                    userId: serverCredentials.userId,
                    tokenId: serverCredentials.tokenId
                });

            });

            if (chatList) {
                for (var i = 0; i < chatList.length; i++) {
                    $('#' + selectId).append($('<option>').text(chatList[i]));
                }
            }

            $('#' + joinChatButtonId).click(function () {
                var chatName = $('#' + selectId).val();
                eventBus.post(Events.CHAT_CONNECTION_REQUEST, {
                    name: chatName
                });
            });
        }

        function Chat(chatId, chatName, eventBus) {

            var messageListId;
            var messageInputId;

            function _init(messages) {
                eventBus.subscribe(Events.CHAT_MESSAGE_CREATION_SUCCESS, function (evt) {
                    if (evt.chat.name === chatName) {
                        _renderMessages(evt.chat.messages);
                        _clearMessageInput();
                    }
                });
                eventBus.subscribe(Events.CHAT_MESSAGE_CREATION_FAIL, function (evt) {
                    if (evt.target === chatName) {
                        $('#' + messageInputId).attr('placeholder', evt.message);
                    }
                });

                _renderChat(chatId, chatName, messages);
            }

            function _renderChat(chatId, name, messages) {
                var currentChatId = chatId + '_chat-' + (++chatCounter);
                var closeButtonId = currentChatId + '_closeButton';
                messageListId = currentChatId + '_messageListId';
                messageInputId = currentChatId + '_inputId';
                var sendButtonId = currentChatId + '_sendButtonId';

                /*TODO all messages rename to reports(Problem: duplicate posted messages and GUI reports)*/


                $('#' + _rootDivId)
                    .append($('<div>').attr('id', currentChatId).attr('class', 'box')
                        .append($('<label>').text('Chat Name: ' + name))
                        .append($('<button>').attr('class', 'btn btn-primary').attr('id', closeButtonId).text('Close'))
                        .append($('<ul>', {'id': messageListId, class: 'chat-block'}))
                        .append($('<textarea>').attr('id', messageInputId).attr('placeholder', 'Type message here'))
                        .append($('<button>').attr('class', 'btn btn-success').attr('id', sendButtonId).text('Send'))
                        .append($('<div>').attr('id', messageId)));

                _renderMessages(messages);

                $('#' + sendButtonId).click(function () {
                    var message = $('#' + messageInputId).val();
                    eventBus.post(Events.CHAT_MESSAGE_CREATION_REQUEST, {
                        chatName: chatName,
                        userNickname: ownerNickname,
                        message: message
                    });
                });

                $('#' + closeButtonId).click(function () {
                    var evt = {
                        nickname: ownerNickname,
                        chatName: chatName
                    };
                    eventBus.post(Events.CHAT_LEAVE_ACTION, evt);
                    $('#' + currentChatId).remove();

                })
            }

            function _renderMessages(messages) {
                if (messages) {
                    var $messageList = $('#' + messageListId);
                    $messageList.empty();
                    for (var i = 0; i < messages.length; i++) {
                        var msg = messages[i];
                        $messageList.append($('<li>').text(msg.ownerName + ': ' + msg.message));
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
        $('#' + messageId)
            .append($('<div>')
                .attr('class', 'message')
                .attr('class', 'success')
                .text(message));
    }

    function _showFail(message) {
        _clearMessage();
        $('#' + messageId)
            .append($('<div>')
                .attr('class', 'message')
                .attr('class', 'warning')
                .text(message));
    }

    function _clearMessage() {
        $('#' + messageId).empty();
    }

    function _render() {
        var registrationForm = new RegistrationComponent(registrationFormId);
        registrationForm.init();

        var loginComponent = new LoginComponent(loginFormId, eventBus);
        loginComponent.init();

        var chatComponent = new ChatComponent(chatId, eventBus);
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
                .attr('id', chatId)
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