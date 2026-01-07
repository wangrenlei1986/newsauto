package com.airss.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.airss.AppGraph

class RefreshWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val repo = AppGraph.repository(applicationContext as android.app.Application)
        return try {
            repo.refreshAll()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
