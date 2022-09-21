package com.example.cuisinele.data

import android.app.Application
import android.content.Context

/**
 * Class to allow static use of application context
 */
class ContextApplication: Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: ContextApplication? = null
        /**
         * returns an instance of an applicationContext
         */
        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()

        val context: Context = ContextApplication.applicationContext()
    }
}