package com.airss.domain

import java.math.BigInteger

object Deduper {
    // 简化版SimHash：按词出现频次生成64位指纹
    fun simHash(text: String): Long {
        val tokens = text.lowercase().split(Regex("\\W+")).filter { it.length > 2 }
        val vec = IntArray(64)
        for (t in tokens) {
            val h = t.hashCode().toLong()
            for (i in 0 until 64) {
                val bit = (h shr i) and 1L
                vec[i] += if (bit == 1L) 1 else -1
            }
        }
        var res = 0L
        for (i in 0 until 64) {
            if (vec[i] > 0) res = res or (1L shl i)
        }
        return res
    }

    fun hamming(a: Long, b: Long): Int {
        return java.lang.Long.bitCount(a xor b)
    }
}
