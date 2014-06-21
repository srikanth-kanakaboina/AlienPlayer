package com.jk.alienplayer.ui;

import com.jk.alienplayer.R;
import com.jk.alienplayer.data.PlayingInfoHolder;
import com.jk.alienplayer.impl.PlayService;
import com.jk.alienplayer.impl.PlayingHelper;
import com.jk.alienplayer.impl.PlayingHelper.PlayStatus;
import com.jk.alienplayer.impl.PlayingHelper.PlayingInfo;
import com.jk.alienplayer.metadata.SongInfo;
import com.jk.alienplayer.utils.PlayingTimeUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SongDetailActivity extends FragmentActivity {

    private static final int FRAGMENT_ARTWORK = 0;
    private static final int FRAGMENT_LYRIC = 1;
    private static final int FRAGMENT_INFO = 2;

    private SongInfo mSongInfo;
    private SeekBar mSeekBar;
    private ImageButton mPlayBtn;
    private ImageButton mNextBtn;
    private ImageButton mPrevBtn;
    private TextView mProgress;
    private TextView mDuration;
    private TextView mSeekTime;
    private PopupWindow mPopupWindow;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(PlayService.ACTION_START)) {
                int duration = intent.getIntExtra(PlayService.TOTAL_DURATION, 0);
                mPlayBtn.setImageResource(R.drawable.pause);
                mSeekBar.setMax(duration);
            } else if (action.equals(PlayService.ACTION_TRACK_CHANGE)) {
                String song = intent.getStringExtra(PlayService.SONG_NAME);
                String artist = intent.getStringExtra(PlayService.ARTIST_NAME);
                setTitle(song);
                getActionBar().setSubtitle(artist);
                // TODO set fragment
            } else if (action.equals(PlayService.ACTION_PAUSE)) {
                mPlayBtn.setImageResource(R.drawable.play);
            } else if (action.equals(PlayService.ACTION_STOP)) {
                mPlayBtn.setImageResource(R.drawable.play);
                mSeekBar.setProgress(0);
            } else if (action.equals(PlayService.ACTION_PROGRESS_UPDATE)) {
                int progress = intent.getIntExtra(PlayService.CURRENT_DURATION, 0);
                if (!mIsTrackingTouch) {
                    mSeekBar.setProgress(progress);
                }
                mProgress.setText(PlayingTimeUtils.toDisplayTime(progress));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);
        init();

        FragmentPagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.song_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.audioEffect) {
            displayAudioEffect();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        mSongInfo = PlayingInfoHolder.getInstance().getCurrentSong();
        setTitle(mSongInfo.title);
        getActionBar().setSubtitle(mSongInfo.artist);
        mProgress = (TextView) findViewById(R.id.progress);
        mDuration = (TextView) findViewById(R.id.duration);
        mProgress.setText(PlayingTimeUtils.toDisplayTime(0));
        mDuration.setText(PlayingTimeUtils.toDisplayTime(mSongInfo.duration));

        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);

        mPlayBtn = (ImageButton) findViewById(R.id.play);
        mPlayBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PlayService.getPlayingCommandIntent(SongDetailActivity.this,
                        PlayService.COMMAND_PLAY_PAUSE);
                startService(intent);
            }
        });

        mSeekTime = new TextView(this);
        mSeekTime.setTextColor(getResources().getColor(android.R.color.white));
        mPopupWindow = new PopupWindow(mSeekTime, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, false);
        mNextBtn = (ImageButton) findViewById(R.id.next);
        mPrevBtn = (ImageButton) findViewById(R.id.prev);
        PlayService.registerReceiver(this, mReceiver);
        syncView();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void syncView() {
        PlayingInfo info = PlayingHelper.getPlayingInfo();
        if (info.status == PlayStatus.Playing) {
            mPlayBtn.setImageResource(R.drawable.pause);
            mSeekBar.setMax(info.duration);
            mSeekBar.setProgress(info.progress);
        } else if (info.status == PlayStatus.Paused) {
            mPlayBtn.setImageResource(R.drawable.play);
            mSeekBar.setMax(info.duration);
            mSeekBar.setProgress(info.progress);
        }
    }

    private void displayAudioEffect() {
        Intent intent = PlayService.getDisplayAudioEffectIntent(this);
        if (getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            startActivityForResult(intent, 0);
        }
    }

    private int mSeekBarW = 0;
    private int mSeekBarH = 0;
    private boolean mIsTrackingTouch = false;
    OnSeekBarChangeListener mSeekBarChangeListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                mPopupWindow.dismiss();
                float ratio = (float) progress / (float) seekBar.getMax();
                mSeekTime.setText(PlayingTimeUtils.toDisplayTime(progress));
                mPopupWindow.showAsDropDown(mSeekBar, (int) (mSeekBarW * ratio), -mSeekBarH * 2);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mIsTrackingTouch = true;
            mSeekBarW = mSeekBar.getWidth();
            mSeekBarH = mSeekBar.getHeight();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mIsTrackingTouch = false;
            mPopupWindow.dismiss();

            Intent intent = PlayService.getSeekIntent(SongDetailActivity.this,
                    seekBar.getProgress());
            startService(intent);
        }
    };

    class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
            case FRAGMENT_ARTWORK:
                return new ArtworkFragment();
            case FRAGMENT_LYRIC:
                return new ArtworkFragment();
            case FRAGMENT_INFO:
                return new ArtworkFragment();
            default:
                return new ArtworkFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
