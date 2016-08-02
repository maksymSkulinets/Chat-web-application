define(function (require) {

    var EventBus = require('./eventBus');
    var eventBus = new EventBus();

    var Events = require('./Events');

    var Storage = require('./storage');
    var storage = new Storage();

    var ChatApp = require('./chatApp');
    var chatApp = new ChatApp("chat-appId", eventBus, Events);
    chatApp.init();

    var UserService = require('./userService');
    var userService = new UserService(eventBus, Events, storage);
    userService.init();

    var ChatsService = require('./chatsService');
    var chatsService = new ChatsService(eventBus, Events, storage);
    chatsService.init();
});