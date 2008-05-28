package com.calclab.emite.client.xep.muc;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static com.calclab.emite.testing.MockitoEmiteHelper.packetLike;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.calclab.emite.client.im.chat.AbstractChatManagerTest;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.im.chat.ChatManagerListener;
import com.calclab.emite.client.xep.muc.Occupant.Affiliation;
import com.calclab.emite.client.xep.muc.Occupant.Role;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;

public class MUCRoomManagerTest extends AbstractChatManagerTest {

    @Test
    public void shouldAcceptRoomPresenceWithAvatar() {

	final Room room = (Room) manager.openChat(uri("room1@domain/nick"), null, null);
	emite.receives("<presence to='user@domain/resource' from='room1@domain/otherUser2'>" + "<priority>0</priority>"
		+ "<x xmlns='http://jabber.org/protocol/muc#user'>"
		+ "<item jid='otheruserjid@domain/otherresoruce' affiliation='none' " + "role='participant'/></x>"
		+ "<x xmlns='vcard-temp:x:update'><photo>af70fe6519d6a27a910c427c3bc551dcd36073e7</photo></x>"
		+ "</presence>");
	assertEquals(1, room.getOccupantsCount());
	final Occupant occupant = room.findOccupant(uri("room1@domain/otherUser2"));
	assertNotNull(occupant);
	assertEquals(Affiliation.none, occupant.getAffiliation());
	assertEquals(Role.participant, occupant.getRole());
    }

    @Deprecated
    @Test
    public void shouldCloseAllActiveRoomsWhenLoggedOut() {
	final ChatManagerListener listener = mock(ChatManagerListener.class);
	manager.addListener(listener);
	final Chat room1 = manager.openChat(uri("room1@domain/nick"), null, null);
	final Chat room2 = manager.openChat(uri("room2@domain/nick"), null, null);
	emite.receives(SessionManager.Events.onLoggedOut);
	verify(listener).onChatClosed(room2);
	verify(listener).onChatClosed(room1);
    }

    @Test
    public void shouldCreateInstantRoomIfNeeded() {
	manager.openChat(uri("newroomtest1@rooms.localhost/nick"), null, null);
	emite.receives("<presence from='newroomtest1@rooms.localhost/nick' to='user@localhost/resource' >"
		+ "<priority>5</priority>" + "<x xmlns='http://jabber.org/protocol/muc#user'>"
		+ "<item affiliation='owner' role='moderator' jid='vjrj@localhost/Psi' />" + "<status code='201' />"
		+ "</x>" + "</presence>");
	emite.verifyIQSent(new IQ(Type.set));
    }

    @Test
    public void shouldFireChatMessages() {
	final Chat chat = manager.openChat(uri("room@rooms.domain/user"), null, null);
	final RoomListener roomListener = mock(RoomListener.class);
	chat.addListener(roomListener);
	final String message = "<message from='room@rooms.domain/other' to='user@domain/resource' "
		+ "type='groupchat'><body>the message body</body></message>";
	emite.receives(message);
	verify(roomListener).onMessageReceived(eq(chat), (Message) packetLike(message));
    }

    @Test
    public void shouldGiveSameRoomsWithSameURIS() {
	final Room room1 = (Room) manager.openChat(uri("room@domain/nick"), null, null);
	final Room room2 = (Room) manager.openChat(uri("room@domain/nick"), null, null);
	assertSame(room1, room2);
    }

    @Test
    public void shouldHandleRoomInvitations() {
	final RoomManagerListener listener = mock(RoomManagerListener.class);
	manager.addListener(listener);
	final String message = "<message to='user@domain/resource' from='room@conference.domain'>"
		+ "<x xmlns='http://jabber.org/protocol/muc#user'><invite from='otherUser@domain/resource'>"
		+ "<reason>The reason here</reason></invite></x></message>";
	emite.receives(message);
	verify(listener).onInvitationReceived(uri("otherUser@domain/resource"), uri("room@conference.domain"),
		"The reason here");
    }

    @Test
    public void shouldUpdateRoomPresence() {
	final Room room = (Room) manager.openChat(uri("room1@domain/nick"), null, null);

	emite.receives("<presence to='user@domain/resource' xmlns='jabber:client' from='room1@domain/otherUser'>"
		+ "<x xmlns='http://jabber.org/protocol/muc#user'>"
		+ "<item role='moderator' affiliation='owner' /></x></presence>");
	assertEquals(1, room.getOccupantsCount());
	Occupant user = room.findOccupant(uri("room1@domain/otherUser"));
	assertNotNull(user);
	assertEquals(Affiliation.owner, user.getAffiliation());
	assertEquals(Role.moderator, user.getRole());

	emite.receives("<presence to='user@domain/resource' xmlns='jabber:client' from='room1@domain/otherUser'>"
		+ "<x xmlns='http://jabber.org/protocol/muc#user'>"
		+ "<item role='participant' affiliation='member' /></x></presence>");
	assertEquals(1, room.getOccupantsCount());
	user = room.findOccupant(uri("room1@domain/otherUser"));
	assertNotNull(user);
	assertEquals(Affiliation.member, user.getAffiliation());
	assertEquals(Role.participant, user.getRole());

	emite
		.receives("<presence to='user@domain/res1' type='unavailable' xmlns='jabber:client' from='room1@domain/otherUser'>"
			+ "<status>custom message</status><x xmlns='http://jabber.org/protocol/muc#user'>"
			+ "<item role='none' affiliation='member' /></x></presence>");
	assertEquals(0, room.getOccupantsCount());

    }

    @Override
    protected ChatManager createChatManager() {
	final MUCRoomManager roomManager = new MUCRoomManager(emite);
	roomManager.setUserURI(MYSELF);
	return roomManager;
    }
}
