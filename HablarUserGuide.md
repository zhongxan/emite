# Introduction #

**WARNING! This is a stub**

This guide wants to be a complete guide on installing and using Hablar.

# Configuration #

## Search configuration ##

The search facility of Hablar allows to query the search XMPP server to get information about registered users. The search term is used to search in the user base.

To provide your own implementation:

  * implement SearchQueryFactory;
  * register it in your module initialization. If you are using HablarConfig:

```
final HablarConfig config = HablarConfig.getFromMeta();
...
config.searchConfig.queryFactory = new NicknameContainsOrderedWordsSearchQueryFactory();
```

Currently there are three implementations:

  * **NicknameStartsWithSearchQueryFactory** (DEFAULT): searches users whose nickname **starts with** the desidered search term;
  * **NicknameContainsSearchQueryFactory**: searches users whose nickname **contains** the desidered search term;
  * **NicknameContainsOrderedWordsSearchQueryFactory**: searches users whose nickname **contains the words in the specified order** in the desidered search term.