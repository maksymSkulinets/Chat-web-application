var Events = {
    REGISTRATION_REQUEST: 'registrationDataRequest',
    REGISTRATION_SUCCESS: 'registrationSuccess',
    REGISTRATION_FAIL: 'registrationFail',
    LOGIN_DATA_REQUEST: 'loginDataRequest',
    LOGIN_SUCCESS: 'loginSuccess',
    LOGIN_FAIL: 'loginFail',
    CHAT_LIST_UPDATED: 'chatListUpdated',
    CHAT_CREATION_REQUEST: 'chatCreationRequest',
    CHAT_CREATION_SUCCESS: 'chatCreationSuccess',
    CHAT_CREATION_FAIL: 'chatCreationFail',
    CHAT_CONNECTION_REQUEST: 'chatConnectionRequest',
    CHAT_CONNECTION_SUCCESS: 'chatConnectionSuccess',
    CHAT_CONNECTION_FAIL: 'chatConnectionFail',
    POST_MESSAGE_REQUEST: 'postMessageRequest',
    POST_MESSAGE_SUCCESS: 'postMessageSuccess',
    POST_MESSAGE_FAIL: 'postMessageFail',
    CHAT_LEAVE_REQUEST: 'chatLeaveRequest',
    REGISTRATION_FORM_RENDERING: 'registrationFormRendering',
    LOGIN_FORM_RENDERING: 'loginFormRendering'
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function () {
    return Events;
});

