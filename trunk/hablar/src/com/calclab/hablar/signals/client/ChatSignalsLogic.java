package com.calclab.hablar.signals.client;

import java.util.HashSet;
import java.util.Set;

import com.calclab.hablar.basic.client.ui.page.PageView;
import com.calclab.hablar.basic.client.ui.page.PageView.Visibility;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

public class ChatSignalsLogic {
    private final Set<PageView> set;
    private final Event<Set<PageView>> chatsUnattendedChanged;
    private final Event<PageView> newUnattendedChat;

    public ChatSignalsLogic() {
	this.newUnattendedChat = new Event<PageView>("newUnattendedChat");
	this.chatsUnattendedChanged = new Event<Set<PageView>>("chatsUnattendedChanged");
	set = new HashSet<PageView>();
    }

    public void addChatsUnattendedChanged(final Listener<Set<PageView>> listener) {
	chatsUnattendedChanged.add(listener);
    }

    public void addNewUnattendedChat(final Listener<PageView> listener) {
	newUnattendedChat.add(listener);
    }

    public void onChatClosed(final PageView chatPage) {
	if (set.remove(chatPage)) {
	    fire();
	}
    }

    public void onChatOpened(final PageView chatPage) {
	if (chatPage.getVisibility() == Visibility.focused && set.remove(chatPage)) {
	    fire();
	}
    }

    public void onNewMsg(final PageView chatPage) {
	if (chatPage.getVisibility() != Visibility.focused && set.add(chatPage)) {
	    fire();
	    // This chat it's new
	    newUnattendedChat.fire(chatPage);
	}
    }

    private void fire() {
	chatsUnattendedChanged.fire(set);
    }

}