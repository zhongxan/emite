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
package com.calclab.emite.im.client;

import com.calclab.emite.core.client.bosh.Bosh3Settings;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.xold_roster.XRoster;
import com.calclab.emite.im.client.xold_roster.XRosterManager;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.Container;

@Deprecated
public class Xmpp {

    /**
     * Create a Xmpp object and install the specified modules before (you need
     * to specify a ServicesModule, like GWTServicesModule first) for example:
     * <code>Xmpp.create(new GWTServicesModule());</code>
     * 
     * @param container
     * @return
     */
    public static Xmpp create() {
	return Suco.get(Xmpp.class);
    }

    private Session session;
    private final boolean isStarted;
    private final Container container;

    protected Xmpp(final Container container) {
	this.container = container;
	this.isStarted = false;
    }

    public ChatManager getChatManager() {
	getSession();
	return container.getInstance(ChatManager.class);
    }

    public PresenceManager getPresenceManager() {
	getSession();
	return container.getInstance(PresenceManager.class);
    }

    public XRoster getRoster() {
	getSession();
	return container.getInstance(XRoster.class);
    }

    public XRosterManager getRosterManager() {
	return container.getInstance(XRosterManager.class);
    }

    public Session getSession() {
	if (this.session == null) {
	    this.session = container.getInstance(Session.class);
	}
	return session;
    }

    public void login(final XmppURI uri, final String password, final Presence.Show show, final String status) {
	start();
	session.login(uri, password);
	getPresenceManager().setOwnPresence(Presence.build(status, show));
    }

    public void logout() {
	session.logout();
    }

    public void setBoshSettings(final Bosh3Settings settings) {
	container.getInstance(Connection.class).setSettings(settings);
    }

    public void start() {
	getSession();
    }

    public void stop() {
	if (isStarted) {
	    logout();
	}
    }

}