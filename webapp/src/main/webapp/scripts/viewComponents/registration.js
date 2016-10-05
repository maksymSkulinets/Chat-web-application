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
            .append($('<div>').attr('id', messageId))
            .append($('<button>)').attr({
                'id': startLoginButtonId,
                'class': 'btn btn-success'
            }).text('Start login'));

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

    function _createRegistrationContainer() {
        $('#' + _rootDivId)
            .append($('<div>')
                .attr('id', registrationFormId)
                .attr('class', 'registration-form box')
                .text('registration-form '));
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