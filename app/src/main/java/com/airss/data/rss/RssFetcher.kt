package com.airss.data.rss

import okhttp3.OkHttpClient
import okhttp3.Request
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

data class RssItem(
    val id: String,
    val title: String,
    val link: String,
    val source: String,
    val published: Long,
    val description: String
)

class RssFetcher(private val client: OkHttpClient = OkHttpClient()) {
    fun fetch(url: String): List<RssItem> {
        val req = Request.Builder().url(url).build()
        client.newCall(req).execute().use { resp ->
            val body = resp.body?.string() ?: return emptyList()
            return parseXml(body, sourceLabel(url))
        }
    }

    private fun parseXml(xml: String, source: String): List<RssItem> {
        val list = mutableListOf<RssItem>()
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(xml.reader())
        var event = parser.eventType
        var title: String? = null
        var link: String? = null
        var guid: String? = null
        var pubDate: String? = null
        var desc: String? = null
        while (event != XmlPullParser.END_DOCUMENT) {
            when (event) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "item", "entry" -> {
                            title = null; link = null; guid = null; pubDate = null; desc = null
                        }
                        "title" -> title = parser.nextText()
                        "link" -> link = try { parser.nextText() } catch (_: Exception) { link }
                        "guid" -> guid = parser.nextText()
                        "id" -> if (guid == null) guid = parser.nextText()
                        "published", "pubDate", "updated" -> pubDate = parser.nextText()
                        "description", "summary" -> desc = parser.nextText()
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "item" || parser.name == "entry") {
                        val id = (guid ?: link ?: title ?: System.currentTimeMillis().toString())
                        val ts = parseDate(pubDate)
                        list.add(
                            RssItem(
                                id = id,
                                title = title ?: "",
                                link = link ?: "",
                                source = source,
                                published = ts,
                                description = desc ?: ""
                            )
                        )
                    }
                }
            }
            event = parser.next()
        }
        return list
    }

    private fun sourceLabel(url: String): String = when {
        url.contains("openai.com") -> "OpenAI"
        url.contains("huggingface.co") -> "Hugging Face"
        url.contains("deepmind.com") -> "DeepMind"
        url.contains("arxiv.org") -> "arXiv"
        url.contains("mit.edu") -> "MIT News"
        url.contains("leiphone.com") -> "雷锋网"
        url.contains("qbitai.com") -> "量子位"
        url.contains("36kr.com") -> "36氪"
        url.contains("tmtpost.com") -> "钛媒体"
        else -> url
    }

    private fun parseDate(text: String?): Long {
        return System.currentTimeMillis() // 简化：实际应解析RFC822/ISO8601
    }
}
