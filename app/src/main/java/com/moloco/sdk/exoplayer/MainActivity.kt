package com.moloco.sdk.exoplayer

import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView

class MainActivity : AppCompatActivity() {
    private val context: Context get() = this
    private var playerView: StyledPlayerView? = null
    private var exoPlayer: ExoPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        Log.i("==tomi==", "initOrResumeExoPlayer +++")

        try {
            if (playerView == null) {
                playerView = StyledPlayerView(context).apply {
                    // Disables default ExoPlayer UI.
                    useController = false
                }
            }
        } catch (e: Exception) {
            Log.e("==tomi==", "initOrResumeExoPlayer", e)
            return
        }

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

            playerView!!.player = exoPlayer
        }

        // Important for it to be at the end of exoPlayer creation.
        playerView!!.onResume()

        Log.i("==tomi==", "initOrResumeExoPlayer ---")
    }

    private fun disposeExoPlayer() {
        Log.i("==tomi==", "disposeExoPlayer +++")

        playerView?.run {
            // Important for it to be at the start of exoPlayer disposal.
            onPause()
            player = null
        }

        exoPlayer?.run {
            release()
        }
        exoPlayer = null

        Log.i("==tomi==", "disposeExoPlayer ---")
    }
}