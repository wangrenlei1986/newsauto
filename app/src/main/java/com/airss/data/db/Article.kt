package com.airss.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey val id: String,
    val title: String,
    val source: String,
    val url: String,
    val published: Long,
    val summary: String,
    val audioPath: String? = null,
    val read: Boolean = false
)
