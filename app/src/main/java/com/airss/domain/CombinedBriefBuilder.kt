package com.airss.domain

import com.airss.data.db.Article

object CombinedBriefBuilder {
    fun buildBrief(list: List<Article>, maxItems: Int = 10): String {
        val sb = StringBuilder()
        sb.append("今天的AI简报如下：\n")
        list.take(maxItems).forEachIndexed { idx, a ->
            sb.append("${idx + 1}. 来自${a.source}：${a.title}。${a.summary}\n")
        }
        sb.append("以上为主要动态。")
        return sb.toString()
    }
}
