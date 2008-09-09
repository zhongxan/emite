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
package com.calclab.emite.client.core.packet;

public class Filters {

    public static final PacketFilter ANY = new PacketFilter() {
	public boolean isValid(final IPacket packet) {
	    return true;
	}
    };

    public static PacketFilter byName(final String nodeName) {
	return new PacketFilter() {
	    public boolean isValid(final IPacket packet) {
		return nodeName.equals(packet.getName());
	    }
	};
    }

    public static PacketFilter byNameAndXMLNS(final String nodeName, final String nodeXmls) {
	return new PacketFilter() {
	    public boolean isValid(final IPacket packet) {
		return nodeName.equals(packet.getName()) && packet.hasAttribute("xmlns", nodeXmls);
	    }
	};
    }
}