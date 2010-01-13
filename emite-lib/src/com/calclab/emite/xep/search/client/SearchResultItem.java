package com.calclab.emite.xep.search.client;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

/**
 * Item search result
 * 
 * See: http://xmpp.org/extensions/xep-0055.html#schema
 */
public class SearchResultItem {
    public static SearchResultItem parse(final IPacket child) {
        final XmppURI jid = XmppURI.jid(child.getAttribute("jid"));
        if (jid == null) {
            throw new RuntimeException("a item should have a jid");
        }
        final SearchResultItem searchResultItem = new SearchResultItem(jid);
        final IPacket firstChild = child.getFirstChild("first");
        if (firstChild != null) {
            searchResultItem.setFirst(firstChild.getText());
        }
        final IPacket lastChild = child.getFirstChild("last");
        if (lastChild != null) {
            searchResultItem.setLast(lastChild.getText());
        }
        final IPacket nickChild = child.getFirstChild("nick");
        if (nickChild != null) {
            searchResultItem.setNick(nickChild.getText());
        }
        final IPacket emailChild = child.getFirstChild("email");
        if (emailChild != null) {
            searchResultItem.setEmail(emailChild.getText());
        }
        return searchResultItem;
    }
    
    private final XmppURI jid;
    private String first;
    private String last;
    private String nick;
    private String email;

    /**
     * Created a search result items
     * 
     * @param jid
     *            jid of the result (is a required field)
     * 
     */
    public SearchResultItem(final XmppURI jid) {
        this.jid = jid;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst() {
        return first;
    }

    public XmppURI getJid() {
        return jid;
    }

    public String getLast() {
        return last;
    }

    public String getNick() {
        return nick;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setFirst(final String first) {
        this.first = first;
    }

    public void setLast(final String last) {
        this.last = last;
    }

    public void setNick(final String nick) {
        this.nick = nick;
    }
}