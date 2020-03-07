package pw.forst.exposed

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.TransactionManager

/**
 * Inserts or updates on conflict with [keys].
 *
 * Usage - pollId and userId is composite primary key, intention is to insert new pollOption or update current one.
```kotlin
Votes.insertOrUpdate(Votes.pollId, Votes.userId) {
it[pollId] = pollAction.pollId
it[pollOption] = pollAction.optionId
it[userId] = pollAction.userId
}
```
 */
fun <T : Table> T.insertOrUpdate(vararg keys: Column<*>, body: T.(InsertStatement<Number>) -> Unit) =
    InsertOrUpdate<Number>(this, keys = *keys).apply {
        body(this)
        execute(TransactionManager.current())
    }
