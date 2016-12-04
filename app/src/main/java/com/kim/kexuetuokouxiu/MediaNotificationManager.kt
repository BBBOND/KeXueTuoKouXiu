package com.kim.kexuetuokouxiu

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.RemoteException
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.app.NotificationCompat
import com.kim.kexuetuokouxiu.app.activity.DetailActivity
import com.kim.kexuetuokouxiu.helper.LogHelper
import com.kim.kexuetuokouxiu.helper.ResourceHelper
import com.kim.kexuetuokouxiu.service.PlayService

/**
 * Created by Weya on 2016/11/27.
 */

class MediaNotificationManager(private val mPlayService: PlayService) : BroadcastReceiver() {

    private val TAG: String = LogHelper.makeLogTag(MediaNotificationManager::class.java)

    private val NOTIFICATION_ID = 41340727
    private val REQUEST_CODE = 100

    public val ACTION_PAUSE = "com.kim.kexuetuokouxiu.pause"
    public val ACTION_PLAY = "com.kim.kexuetuokouxiu.play"
    public val ACTION_PREV = "com.kim.kexuetuokouxiu.prev"
    public val ACTION_NEXT = "com.kim.kexuetuokouxiu.next"
    public val ACTION_STOP_CASTING = "com.kim.kexuetuokouxiu.stop_cast"

    private var mSessionToken: MediaSessionCompat.Token? = null
    private var mController: MediaControllerCompat? = null
    private var mTransportControls: MediaControllerCompat.TransportControls? = null

    private var mPlaybackState: PlaybackStateCompat? = null
    private var mMetadata: MediaMetadataCompat? = null

    private val mNotificationManager: NotificationManagerCompat

    private val mPauseIntent: PendingIntent
    private val mPlayIntent: PendingIntent
    private val mPreviousIntent: PendingIntent
    private val mNextIntent: PendingIntent

    private val mStopCastIntent: PendingIntent

    private val mNotificationColor: Int

    private var mStarted = false

    init {
        updateSessionToken()

        mNotificationColor = ResourceHelper.getThemeColor(mPlayService, R.attr.colorPrimary, Color.DKGRAY)
        mNotificationManager = NotificationManagerCompat.from(mPlayService)

        val pkg: String = mPlayService.packageName
        mPauseIntent = PendingIntent.getBroadcast(mPlayService, REQUEST_CODE, Intent(ACTION_PAUSE).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT)
        mPlayIntent = PendingIntent.getBroadcast(mPlayService, REQUEST_CODE, Intent(ACTION_PLAY).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT)
        mPreviousIntent = PendingIntent.getBroadcast(mPlayService, REQUEST_CODE, Intent(ACTION_PREV).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT)
        mNextIntent = PendingIntent.getBroadcast(mPlayService, REQUEST_CODE, Intent(ACTION_NEXT).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT)
        mStopCastIntent = PendingIntent.getBroadcast(mPlayService, REQUEST_CODE, Intent(ACTION_STOP_CASTING).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT)

        mNotificationManager.cancelAll()
    }

    /**
     * 显示通知并跟踪Session保持通知更新。
     * 如果Session在调用{@link #stopNotification}前被销毁，通知将会自动被移除
     */
    public fun startNotification() {
        if (!mStarted) {
            mMetadata = mController?.metadata
            mPlaybackState = mController?.playbackState
            val notification: Notification? = createNotification()
            if (notification != null) {
                mController?.registerCallback(mCb)
                val filter: IntentFilter = IntentFilter()
                filter.addAction(ACTION_NEXT)
                filter.addAction(ACTION_PAUSE)
                filter.addAction(ACTION_PLAY)
                filter.addAction(ACTION_PREV)
                filter.addAction(ACTION_STOP_CASTING)
                mPlayService.registerReceiver(this, filter)

                mPlayService.startForeground(NOTIFICATION_ID, notification)
                mStarted = true
            }
        }
    }

    /**
     * 移除通知，停止跟踪Session
     * 如果Session已经被销毁，这个方法则没有影响
     */
    public fun stopNotification() {
        if (mStarted) {
            mStarted = false
            mController?.unregisterCallback(mCb)
            try {
                mNotificationManager.cancel(NOTIFICATION_ID)
                mPlayService.unregisterReceiver(this)
            } catch(e: Exception) {
            }
            mPlayService.stopForeground(true)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action: String = intent.action
        LogHelper.d(TAG, "Received intent with action " + action)
        when (action) {
            ACTION_PAUSE -> mTransportControls?.pause()
            ACTION_PLAY -> mTransportControls?.play()
            ACTION_NEXT -> mTransportControls?.skipToNext()
            ACTION_PREV -> mTransportControls?.skipToPrevious()
            ACTION_STOP_CASTING -> {
                // todo
                val i = Intent(context, PlayService::class.java)
                i.action = ""
                i.putExtra("", "")
                mPlayService.startService(i)
            }
        }

    }

    /**
     * 基于Session Token更新状态
     * 在第一次运行或Media Session被所有者销毁时被调用
     */
    private fun updateSessionToken() {
        val freshToken = mPlayService.sessionToken
        if (mSessionToken == null && freshToken != null ||
                mSessionToken != null && !(mSessionToken?.equals(freshToken) as Boolean)) {
            if (mController != null) {
                mController?.unregisterCallback(mCb)
            }
            mSessionToken = freshToken
            if (mSessionToken != null) {
                mController = MediaControllerCompat(mPlayService, mSessionToken)
                mTransportControls = mController?.transportControls
                if (mStarted)
                    mController?.registerCallback(mCb)
            }
        }
    }

    private fun createContentIntent(description: MediaDescriptionCompat?): PendingIntent {
        val openUI: Intent = Intent(mPlayService, DetailActivity::class.java)
        openUI.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        // todo
        if (description != null) {
            // todo
        }
        return PendingIntent.getActivity(mPlayService, REQUEST_CODE, openUI, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    private val mCb: MediaControllerCompat.Callback = object : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
            mPlaybackState = state
            LogHelper.d(TAG, "Received new playback state", state)
            if (state.state == PlaybackStateCompat.STATE_STOPPED ||
                    state.state == PlaybackStateCompat.STATE_NONE) {
                stopNotification()
            } else {
                val notification: Notification? = createNotification()
                if (notification != null) {
                    mNotificationManager.notify(NOTIFICATION_ID, notification)
                }
            }
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            mMetadata = metadata
            LogHelper.d(TAG, "Received new metadata ", metadata)
            val notification: Notification? = createNotification()
            if (notification != null) {
                mNotificationManager.notify(NOTIFICATION_ID, notification)
            }
        }

        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
            LogHelper.d(TAG, "Session was destroyed, resetting to the new session token")
            try {
                updateSessionToken()
            } catch (e: RemoteException) {
                LogHelper.e(TAG, e, "could not connect media controller")
            }

        }
    }

    private fun createNotification(): Notification? {
        LogHelper.d(TAG, "updateNotificationMetadata. mMetadata=" + mMetadata)
        if (mMetadata == null || mPlaybackState == null)
            return null
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(mPlayService)
        var playPauseButtonPosition: Int = 0

        if ((mPlaybackState?.actions as Long and PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) != 0L) {
            builder.addAction(android.R.drawable.ic_media_previous, "上一首", mPreviousIntent)
            playPauseButtonPosition = 1
        }

        addPlayPauseAction(builder)

        if ((mPlaybackState?.actions as Long and PlaybackStateCompat.ACTION_SKIP_TO_NEXT) != 0L) {
            builder.addAction(android.R.drawable.ic_media_next, "下一首", mNextIntent)
        }

        val description: MediaDescriptionCompat = mMetadata?.description as MediaDescriptionCompat
        val art: Bitmap = BitmapFactory.decodeResource(mPlayService.resources, R.drawable.kexuetuokouxiu_2014)

        builder
                .setStyle(NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(playPauseButtonPosition)
                        .setMediaSession(mSessionToken))
                .setColor(mNotificationColor)
                .setSmallIcon(R.drawable.kexuetuokouxiu_2014)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setUsesChronometer(true)
                .setContentIntent(createContentIntent(description))
                .setContentTitle(description.title)
                .setContentText(description.subtitle)
                .setLargeIcon(art)

        if (mController != null && mController?.extras != null) {
            val castName = mController?.extras?.getString(PlayService.EXTRA_CONNECTED_CAST)
            if (castName != null) {
                val castInfo = mPlayService.resources.getString(R.string.casting_to_device, castName)
                builder.setSubText(castInfo)
                builder.addAction(android.R.drawable.ic_menu_close_clear_cancel, "停止", mStopCastIntent)
            }
        }

        setNotificationPlaybackState(builder)
        return builder.build()
    }

    private fun addPlayPauseAction(builder: NotificationCompat.Builder) {
        LogHelper.d(TAG, "addPlayPauseAction")
        val label: String
        val icon: Int
        val intent: PendingIntent
        if (mPlaybackState?.state == PlaybackStateCompat.STATE_PLAYING) {
            label = "暂停"
            icon = android.R.drawable.ic_media_pause
            intent = mPauseIntent
        } else {
            label = "播放"
            icon = android.R.drawable.ic_media_play
            intent = mPlayIntent
        }
        builder.addAction(android.support.v4.app.NotificationCompat.Action(icon, label, intent))
    }

    private fun setNotificationPlaybackState(builder: NotificationCompat.Builder) {
        LogHelper.d(TAG, "updateNotificationPlaybackState. mPlaybackState=" + mPlaybackState)
        if (mPlaybackState == null || !mStarted) {
            LogHelper.d(TAG, "updateNotificationPlaybackState. cancelling notification!")
            mPlayService.stopForeground(true)
            return
        }
        if (mPlaybackState?.state == PlaybackStateCompat.STATE_PLAYING
                && mPlaybackState?.position as Long >= 0) {
            LogHelper.d(TAG, "updateNotificationPlaybackState. updating playback position to ",
                    (System.currentTimeMillis() - mPlaybackState?.position as Long) / 1000, " seconds")
            builder
                    .setWhen(System.currentTimeMillis() - mPlaybackState?.position as Long)
                    .setShowWhen(true)
                    .setUsesChronometer(true)
        } else {
            LogHelper.d(TAG, "updateNotificationPlaybackState. hiding playback position")
            builder
                    .setWhen(0)
                    .setShowWhen(false)
                    .setUsesChronometer(false)
        }
        // 确保在不播放的时候通知可被清除
        builder.setOngoing(mPlaybackState?.state == PlaybackStateCompat.STATE_PLAYING)
    }
}