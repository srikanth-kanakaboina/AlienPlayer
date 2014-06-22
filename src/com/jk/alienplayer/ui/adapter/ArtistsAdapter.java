package com.jk.alienplayer.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.jk.alienplayer.R;
import com.jk.alienplayer.metadata.ArtistInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ArtistsAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ArtistInfo> mArtists;
    private OnItemClickListener mItemClickListener = null;

    public void setArtists(List<ArtistInfo> artists) {
        if (artists != null) {
            mArtists = artists;
            notifyDataSetChanged();
        }
    }

    public ArtistsAdapter(Context context, OnItemClickListener listener) {
        super();
        mItemClickListener = listener;
        mInflater = LayoutInflater.from(context);
        mArtists = new ArrayList<ArtistInfo>();
    }

    @Override
    public int getCount() {
        return mArtists.size();
    }

    @Override
    public ArtistInfo getItem(int position) {
        return mArtists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.list_item, null);
            viewHolder.name = (TextView) view.findViewById(R.id.content);
            viewHolder.action = (ImageView) view.findViewById(R.id.action);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ArtistInfo info = mArtists.get(position);
        viewHolder.name.setText(info.name);
        viewHolder.action.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(null, v, position, getItemId(position));
            }
        });
        return view;
    }

    static class ViewHolder {
        TextView name;
        ImageView action;
    }
}
