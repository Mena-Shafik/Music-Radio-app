package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.musicplayer.Model.Song;
import com.example.musicplayer.Services.NotificationActionService;

public class CreateNotification {

    public static final String CHANNEL_ID = "channel1";

    public static final String ACTION_PREVIOUS = "actionprevious";
    public static final String ACTION_PLAY = "actionplay";
    public static final String ACTION_NEXT = "actionnext";
    //public static final String ACTION_SCROLL = "actionscroll";

    public static Notification notification;
    @SuppressLint("UnspecifiedImmutableFlag")
    public static void createNotification(Context context, Song song, int playButton, int pos, int size){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSession = new MediaSessionCompat(context, "tag");
            Bitmap icon = Util.getThumbnail(context, Uri.parse(song.getPath()));

            PendingIntent pendingIntentPrevious;
            int drw_previous;

            //if(pos == 0){
             //   pendingIntentPrevious = null;
             //   drw_previous =0;
            //}else{
                Intent intentPrevious = new Intent(context, NotificationActionService.class).setAction(ACTION_PREVIOUS);
                pendingIntentPrevious = PendingIntent.getBroadcast(context,0,intentPrevious,PendingIntent.FLAG_MUTABLE);
                drw_previous = R.drawable.ic_prev_track;
            //}

            Intent intentPlay = new Intent(context, NotificationActionService.class).setAction(ACTION_PLAY);
            PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context,0,intentPlay,PendingIntent.FLAG_MUTABLE);

            PendingIntent pendingIntentNext;
            int drw_next;

            //if(pos ==size){
            //    pendingIntentNext = null;
            //    drw_next =0;
            //}else{
                Intent intentNext = new Intent(context, NotificationActionService.class).setAction(ACTION_NEXT);
                pendingIntentNext = PendingIntent.getBroadcast(context,0,intentNext,PendingIntent.FLAG_MUTABLE);
                drw_next = R.drawable.ic_next_track;
            //}

            //PendingIntent pendingIntentScroll;
            //int drw_scroll;
            //Intent intentScroll = new Intent(context, NotificationActionService.class).setAction(ACTION_SCROLL);
            //pendingIntentScroll = PendingIntent.getBroadcast(context,0,intentNext,PendingIntent.FLAG_MUTABLE);
            //drw_scroll = R.drawable.ic_next_track;

            // Create Notification
            /*notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_music_note)
                    .setContentTitle(song.getTitle())
                    .setContentText(song.getArtist())
                    .setLargeIcon(icon)
                    //.setColor(context.getResources().getColor(R.color.black))
                    .setColor(ContextCompat.getColor(context, R.color.black))
                    .setColorized(true)
                    .setOnlyAlertOnce(true) // show notification for only the first time
                    .setShowWhen(false)
                    .addAction(drw_previous, "Previous",pendingIntentPrevious)
                    .addAction(playButton, "Play",pendingIntentPlay)
                    .addAction(drw_next, "Next",pendingIntentNext)
                    .setStyle(new androidx.media.app.NotificationCompat.DecoratedMediaCustomViewStyle()                                         //androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1, 2))
                            //.setMediaSession(mediaSessionCompat.getSessionToken())) // breaks notifications
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();*/

            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_album)
                    .setContentTitle(song.getTitle())
                    .setContentText(song.getArtist())
                    .setLargeIcon(icon)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1, 2)
                    .setMediaSession(mediaSession.getSessionToken()))
                    .setColor(ContextCompat.getColor(context, R.color.black))
                    .setColorized(true)
                    .addAction(drw_previous, "Previous",pendingIntentPrevious)
                    .addAction(playButton, "Play",pendingIntentPlay)
                    .addAction(drw_next, "Next",pendingIntentNext)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .build();

            notificationManager.notify(1, notification);
        }
    }

}
