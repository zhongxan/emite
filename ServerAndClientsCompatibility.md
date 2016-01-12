Emite Xmpp servers compatibility matrix:

| **Xmpp Server** | **Version** | **Compatible?** | **Know Issues** | **Notes** |
|:----------------|:------------|:----------------|:----------------|:----------|
| openfire        | 3.3.2       | yes             | Some server issue affects emite connectivity |           |
| openfire        | 3.4.5       | yes             |                 |           |
| openfire        | 3.4.6       | no              |                 |           |
| openfire        | 3.5.0       | no              | BOSH bug in server |           |
| openfire        | 3.5.1       | yes             | Some [openfire issue](http://www.igniterealtime.org/community/message/169420), [#6](http://code.google.com/p/emite/issues/detail?id=6&colspec=ID%20Type%20Status%20Priority%20Milestone%20Owner%20Summary%20Component) cause problems of connectivity | Under test in our demo |
| openfire        | 3.6.0a      | yes             |                 |           |
| openfire        | 3.6.4       | yes             |                 |           |
| jabberd         | 1.4.3       | no              | Protocol compatibility issue |           |
| ejabberd        | 2.0.0       | yes             | roster problems (not only with emite) | upgrade to 2.0.2 |
| ejabberd        | 2.0.2       | yes             |                 | [some bosh issues](https://support.process-one.net/browse/EJAB-724;jsessionid=7DA6E5F568CA3368182DC577B8EFAFD9?page=com.atlassian.jira.ext.fisheye%3Afisheye-issuepanel) |
| ejabberd        | 2.0.3       | yes             |                 |           |
| tigase          | ?           | ?               | Not tested      |           |
| zimbra          | 5.0.18      | yes             |                 |           |



## Clients ##

How emite talk with other clients:

| **Xmpp Client** | **Compatible?** | **Know Issues** | **Notes**|
|:----------------|:----------------|:----------------|:|
| pidgin          | yes             |                 | |
| gajim           | yes             |                 | |
| psi             | yes             |                 | |
| adium           | yes             |                 | |
| gmail (web)     | yes             |                 | |
| gtalk (client)  | ?               |                 | |
| iChat           | yes             |                 | |