/*
 * Copyright (C) 2007 The kune development team (see CREDITS for details)
 * This file is part of kune.
 *
 * Kune is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kune is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.calclab.examplechat.client.chatuiplugin.dialog;

import org.ourproject.kune.platf.client.extend.UIExtensionElement;

import com.calclab.examplechat.client.chatuiplugin.abstractchat.ChatId;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.params.ChatMessageParam;

public interface MultiChatListener {

    void onCloseGroupChat(GroupChat groupChat);

    void onStatusSelected(final int status);

    void onClosePairChat(PairChatPresenter pairChat);

    void setGroupChatSubject(ChatId groupChatId, String subject);

    void onUserColorChanged(String color);

    void attachToExtPoint(UIExtensionElement extensionElement);

    void doAction(String eventId, Object param);

    void onSendMessage(ChatMessageParam outputMessage);

}
