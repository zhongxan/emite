package com.calclab.hablar;

import com.calclab.hablar.chat.client.HablarChat;
import com.calclab.hablar.clipboard.client.HablarClipboard;
import com.calclab.hablar.core.client.Hablar;
import com.calclab.hablar.core.client.HablarCore;
import com.calclab.hablar.core.client.HablarWidget;
import com.calclab.hablar.dock.client.DockConfig;
import com.calclab.hablar.dock.client.HablarDock;
import com.calclab.hablar.dock.client.DockConfig.Position;
import com.calclab.hablar.editbuddy.client.HablarEditBuddy;
import com.calclab.hablar.group.client.HablarGroup;
import com.calclab.hablar.groupchat.client.HablarGroupChat;
import com.calclab.hablar.openchat.client.HablarOpenChat;
import com.calclab.hablar.rooms.client.HablarRooms;
import com.calclab.hablar.roster.client.HablarRoster;
import com.calclab.hablar.roster.client.page.RosterPage;
import com.calclab.hablar.search.client.HablarSearch;
import com.calclab.hablar.signals.client.HablarSignals;
import com.calclab.hablar.signals.sound.client.HablarSoundSignals;
import com.calclab.hablar.user.client.HablarUser;
import com.calclab.hablar.user.client.UserPage;
import com.calclab.hablar.usergroups.client.HablarUserGroups;
import com.calclab.hablar.vcard.client.HablarVCard;

public class HablarComplete {

    public static void install(final Hablar hablar, final HablarConfig config) {
	HablarCore.install(hablar);
	HablarChat.install(hablar);
	new HablarRooms(hablar, config.roomsConfig);
	new HablarGroupChat(hablar, config.roomsConfig);

	final DockConfig dock = new DockConfig();
	config.dockUser = true;
	if (config.dockUser) {
	    dock.set(Position.top, UserPage.TYPE, 24);
	}

	if ("left".equals(config.dockRoster)) {
	    dock.set(Position.left, RosterPage.TYPE, 250);
	} else if ("right".equals(config.dockRoster)) {
	    dock.set(Position.right, RosterPage.TYPE, 250);
	}
	HablarDock.install(hablar, dock);

	HablarUser.install(hablar);

	RosterPage roster = null;
	if (config.hasRoster) {
	    roster = HablarRoster.installModule(hablar, config.rosterConfig);
	}

	if (config.hasVCard) {
	    HablarVCard.install(hablar, config.vcardConfig);
	}

	if (config.hasRoster) {
	    new HablarOpenChat(hablar);
	    HablarEditBuddy.install(hablar);
	    HablarUserGroups.install(roster, hablar);
	    new HablarGroup(hablar);
	    HablarRoster.addActions(roster);
	}

	if (config.hasSearch) {
	    HablarSearch.install(hablar, config.searchConfig);
	}

	if (config.hasSignals) {
	    HablarSignals.install(hablar);
	}

	if (config.hasSound) {
	    HablarSoundSignals.install(hablar, config.soundConfig);
	}

	if (config.hasCopyToClipboard) {
	    HablarClipboard.install(hablar);
	}

    }

    public static void install(final HablarWidget widget, final HablarConfig config) {
	final Hablar hablar = widget.getHablar();
	install(hablar, config);

    }

}
