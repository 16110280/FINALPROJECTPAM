package com.example.android.moviedatabase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.PendingIntent;

import com.example.android.moviedatabase.utilities.NotificationUtils;
import com.squareup.picasso.Picasso;
import android.app.Notification;
import android.app.NotificationManager;
import android.widget.Button;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;

import static com.example.android.moviedatabase.utilities.NotificationUtils.ANDROID_CHANNEL_ID;

public class DetailActivity extends AppCompatActivity {

    private ImageView mMoviePoster;
    private TextView mMovieTitle;
    private TextView mMovieReleaseDate;
    private TextView mMoviePlot;
    private TextView mMovieRating;

    private static final String MOVIEDB_BASEURL =
            "https://image.tmdb.org/t/p/w600_and_h900_bestv2";
    public static final int NOTIF_ID = 1;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://dicoding.com"));
        pendingIntent = PendingIntent
                .getActivity(DetailActivity.this, 0, intent, 0);

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    showNotifOreo();
                else showNotifDefault();
            }
        });

        mMoviePoster = (ImageView) findViewById(R.id.da_iv_poster);
        mMovieTitle = (TextView) findViewById(R.id.da_tv_title);
        mMovieReleaseDate = (TextView) findViewById(R.id.da_tv_release_date);
        mMoviePlot = (TextView) findViewById(R.id.da_tv_plot);
        mMovieRating = (TextView) findViewById(R.id.da_tv_rating);

        String intentTitle = getResources().getResourceName(R.string.intent_title);
        String intentPlot = getResources().getResourceName(R.string.intent_plot);
        String intentReleaseDate = getResources().getResourceName(R.string.intent_release_date);
        String intentVoting = getResources().getResourceName(R.string.intent_voting);
        String intentPoster = getResources().getResourceName(R.string.intent_poster);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(intentTitle)) {
                String mTitle = intentThatStartedThisActivity.getStringExtra(intentTitle);
                mMovieTitle.setText(mTitle);
                this.setTitle(mTitle);
            }

            if (intentThatStartedThisActivity.hasExtra(intentReleaseDate)) {
                String mReleaseDate = intentThatStartedThisActivity.getStringExtra(intentReleaseDate);
                mMovieReleaseDate.setText(mReleaseDate);
            }

            if (intentThatStartedThisActivity.hasExtra(intentPlot)) {
                String mPlot = intentThatStartedThisActivity.getStringExtra(intentPlot);
                mMoviePlot.setText(mPlot);
            }

            if (intentThatStartedThisActivity.hasExtra(intentPoster)) {
                String mUrl = intentThatStartedThisActivity.getStringExtra(intentPoster);
                String URL =  MOVIEDB_BASEURL + mUrl;
                Picasso.get().load(URL).into(mMoviePoster);
            }

            if (intentThatStartedThisActivity.hasExtra(intentVoting)) {
                String mVote = Double.toString(intentThatStartedThisActivity.getDoubleExtra(intentVoting, 0));
                CharSequence maxRating = getResources().getText(R.string.da_rating_max);
                String mRating = mVote + maxRating;
                mMovieRating.setText(mRating);
            }
        }
    }
    public void showNotifDefault(){
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(DetailActivity.this)
                .setSmallIcon(R.drawable.ic_access_alarm_black_36dp)
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources()
                        , R.drawable.ic_access_alarm_black_36dp))
                .setContentTitle(getResources().getString(R.string.content_title))
                .setContentText(getResources().getString(R.string.content_text))
                .setSubText(getResources().getString(R.string.subtext))
                .setAutoCancel(true);

        NotificationManagerCompat notifManager = NotificationManagerCompat.from(getApplicationContext());
        notifManager.notify(NOTIF_ID, notifBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNotifOreo(){
        Notification.Builder notifBuilder = new Notification.Builder(DetailActivity.this, ANDROID_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_access_alarm_black_36dp)
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources()
                        , R.drawable.ic_access_alarm_black_36dp))
                .setContentTitle(getResources().getString(R.string.content_title))
                .setContentText(getResources().getString(R.string.content_text))
                .setSubText(getResources().getString(R.string.subtext))
                .setAutoCancel(true);

        NotificationUtils utils = new NotificationUtils(DetailActivity.this);
        utils.getManager().notify(NOTIF_ID, notifBuilder.build());
    }
}
