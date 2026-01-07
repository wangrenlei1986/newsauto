package com.airss

import android.app.Application
import com.airss.data.Repository

object AppGraph {
    private var repo: Repository? = null

    fun repository(app: Application): Repository {
        if (repo == null) repo = Repository(app)
        return repo!!
    }
}
