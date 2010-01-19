package com.calclab.hablar.basic.client.roster;

import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.hablar.basic.client.ui.icons.HablarIcons;

public interface RosterItemView {

    RosterItem getItem();

    void setIcon(HablarIcons.IconType icon);

    void setItem(RosterItem item);

    void setJID(String string);

    void setNameDebugId(String debugId);

    void setMenuDebugId(String debugId);

    void setName(String name);

    void setSelected(boolean selected);

    void setStatus(String status);

    void setStatusVisible(boolean visible);
}
