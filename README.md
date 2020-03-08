# Exposed PostgreSQL Upsert
![CI Build Pipeline](https://github.com/LukasForst/exposed-upsert/workflows/CI%20Build%20Pipeline/badge.svg) [ ![Download](https://api.bintray.com/packages/lukas-forst/jvm-packages/exposed-upsert/images/download.svg?version=1.0) ](https://bintray.com/lukas-forst/jvm-packages/exposed-upsert/1.0/link)

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

Library is hosted on [Github Packages](https://github.com/LukasForst/exposed-upsert/packages) and on [Bintray](https://bintray.com/beta/#/lukas-forst/jvm-packages/exposed-upsert). To include the libary in your project:
Maven:
```
<dependency>
	<groupId>pw.forst</groupId>
	<artifactId>exposed-upsert</artifactId>
	<version>1.0</version>
</dependency>
```
Gradle:
```
implementation("pw.forst", "exposed-upsert", "1.0")
```
