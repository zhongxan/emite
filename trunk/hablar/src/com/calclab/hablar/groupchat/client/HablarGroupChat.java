package com.calclab.hablar.groupchat.client;

import com.calclab.hablar.chat.client.ui.ChatPage;
import com.calclab.hablar.core.client.Hablar;
import com.calclab.hablar.core.client.container.PageAddedEvent;
import com.calclab.hablar.core.client.container.PageAddedHandler;
import com.calclab.hablar.core.client.container.overlay.OverlayContainer;
import com.calclab.hablar.core.client.page.PagePresenter.Visibility;
import com.calclab.hablar.core.client.ui.icon.HablarIcons;
import com.calclab.hablar.core.client.ui.icon.HablarIcons.IconType;
import com.calclab.hablar.core.client.ui.menu.SimpleAction;
import com.calclab.hablar.rooms.client.HablarRoomsConfig;
import com.calclab.hablar.rooms.client.ui.open.OpenRoomWidget;
import com.calclab.hablar.roster.client.page.RosterPage;
import com.calclab.hablar.roster.client.ui.groups.RosterGroupPresenter;
import com.google.gwt.core.client.EntryPoint;

public class HablarGroupChat implements EntryPoint {
    private static final String ACTION_ID_CONVERT = "hablarGroupChat-convertToGroup";
    private static final String ACTION_ID_OPEN = "hablarGroupChat-openGroupChatAction";

    public static void install(final Hablar hablar, final HablarRoomsConfig config) {
	final OpenGroupChatPresenter openGroupPage = new OpenGroupChatPresenter(config.roomsService, hablar
		.getEventBus(), new OpenRoomWidget());
	hablar.addPage(openGroupPage, OverlayContainer.ROL);
	final ConvertToGroupChatPresenter convertToGroupPage = new ConvertToGroupChatPresenter(config.roomsService,
		hablar.getEventBus(), new OpenRoomWidget());
	hablar.addPage(convertToGroupPage, OverlayContainer.ROL);

	hablar.addPageAddedHandler(new PageAddedHandler() {
	    @Override
	    public void onPageAdded(final PageAddedEvent event) {
		if (event.isType(ChatPage.TYPE)) {
		    final ChatPage chatPage = (ChatPage) event.getPage();
		    chatPage.addAction(createConvertToGroupChatAction(convertToGroupPage));
		} else if (event.isType(RosterPage.TYPE)) {
		    final RosterPage roster = (RosterPage) event.getPage();
		    roster.getGroupMenu().addAction(openGroupChatAction(openGroupPage));
		}
	    }
	}, true);
    }

    private static SimpleAction<ChatPage> createConvertToGroupChatAction(
	    final ConvertToGroupChatPresenter convertToGroupPage) {
	return new SimpleAction<ChatPage>("Convert to group", ACTION_ID_CONVERT, HablarIcons.get(IconType.buddyAdd)) {
	    @Override
	    public void execute(final ChatPage target) {
		convertToGroupPage.requestVisibility(Visibility.focused);
	    }
	};
    }

    private static SimpleAction<RosterGroupPresenter> openGroupChatAction(final OpenGroupChatPresenter openGroupPage) {
	return new SimpleAction<RosterGroupPresenter>("Open group chat", ACTION_ID_OPEN, HablarIcons.get(IconType.off)) {
	    @Override
	    public void execute(final RosterGroupPresenter target) {
		openGroupPage.setGroupName(target.getGroupName());
		openGroupPage.requestVisibility(Visibility.focused);
	    }
	};
    }

    @Override
    public void onModuleLoad() {
    }

}