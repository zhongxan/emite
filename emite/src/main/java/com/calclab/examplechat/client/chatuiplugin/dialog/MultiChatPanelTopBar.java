package com.calclab.examplechat.client.chatuiplugin.dialog;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.im.roster.Roster.SubscriptionMode;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.ColorPalette;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarMenuButton;
import com.gwtext.client.widgets.MessageBox.PromptCallback;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.CheckItem;
import com.gwtext.client.widgets.menu.ColorMenu;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtext.client.widgets.menu.TextItem;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtext.client.widgets.menu.event.CheckItemListenerAdapter;
import com.gwtext.client.widgets.menu.event.ColorMenuListener;

public class MultiChatPanelTopBar extends Toolbar {
    private final Menu statusMenu;
    private CheckItem onlineMenuItem;
    private CheckItem offlineMenuItem;
    private CheckItem busyMenuItem;
    private CheckItem awayMenuItem;
    private final ToolbarMenuButton statusButton;
    private final ToolbarButton inviteUserToGroupChat;
    private final I18nTranslationService i18n;
    private final MultiChatPresenter presenter;
    private final Item closeAllOption;
    private final ToolbarButton loading;
    private final ToolbarButton addRosterItem;
    private CheckItem manualSubsItem;
    private CheckItem autoRejectSubsItem;
    private CheckItem autoAcceptSubsItem;

    public MultiChatPanelTopBar(final I18nTranslationService i18n, final MultiChatPresenter presenter) {
        this.i18n = i18n;
        this.presenter = presenter;

        Menu chatMenu = new Menu();
        chatMenu.setShadow(true);
        Item joinOption = new Item();
        joinOption.setText(i18n.t("Join a chat room"));
        joinOption.addListener(new BaseItemListenerAdapter() {
            public void onClick(final BaseItem item, final EventObject e) {
                MessageBox.alert("In development");
            }
        });
        closeAllOption = createCloseAllMenuItem(i18n);

        MenuItem optionsItem = new MenuItem(i18n.t("Options"), createOptionsMenu());
        chatMenu.addItem(joinOption);
        chatMenu.addItem(closeAllOption);
        chatMenu.addItem(optionsItem);

        ToolbarMenuButton chatMenuButton = new ToolbarMenuButton(i18n.t("Chat"));
        chatMenuButton.setMenu(chatMenu);
        this.addButton(chatMenuButton);
        this.addSeparator();

        statusMenu = createStatusMenu();
        statusButton = new ToolbarMenuButton("Set status", statusMenu);
        statusButton.setTooltip(i18n.t("Set status"));
        this.addButton(statusButton);
        statusButton.addListener(new ButtonListenerAdapter() {
            public void onClick(final Button button, final EventObject e) {
                statusMenu.show("chat-menu-button");
            }
        });

        loading = new ToolbarButton();
        loading.setIcon("images/ext-load.gif");
        loading.setCls("x-btn-icon");
        this.addButton(loading);
        setLoadingVisible(false);

        this.addFill();

        inviteUserToGroupChat = new ToolbarButton();
        inviteUserToGroupChat.setIcon("images/group_add.png");
        inviteUserToGroupChat.setCls("x-btn-icon");
        inviteUserToGroupChat.setTooltip(i18n.t("Invite another user to this chat room"));

        addRosterItem = new ToolbarButton();
        addRosterItem.setIcon("images/user_add.png");
        addRosterItem.setCls("x-btn-icon");
        addRosterItem.setTooltip(i18n.t("Add a new buddy"));

        // final EntityLiveSearchListener inviteUserToRoomListener = new
        // EntityLiveSearchListener() {
        // public void onSelection(String shortName, String longName) {
        // presenter.inviteUserToRoom(shortName, longName);
        // }
        // };

        // final EntityLiveSearchListener addBuddyListener = new
        // EntityLiveSearchListener() {
        // public void onSelection(String shortName, String longName) {
        // presenter.addBuddy(shortName, longName);
        // }
        // };

        addRosterItem.addListener(new ButtonListenerAdapter() {
            private RosterItemDialog rosterItemDialog;

            public void onClick(final Button button, final EventObject e) {
                if (rosterItemDialog == null) {
                    rosterItemDialog = new RosterItemDialog(i18n, presenter);
                }
                rosterItemDialog.show();
                // DefaultDispatcher.getInstance().fire(PlatformEvents.ADD_USERLIVESEARCH,
                // addBuddyListener, null);
            }
        });

        inviteUserToGroupChat.addListener(new ButtonListenerAdapter() {
            public void onClick(final Button button, final EventObject e) {
                MessageBox.alert("In development");
                // DefaultDispatcher.getInstance().fire(PlatformEvents.ADD_USERLIVESEARCH,
                // inviteUserToRoomListener, null);
            }
        });

        this.addButton(addRosterItem);

        this.addButton(inviteUserToGroupChat);

        setSubscritionMode(presenter.getUserChatOptions().getSubscriptionMode());
    }

    private Menu createOptionsMenu() {
        Menu submenu = new Menu();

        ColorMenu colorMenu = new ColorMenu();
        colorMenu.addListener(new ColorMenuListener() {
            public void onSelect(final ColorPalette colorPalette, final String color) {
                presenter.onUserColorChanged(color);
            }
        });

        MenuItem colorMenuItem = new MenuItem(i18n.t("Choose your color"), colorMenu);
        colorMenuItem.setIconCls("colors-icon");
        colorMenuItem.setIcon("css/img/colors.gif");

        MenuItem subsItem = new MenuItem(i18n.t("Subscription options"), createUserSubscriptionMenu());

        submenu.addItem(colorMenuItem);
        submenu.addItem(subsItem);

        return submenu;
    }

    private Menu createUserSubscriptionMenu() {
        Menu submenu = new Menu();
        submenu.setShadow(true);
        submenu.setMinWidth(10);
        submenu.addItem(new TextItem("<b class=\"menu-title\">" + i18n.t("Subscription options") + "</b>"));
        autoAcceptSubsItem = createSubscritionItem(i18n.t("Automatically accept other users requests"), submenu,
                SubscriptionMode.auto_accept_all);
        autoRejectSubsItem = createSubscritionItem(i18n.t("Automatically reject other users requests"), submenu,
                SubscriptionMode.auto_reject_all);
        manualSubsItem = createSubscritionItem(i18n.t("Manual accept/reject users requests"), submenu,
                SubscriptionMode.manual);
        return submenu;
    }

    private CheckItem createSubscritionItem(final String text, final Menu submenu, final SubscriptionMode mode) {
        final CheckItemListenerAdapter listener = new CheckItemListenerAdapter() {
            public void onCheckChange(CheckItem item, boolean checked) {
                if (checked) {
                    presenter.onUserSubscriptionModeChanged(mode);
                }
            }
        };
        CheckItem item = new CheckItem();
        item.setText(text);
        item.setGroup("subscription");
        item.addListener(listener);
        submenu.addItem(item);
        return item;
    }

    private Menu createStatusMenu() {
        Menu statusMenu = new Menu();
        statusMenu.setShadow(true);
        statusMenu.addItem(new TextItem("<b class=\"menu-title\">" + i18n.t("Change your status") + "</b>"));
        onlineMenuItem = createStatusCheckItem(MultiChatView.STATUS_ONLINE);
        offlineMenuItem = createStatusCheckItem(MultiChatView.STATUS_OFFLINE);
        busyMenuItem = createStatusCheckItem(MultiChatView.STATUS_BUSY);
        awayMenuItem = createStatusCheckItem(MultiChatView.STATUS_AWAY);
        Item setStatusText = new Item();
        final String setStatusTextTitle = i18n.t("Set your status message");
        setStatusText.setText(setStatusTextTitle);
        setStatusText.addListener(new BaseItemListenerAdapter() {
            public void onClick(final BaseItem item, final EventObject e) {
                MessageBox.prompt(setStatusTextTitle, i18n
                        .t("Set your current status text (something like 'Out for dinner' or 'Working)"),
                        new PromptCallback() {
                            public void execute(final String btnID, final String text) {
                                presenter.setPresenceStatusText(text);
                            }
                        });
            }
        });
        statusMenu.addItem(onlineMenuItem);
        statusMenu.addItem(offlineMenuItem);
        statusMenu.addItem(busyMenuItem);
        statusMenu.addItem(awayMenuItem);
        statusMenu.addItem(setStatusText);
        return statusMenu;
    }

    private Item createCloseAllMenuItem(final I18nTranslationService i18n) {
        Item closeAllOption = new Item();
        closeAllOption.setText(i18n.t("Close all chats"));
        closeAllOption.setIconCls("exit-icon");
        closeAllOption.addListener(new BaseItemListenerAdapter() {
            public void onClick(final BaseItem item, final EventObject e) {
                confirmCloseAll();
            }
        });
        return closeAllOption;
    }

    // private Item createOptionsMenu() {
    // Item optionsItem = new Item();
    // optionsItem.setText(i18n.t("Options"));
    // // optionsItem.setIconCls("exit-icon");
    // optionsItem.addListener(new BaseItemListenerAdapter() {
    // private MultiChatOptionsDialog optionsDialog;
    //
    // public void onClick(final BaseItem item, final EventObject e) {
    // if (optionsDialog == null) {
    // optionsDialog = new MultiChatOptionsDialog(i18n, presenter);
    // }
    // optionsDialog.setChatOptions(presenter.getUserChatOptions());
    // optionsDialog.show();
    // }
    // });
    // return optionsItem;
    // }

    private CheckItem createStatusCheckItem(final int status) {
        CheckItem checkItem = new CheckItem();
        checkItem.setText(StatusUtil.getStatusIconAndText(i18n, status));
        checkItem.setGroup("chatstatus");

        checkItem.addListener(new BaseItemListenerAdapter() {
            public void onClick(final BaseItem item, final EventObject e) {
                presenter.onStatusSelected(status);
            }
        });
        return checkItem;
    }

    public void setCloseAllOptionEnabled(boolean enabled) {
        closeAllOption.setDisabled(!enabled);
    }

    public void setInviteToGroupChatButtonVisible(final boolean enable) {
        inviteUserToGroupChat.setVisible(enable);
    }

    public void setAddRosterItemButtonVisible(final boolean visible) {
        addRosterItem.setVisible(visible);
    }

    public void setLoadingVisible(final boolean visible) {
        loading.setVisible(visible);
    }

    public void confirmCloseAll() {
        MessageBox.confirm(i18n.t("Confirm"), i18n.t("Are you sure you want to exit all the chats?"),
                new MessageBox.ConfirmCallback() {
                    public void execute(final String btnID) {
                        if (btnID.equals("yes")) {
                            presenter.onCloseAllConfirmed();
                        } else {
                            presenter.onCloseAllNotConfirmed();
                        }
                    }
                });
    }

    public void setStatus(final int status) {
        switch (status) {
        case MultiChatView.STATUS_ONLINE:
            onlineMenuItem.setChecked(true);
            break;
        case MultiChatView.STATUS_OFFLINE:
            offlineMenuItem.setChecked(true);
            break;
        case MultiChatView.STATUS_BUSY:
            busyMenuItem.setChecked(true);
            break;
        case MultiChatView.STATUS_AWAY:
            awayMenuItem.setChecked(true);
            break;
        default:
            break;
        }
        String icon = StatusUtil.getStatusIcon(status).getHTML();
        statusButton.setText(icon);
    }

    public void setSubscritionMode(final SubscriptionMode mode) {
        switch (mode) {
        case auto_accept_all:

            onlineMenuItem.setChecked(true);
            break;
        case auto_reject_all:
            offlineMenuItem.setChecked(true);
            break;
        default:
            break;
        }
    }
}
