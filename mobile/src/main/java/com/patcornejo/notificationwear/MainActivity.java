package com.patcornejo.notificationwear;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.widget.RemoteViews;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_VOICE_REPLY = "voice_reply";

    int notificationId = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.basic).setOnClickListener(basicl);
        findViewById(R.id.basic_with_magic).setOnClickListener(basicl);
        findViewById(R.id.with_action).setOnClickListener(basicl);
        findViewById(R.id.extended).setOnClickListener(basicl);
        findViewById(R.id.with_page).setOnClickListener(basicl);
        findViewById(R.id.big_text).setOnClickListener(basicl);
        findViewById(R.id.with_voice).setOnClickListener(basicl);
        findViewById(R.id.with_button).setOnClickListener(basicl);
        findViewById(R.id.with_view).setOnClickListener(basicl);
    }

    private View.OnClickListener basicl = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent viewIntent = new Intent(MainActivity.this, EventActivity.class);
            viewIntent.putExtra(EventActivity.EXTRA_EVENT_ID, new Random().nextInt());

            PendingIntent viewPendingIntent =
                    PendingIntent.getActivity(MainActivity.this, 0, viewIntent, 0);

            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(MainActivity.this);

            NotificationCompat.Builder nb = getBuilder(
                    "WorkShop Android Wear",
                    "Teatinos 251",
                    "Información",
                    viewPendingIntent
            );

            switch (v.getId()) {
                case R.id.basic_with_magic:
                    nb.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.url));
                    break;
                case R.id.with_action:
                    //nb.addAction(android.R.drawable.ic_dialog_map, "Open Map", getMapIntent());
                    nb.addAction(getAction());
                    break;
                case R.id.extended:
                    nb.extend(getExtender());
                    break;
                case R.id.with_page:
                    nb.extend(getExtender().addPage(getPage()));
                    break;
                case R.id.big_text:
                    nb.setStyle(getCompatStyle());
                    break;
                case R.id.with_voice:
                    nb.extend(getExtender().addAction(getVoice()));
                    break;
                case R.id.with_button:
                    nb.extend(getExtender()
                            .setHintHideIcon(true)
                            .setContentIcon(R.drawable.content_icon_small)
                            .setContentIconGravity(Gravity.END | Gravity.CENTER_VERTICAL)
                            .setContentAction(0));
                    break;
                case R.id.with_view:
                    nb.setContent(getRemoteViews());
                    break;
            }

            notificationManager.notify(notificationId, nb.build());
        }
    };

    private RemoteViews getRemoteViews() {
        return new RemoteViews(getPackageName(), R.layout.notification);
    }

    private NotificationCompat.Action getVoice() {

        return new NotificationCompat.Action.Builder(
                    R.drawable.ic_cast_dark,
                    "Reply",
                    getReplyIntent()
                )
                .addRemoteInput(getRemoteInput())
                .build();
    }

    private Notification getPage() {
        return new Notification.Builder(this)
                .setContentTitle("Soy un titulo")
                .setContentText("Soy un texto en la segunda pagina.")
                //.setStyle(getBigTextStyle())
                .build();
    }

    private NotificationCompat.BigPictureStyle getPictureStyle() {
        NotificationCompat.BigPictureStyle pic = new NotificationCompat.BigPictureStyle();
        pic.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.url));

        return pic;
    }

    private Notification.BigTextStyle getBigTextStyle() {
        Notification.BigTextStyle big = new Notification.BigTextStyle();
        big.bigText("Soy un texto muy largo en la pagina dos de Android Wear");
        big.setBigContentTitle("Soy un bigTextStyle");
        big.setSummaryText("Soy un resumen");

        return big;
    }

    private NotificationCompat.BigTextStyle getCompatStyle() {
        NotificationCompat.BigTextStyle big = new NotificationCompat.BigTextStyle();
        big.setBigContentTitle("Soy un titulo Grande");
        big.setSummaryText("Soy un resument");
        big.bigText("Texto muy largo...");

        return big;
    }

    private RemoteInput getRemoteInput() {

        return new RemoteInput.Builder(EXTRA_VOICE_REPLY)
                .setLabel("Habla Ahora")
                .setChoices(getResources().getStringArray(R.array.reply_choices))
                .build();
    }

    private WearableExtender getExtender() {
        return new WearableExtender()
                .addAction(getAction())
                .setBackground(BitmapFactory.decodeResource(getResources(), R.drawable.url));
    }

    private NotificationCompat.Action getAction() {

        return new NotificationCompat.Action(
                R.drawable.content_icon_small,
                "Open Map",
                getMapIntent());
    }

    private PendingIntent getReplyIntent() {
        Intent replyIntent = new Intent(this, ReplyActivity.class);
        return PendingIntent.getActivity(this, 0, replyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getMapIntent() {
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Uri geoUri = Uri.parse("geo:0,0?q=Santiago, Chile");
        mapIntent.setData(geoUri);

        return PendingIntent.getActivity(this, 0, mapIntent, 0);
    }

    private NotificationCompat.Builder getBuilder(String title, String location, String info, PendingIntent pi) {

        return new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                //.setContentTitle(title)
                //.setContentText(location)
                //.setContentInfo(info)
                .setContentIntent(pi);
    }
}
