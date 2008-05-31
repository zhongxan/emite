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
package com.calclab.emite.client.xmpp.sasl;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class AuthorizationTicket {
    public static enum State {
	succeed, failed, notStarted, waitingForAuthorization
    }

    public final XmppURI uri;
    private String password;
    private State state;

    public AuthorizationTicket(final XmppURI uri, final String password) {
	this.uri = uri;
	this.password = password;
	this.state = State.notStarted;
    }

    /**
     * Testing purposes only! Not state logic!
     */
    public AuthorizationTicket(final XmppURI uri, final String password, final State state) {
	this(uri, password);
	this.state = state;
    }

    public String getPassword() {
	return password;
    }

    public State getState() {
	return state;
    }

    public void setState(final State state) {
	this.state = state;
	this.password = null;
    }

}
