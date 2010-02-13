package com.calclab.emite.xep.vcard.client;

import static com.calclab.emite.core.client.packet.MatcherFactory.byName;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.NoPacket;
import com.calclab.emite.core.client.packet.Packet;

/**
 * VCard java representation
 */
public class VCard extends VCardData {

    public static enum Data {
	DESC, FN, JABBERID, NICKNAME, TITLE, URL
    }

    public static enum Name {
	FAMILY, GIVEN, MIDDLE
    }

    public static final String VCARD = "vCard";
    public static final String DATA_XMLS = "vcard-temp";
    public static final String ORG = "ORG";
    public static final String ADR = "ADR";
    public static final String TEL = "TEL";
    public static final String EMAIL = "EMAIL";
    private static final String N = "N";

    private IPacket nameChild;
    private List<VCardTelephone> telephones;
    private List<VCardAddress> addresses;
    private List<VCardEmail> emails;

    public VCard() {
	super(new Packet(VCARD, DATA_XMLS));
    }

    public VCard(final IPacket packet) {
	super(packet);
    }

    public void addAddresses(final VCardAddress address) {
	parseAddresses();
	addresses.add(address);
	addChild(address);
    }

    public void addEmail(final VCardEmail email) {
	parseEmails();
	emails.add(email);
	addChild(email);
    }

    public void addTelephone(final VCardTelephone telefone) {
	parseTelephones();
	telephones.add(telefone);
	addChild(telefone);
    }

    public List<VCardAddress> getAddresses() {
	parseAddresses();
	return addresses;
    }

    public String getDescription() {
	return getValue(Data.DESC);
    }

    public String getDisplayName() {
	return getValue(Data.FN);
    }

    public List<VCardEmail> getEmails() {
	parseEmails();
	return emails;
    }

    public String getFamilyName() {
	return getName(Name.FAMILY);
    }

    public String getGivenName() {
	return getName(Name.GIVEN);
    }

    public String getJabberID() {
	return getValue(Data.JABBERID);
    }

    public String getMiddleName() {
	return getName(Name.MIDDLE);
    }

    public String getNickName() {
	return getValue(Data.NICKNAME);
    }

    public VCardOrganization getOrganization() {
	return new VCardOrganization(getFirstChild(byName(ORG)));
    }

    public List<VCardTelephone> getTelephones() {
	parseTelephones();
	return telephones;
    }

    public String getTitle() {
	return getValue(Data.TITLE);
    }

    public String getURL() {
	return getValue(Data.URL);
    }

    public void getURL(final String text) {
	setValue(Data.URL, text);
    }

    public void removeEmails() {
	for (final VCardEmail email : emails) {
	    removeChild(email);
	}
	emails.clear();
    }

    public void setDescription(final String text) {
	setValue(Data.DESC.toString(), text);
    }

    public void setDisplayName(final String text) {
	setValue(Data.FN, text);
    }

    public void setFamilyName(final String text) {
	setName(Name.FAMILY, text);
    }

    public void setGivenName(final String text) {
	setName(Name.GIVEN, text);
    }

    public void setJabberID(final String text) {
	setValue(Data.JABBERID, text);
    }

    public void setMiddleName(final String text) {
	setName(Name.MIDDLE, text);
    }

    public void setNickName(final String text) {
	setValue(Data.NICKNAME, text);
    }

    public void setOrganization(final VCardOrganization orga) {
	removeChild(getOrganization());
	addChild(orga);
    }

    public void setTitle(final String text) {
	setValue(Data.TITLE, text);
    }

    private IPacket getN() {
	if (this.nameChild == null) {
	    this.nameChild = getFirstChild(byName(N));
	}
	return nameChild;
    }

    private String getName(final Name name) {
	return getN().getFirstChild(byName(name.toString())).getText();
    }

    private String getValue(final Data data) {
	return getValue(data.toString());
    }

    private void parseAddresses() {
	if (addresses == null) {
	    addresses = new ArrayList<VCardAddress>();
	    final List<? extends IPacket> children = getChildren(byName(ADR));
	    for (final IPacket child : children) {
		addresses.add(new VCardAddress(child));
	    }
	}
    }

    private void parseEmails() {
	if (emails == null) {
	    emails = new ArrayList<VCardEmail>();
	    final List<? extends IPacket> children = getChildren(byName(EMAIL));
	    for (final IPacket child : children) {
		emails.add(new VCardEmail(child));
	    }
	}
    }

    private void parseTelephones() {
	if (telephones == null) {
	    telephones = new ArrayList<VCardTelephone>();
	    final List<? extends IPacket> children = getChildren(byName(TEL));
	    for (final IPacket child : children) {
		telephones.add(new VCardTelephone(child));
	    }
	}
    }

    private void setName(final Name subname, final String text) {
	if (getN() == NoPacket.INSTANCE) {
	    nameChild = addChild(N);
	}
	nameChild.setTextToChild(subname.toString(), text);
    }

    private void setValue(final Data data, final String text) {
	setValue(data.toString(), text);
    }
}
