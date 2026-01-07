package com.airss.domain

// 简化版 TextRank 摘要（占位实现）：现实中可替换为更完善算法
object Summarizer {
    fun summarize(text: String, maxSentences: Int = 4): String {
        val sents = text.split(Regex("(?<=[.!?。！？])\\s+"))
        return sents.take(maxSentences).joinToString(" ")
    }
}
