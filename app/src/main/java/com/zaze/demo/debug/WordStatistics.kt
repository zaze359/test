package com.zaze.demo.debug

/**
 * 单词频率
 */
data class WordFreq(val word: String, val frequency: Int)

/**
 * 单词统计
 * - 英文单词
 */
object WordStatistics {

    /**
     * 处理问文本
     */
    fun processText(text: String): List<WordFreq> {
        return text
            .clean()
            .split(" ")
            .filter { it.isNotEmpty() }
            .groupBy { it }
            .map { WordFreq(it.key, it.value.size) }
            .sortedByDescending { it.frequency }
    }

    private fun String.clean(): String {
        return this.replace("[^A-Za-z]".toRegex(), " ")
            .trim()
    }

}