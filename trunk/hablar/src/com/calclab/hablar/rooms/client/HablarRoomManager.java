package com.calclab.hablar.rooms.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomInvitation;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.hablar.chat.client.ui.ChatMessage;
import com.calclab.hablar.core.client.Hablar;
import com.calclab.hablar.core.client.mvp.HablarEventBus;
import com.calclab.hablar.core.client.page.PagePresenter.Visibility;
import com.calclab.hablar.rooms.client.room.RoomDisplay;
import com.calclab.hablar.rooms.client.room.RoomPresenter;
import com.calclab.hablar.rooms.client.room.RoomWidget;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;

public class HablarRoomManager {

    public static interface RoomPageFactory {
	RoomDisplay create(boolean sendButtonVisible);
    }
    
    public static interface RoomPresenterFactory {
    	RoomPresenter create(HablarEventBus eventBus, Room room, RoomDisplay display);
    }

    private final Hablar hablar;
    private final RoomPageFactory factory;
    private final RoomPresenterFactory presenterFactory;
    private final HashMap<XmppURI, RoomPresenter> roomPages;
    private final ArrayList<RoomInvitation> acceptedInvitations;

    public HablarRoomManager(final Hablar hablar, final HablarRoomsConfig config) {
	this(hablar, config, new RoomPageFactory() {
	    @Override
	    public RoomDisplay create(final boolean sendButtonVisible) {
		return new RoomWidget(sendButtonVisible);
	    }},
	    new RoomPresenterFactory() {

			@Override
			public RoomPresenter create(HablarEventBus eventBus, Room room, RoomDisplay display) {
				return new RoomPresenter(eventBus, room, display);
			}
	    	
	    }
	);
    }

    public HablarRoomManager(final Hablar hablar, final HablarRoomsConfig config,
	    final RoomPageFactory factory, final RoomPresenterFactory presenterFactory) {
	this.hablar = hablar;
	this.factory = factory;
	this.presenterFactory = presenterFactory;
	acceptedInvitations = new ArrayList<RoomInvitation>();
	roomPages = new HashMap<XmppURI, RoomPresenter>();

	final RoomManager rooms = Suco.get(RoomManager.class);

	rooms.onChatCreated(new Listener<Chat>() {
	    @Override
	    public void onEvent(final Chat chat) {
		createRoom((Room) chat);
	    }
	});

	rooms.onChatOpened(new Listener<Chat>() {
	    @Override
	    public void onEvent(final Chat chat) {
		openRoom(chat);
	    }
	});

	rooms.onInvitationReceived(new Listener<RoomInvitation>() {
	    @Override
	    public void onEvent(final RoomInvitation invitation) {
		acceptedInvitations.add(invitation);
		rooms.acceptRoomInvitation(invitation);
	    }
	});
    }

    protected void createRoom(final Room room) {
	final RoomDisplay display = factory.create(true);
	final RoomPresenter presenter = presenterFactory.create(hablar.getEventBus(), room, display);
	roomPages.put(room.getURI(), presenter);
	hablar.addPage(presenter);
    }

    protected RoomInvitation getInvitation(final XmppURI uri) {
	for (final RoomInvitation invitation : acceptedInvitations) {
	    if (invitation.getRoomURI().getNode().equals(uri.getNode())) {
		return invitation;
	    }
	}
	return null;
    }

    protected void openRoom(final Chat room) {
	final RoomPresenter roomPage = roomPages.get(room.getURI());
	assert roomPage != null : "Error in room pages - HablarRoomManager";
	final RoomInvitation invitation = getInvitation(room.getURI());
	if (invitation != null) {
	    acceptedInvitations.remove(invitation);
	    roomPage.requestVisibility(Visibility.notFocused);
	    String message = "You have been invited to this group chat";
	    
	    if(invitation.getInvitor() != null) {
	    	message += " by " + invitation.getInvitor().getNode();
	    }
	    
	    message += invitation.getReason() != null ? ": " + invitation.getReason() : "";
	    roomPage.addMessage(new ChatMessage(message));
	} else {
	    roomPage.requestVisibility(Visibility.focused);
	}
    }
}
