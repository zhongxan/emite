package com.calclab.hablar.roster.client.groups;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class RosterGroupWidget extends FlowPanel implements RosterGroupDisplay {

    @Override
    public void add(final RosterItemDisplay itemDisplay) {
	add(itemDisplay.asWidget());
    }

    @Override
    public Widget asWidget() {
	return this;
    }

    @Override
    public RosterItemDisplay newRosterItemDisplay() {
	return new RosterItemWidget();
    }

    @Override
    public void remove(final RosterItemDisplay itemDisplay) {
	remove(itemDisplay.asWidget());
    }

}