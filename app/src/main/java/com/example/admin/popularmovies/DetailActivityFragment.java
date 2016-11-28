package com.example.admin.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Admin on 11/16/16.
 */
public class DetailActivityFragment extends Fragment {

    public static String title;
    public static String poster;
    public static String synopsis;
    public static String date;
    public static String rating;

    private ShareActionProvider shareActionProvider;

    public DetailActivityFragment(){

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        getActivity().setTitle("Movie Details");

        if(intent != null && intent.hasExtra("poster")){

            poster = intent.getStringExtra("poster");
            ImageView imageView = (ImageView) rootView.findViewById(R.id.poster);
            Picasso.with(getActivity()).load("http://image.tmdb.org/tp/w185/" + poster).resize(MainFragment.ancho, (int)(MainFragment.ancho*1.5)).into(imageView);


        }

        if(intent != null && intent.hasExtra("title")){

            title = intent.getStringExtra("title");
            TextView textView = (TextView) rootView.findViewById(R.id.title);
            textView.setText(title);

        }

        if(intent != null && intent.hasExtra("synopsis")){

            synopsis = intent.getStringExtra("synopsis");
            TextView textView = (TextView) rootView.findViewById(R.id.rating);
            textView.setText(synopsis);

        }

        if(intent != null && intent.hasExtra("rating")){

            rating = intent.getStringExtra("rating");
            TextView textView = (TextView) rootView.findViewById(R.id.rating);
            textView.setText(rating);

        }

        if(intent != null && intent.hasExtra("date")){

            date = intent.getStringExtra("date");
            TextView textView = (TextView) rootView.findViewById(R.id.date);
            textView.setText(date);

        }


        return rootView;

    }



}
