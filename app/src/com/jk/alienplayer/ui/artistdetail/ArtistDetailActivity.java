package com.jk.alienplayer.ui.artistdetail;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.jk.alienplayer.R;
import com.jk.alienplayer.metadata.CurrentlistInfo;
import com.jk.alienplayer.ui.BaseActivity;
import com.jk.alienplayer.ui.main.AlbumsFragment;
import com.jk.alienplayer.widget.Playbar;

public class ArtistDetailActivity extends BaseActivity {
    private static final int FRAGMENT_ALBUMS = 0;
    private static final int FRAGMENT_SONGS = 1;
    private static final int FRAGMENT_COUNT = 2;

    public static final String ARTIST_ID = "artist_id";
    public static final String ARTIST_NAME = "artist_name";

    private Playbar mPlaybar;
    private long mArtistId;
    private String mArtistName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);
        init();
    }

    @Override
    protected void onDestroy() {
        //mPlaybar.finish();
        super.onDestroy();
    }

    private void init() {
        //mPlaybar = (Playbar) findViewById(R.id.playbar);
        mArtistId = getIntent().getLongExtra(ARTIST_ID, 0);
        mArtistName = getIntent().getStringExtra(ARTIST_NAME);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mArtistName);
        setSupportActionBar(toolbar);

        FragmentPagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(adapter);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(pager);
    }


    class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case FRAGMENT_ALBUMS:
                    AlbumsFragment af = new AlbumsFragment();
                    Bundle argA = new Bundle();
                    argA.putString(AlbumsFragment.ARTIST_NAME, mArtistName);
                    af.setArguments(argA);
                    return af;
                case FRAGMENT_SONGS:
                    SongsFragment sf = new SongsFragment();
                    Bundle argS = new Bundle();
                    argS.putInt(SongsFragment.KEY_TYPE, CurrentlistInfo.TYPE_ARTIST);
                    argS.putLong(SongsFragment.KEY, mArtistId);
                    argS.putString(SongsFragment.LABEL, mArtistName);
                    sf.setArguments(argS);
                    return sf;
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case FRAGMENT_ALBUMS:
                    return getString(R.string.albums);
                case FRAGMENT_SONGS:
                    return getString(R.string.tracks);
                default:
                    return "";
            }
        }

        @Override
        public int getCount() {
            return FRAGMENT_COUNT;
        }
    }
}
