/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.core.client.bosh;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.suco.client.events.Listener;

/**
 * A connection to a xmpp server.
 */
public interface Connection {

    public abstract void connect();

    public abstract void disconnect();

    public abstract boolean isConnected();

    public abstract void onError(final Listener<String> listener);

    public abstract void onResponse(final Listener<String> listener);

    public abstract void onStanzaReceived(final Listener<IPacket> listener);

    public abstract void onStanzaSent(final Listener<IPacket> listener);

    /**
     * Pause the connection and return a stream settings object that can be
     * serialized to restore the session
     * 
     * @return StreamSettings object if the connection if a stream is present
     *         (the connection is active), null otherwise
     */
    public abstract StreamSettings pause();

    public abstract void removeOnStanzaReceived(Listener<IPacket> listener);

    public abstract void restartStream();

    public abstract boolean resume(StreamSettings settings);

    public abstract void send(final IPacket packet);

    public abstract void setSettings(BoshSettings settings);

}
