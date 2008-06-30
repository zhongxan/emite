/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emiteuimodule.client.roster;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.core.packet.TextUtils;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.im.roster.RosterItem.Subscription;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;
import com.calclab.emiteuimodule.client.params.AvatarProvider;
import com.calclab.emiteuimodule.client.users.ChatUserUI;
import com.calclab.emiteuimodule.client.users.UserGridMenuItem;
import com.calclab.emiteuimodule.client.users.UserGridMenuItemList;
import com.calclab.emiteuimodule.client.users.UserGridMenuItem.UserGridMenuItemListener;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;

public class RosterUIPresenter {

    public static interface RosterPresenceListener {

	void onCancelSubscriptor(XmppURI userURI);

	void onOpenChat(XmppURI userURI);

	void onRequestRemoveItem(XmppURI userURI);

	void onRequestSubscribe(XmppURI userURI);

	void onRequestUnsubscribe(XmppURI userURI);

    }

    private RosterUIView view;
    private final HashMap<XmppURI, ChatUserUI> rosterMap;
    private final I18nTranslationService i18n;
    private final PresenceManager presenceManager;
    private final Roster roster;
    private final RosterManager rosterManager;
    private final AvatarProvider avatarProvider;
    private boolean showUnavailableItems;
    private RosterPresenceListener listener;
    private final Signal<XmppURI> onOpenChat;
    private final Signal<String> onUserAlert;

    public RosterUIPresenter(final Xmpp xmpp, final I18nTranslationService i18n, final AvatarProvider avatarProvider) {
	this.i18n = i18n;
	this.avatarProvider = avatarProvider;
	rosterMap = new HashMap<XmppURI, ChatUserUI>();
	presenceManager = xmpp.getPresenceManager();
	rosterManager = xmpp.getRosterManager();
	roster = xmpp.getRoster();
	showUnavailableItems = false;
	this.onOpenChat = new Signal<XmppURI>("onOpenChat");
	this.onUserAlert = new Signal<String>("onUserAlert");
	// FIXME: User signals...
	listener = new RosterPresenceListener() {
	    public void onCancelSubscriptor(final XmppURI userURI) {
		rosterManager.cancelSubscriptor(userURI);
	    }

	    public void onOpenChat(final XmppURI userURI) {
		onOpenChat.fire(userURI);
	    }

	    public void onRequestRemoveItem(final XmppURI userURI) {
		rosterManager.requestRemoveItem(userURI);
	    }

	    public void onRequestSubscribe(final XmppURI userURI) {
		rosterManager.requestSubscribe(userURI);
	    }

	    public void onRequestUnsubscribe(final XmppURI userURI) {
		rosterManager.requestUnsubscribe(userURI);
	    }
	};
    }

    public void clearRoster() {
	rosterMap.clear();
	view.clearRoster();
    }

    public String getShowText(final Type type, final Show show) {
	String textLabel = "";
	switch (show) {
	case chat:
	    textLabel = i18n.t("Available to Chat");
	    break;
	case away:
	case xa:
	    textLabel = i18n.t("Away");
	    break;
	case dnd:
	    textLabel = i18n.t("Don't disturb");
	    break;
	case notSpecified:
	    if (type.equals(Type.available)) {
		textLabel = i18n.t("Online");
	    } else if (type.equals(Type.unavailable)) {
		textLabel = i18n.t("Offline");
	    }
	}
	return textLabel;
    }

    public ChatUserUI getUserByJid(final XmppURI jid) {
	return rosterMap.get(jid);
    }

    public RosterUIView getView() {
	return view;
    }

    public void init(final RosterUIView view) {
	this.view = view;
	createXmppListeners();
    }

    public void onOpenChat(final Slot<XmppURI> slot) {
	onOpenChat.add(slot);
    }

    public void onPresenceAccepted(final Presence presence) {
	rosterManager.acceptSubscription(presence);
    }

    public void onPresenceNotAccepted(final Presence presence) {
	rosterManager.denySubscription(presence);
    }

    public void onUserAlert(final Slot<String> slot) {
	onUserAlert.add(slot);
    }

    public void openChat(final XmppURI userURI) {
	listener.onOpenChat(userURI);
    }

    public void setListener(final RosterPresenceListener listener) {
	this.listener = listener;
    }

    public void showUnavailableRosterItems(final boolean show) {
	showUnavailableItems = show;
	for (final Iterator<XmppURI> iterator = rosterMap.keySet().iterator(); iterator.hasNext();) {
	    final XmppURI jid = iterator.next();
	    final RosterItem item = roster.findItemByJID(jid);
	    final ChatUserUI user = rosterMap.get(jid);
	    if (item == null) {
		Log.error("Trying to update a ui roster item not in roster");
	    } else {
		refreshRosterItemInView(item, user, show);
	    }
	}
    }

    String formatRosterItemStatusText(final Presence presence, final Subscription subscription) {
	String statusText = "";
	if (presence != null) {
	    statusText = presence.getStatus();
	}
	if (statusText == null || statusText.equals("null")) {
	    // FIXME: Dani we are receiving "null" as String
	    statusText = "";
	}
	if (presence != null) {
	    final Show show = presence.getShow();
	    if (statusText.equals("")) {
		statusText += getShowText(presence.getType(), show);
	    }
	}
	if (subscription != null) {
	    switch (subscription) {
	    case none:
		statusText += " " + i18n.t("(you cannot see your buddy status, your buddy neither)");
		break;
	    case to:
		statusText += " " + i18n.t("(your buddy cannot see your status)");
		break;
	    case from:
		statusText += " " + i18n.t("(you cannot see your buddy status)");
		break;
	    case both:
		break;
	    }
	}
	return statusText.equals("") ? " " : TextUtils.escape(statusText);
    }

    ChatIconDescriptor getPresenceIcon(final Presence presence) {
	switch (presence.getType()) {
	case available:
	    switch (presence.getShow()) {
	    case chat:
	    case away:
	    case dnd:
	    case xa:
		return ChatIconDescriptor.valueOf(presence.getShow().toString());
	    case notSpecified:
	    case unknown:
		return ChatIconDescriptor.available;
	    }
	case unavailable:
	    switch (presence.getShow()) {
	    case away:
	    case notSpecified:
		return ChatIconDescriptor.offline;
	    case unknown:
		return ChatIconDescriptor.unknown;
	    }
	}
	return ChatIconDescriptor.unknown;
    }

    void refreshRosterItemInView(final RosterItem item, final ChatUserUI user, final boolean showUnavailable) {
	final boolean mustShow = isAvailable(item) || showUnavailable;
	if (mustShow) {
	    if (user.getVisible()) {
		view.updateRosterItem(user, createMenuItemList(item));
	    } else {
		view.addRosterItem(user, createMenuItemList(item));
	    }
	} else {
	    if (user.getVisible()) {
		view.removeRosterItem(user);
	    }
	}
	user.setVisible(mustShow);
    }

    private UserGridMenuItem<XmppURI> createCancelSubscriptorBuddyMenuItem(final XmppURI userURI) {
	return new UserGridMenuItem<XmppURI>("del-icon",
		i18n.t("Stop to show when I'm connected or not to this buddy"), new UserGridMenuItemListener() {
		    public void onAction() {
			listener.onCancelSubscriptor(userURI);
		    }
		});
    }

    private UserGridMenuItemList createMenuItemList(final RosterItem item) {
	return createMenuItemList(item.getJID(), item.getPresence(), item.getSubscription());
    }

    private UserGridMenuItemList createMenuItemList(final XmppURI userURI, final Presence presence,
	    final Subscription subscription) {
	final UserGridMenuItemList itemList = new UserGridMenuItemList();
	itemList.addItem(createStartChatMenuItem(userURI));
	switch (subscription) {
	case to:
	    itemList.addItem(createUnsubscribeBuddyMenuItem(userURI));
	    break;
	case both:
	    itemList.addItem(createUnsubscribeBuddyMenuItem(userURI));
	    itemList.addItem(createCancelSubscriptorBuddyMenuItem(userURI));
	    break;
	case from:
	    itemList.addItem(createSubscribeBuddyMenuItem(userURI));
	    itemList.addItem(createCancelSubscriptorBuddyMenuItem(userURI));
	    break;
	case none:
	    itemList.addItem(createSubscribeBuddyMenuItem(userURI));
	    break;
	}
	itemList.addItem(createRemoveBuddyMenuItem(userURI));
	return itemList;
    }

    private UserGridMenuItem<XmppURI> createRemoveBuddyMenuItem(final XmppURI userURI) {
	return new UserGridMenuItem<XmppURI>("cancel-icon", i18n.t("Remove this buddy"),
		new UserGridMenuItemListener() {
		    public void onAction() {
			listener.onRequestRemoveItem(userURI);
		    }
		});
    }

    private UserGridMenuItem<XmppURI> createStartChatMenuItem(final XmppURI userURI) {
	return new UserGridMenuItem<XmppURI>("newchat-icon", i18n.t("Start a chat with this buddy"),
		new UserGridMenuItemListener() {
		    public void onAction() {
			listener.onOpenChat(userURI);
		    }
		});
    }

    private UserGridMenuItem<XmppURI> createSubscribeBuddyMenuItem(final XmppURI userURI) {
	return new UserGridMenuItem<XmppURI>("add-icon", i18n.t("Request to see when this buddy is connected or not"),
		new UserGridMenuItemListener() {
		    public void onAction() {
			listener.onRequestSubscribe(userURI);
		    }
		});
    }

    private UserGridMenuItem<XmppURI> createUnsubscribeBuddyMenuItem(final XmppURI userURI) {
	return new UserGridMenuItem<XmppURI>("del-icon", i18n.t("Stop to see when this buddy is connected or not"),
		new UserGridMenuItemListener() {
		    public void onAction() {
			listener.onRequestUnsubscribe(userURI);
		    }
		});
    }

    private void createXmppListeners() {
	roster.onItemChanged(new Slot<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		final ChatUserUI user = rosterMap.get(item.getJID());
		if (user == null) {
		    Log.error("Trying to update a user is not in roster: " + item.getJID() + " ----> Roster: "
			    + rosterMap);
		} else {
		    logRosterItem("Updating", item);
		    updateUserWithRosterItem(user, item);
		    refreshRosterItemInView(item, user, showUnavailableItems);
		}
	    }

	});

	roster.onRosterChanged(new Slot<Collection<RosterItem>>() {
	    public void onEvent(final Collection<RosterItem> roster) {
		rosterMap.clear();
		view.clearRoster();
		for (final RosterItem item : roster) {
		    logRosterItem("Adding", item);
		    final ChatUserUI user = new ChatUserUI(avatarProvider.getAvatarURL(item.getJID()), item, "black");
		    updateUserWithRosterItem(user, item);
		    if (showUnavailableItems || isAvailable(item)) {
			user.setVisible(true);
			view.addRosterItem(user, createMenuItemList(item));
		    } else {
			user.setVisible(false);
		    }
		    rosterMap.put(user.getURI(), user);
		}
	    }
	});

	rosterManager.onSubscriptionRequested(new Slot<Presence>() {
	    public void onEvent(final Presence presence) {
		switch (rosterManager.getSubscriptionMode()) {
		case autoAcceptAll:
		    Log.info("Accepting because we are auto accepting");
		    break;
		case autoRejectAll:
		    Log.info("Rejecting because we are auto rejecting");
		    break;
		default:
		    Log.info("Manual accept/reject");
		    onUserAlert.fire("");
		    view.confirmSusbscriptionRequest(presence);
		    break;
		}
	    }
	});

	rosterManager.onUnsubscribedReceived(new Slot<XmppURI>() {
	    public void onEvent(final XmppURI userUnsubscribed) {
		Log.info("UNSUBS RECEIVED");
		view.showMessageAboutUnsuscription(userUnsubscribed);
	    }
	});

	presenceManager.onPresenceReceived(new Slot<Presence>() {
	    public void onEvent(final Presence presence) {
		logPresence(presence, "not processed in RosterUIPresenter presence listener but logged");
	    }
	});

    }

    private boolean isAvailable(final RosterItem item) {
	final Presence presence = item.getPresence();
	switch (presence.getType()) {
	case available:
	    switch (presence.getShow()) {
	    case chat:
	    case away:
	    case dnd:
	    case xa:
	    case notSpecified:
		return true;
	    case unknown:
		return false;
	    }
	case unavailable:
	    switch (presence.getShow()) {
	    case away:
	    case notSpecified:
		return false;
	    case unknown:
		return false;
	    }
	}
	return false;
    }

    private void logPresence(final Presence presence, final String subTitle) {
	Log.info("Presence: type: " + presence.getType() + ", from: " + presence.getFromAsString() + ", show: "
		+ presence.getShow().toString() + ", status: " + presence.getStatus() + " (" + subTitle + ")");
    }

    private void logRosterItem(final String operation, final RosterItem item) {
	final String name = item.getName();
	final Presence presence = item.getPresence();
	Log.info(operation + " roster item: " + item.getJID() + ", name: " + name + ", subsc: "
		+ item.getSubscription());
	if (presence != null) {
	    logPresence(presence, "processed after RosterChanged or RosterItemChanged");
	} else {
	    Log.info("with null presence");
	}
    }

    private void updateUserWithRosterItem(final ChatUserUI user, final RosterItem item) {
	user.setStatusIcon(getPresenceIcon(item.getPresence()));
	user.setStatusText(formatRosterItemStatusText(item.getPresence(), item.getSubscription()));
    }

}
