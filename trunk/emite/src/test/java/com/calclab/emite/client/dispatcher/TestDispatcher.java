package com.calclab.emite.client.dispatcher;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calclab.emite.client.TestHelper;
import com.calclab.emite.client.matcher.BasicMatcher;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.Packet;

public class TestDispatcher {

    private boolean handled;

    @Test
    public void simpleTest() {
        handled = false;
        final DispatcherDefault dispatcher = new DispatcherDefault(TestHelper.createLogger());
        final Event event = new Event("simple");
        final BasicMatcher matcher = new BasicMatcher("event", "name", event.getAttribute("name"));
        dispatcher.subscribe(matcher, new Action() {
            public void handle(final Packet stanza) {
                handled = true;
            }
        });
        dispatcher.publish(event);
        assertTrue(handled);
    }
}
