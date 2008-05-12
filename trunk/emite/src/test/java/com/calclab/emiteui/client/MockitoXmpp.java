package com.calclab.emiteui.client;

import static org.mockito.Mockito.mock;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.extra.muc.RoomManager;
import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.modular.ModuleContainer;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;

public class MockitoXmpp extends Xmpp {

    private final ChatManager chat;
    private final PresenceManager presenceManager;
    private final Roster roster;
    private final RosterManager rosterManager;
    private final Session session;
    private final RoomManager roomManager;

    public MockitoXmpp() {
	super(new ModuleContainer());
	chat = mock(ChatManager.class);
	presenceManager = mock(PresenceManager.class);
	roster = mock(Roster.class);
	rosterManager = mock(RosterManager.class);
	session = mock(Session.class);
	roomManager = mock(RoomManager.class);
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
    public RoomManager getRoomManager() {
	return roomManager;
    }

    @Override
    public Roster getRoster() {
	return roster;
    }

    @Override
    public RosterManager getRosterManager() {
	return rosterManager;
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