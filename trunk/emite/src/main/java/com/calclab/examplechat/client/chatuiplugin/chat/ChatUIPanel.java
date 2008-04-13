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
package com.calclab.examplechat.client.chatuiplugin.chat;

import org.ourproject.kune.platf.client.ui.HorizontalLine;

import com.calclab.examplechat.client.chatuiplugin.utils.ChatTextFormatter;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;

public class ChatUIPanel extends Panel implements ChatUIView {

    private final Panel childPanel;

    public ChatUIPanel(final ChatUIPresenter presenter) {
        setClosable(true);
        setAutoScroll(true);
        setBorder(false);
        childPanel = new Panel();
        childPanel.setAutoScroll(false);
        childPanel.setBorder(false);
        childPanel.setPaddings(5);
        add(childPanel);
        addStyleName("emite-ChatPanel-Conversation");
        this.addListener(new PanelListenerAdapter() {
            public void onActivate(final Panel panel) {
                presenter.onActivated();
            }

            public void onDeactivate(final Panel panel) {
                presenter.onDeactivated();
            }
        });
    }

    public void addDelimiter(final String datetime) {
        HorizontalPanel hp = new HorizontalPanel();
        HorizontalLine hr = new HorizontalLine();
        hp.add(new Label(datetime));
        hp.add(hr);
        hp.setWidth("100%");
        hp.setCellWidth(hr, "100%");
        addWidget(hp);
        hp.setStyleName("emite-ChatPanel-HorizDelimiter");
    }

    public void addInfoMessage(final String message) {
        HTML messageHtml = new HTML(message);
        addWidget(messageHtml);
        messageHtml.addStyleName("emite-ChatPanel-EventMessage");
    }

    public void addMessage(final String userAlias, final String color, final String message) {
        // FIXME: Use gwt DOM.create... for this:
        // Element userAliasSpan = DOM.createSpan();
        // DOM.setInnerText(userAliasSpan, userAlias);
        // DOM.setStyleAttribute(userAliasSpan, "color", color);
        String userHtml = "<span style=\"color: " + color + ";\">" + userAlias + "</span>:&nbsp;";
        HTML messageHtml = new HTML(userHtml + ChatTextFormatter.format(message).getHTML());
        addWidget(messageHtml);
    }

    public void setChatTitle(final String name) {
        super.setTitle(name);
    }

    private void addWidget(final Widget widget) {
        childPanel.add(widget);
        if (childPanel.isRendered()) {
            childPanel.doLayout();
        }
        widget.addStyleName("emite-ChatPanel-Message");
        scrollDown();
    }

    private Element getScrollableElement() {
        return DOM.getParent(childPanel.getElement());
    }

    private void scrollDown() {
        DOM.setElementPropertyInt(getScrollableElement(), "scrollTop", childPanel.getOffsetHeight());
    }

}
