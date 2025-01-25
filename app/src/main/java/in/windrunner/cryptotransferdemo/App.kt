package `in`.windrunner.cryptotransferdemo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (`in`.windrunner.cryptotransferdemo.BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}