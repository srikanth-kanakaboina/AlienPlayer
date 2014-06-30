package com.jk.alienplayer.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.jk.alienplayer.R;
import com.jk.alienplayer.metadata.NetworkAlbumInfo;
import com.jk.alienplayer.utils.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NetworkAlbumsAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<NetworkAlbumInfo> mAlbums;

    public void setAlbums(List<NetworkAlbumInfo> albums) {
        if (albums != null) {
            mAlbums = albums;
            notifyDataSetChanged();
        }
    }

    public NetworkAlbumsAdapter(Context context) {
        super();
        mInflater = LayoutInflater.from(context);
        mAlbums = new ArrayList<NetworkAlbumInfo>();
    }

    @Override
    public int getCount() {
        return mAlbums.size();
    }

    @Override
    public NetworkAlbumInfo getItem(int position) {
        return mAlbums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.search_network_album_item, null);
            viewHolder.artwork = (ImageView) view.findViewById(R.id.artwork);
            viewHolder.name = (TextView) view.findViewById(R.id.content);
            viewHolder.artist = (TextView) view.findViewById(R.id.artist);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        NetworkAlbumInfo info = mAlbums.get(position);
        viewHolder.name.setText(info.name);
        viewHolder.artist.setText(info.artist);
        viewHolder.artwork.setImageResource(R.drawable.disk);
        ImageLoader.getInstance().displayImage(info.avatar, viewHolder.artwork,
                ImageLoaderUtils.sOptions);
        return view;
    }

    static class ViewHolder {
        ImageView artwork;
        TextView name;
        TextView artist;
    }
}
