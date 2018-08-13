package com.howky.mike.bakingapp.StepDetail;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
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
import com.howky.mike.bakingapp.MainActivity;
import com.howky.mike.bakingapp.R;
import com.howky.mike.bakingapp.RecipeDetail.RecipeDetailActivity;
import com.howky.mike.bakingapp.RecipeDetail.RecipeDetailFragment;
import com.howky.mike.bakingapp.RecipeDetail.StepsAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepDetailFragment extends Fragment implements  View.OnClickListener,
    Player.EventListener{

    private static final String PLAYER_CONTENT_POSITION = "player_content_position";
    private static final String PLAYER_WHEN_READY = "player_when_ready";
    private static final String TAG = StepDetailFragment.class.getSimpleName();

    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private ImageButton prevImgbtn, nextImgbtn;

    private int mOrientation;
    private TextView mDescriptionText;
    private TextView mStepInfo;

    private static final String INTENT_STEPS_COUNT = "steps_count";
    private static final String INTENT_STEP_ID = "step_id";

    private int mStepId;
    private int mStepsCount;
    private long mExoPosition;
    private boolean mExoWhenReady;

    private OnFragmentInteractionListener mListener;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param stepCount Number of steps.
     * @param stepId Step Id.
     * @return A new instance of fragment stepDetailFragment.
     */
    public static StepDetailFragment newInstance(int stepCount, int stepId) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putInt(INTENT_STEPS_COUNT, stepCount);
        args.putInt(INTENT_STEP_ID, stepId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStepsCount = getArguments().getInt(INTENT_STEPS_COUNT, 1);
            mStepId = getArguments().getInt(INTENT_STEP_ID ,1);
        } else {
            mStepsCount = 1;
            mStepId = 1;
        }
        mOrientation = getResources().getConfiguration().orientation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        if (mOrientation == Configuration.ORIENTATION_PORTRAIT || MainActivity.mIsTablet) {
            mDescriptionText = rootView.findViewById(R.id.step_detail_tv);
            mDescriptionText.setText(RecipeDetailFragment.mStepDesc[mStepId]);
        }
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT && !MainActivity.mIsTablet) {

            prevImgbtn = rootView.findViewById(R.id.step_detail_prev_imgbtn);
            prevImgbtn.setOnClickListener(this);

            nextImgbtn = rootView.findViewById(R.id.step_detail_next_imgbtn);
            nextImgbtn.setOnClickListener(this);

            mStepInfo = rootView.findViewById(R.id.step_detail_info_tv);
            String stepInfoText = String.format(getResources().getString(R.string.step_info_format), mStepId, mStepsCount - 1);
            mStepInfo.setText(stepInfoText);
        }

        mPlayerView = rootView.findViewById(R.id.playerView);

        // Inflate the layout for this fragment
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mExoWhenReady = true;
        mExoPosition = C.POSITION_UNSET;
        Log.d("Tag", "in onActivityCreated");
        if (savedInstanceState != null) {
            mExoPosition = savedInstanceState.getLong(PLAYER_CONTENT_POSITION, C.POSITION_UNSET);
            mExoWhenReady = savedInstanceState.getBoolean(PLAYER_WHEN_READY, true);
            Log.d("Tag", "mExoPosition: " + mExoPosition);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            checkInitializeMediaSession();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            checkInitializeMediaSession();
        }
    }

    public void checkInitializeMediaSession() {
        String videoUrl = RecipeDetailFragment.mStepVideoURL[mStepId];
        String thumbnailUrl = RecipeDetailFragment.mStepVideoThumbnail[mStepId];
        if (!videoUrl.equals("") || Patterns.WEB_URL.matcher(videoUrl).matches()) {
            // Initialize the Media Session.
            initializeMediaSession();

            // Initialize the player.
            initializePlayer(Uri.parse(RecipeDetailFragment.mStepVideoURL[mStepId]));
        } else if (!thumbnailUrl.equals("") || Patterns.WEB_URL.matcher(videoUrl).matches()){
            // Initialize the Media Session.
            initializeMediaSession();

            // Initialize the player.
            initializePlayer(Uri.parse(RecipeDetailFragment.mStepVideoThumbnail[mStepId]));
        } else {
            Log.e(TAG, "videourl is invalid! " + videoUrl);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

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
            RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            DataSource.Factory factory = new DefaultDataSourceFactory(getContext(), userAgent);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(factory).createMediaSource(mediaUri);

            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(mExoWhenReady);
            if (mExoPosition != C.TIME_UNSET) mExoPlayer.seekTo(mExoPosition);
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
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (mMediaSession != null) {
                releasePlayer();
                mMediaSession.setActive(false);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoPosition = mExoPlayer.getCurrentPosition();
            mExoWhenReady = mExoPlayer.getPlayWhenReady();
            if (Util.SDK_INT <= 23) {
                if (mMediaSession != null) {
                    releasePlayer();
                    mMediaSession.setActive(false);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

        Intent openDetailStepIntent = new Intent(getContext(), StepDetailActivity.class);
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
                if (mStepId > 0) {
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
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {}

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

    @Override
    public void onLoadingChanged(boolean isLoading) {}

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
    public void onRepeatModeChanged(int repeatMode) {}

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {}

    @Override
    public void onPlayerError(ExoPlaybackException error) {}

    @Override
    public void onPositionDiscontinuity(int reason) {}

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}

    @Override
    public void onSeekProcessed() {}


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("tag", "position:" + mExoPosition);
        outState.putLong(PLAYER_CONTENT_POSITION, mExoPosition);
        outState.putBoolean(PLAYER_WHEN_READY, mExoWhenReady);
    }


    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    public class MySessionCallback extends MediaSessionCompat.Callback {
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
}
