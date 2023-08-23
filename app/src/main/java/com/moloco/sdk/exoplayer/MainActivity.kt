package com.moloco.sdk.exoplayer

import android.content.Context
import android.net.http.SslCertificate.saveState
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView

class MainActivity : AppCompatActivity() {
    private val context: Context get() = this
    private lateinit var playerView: StyledPlayerView
    private var exoPlayer: ExoPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView = StyledPlayerView(context).apply {
            // Disables default ExoPlayer UI.
            useController = false
        }
    }

    override fun onResume() {
        super.onResume()
        initOrResumeExoPlayer()
    }

    override fun onPause() {
        super.onPause()
        disposeExoPlayer()
    }

    private fun initOrResumeExoPlayer() {
        Log.i("==tomi==", "initOrResumeExoPlayer")

        if (exoPlayer == null) {
            // ExoPlayer can be called only from one thread.
            // Compose might run @Composable function in different threads and/or in parallel,
            // that's why I'm scoping ExoPlayer to only to work with calls on the main thread.
            // https://exoplayer.dev/hello-world.html#a-note-on-threading
            // https://developer.android.com/jetpack/compose/mental-model#parallel
            val mainThreadLooper = Looper.getMainLooper()

            exoPlayer = ExoPlayer.Builder(context)
                    .setLooper(mainThreadLooper)
                    .setPauseAtEndOfMediaItems(true)
                    .build()
                    .apply {
                        playWhenReady = false
                    }

            playerView.player = exoPlayer
        }

        // Important for it to be at the end of exoPlayer creation.
        playerView.onResume()
    }

    private fun disposeExoPlayer() {
        Log.i("==tomi==", "disposeExoPlayer")

        playerView.run {
            // Important for it to be at the start of exoPlayer disposal.
            onPause()
            player = null
        }

        exoPlayer?.run {
            release()
        }
        exoPlayer = null
    }
}