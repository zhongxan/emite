Currently the following modules are developed:
  * **Core**: Provides the xmpp infrastructure and XmppSession services. Required.
  * **InstantMessaging**: Provides Chat abstractions to track conversations between xmpp users.
  * **Multi-user chats**: Multi-user chat functionallity (RoomChat abstractions)
  * **Browser module**: A convenient way to configure Xmpp using html meta tags (recommended). It also does a proper logout when the html page is closed (and some other handy utilities).
  * **ChatState**: Implements Chat State Notifications (xep-0085)
  * **MucChatState**: Implements Chat State Notifications in multi-user chats
  * **Search**: Implements XEP-0055: Jabber Search
  * **Storage**: Implements XEP-0049: Private XML Storage
  * **!VCard**: Implements XEP-0054: VCard-temp
  * **DataForms**: Some abstractions to implement xmpp data forms.
  * **Disco**: Implements XEP-0030 Service Discovery
  * **MucDisco**: Extends Disco module to query about MUC support in server and clients