var Registration = function (_rootDivId, eventBus, Events) {

    var registrationFormId = _rootDivId + "_registrationFormId";
    var messageId = _rootDivId + "_messageId";

    function _render() {

        _createRegistrationContainer();

        eventBus.subscribe(Events.REGISTRATION_FAIL, function (evt) {
            _showFail(evt.eventMessage);
        });

        eventBus.subscribe(Events.REGISTRATION_SUCCESS, function (evt) {
            _showSuccess(evt.eventMessage);
        });

        _renderForm();
    }

    function _renderForm() {
        var nicknameId = registrationFormId + "_nicknameId";
        var passwordId = registrationFormId + "_passwordId";
        var verifyPasswordId = registrationFormId + "_verifyPasswordId";
        var registerButtonId = registrationFormId + "_registerButtonId";
        var startLoginButtonId = registrationFormId + '_startLoginButtonId';

        $('#' + registrationFormId)
            .append($('<h3>').text('Registration:'))
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
            .append($('<br>'))
            .append($('<label>').attr({
                'id': 'icon',
                'for': verifyPasswordId
            }).append($('<i>').attr('class', 'glyphicon glyphicon-lock'))).append($('<input>').attr({
            'id': verifyPasswordId,
            'type': 'password',
            'placeholder': 'repeat password'
        })).append($('<button>').attr({'id': registerButtonId, 'class': 'large-button'}).text('Register'))
            .append($('<button>)').attr({
                'id': startLoginButtonId,
                'class': 'large-button'
            }).text('Start login'))
            .append($('<span>').attr('id', messageId));


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

        $('#' + startLoginButtonId).click(function () {
            $('#' + registrationFormId).remove();
            eventBus.post(Events.LOGIN_FORM_RENDERING)
        });
    }

    function _showSuccess(message) {
        _clearMessage();
        $('#' + messageId)
            .append($('<span>')
                .attr('class', 'message success glyphicon glyphicon-ok')
                .text(message));
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

    function _createRegistrationContainer() {
        $('#' + _rootDivId)
            .append($('<div>')
                .attr('id', registrationFormId)
                .attr('class', 'registration-form_box'))
    }

    return {
        'render': _render
    };

};


if (typeof define !== 'function') {
    var define = require('amdefine')(module)
}

define(function () {
    return Registration;
});