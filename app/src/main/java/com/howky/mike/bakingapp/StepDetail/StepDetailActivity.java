package com.howky.mike.bakingapp.StepDetail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.howky.mike.bakingapp.R;
import com.howky.mike.bakingapp.RecipeDetail.RecipeDetailActivity;
import com.howky.mike.bakingapp.RecipeDetail.StepsAdapter;

public class StepDetailActivity extends AppCompatActivity implements View.OnClickListener, Player.EventListener{

    private static final String TAG = StepDetailActivity.class.getSimpleName();

    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private ImageButton prevImgbtn, nextImgbtn;

    private int mOrientation;
    private int mStepId;
    private TextView mDescriptionText;
    private int mStepsCount;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);



        Intent receivedIntent = getIntent();
        mStepsCount = receivedIntent.getIntExtra(StepsAdapter.INTENT_STEPS_COUNT, 0);
        if (mStepsCount == 0) {
            Log.e(TAG, "Error 0 steps found!");
            return;
        }

        mOrientation = getResources().getConfiguration().orientation;


        setTitle(RecipeDetailActivity.mTitle);

        mStepId = receivedIntent.getIntExtra(StepsAdapter.INTENT_STEP_ID, -1);
        if (mStepId == -1) {
            Log.e(TAG, "Error getting step ID!");
        }
        else {
            if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
                mDescriptionText = findViewById(R.id.step_detail_tv);
                mDescriptionText.setText(RecipeDetailActivity.mStepDesc[mStepId]);
                prevImgbtn = findViewById(R.id.step_detail_prev_imgbtn);
                prevImgbtn.setOnClickListener(this);

                nextImgbtn = findViewById(R.id.step_detail_next_imgbtn);
                nextImgbtn.setOnClickListener(this);
            }


            mPlayerView = findViewById(R.id.playerView);

            String videoUrl = RecipeDetailActivity.mStepVideoURL[mStepId];
            if (!videoUrl.equals("") || Patterns.WEB_URL.matcher(videoUrl).matches()) {
                // Initialize the Media Session.
                initializeMediaSession();

                Log.d("TAG", Uri.parse(RecipeDetailActivity.mStepVideoURL[mStepId]).toString());
                // Initialize the player.
                initializePlayer(Uri.parse(RecipeDetailActivity.mStepVideoURL[mStepId]));
            } else {
                Log.e(TAG, "videourl is invalid! " + videoUrl);
            }
        }

    }


    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(this, TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            RenderersFactory renderersFactory = new DefaultRenderersFactory(this);
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this, "BakingApp");
            DataSource.Factory factory = new DefaultDataSourceFactory(this, userAgent);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(factory).createMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaSession != null) {
            releasePlayer();
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void onClick(View v) {

        Intent openDetailStepIntent = new Intent(this, StepDetailActivity.class);
        openDetailStepIntent.putExtra(StepsAdapter.INTENT_STEPS_COUNT, mStepsCount);
        Log.d(TAG, "onCLick: " + v.getId());
        Log.d(TAG, "next button id: " + R.id.step_detail_next_imgbtn);
        switch (v.getId()) {
            case R.id.step_detail_next_imgbtn:
                Log.d(TAG, "next button");
                Log.d(TAG, "mStepId: " + mStepId + "  mStepsCOunt: " + mStepsCount);
                if (mStepId + 1 < mStepsCount) {
                    Log.d(TAG, "im here!");
                    openDetailStepIntent.putExtra(StepsAdapter.INTENT_STEP_ID, mStepId + 1);
                    startActivity(openDetailStepIntent);
                } else {
                    Log.d(TAG, "There are no more next steps.");
                }
                break;
            case R.id.step_detail_prev_imgbtn:
                if (mStepId - 1 > 0) {
                    openDetailStepIntent.putExtra(StepsAdapter.INTENT_STEP_ID, mStepId - 1);
                    startActivity(openDetailStepIntent);
                } else {
                    Log.d(TAG, "There are no more previous steps.");
                }
                break;
            default:
                Log.d(TAG, "This view is not implemented yet!");
                break;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == Player.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == Player.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }


    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
}
