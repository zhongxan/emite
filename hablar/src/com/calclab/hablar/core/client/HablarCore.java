package com.calclab.hablar.core.client;

import com.calclab.hablar.core.client.browser.BrowserFocusHandler;
import com.calclab.hablar.core.client.container.overlay.OverlayContainer;
import com.calclab.hablar.core.client.ui.prompt.ConfirmPage;
import com.calclab.hablar.core.client.ui.prompt.ConfirmWidget;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class HablarCore implements EntryPoint {

    @Override
    public void onModuleLoad() {
	// We will instantiate the BrowserFocusHandler singleton so that it
	// starts tracking focus events as soon as possible.
	BrowserFocusHandler.getInstance();

	CoreMessages messages = GWT.create(CoreMessages.class);
	ConfirmWidget.setMessages(messages);
    }

    public static void install(Hablar hablar) {
	final ConfirmPage confirmPage = new ConfirmPage(hablar.getEventBus(), new ConfirmWidget());
	hablar.addPage(confirmPage, OverlayContainer.ROL);
    }
}
