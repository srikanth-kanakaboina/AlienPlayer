package com.jk.alienplayer.data;

import com.jk.alienplayer.metadata.NetworkAlbumInfo;
import com.jk.alienplayer.metadata.NetworkArtistInfo;
import com.jk.alienplayer.metadata.NetworkSearchResult;
import com.jk.alienplayer.metadata.NetworkTrackInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonHelper {
    public static final String RESULT = "result";
    public static final String STATUS = "code";
    public static final int STATUS_OK = 200;

    public static List<NetworkSearchResult> parseSearchArtists(String jsonStr) {
        List<NetworkSearchResult> artists = new ArrayList<NetworkSearchResult>();
        try {
            JSONObject json = new JSONObject(jsonStr);
            int status = json.getInt(STATUS);
            if (status == STATUS_OK) {
                JSONObject result = json.getJSONObject(RESULT);
                JSONArray artistArray = result.getJSONArray("artists");
                for (int i = 0; i < artistArray.length(); i++) {
                    JSONObject artistObj = artistArray.getJSONObject(i);
                    long id = artistObj.getLong("id");
                    String name = artistObj.getString("name");
                    String avatar = artistObj.getString("picUrl");
                    NetworkArtistInfo info = new NetworkArtistInfo(id, name, avatar);
                    artists.add(info);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return artists;
    }

    public static List<NetworkSearchResult> parseSearchAlbums(String jsonStr) {
        List<NetworkSearchResult> albums = new ArrayList<NetworkSearchResult>();
        try {
            JSONObject json = new JSONObject(jsonStr);
            int status = json.getInt(STATUS);
            if (status == STATUS_OK) {
                JSONObject result = json.getJSONObject(RESULT);
                JSONArray albumArray = result.getJSONArray("albums");
                for (int i = 0; i < albumArray.length(); i++) {
                    JSONObject albumObj = albumArray.getJSONObject(i);
                    long id = albumObj.getLong("id");
                    String name = albumObj.getString("name");
                    String avatar = albumObj.getString("picUrl");
                    long publishTime = albumObj.getLong("publishTime");
                    JSONObject artistObj = albumObj.getJSONObject("artist");
                    String artist = artistObj.getString("name");
                    NetworkAlbumInfo info = new NetworkAlbumInfo(id, name, avatar, artist,
                            publishTime);
                    albums.add(info);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return albums;
    }

    public static List<NetworkSearchResult> parseSearchTracks(String jsonStr) {
        List<NetworkSearchResult> tracks = new ArrayList<NetworkSearchResult>();
        try {
            JSONObject json = new JSONObject(jsonStr);
            int status = json.getInt(STATUS);
            if (status == STATUS_OK) {
                JSONObject result = json.getJSONObject(RESULT);
                JSONArray trackArray = result.getJSONArray("songs");
                for (int i = 0; i < trackArray.length(); i++) {
                    JSONObject trackObj = trackArray.getJSONObject(i);
                    long id = trackObj.getLong("id");
                    String name = trackObj.getString("name");

                    JSONObject albumObj = trackObj.getJSONObject("album");
                    String albumStr = albumObj.getString("name");
                    String artists = getArtistsOfItem(trackObj);

                    NetworkTrackInfo info = new NetworkTrackInfo(id, name, artists);
                    info.album = albumStr;
                    tracks.add(info);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tracks;
    }

    public static List<NetworkAlbumInfo> parseAlbums(String jsonStr) {
        List<NetworkAlbumInfo> albums = new ArrayList<NetworkAlbumInfo>();
        try {
            JSONObject json = new JSONObject(jsonStr);
            int status = json.getInt(STATUS);
            if (status == STATUS_OK) {
                JSONObject artistObj = json.getJSONObject("artist");
                String artist = artistObj.getString("name");
                JSONArray albumArray = json.getJSONArray("hotAlbums");
                for (int i = 0; i < albumArray.length(); i++) {
                    JSONObject albumObj = albumArray.getJSONObject(i);
                    long id = albumObj.getLong("id");
                    String name = albumObj.getString("name");
                    String avatar = albumObj.getString("picUrl");
                    long publishTime = albumObj.getLong("publishTime");
                    NetworkAlbumInfo info = new NetworkAlbumInfo(id, name, avatar, artist,
                            publishTime);
                    albums.add(info);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return albums;
    }

    public static List<NetworkTrackInfo> parseTracks(String jsonStr) {
        List<NetworkTrackInfo> tracks = new ArrayList<NetworkTrackInfo>();
        try {
            JSONObject json = new JSONObject(jsonStr);
            int status = json.getInt(STATUS);
            if (status == STATUS_OK) {
                JSONObject album = json.getJSONObject("album");
                JSONArray trackArray = album.getJSONArray("songs");
                for (int i = 0; i < trackArray.length(); i++) {
                    JSONObject trackObj = trackArray.getJSONObject(i);
                    long id = trackObj.getLong("id");
                    String name = trackObj.getString("name");
                    int position = trackObj.getInt("position");

                    JSONObject albumObj = trackObj.getJSONObject("album");
                    String albumStr = albumObj.getString("name");
                    long publishTime = albumObj.getLong("publishTime");
                    String artistAlbum = getArtistsOfItem(albumObj);
                    String artists = getArtistsOfItem(trackObj);

                    NetworkTrackInfo info = new NetworkTrackInfo(id, name, artists);
                    info.coverUrl = albumObj.getString("picUrl");
                    info.dfsId = getDfsIdOfItem(trackObj);
                    info.artistAlbum = artistAlbum;
                    info.album = albumStr;
                    info.position = position;
                    info.setYear(publishTime);
                    tracks.add(info);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tracks;
    }

    public static NetworkTrackInfo parseTrack(String jsonStr) {
        try {
            JSONObject json = new JSONObject(jsonStr);
            int status = json.getInt(STATUS);
            if (status == STATUS_OK) {
                JSONArray trackArray = json.getJSONArray("songs");
                for (int i = 0; i < trackArray.length(); ) {
                    JSONObject trackObj = trackArray.getJSONObject(i);
                    long id = trackObj.getLong("id");
                    String name = trackObj.getString("name");
                    int position = trackObj.getInt("position");

                    JSONObject albumObj = trackObj.getJSONObject("album");
                    String albumStr = albumObj.getString("name");
                    long publishTime = albumObj.getLong("publishTime");
                    String artistAlbum = getArtistsOfItem(albumObj);
                    String artists = getArtistsOfItem(trackObj);

                    NetworkTrackInfo info = new NetworkTrackInfo(id, name, artists);
                    info.coverUrl = albumObj.getString("picUrl");
                    info.dfsId = getDfsIdOfItem(trackObj);
                    info.album = albumStr;
                    info.artistAlbum = artistAlbum;
                    info.position = position;
                    info.setYear(publishTime);
                    return info;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseLyric(String jsonStr) {
        try {
            JSONObject json = new JSONObject(jsonStr);
            int status = json.getInt(STATUS);
            if (status == STATUS_OK) {
                JSONObject lrc = json.getJSONObject("lrc");
                return lrc.getString("lyric");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getArtistsOfItem(JSONObject Obj) {
        String artists = "";
        JSONArray artistsArray;
        try {
            artistsArray = Obj.getJSONArray("artists");
            for (int j = 0; j < artistsArray.length(); j++) {
                JSONObject artistObj = artistsArray.getJSONObject(j);
                String artist = artistObj.getString("name");
                artists += artist + "&";
            }
            artists = artists.substring(0, artists.length() - 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return artists;
    }

    private static long getDfsIdOfItem(JSONObject trackObj) {
        try {
            JSONObject hMusicObj = trackObj.getJSONObject("hMusic");
            if (hMusicObj != null) {
                return hMusicObj.getLong("dfsId");
            }

            JSONObject mMusicObj = trackObj.getJSONObject("mMusic");
            if (mMusicObj != null) {
                return mMusicObj.getLong("dfsId");
            }

            JSONObject lMusicObj = trackObj.getJSONObject("lMusic");
            if (lMusicObj != null) {
                return lMusicObj.getLong("dfsId");
            }

            JSONObject bMusicObj = trackObj.getJSONObject("bMusic");
            if (bMusicObj != null) {
                return bMusicObj.getLong("dfsId");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
