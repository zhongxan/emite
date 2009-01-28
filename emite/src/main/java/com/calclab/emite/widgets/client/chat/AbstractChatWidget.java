package com.calclab.emite.widgets.client.chat;

import com.calclab.emite.im.client.chat.Conversation;
import com.calclab.emite.widgets.client.base.DockableWidget;
import com.calclab.emite.widgets.client.base.EmiteWidget;
import com.calclab.suco.client.events.Listener;

public interface AbstractChatWidget extends EmiteWidget, DockableWidget {

    public AbstractChatController getController();

    public void setChat(Conversation conversation);

    public void setController(AbstractChatController chatController);

    void onSendMessage(Listener<String> listener);

    void setInputEnabled(boolean enabled);

    void write(String name, String body);

}