define(function (require) {
    var baseUrl = 'http://localhost:8080';

    var EventBus = require('./eventBus');
    var eventBus = new EventBus();

    var Events = require('./Events');

    var Storage = require('./storage');
    var storage = new Storage();

    var ChatApp = require('./chatApp');
    var chatApp = new ChatApp("chat-appId", eventBus, Events);
    chatApp.init();

    var CloudUserService = require('./cloudUserService');
    var cloudUserService = new CloudUserService(eventBus, Events, baseUrl);
    cloudUserService.init();

    var CloudChatService = require('./cloudChatService');
    var cloudChatService = new CloudChatService(eventBus, Events, baseUrl);
    cloudChatService.init();
});