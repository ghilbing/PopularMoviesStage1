package com.example.admin.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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
            //imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(getActivity()).load("http://image.tmdb.org/tp/w185/" + poster).into(imageView);
            Log.i("Poster", poster);


        }

        if(intent != null && intent.hasExtra("titles")){

            title = intent.getStringExtra("titles");
            TextView textView = (TextView) rootView.findViewById(R.id.title);
            textView.setText(title);

            Log.i("Title", title);

        }

        if(intent != null && intent.hasExtra("synopsis")){

            synopsis = intent.getStringExtra("synopsis");
            TextView textView = (TextView) rootView.findViewById(R.id.rating);
            textView.setText(synopsis);

            Log.i("Synopsis", synopsis);

        }

        if(intent != null && intent.hasExtra("rating")){

            rating = intent.getStringExtra("rating");
            TextView textView = (TextView) rootView.findViewById(R.id.rating);
            textView.setText(rating);

            Log.i("Rating", rating);

        }

        if(intent != null && intent.hasExtra("dates")){

            date = intent.getStringExtra("dates");
            TextView textView = (TextView) rootView.findViewById(R.id.date);
            textView.setText(date);

            Log.i("Date", date);

        }


        return rootView;

    }



}
