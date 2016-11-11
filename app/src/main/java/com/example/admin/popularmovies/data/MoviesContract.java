package com.example.admin.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the weather database
 */
public class MoviesContract {

   public static abstract class MoviesPopEntry implements BaseColumns {

       public static final String TABLE_NAME = "moviespop";

       public static final String ID = "id";
       public static final String TITLE = "title";
       public static final String POSTER = "poster_path";
       public static final String SYNOPSIS = "overview";
       public static final String RATING = "vote_count";
       public static final String DATE = "release_date";

   }

}
