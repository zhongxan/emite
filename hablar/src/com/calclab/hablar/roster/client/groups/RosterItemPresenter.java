package com.calclab.hablar.roster.client.groups;

import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.hablar.core.client.mvp.Presenter;
import com.calclab.hablar.core.client.ui.icon.PresenceIcon;
import com.calclab.hablar.core.client.ui.menu.Menu;
import com.calclab.hablar.roster.client.RosterConfig;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class RosterItemPresenter implements Presenter<RosterItemDisplay> {

    private final RosterItemDisplay display;
    private RosterItem item;
    private final String groupName;
    private String clickActionDescription;

    public RosterItemPresenter(final String groupName, final Menu<RosterItemPresenter> itemMenu,
	    final RosterItemDisplay display, final RosterConfig rosterConfig) {
	this.groupName = groupName;
	this.display = display;

	this.clickActionDescription = "";
	if (rosterConfig.rosterItemClickAction != null) {
	    this.clickActionDescription = rosterConfig.rosterItemClickAction.getDescription() + " ";
	    display.getAction().addClickHandler(new ClickHandler() {
		@Override
		public void onClick(final ClickEvent event) {
		    rosterConfig.rosterItemClickAction.execute(item);
		}
	    });
	    display.addStyleName("clickable");
	}

	display.getMenuAction().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(final ClickEvent event) {
		event.stopPropagation();
		event.preventDefault();
		final Element element = event.getRelativeElement();
		itemMenu.setTarget(RosterItemPresenter.this);
		itemMenu.show(element.getAbsoluteLeft(), element.getAbsoluteTop());
	    }
	});

    }

    @Override
    public RosterItemDisplay getDisplay() {
	return display;
    }

    public String getGroupName() {
	return groupName;
    }

    public RosterItem getItem() {
	return item;
    }

    public void setItem(final RosterItem item) {
	this.item = item;

	String name = item.getName();

	if (name == null) {
	    name = item.getJID().getShortName();
	}

	display.getName().setText(name);
	final String jidString = item.getJID().toString();
	display.getJid().setText(jidString);
	final String status = item.getStatus();
	final boolean hasStatus = (status != null) && (status.trim().length() > 0);
	if (hasStatus) {
	    display.getStatus().setText(status);
	}
	display.setStatusVisible(hasStatus);
	display.setIcon(PresenceIcon.get(item.isAvailable(), item.getShow()));
	final String title = clickActionDescription + name + " (" + jidString + ")";
	display.setWidgetTitle(title);
    }

}
