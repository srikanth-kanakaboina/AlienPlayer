package com.jk.alienplayer.ui;

import com.jk.alienplayer.R;
import com.jk.alienplayer.data.PlayingInfoHolder;
import com.jk.alienplayer.data.DatabaseHelper;
import com.jk.alienplayer.data.TrackInfo;
import com.jk.alienplayer.impl.PlayingHelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SongsActivity extends Activity {

    public static final String KEY_TYPE = "key_type";
    public static final String KEY = "key";
    public static final String LABEL = "label";

    private int mKeyType;
    private String mKey;
    private ListView mListView;
    private SongsAdapter mAdapter;
    private PlaybarHelper mPlaybarHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlaybarHelper.syncView();
    }

    private void init() {
        mPlaybarHelper = new PlaybarHelper(this);
        mKeyType = getIntent().getIntExtra(KEY_TYPE, DatabaseHelper.TYPE_ARTIST);
        mKey = getIntent().getStringExtra(KEY);
        setTitle(getIntent().getStringExtra(LABEL));

        mListView = (ListView) findViewById(R.id.list);
        mAdapter = new SongsAdapter(this);
        mListView.setAdapter(mAdapter);
        mAdapter.setSongs(DatabaseHelper.getTracks(this, mKeyType, mKey));
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onSongClick(mAdapter.getItem(position));
            }
        });
    }

    private void onSongClick(TrackInfo song) {
        PlayingInfoHolder.getInstance().setCurrentSong(this, song);
        if (PlayingHelper.getInstance().play(mPlaybarHelper.getListener())) {
            mPlaybarHelper.setPlayBtnImage(R.drawable.pause);
        }
        mPlaybarHelper.setArtwork(song);
    }
}
