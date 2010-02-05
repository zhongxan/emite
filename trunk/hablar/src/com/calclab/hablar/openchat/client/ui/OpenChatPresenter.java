package com.calclab.hablar.openchat.client.ui;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.hablar.core.client.mvp.HablarEventBus;
import com.calclab.hablar.core.client.page.PagePresenter;
import com.calclab.suco.client.Suco;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class OpenChatPresenter extends PagePresenter<OpenChatDisplay> {
    private static int index = 0;
    public static final String TYPE = "OpenChat";
    private final ChatManager manager;
    private final Roster roster;

    public OpenChatPresenter(HablarEventBus eventBus, final OpenChatDisplay display) {
	super(TYPE, "" + (++index), eventBus, display);

	manager = Suco.get(ChatManager.class);
	roster = Suco.get(Roster.class);

	display.getCancel().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		requestHide();
	    }
	});

	display.getOpen().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		String text = display.getJabberId().getText().trim();
		XmppURI userJid = XmppURI.jid(text);
		if (text.length() > 0) {
		    manager.open(userJid);
		}
		if (display.getAddToRoster().getValue()) {
		    addToRoster(userJid);
		}
		requestHide();
	    }
	});
    }

    protected void addToRoster(XmppURI userJid) {
	roster.requestAddItem(userJid, userJid.getNode());
    }

}
