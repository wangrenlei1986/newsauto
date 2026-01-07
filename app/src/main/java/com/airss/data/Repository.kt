package com.airss.data

import android.content.Context
import androidx.room.Room
import com.airss.data.db.AppDatabase
import com.airss.data.db.Article
import com.airss.data.rss.RssFetcher
import com.airss.data.rss.RssSources
import com.airss.domain.Deduper
import com.airss.domain.Summarizer

class Repository(context: Context) {
    private val db = Room.databaseBuilder(context, AppDatabase::class.java, "ai_news.db").build()
    private val dao = db.articleDao()
    private val fetcher = RssFetcher()

    suspend fun refreshAll(): Int {
        val items = RssSources.DEFAULT.flatMap { fetcher.fetch(it) }
        // 简易去重：基于SimHash阈值过滤（可改为更严格）
        val seen = mutableMapOf<Long, Article>()
        val articles = mutableListOf<Article>()
        for (i in items) {
            val sum = Summarizer.summarize(i.description)
            val hash = Deduper.simHash(sum + i.title)
            val dup = seen.keys.any { Deduper.hamming(it, hash) < 6 }
            if (!dup) {
                seen[hash] = Article(
                    id = i.id,
                    title = i.title,
                    source = i.source,
                    url = i.link,
                    published = i.published,
                    summary = sum
                )
            }
        }
        articles.addAll(seen.values)
        dao.upsertAll(articles)
        return articles.size
    }

    fun stream() = dao.all()
}
