package com.googel.tvShow.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.googel.tvShow.dao.TvShowDao;
import com.googel.tvShow.models.TvShows;

@Database(entities = TvShows.class, version = 1, exportSchema = false)
public abstract class TvShowDataBase extends RoomDatabase {
    private static TvShowDataBase tvShowDataBase;

    public static synchronized TvShowDataBase getTvShowDataBase( Context context ) {
        if (tvShowDataBase == null) {
            tvShowDataBase = Room.databaseBuilder
                    (context, TvShowDataBase.class, "tv_shows.db").build();
        }
        return tvShowDataBase;
    }

    public abstract TvShowDao tvShowDao();
}
