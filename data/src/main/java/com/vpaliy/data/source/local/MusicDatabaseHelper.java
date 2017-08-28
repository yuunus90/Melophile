package com.vpaliy.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import static com.vpaliy.data.source.local.MusicContract.Users;
import static com.vpaliy.data.source.local.MusicContract.Playlists;
import static com.vpaliy.data.source.local.MusicContract.Tracks;

@SuppressWarnings({"unused","WeakerAccess"})
public class MusicDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="music.db";
    private static final int DATABASE_VERSION=1;

    public interface Tables {
        String TRACKS = "tracks";
        String PLAYLISTS = "playlists";
        String USERS = "users";
        String ME="me";

        String PLAYLIST_JOIN_TRACKS = "playlists " +
                "INNER JOIN tracks ON playlists.playlist_id=tracks.ref_track_playlist_id";
        String USER_JOIN_TRACKS="users "+
                "INNER JOIN tracks ON users.user_id=tracks.ref_track_user_id";
        String USER_JOIN_LIKED_TRACKS="users "+
                "INNER JOIN tracks on users.user_id=tracks.ref_track_user_liked_id";
        String USER_JOIN_PLAYLISTS="users "+
                "INNER JOIN tracks on users.user_id=playlists.ref_playlist_user_id";
        String ME_JOIN_TRACKS="me "+
                "INNER JOIN tracks ON tracks.track_id=";
    }

    interface References {
        String USER="REFERENCES "+Tables.USERS+"("+Users.USER_ID+")";
        String TRACK="REFERENCES "+Tables.TRACKS+"("+Tracks.TRACK_ID+")";
        String PLAYLIST="REFERENCES "+Tables.PLAYLISTS+"("+Playlists.PLAYLIST_ID+")";
    }

    public MusicDatabaseHelper(@NonNull Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+Tables.TRACKS+" ("+
                Tracks.TRACK_ID+" TEXT PRIMARY KEY NOT NULL,"+
                Tracks.TRACK_TITLE+" TEXT NOT NULL,"+
                Tracks.TRACK_ART_URL+" TEXT NOT NULL,"+
                Tracks.TRACK_STREAM_URL+" TEXT NOT NULL,"+
                Tracks.TRACK_DURATION+" TEXT,"+
                Tracks.TRACK_TAGS+" TEXT,"+
                Tracks.TRACK_RELEASE_DATE+" TEXT,"+
                Tracks.TRACK_ARTIST+" TEXT,"+
                Tracks.TRACK_IS_LIKED+" INTEGER NOT NULL,"+
                Tracks.TRACK_USER_ID+" TEXT "+References.USER+","+
                Tracks.TRACK_USER_LIKED_ID+" TEXT "+References.USER+","+
                Tracks.TRACK_PLAYLIST_ID+" TEXT "+References.PLAYLIST+","+
                " UNIQUE (" + Tracks.TRACK_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE "+Tables.PLAYLISTS+" ("+
                Playlists.PLAYLIST_ID+" TEXT PRIMARY KEY NOT NULL,"+
                Playlists.PLAYLIST_ART_URL+" TEXT NOT NULL,"+
                Playlists.PLAYLIST_DURATION+" TEXT, "+
                Playlists.PLAYLIST_RELEASE_DATE+" TEXT,"+
                Playlists.PLAYLIST_TITLE+" TEXT NOT NULL,"+
                Playlists.PLAYLIST_DESCRIPTION+" TEXT,"+
                Playlists.PLAYLIST_TRACK_COUNT+" INTEGER NOT NULL,"+
                Playlists.PLAYLIST_GENRES+" TEXT,"+
                Playlists.PLAYLIST_TAGS+" TEXT,"+
                Playlists.PLAYLIST_USER_ID+" TEXT "+References.USER+","+
                " UNIQUE (" + Playlists.PLAYLIST_ID + ") ON CONFLICT REPLACE)");

        db.execSQL("CREATE TABLE "+Tables.USERS+" ("+
                Users.USER_ID+" TEXT PRIMARY KEY NOT NULL,"+
                Users.USER_ART_URL+" TEXT NOT NULL,"+
                Users.USER_NICKNAME+" TEXT NOT NULL,"+
                Users.USER_FULLNAME+" TEXT,"+
                Users.USER_DESCRIPTION+" TEXT,"+
                Users.USER_FOLLOWINGS_COUNT+" INTEGER,"+
                Users.USER_FOLLOWER_COUNT+" INTEGER,"+
                Users.USER_TRACKS_COUNT+" INTEGER,"+
                Users.USER_LIKED_TRACKS_COUNT+" INTEGER,"+
                Users.USER_PLAYLISTS_COUNT+" INTEGER,"+
                Users.USER_IS_FOLLOWED+" INTEGER,"+
                " UNIQUE (" + Users.USER_ID + ") ON CONFLICT REPLACE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Tables.PLAYLISTS);
        db.execSQL("DROP TABLE IF EXISTS "+Tables.TRACKS);
        db.execSQL("DROP TABLE IF EXISTS "+Tables.USERS);
        onCreate(db);
    }

    static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}