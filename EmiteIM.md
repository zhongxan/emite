EmiteIM module facilitates the creation of conversations between two or more xmpp users. Provides two abstractions (ChatManager and Chat) that simplifies tracking the status of the conversations.

## Example ##

Given this ginjector:
```
/**
 * A custom injector with Core, Im and Browser modules
 */
interface ExampleIMChatGinjector extends CoreGinjector, ImGinjector, BrowserGinjector {

}
```

You can write this code to send and receive messages (where log is a user defined function):
```
ExampleIMChatGinjector ginjector = GWT.create(ExampleIMChatGinjector.class);
XmppSession session = ginjector.getXmppSession();

session.addSessionStateChangedHandler(true, new StateChangedHandler() {
  @Override
  public void onStateChanged(StateChangedEvent event) {
		String state = event.getState();
		log("Current state: " + state);
  }
});

final ChatManager chatManager = ginjector.getChatManager();
input = new TextBox(); // the user's message will be there
input.addChangeHandler(new ChangeHandler() {
  @Override
  public void onChange(ChangeEvent event) {
		String msg = input.getText();
		log("Message sent: " + msg);
		Chat chat = chatManager.open(uri(user));
		chat.send(new Message(msg));
		input.setText("");
  }
});

Chat chat = chatManager.open(uri(user));
chat.addMessageReceivedHandler(new MessageHandler() {
  @Override
  public void onMessage(MessageEvent event) {
		log("Message received: " + event.getMessage().getBody());
  }
});
```

## ChatManager class ##

ChatManager allows to open chats with other user, and know when other user starts a chat with ourselves (adding the proper handlers).
It also track what are the current conversations and allows to close them.

See ChatManager javadoc for more info.

## Chat class ##

Chat is an abstract class that represents a conversation between two (or more if use RoomChat) xmpp users.
It allows to send messages and add handlers to know when messages arrives.

The difference between using Chat and XmppSession directly is that Chat restricts the messages sent/received to a single conversation.

See Chat, PairChat and RoomChat javadocs for more info.
