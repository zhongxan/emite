# Introduction #

_Hablar_ is the a new Emite user interface builded using GWT widgets and UIBinder GWT2.0 feature. See the HablarUserGuide for instructions on how to use it.

Hablar is build using the MVP pattern and its composed of several modules that can be optional included to create a customized user interface.

Currently is only in svn repository under trunk/hablar

# Screenshots #

[![](http://emite.googlecode.com/svn/wiki/hablar-roster.png)](http://emite.googlecode.com)
[![](http://emite.googlecode.com/svn/wiki/hablar-chat.png)](http://emite.googlecode.com)

# Features #

Hablar is composed of different modules that can be included or not. Each module gives the user interface a unique group of xmpp features. Two different layouts are selectable (accordion and tabs)

A big Hablar.gwt.xml includes all the Hablar modules. Its used to show all the features of hablar (and ready for non-gwt users.

A lot of options can be configured using html meta tags, so Hablar is ready to be embedded in any web page without any notions of GWT or java development.

# Current modules #

The currently implemented Hablar modules include:
  * HablarBasic: the basic UI. It MUST be included.
  * HablarChat: the ability to open one-to-one chats
  * HablarEditBuddy: the ability to edit nickname of the roster items
  * HablarLogin: the ability to login and logout using a user selected jabber id.
  * HablarOpenChat: the ability to open a chat with any jabber id. Needs HablarChat
  * HablarRoster: a roster (contacts) panel. If HablarChat is included, clicking over a roster contact open a chat with that contact.
  * HablarSearch: the ability to search users (using jabber search)
  * HablarSignals: show a signal in the window title with the unattended chats

# Integration with other GWT applications #

A Hablar integration demo is included in the svn repo at trunk/hablar-demo (using UIBinder).

Basically what you need is create a HablarWidget and install the desired modules:
```
  HablarWidget hablar = new HablarWidget(Layout.accordion);
  HablarChat.install(hablar);
  HablarRoster.install(hablar, false);
  HablarSearch.install(hablar);
  HablarSignals.install(hablar);
  HablarEditBuddy.install(hablar);
```

The modules must be also included in your .gwt.xml file.

Hablar widget is UIBinder compatible. Here's an example:
```
<!DOCTYPE ui:UiBinder SYSTEM "http://dl.google.com/gwt/DTD/xhtml.ent">
<ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	xmlns:g="urn:import:com.google.gwt.user.client.ui" xmlns:hablar="urn:import:com.calclab.hablar.basic.client.ui">
  <ui:style/>
  <hablar:HablarWidget layout="accordion"></hablar:HablarWidget>
```

# Configurable parameters via html tags #

The big Hablar.gwt.xml module has a lot of options configurable via html tags:

```
<!-- the proxy url, required -->
  <meta id="emite.httpBase" content="/proxy" /> 
<!-- xmpp the server name, required -->
  <meta id="emite.host" content="localhost" />
<!-- the xmpp search server name -->
  <meta id="emite.searchHost" content="search.localhost" /> 
    
<!-- autologin: if included, will try to login when the page opens and logout when the page is closed -->
    <meta id="emite.session" content="login" /> 
<!-- the username of the autologin feature -->
    <meta id="emite.user" content="test2@localhost" /> 
<!-- the password of the autologin feature -->
    <meta id="emite.password" content="test2" /> 

<!-- show or not the login panel -->
    <meta id="hablar.loginWidget" content="true" />
<!-- the default user name in HablarLogin panel, optional -->
    <meta id="hablar.user" content="test1@localhost" /> 
<!-- the default user password in HablarLogin panel, optional -->
    <meta id="hablar.password" content="test1" /> 
<!-- show or not the roster panel -->
    <meta id="hablar.rosterWidget" content="true" /> 
   
<!-- the hablar layout (accordion|tabs) -->
    <meta id="hablar.layout" content="accordion" /> 
<!-- autoembed: if present, a HablarWidget will be inserted in the given div id -->
    <meta id="hablar.inline" content="vertical_div" /> 
<!-- the size of the HablarWidget in autoembed feature -->
    <meta id="hablar.width" content="100%" /> 
<!-- the height of the HablarWidget in the autoembed feature -->
    <meta id="hablar.height" content="100%" /> 
```

# Integration in HTML pages without any GWT knowledge #

When finished, a zip file with instructions will be in the download section. Currenlty you have to use the gwt compiler with Hablar module in order to have the deliverable.