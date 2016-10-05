define(function (require) {
    var baseUrl = 'http://localhost:8080';

    var EventBus = require('./eventBus');
    var eventBus = new EventBus();

    var Events = require('./Events');

    var Registration = require('./viewComponents/registration');
    var registration = new Registration("chat-appId", eventBus, Events);
    registration.render();

    var Login = require('./viewComponents/login');
    var login = new Login("chat-appId", eventBus, Events);
    login.init();

    var Chat = require('./viewComponents/chat');
    var chat = new Chat("chat-appId", eventBus, Events);
    chat.init();

    var UserService = require('./userService');
    var userService = new UserService(eventBus, Events, baseUrl);
    userService.init();

    var ChatService = require('./chatService');
    var chatService = new ChatService(eventBus, Events, baseUrl);
    chatService.init();
});