var Login = function (_rootDivId, eventBus, Events) {

    var loginFormId = _rootDivId + "_loginFormId";
    var messageId = _rootDivId + "_messageId";

    function _init() {

        eventBus.subscribe(Events.LOGIN_FORM_RENDERING, function () {
            _renderForm();
        });

        eventBus.subscribe(Events.LOGIN_FAIL, function (evt) {
            _showFail(evt.eventMessage);
        });

        eventBus.subscribe(Events.LOGIN_SUCCESS, function () {
            _removeForm();
        });

    }

    function _renderForm() {
        _createLoginContainer();

        var nicknameId = loginFormId + '_nicknameId';
        var passwordId = loginFormId + '_passwordId';
        var loginButtonId = loginFormId + '_loginButtonId';

        $('#' + loginFormId)
            .append($('<h3>').text('Login:'))
            .append($('<hr>'))
            .append($('<label>').attr({
                'id': 'icon',
                'for': nicknameId
            }).append($('<i>').attr('class', 'glyphicon glyphicon-user')))
            .append($('<input>').attr({'id': nicknameId, 'type': 'text', 'placeholder': 'nickname'}))
            .append($('<br>'))
            .append($('<label>').attr({
                'id': 'icon',
                'for': passwordId
            }).append($('<i>').attr('class', 'glyphicon glyphicon-lock'))).append($('<input>').attr({
            'id': passwordId,
            'type': 'password',
            'placeholder': 'password'
        }))
            .append($('<button>').attr({'id': loginButtonId, 'class': 'large-button'}).text('Login'))
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

            eventBus.post(Events.LOGIN_DATA_REQUEST, evt);
        })
    }

    function _showFail(message) {
        _clearMessage();
        $('#' + messageId)
            .append($('<span>')
                .attr('class', 'message warning glyphicon glyphicon-remove')
                .text(message));
    }

    function _clearMessage() {
        $('#' + messageId).empty();
    }

    function _createLoginContainer() {
        $('#' + _rootDivId)
            .append($('<div>')
                .attr('id', loginFormId)
                .attr('class', 'login-form_box'));
    }

    function _removeForm() {
        $('#' + loginFormId).remove();
    }

    return {"init": _init};

};

if (typeof define !== 'function') {
    var define = require('amdefine')(module)
}

define(function () {
    return Login;
});