package com.cakk.domain.mysql.config

import com.p6spy.engine.logging.Category
import com.p6spy.engine.spy.appender.MessageFormattingStrategy
import org.hibernate.engine.jdbc.internal.FormatStyle
import java.text.MessageFormat
import java.util.*
import java.util.function.Predicate

class P6spySqlFormatterConfig : MessageFormattingStrategy {
    override fun formatMessage(
		connectionId: Int,
		now: String,
		elapsed: Long,
		category: String,
		prepared: String,
		sql: String,
		url: String
	): String {
        return sqlFormatToUpper(sql, category, getMessage(connectionId, elapsed, stackBuilder))
    }

    private fun sqlFormatToUpper(sql: String, category: String, message: String): String {
        return if (sql.trim { it <= ' ' }.isEmpty()) {
            ""
        } else StringBuilder()
                .append(sqlFormatToUpper(sql, category))
                .append(message)
                .toString()
    }

    private fun sqlFormatToUpper(sql: String, category: String): String {
        return if (isStatementDdl(sql, category)) {
            FormatStyle.DDL
                    .formatter
                    .format(sql)
                    .uppercase()
                    .replace("+0900", "")
        } else FormatStyle.BASIC
                .formatter
                .format(sql)
                .replace("+0900", "")
    }

    private fun isStatementDdl(sql: String, category: String): Boolean {
        return isStatement(category) && isDdl(sql.trim { it <= ' ' }.lowercase())
    }

    private fun isStatement(category: String): Boolean {
        return Category.STATEMENT.name == category
    }

    private fun isDdl(lowerSql: String): Boolean {
        return lowerSql.startsWith(CREATE) || lowerSql.startsWith(ALTER) || lowerSql.startsWith(COMMENT)
    }

    private fun getMessage(connectionId: Int, elapsed: Long, callStackBuilder: StringBuilder): String {
        return StringBuilder()
                .append(NEW_LINE)
                .append(NEW_LINE)
                .append("\t").append(String.format("Connection ID: %s", connectionId))
                .append(NEW_LINE)
                .append("\t").append(String.format("Execution Time: %s ms", elapsed))
                .append(NEW_LINE)
                .append(NEW_LINE)
                .append("\t").append(String.format("Call Stack (number 1 is entry point): %s", callStackBuilder))
                .append(NEW_LINE)
                .append(NEW_LINE)
                .append("------------------------------------------------------------------------------------------------")
                .toString()
    }

    private val stackBuilder: StringBuilder
        private get() {
            val callStack = Stack<String>()
            Arrays.stream(Throwable().stackTrace)
                    .map { obj: StackTraceElement -> obj.toString() }
                    .filter(isExcludeWords)
                    .forEach { item: String -> callStack.push(item) }
            var order = 1
            val callStackBuilder = StringBuilder()
            while (!callStack.empty()) {
                callStackBuilder.append(MessageFormat.format("{0}\t\t{1}. {2}", NEW_LINE, order++, callStack.pop()))
            }
            return callStackBuilder
        }
    private val isExcludeWords: Predicate<String>
        private get() = Predicate { charSequence: String -> charSequence.startsWith(PACKAGE) && !charSequence.contains(P6SPY_FORMATTER) }

    companion object {
        private val NEW_LINE = System.lineSeparator()
        private const val P6SPY_FORMATTER = "P6spySqlFormatterConfig"
        private const val PACKAGE = "com.cakk"
        private const val CREATE = "create"
        private const val ALTER = "alter"
        private const val COMMENT = "comment"
    }
}
