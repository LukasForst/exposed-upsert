# Exposed PostgreSQL Upsert
![CI Build Pipeline](https://github.com/LukasForst/exposed-upsert/workflows/CI%20Build%20Pipeline/badge.svg) 
![CD publish pipeline](https://github.com/LukasForst/exposed-upsert/workflows/CD%20publish%20pipeline/badge.svg)

Unfortunately, current [Exposed](https://github.com/JetBrains/Exposed) framework does not contain upsert 
(update or insert) functionality out of the box. 
This simple extension library provides implementation for PostgreSQL.
Implementation is based on the comment [Maxr1998](https://github.com/Maxr1998) made
[here](https://github.com/JetBrains/Exposed/issues/167#issuecomment-480199613).

Usage can be seen for example in [Wire](https://github.com/wireapp/) [Poll Bot](https://github.com/wireapp/poll-bot)
[here](https://github.com/wireapp/poll-bot/blob/fc74e2ae15691484714efe2b7803dbc5e235da01/src/main/kotlin/com/wire/bots/polls/dao/PollRepository.kt#L49)

Following code tries to insert new value of `optionId` to `Votes` table where `pollId` and `userId` is composite key.
If the combination `pollId` and `userId` already exist, `optionId` is updated.
```kotlin
Votes.insertOrUpdate(Votes.pollId, Votes.userId) {
    it[pollId] = pollAction.pollId
    it[pollOption] = pollAction.optionId
    it[userId] = pollAction.userId
}
```

Library is hosted on Maven Central.
```xml
<dependency>
	<groupId>pw.forst</groupId>
	<artifactId>exposed-upsert</artifactId>
	<version>1.1.0</version>
</dependency>
```
Gradle:
```kotlin
implementation("pw.forst", "exposed-upsert", "1.1.0")
```
