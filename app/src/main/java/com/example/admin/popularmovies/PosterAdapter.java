package com.example.admin.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin on 11/16/16.
 */
public class PosterAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<String> arrayList;
    private int ancho;

    public PosterAdapter(Context context, ArrayList<String> paths, int x){

        mContext = context;
        arrayList = paths;
        ancho = x+1;

    }



    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ImageView imageView;

        if(convertView == null){

            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);



        } else {

            imageView = (ImageView) convertView;


        }

        Drawable drawable = resizeDrawable(mContext.getResources().getDrawable(R.drawable.movies));


        //Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + arrayList.get(position)).into(imageView);
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + arrayList.get(position)).resize(ancho, (int)(ancho*1.5)).into(imageView);


        Log.i("Ancho", String.valueOf(ancho));

        return imageView;
    }

    private Drawable resizeDrawable(Drawable image){

        Bitmap bitmap = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, ancho, (int)(ancho*1.5), false);
        return new BitmapDrawable(mContext.getResources(),bitmap1);

    }



}
