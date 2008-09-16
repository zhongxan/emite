package com.calclab.emiteuimodule.client;

import static org.mockito.Mockito.mock;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.im.client.Xmpp;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.xold_roster.XRoster;
import com.calclab.emite.im.client.xold_roster.XRosterManager;
import com.calclab.suco.client.SucoFactory;

public class MockitoXmpp extends Xmpp {

    private final ChatManager chat;
    private final PresenceManager presenceManager;
    private final XRoster xRoster;
    private final XRosterManager xRosterManager;
    private final Session session;

    public MockitoXmpp() {
	super(SucoFactory.create());
	chat = mock(ChatManager.class);
	presenceManager = mock(PresenceManager.class);
	xRoster = mock(XRoster.class);
	xRosterManager = mock(XRosterManager.class);
	session = mock(Session.class);
    }

    @Override
    public ChatManager getChatManager() {
	return chat;
    }

    @Override
    public PresenceManager getPresenceManager() {
	return presenceManager;
    }

    @Override
    public XRoster getRoster() {
	return xRoster;
    }

    @Override
    public XRosterManager getRosterManager() {
	return xRosterManager;
    }

    @Override
    public Session getSession() {
	return session;
    }

    @Override
    public void login(final XmppURI uri, final String password, final Show show, final String status) {
	throw new RuntimeException("not implemented");
    }

    @Override
    public void logout() {
	throw new RuntimeException("not implemented");
    }
}