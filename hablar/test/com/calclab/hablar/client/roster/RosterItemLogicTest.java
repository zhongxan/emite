package com.calclab.hablar.client.roster;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.hablar.client.ui.styles.HablarStyles.IconType;

public class RosterItemLogicTest {

    private RosterItem item;
    private RosterItemView view;

    @Before
    public void before() {
	view = mock(RosterItemView.class);
	item = new RosterItem(XmppURI.uri("one"), null, "name", null);
	item.setAvailable(true);
	item.setStatus("the status");
	item.setShow(Show.dnd);
    }

    @Test
    public void shouldSetAvailable() {
	item.setAvailable(true);
	item.setShow(Show.notSpecified);
	assertSame(IconType.buddyOn, RosterItemLogic.getIcon(item));
    }

    @Test
    public void shouldSetProperties() {
	RosterItemLogic.setItem(item, view);
	verify(view).setName(item.getName());
	verify(view).setJID(item.getJID().toString());
	verify(view).setStatus(item.getStatus());
	verify(view).setItem(item);
    }
}
