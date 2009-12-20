package com.calclab.hablar.client.chat;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.suco.client.events.Listener;

public class ChatLogic {
    private final Chat chat;
    private final ChatWidget widget;

    public ChatLogic(Chat chat, final ChatWidget widget) {
	this.chat = chat;
	this.widget = widget;

	final String name = chat.getURI().getNode();
	widget.setHeaderTitle("Chat with " + name);
	chat.onMessageReceived(new Listener<Message>() {
	    @Override
	    public void onEvent(Message message) {
		widget.showMessage(name, message.getBody(), ChatWidget.MessageType.incoming);
	    }
	});
    }

    public void onTalk(String text) {
	widget.showMessage("me", text, ChatWidget.MessageType.sent);
	chat.send(new Message(text));
	widget.clearAndFocus();
    }

}
