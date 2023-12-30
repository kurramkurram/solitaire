package io.github.kurramkurram.solitaire.service

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.data.Movie
import io.github.kurramkurram.solitaire.repository.MovieRepositoryImpl
import io.github.kurramkurram.solitaire.util.L
import io.github.kurramkurram.solitaire.util.RECORD_RESULT_DATA
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 録画用のサービス.
 */
class RecordService : Service() {

    private lateinit var mediaProjection: MediaProjection
    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var virtualDisplay: VirtualDisplay
    private lateinit var file: File

    companion object {
        private const val TAG = "RecordService"
        private const val NOTIFICATION_CHANNEL = "RECORD_NOTIFICATION"
        private const val NOTIFICATION_ID = 1
        private const val VIRTUAL_DISPLAY_NAME = "record"
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val resources = applicationContext.resources
        if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL) == null) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                resources.getString(R.string.notification_channel_recoding),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notification = Notification.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setContentText(resources.getString(R.string.notification_recoding_title))
            .setContentTitle(resources.getString(R.string.notification_recoding_description))
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(RECORD_RESULT_DATA, Intent::class.java)
        } else {
            intent?.getParcelableExtra(RECORD_RESULT_DATA)
        } ?: return START_NOT_STICKY

        val projectionManager =
            applicationContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        mediaProjection = projectionManager.getMediaProjection(Activity.RESULT_OK, data)
        mediaProjection.registerCallback(object : MediaProjection.Callback() {}, null)

        val metrics = applicationContext.resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels

        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(applicationContext)
        } else {
            MediaRecorder()
        }.apply {
            val repository = MovieRepositoryImpl(applicationContext)
            file = repository.getSaveFile()
            L.d(TAG, "#File = ${file.path}")
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setVideoEncodingBitRate(1080 * 10000)
            setVideoFrameRate(30)
            setVideoSize(width, height)
            setOutputFile(file)
            prepare()
        }

        virtualDisplay = mediaProjection.createVirtualDisplay(
            VIRTUAL_DISPLAY_NAME,
            width,
            height,
            metrics.densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mediaRecorder.surface,
            null,
            null,
        )

        mediaRecorder.start()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaRecorder.stop()
        mediaRecorder.release()
        virtualDisplay.release()
        mediaProjection.stop()
        CoroutineScope(Dispatchers.IO).launch {
            val repository = MovieRepositoryImpl(applicationContext)
            repository.saveMovieInfo(Movie(0, file.name, file.path))
            repository.deleteOldestMovie()
        }
    }
}
