package com.calclab.examplechat.client.chatuiplugin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.AbstractXmpp;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.im.chat.ChatManagerDefault;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatListener;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatView;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.params.MultiChatCreationParam;

public class MultiChatPresenterTest {

    private ChatDialogFactory factory;
    private MultiChatPresenter multiChat;
    private PairChatUser otherUser;
    private PairChatPresenter pairChat;
    private PairChatUser sessionUser;
    private Chat chat;
    private ChatListener chatListener;
    private MultiChatView multiChatPanel;
    private String messageBody;
    private AbstractXmpp xmpp;
    private Session session;
    private Roster roster;
    private ChatManagerDefault chatManager;
    private PresenceManager presenceManager;

    @Before
    public void begin() {
        factory = Mockito.mock(ChatDialogFactory.class);

        final MultiChatListener multiChatlistener = Mockito.mock(MultiChatListener.class);
        XmppURI meUri = XmppURI.parse("lutherb@example.com");
        sessionUser = new PairChatUser("", meUri, "lutherb", "red", new Presence());
        XmppURI otherUri = XmppURI.parse("matt@example.com");
        otherUser = new PairChatUser("", otherUri, "matt", "blue", new Presence());

        // Xmpp creation
        xmpp = Mockito.mock(AbstractXmpp.class);
        session = Mockito.mock(Session.class);
        roster = Mockito.mock(Roster.class);
        chatManager = Mockito.mock(ChatManagerDefault.class);
        presenceManager = Mockito.mock(PresenceManager.class);
        Mockito.stub(xmpp.getSession()).toReturn(session);
        Mockito.stub(xmpp.getRoster()).toReturn(roster);
        Mockito.stub(xmpp.getChat()).toReturn(chatManager);
        Mockito.stub(xmpp.getPresenceManager()).toReturn(presenceManager);
        MultiChatCreationParam param = new MultiChatCreationParam(null, sessionUser, "passwdofuser",
                new UserChatOptions("color", Roster.DEF_SUBSCRIPTION_MODE));
        multiChat = new MultiChatPresenter(xmpp, factory, param, multiChatlistener);
        multiChatPanel = Mockito.mock(MultiChatView.class);
        multiChat.init(multiChatPanel);

        // Basic chat creation
        chat = Mockito.mock(Chat.class);
        chatListener = Mockito.mock(ChatListener.class);
        chat.addListener(chatListener);
        pairChat = Mockito.mock(PairChatPresenter.class);
        Mockito.stub(factory.createPairChat(chat, multiChat, sessionUser, otherUser)).toReturn(pairChat);
        Mockito.stub(pairChat.getChat()).toReturn(chat);
        Mockito.stub(pairChat.getChat().getOtherURI()).toReturn(otherUri);
        // TODO multiChat.addRosterItem(otherUser);
        multiChat.createPairChat(chat);
        messageBody = "hello world :)";
    }

    @Test
    public void testOnSendMessage() {
        multiChat.onCurrentUserSend(messageBody);
        Mockito.verify(chat).send(messageBody);
    }

    @Test
    public void testReceiveMessage() {
        sendMessageFromOther();
    }

    private void sendMessageFromOther() {
        final Message message = new Message(otherUser.getUri(), sessionUser.getUri(), messageBody);
        multiChat.messageReceived(chat, message);
        Mockito.verify(pairChat).addMessage(otherUser.getUri(), messageBody);
    }

    @Test
    public void removeAndAddPresenceAndSend() {
        multiChat.removePresenceBuddy(otherUser);
        // TODO: multiChat.addRosterItem(otherUser);
        sendMessageFromOther();
    }

    @Test
    public void removeAndCreateChat2() {
        multiChat.onCurrentUserSend(messageBody);
        multiChat.closePairChat(pairChat);
        multiChat.createPairChat(chat);
        multiChat.onCurrentUserSend(messageBody);
        Mockito.verify(chat, Mockito.times(2)).send(messageBody);
    }

    @Test
    public void removeAndCreateChat() {
        multiChat.onCurrentUserSend(messageBody);
        multiChat.closeAllChats(false);
        multiChat.createPairChat(chat);
        multiChat.onCurrentUserSend(messageBody);
        Mockito.verify(chat, Mockito.times(2)).send(messageBody);
    }

}

// src/test/java/com/calclab/examplechat/client/chatuiplugin/MultiChatPresenterTest.java
