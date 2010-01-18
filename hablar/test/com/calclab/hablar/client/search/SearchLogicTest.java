package com.calclab.hablar.client.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.search.client.SearchResultItem;
import com.calclab.hablar.client.chat.EmiteTester;
import com.calclab.hablar.client.roster.AbstractLogicTest;
import com.calclab.hablar.client.search.SearchView.Level;

public class SearchLogicTest {
    private EmiteTester tester;
    private SearchView view;
    private SearchLogic logic;

    @Before
    public void before() {
	tester = new EmiteTester();
	view = mock(SearchView.class);
	AbstractLogicTest.registerI18n();
	logic = new SearchLogic(view);
    }

    @Test
    public void shouldAddToRoster() {
	final SearchResultItem item = newItem("some");
	final SearchResultView resultView = mock(SearchResultView.class);
	when(resultView.getItem()).thenReturn(item);

	logic.onResultToRoster(resultView);
	assertTrue(tester.roster.hasRequestedToAdd(item.getJid()));
    }

    @Test
    public void shouldHideAddToRosterOnRosterItems() {
	tester.roster.addItem(XmppURI.uri("one@host"), "one");
	final List<SearchResultItem> results = new ArrayList<SearchResultItem>();
	results.add(newItem("one"));
	logic.search("anything");
	tester.searchManager.fireSearchSuccess(results);
	verify(view).addResult(results.get(0));
    }

    @Test
    public void shouldSearchOnNick() {
	logic.search("myText");
	final HashMap<String, String> query = tester.searchManager.getLastQuery();
	assertEquals("myText*", query.get("nick"));
    }

    @Test
    public void shouldShowMessageIfSuccess() {
	logic.search("anything");
	tester.searchManager.fireSearchSuccess(new ArrayList<SearchResultItem>());
	verify(view).showMessage(anyString(), same(Level.success));
    }

    @Test
    public void shouldShowMessageWhenSearching() {
	logic.search("anything");
	verify(view).showMessage(anyString(), same(Level.info));
    }

    @Test
    public void shouldShowSearchResults() {
	logic.search("anything");
	final List<SearchResultItem> results = new ArrayList<SearchResultItem>();
	results.add(newItem("one"));
	results.add(newItem("one"));
	tester.searchManager.fireSearchSuccess(results);
	verify(view).addResult(results.get(0));
	verify(view).addResult(results.get(1));
    }

    private SearchResultItem newItem(final String name) {
	return new SearchResultItem(XmppURI.uri(name + "@host"), name, null, null, null);
    }
}
