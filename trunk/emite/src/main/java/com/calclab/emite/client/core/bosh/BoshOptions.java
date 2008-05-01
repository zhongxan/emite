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
package com.calclab.emite.client.core.bosh;

public class BoshOptions {
    private final int hold;
    private final String httpBase;
    private final String version;
    private final int wait;

    public BoshOptions(final String httpBase) {
	this(httpBase, "1.6", 2000, 1);
    }

    public BoshOptions(final String httpBase, final String version, final int wait, final int hold) {
	this.httpBase = httpBase;
	this.version = version;
	this.wait = wait;
	this.hold = hold;
    }

    public int getHold() {
	return hold;
    }

    public String getHttpBase() {
	return httpBase;
    }

    public String getVersion() {
	return version;
    }

    public int getWait() {
	return wait;
    }

}