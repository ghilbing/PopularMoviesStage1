package com.example.admin.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.UUID;
import com.example.admin.popularmovies.data.MoviesContract.MoviesPopEntry;

/**
 * Created by Admin on 11/11/16.
 */
public class MoviesPop {

    private String id;
    private String title;
    private String poster;
    private String synopsis;
    private String rating;
    private String date;

    public MoviesPop(String title, String poster, String synopsis, String rating, String date){

        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.poster = poster;
        this.synopsis = synopsis;
        this.rating = rating;
        this.date = date;

    }

    public MoviesPop(Cursor cursor){
        id = cursor.getString(cursor.getColumnIndex(MoviesPopEntry.ID));
        title = cursor.getString(cursor.getColumnIndex(MoviesPopEntry.TITLE));
        poster = cursor.getString(cursor.getColumnIndex(MoviesPopEntry.POSTER));
        synopsis = cursor.getString(cursor.getColumnIndex(MoviesPopEntry.SYNOPSIS));
        rating = cursor.getString(cursor.getColumnIndex(MoviesPopEntry.RATING));
        date = cursor.getString(cursor.getColumnIndex(MoviesPopEntry.DATE));


    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(MoviesPopEntry.ID, id);
        values.put(MoviesPopEntry.TITLE, title);
        values.put(MoviesPopEntry.POSTER, poster);
        values.put(MoviesPopEntry.SYNOPSIS, synopsis);
        values.put(MoviesPopEntry.RATING, rating);
        values.put(MoviesPopEntry.DATE, date);
        return values;

    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getRating() {
        return rating;
    }

    public String getDate() {
        return date;
    }
}
