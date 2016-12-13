package com.example.admin.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //ArrayAdapter<String> mMoviesAdapter;

    static GridView gridViewMovies;
    static ArrayList<String> posterPopRat;
    static ArrayList<String> titles;
    static ArrayList<String> synopsis;
    static ArrayList<String> dates;
    static ArrayList<String> rating;
    static ArrayList<String> ids;
    static ArrayList<String> youtube1;
    static ArrayList<String> youtube2;
    static ArrayList<String> favorite;
    static ArrayList<ArrayList<String>> comments;

    static int ancho;
    static boolean byPop = true;
    static String API_KEY = "c0ce143817426b86ae199a8ede8d8775";

    static PreferenceChangeListener listener;
    static SharedPreferences sharedPreferences;
    static boolean byFav;
    static ArrayList<String> posterFav = new ArrayList<String>();
    static boolean byRat;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle action bar item clicks here. The action bar will
        //automatically handle clicks on the Home/Up button, so long
        //as you specify a parent activity in AndroidManifest.xml

        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchMoviesTask moviesTask = new FetchMoviesTask();
            moviesTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        if(MainActivity.TABLET){

            ancho = size.x/6;

        }
        else ancho=size.x/3;



        if (getActivity() != null) {

            ArrayList<String> arrayList = new ArrayList<String>();
            PosterAdapter posterAdapter = new PosterAdapter(getActivity(), arrayList, ancho);
            gridViewMovies = (GridView) rootView.findViewById(R.id.gridView_movies);

            gridViewMovies.setColumnWidth(ancho);
            gridViewMovies.setAdapter(posterAdapter);



        }

        gridViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("titles", titles.get(position))
                        .putExtra("poster", posterPopRat.get(position))
                        .putExtra("synopsis", synopsis.get(position))
                        .putExtra("dates", dates.get(position))
                        .putExtra("rating", rating.get(position))
                        .putExtra("youtube1", youtube1.get(position))
                        .putExtra("youtube2", youtube2.get(position))
                        .putExtra("comments", comments.get(position))
                        .putExtra("favorite", favorite.get(position));

                startActivity(intent);

            }
        });


        return rootView;

    }


    private class PreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            gridViewMovies.setAdapter(null);
            onStart();

        }
    }

    @Override
    public void onStart()

    {

        super.onStart();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        listener = new PreferenceChangeListener();
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

        if (sharedPreferences.getString("sortby", "popularity").equals("popularity"))

        {

            getActivity().setTitle("Popular Movies");
            byPop = true;
            byFav = false;

        } else if (sharedPreferences.getString("sortby", "popularity").equals("rating")) {

            getActivity().setTitle("Rating");
            byPop = false;
            byFav = false;

        } else if (sharedPreferences.getString("sortby", "popularity").equals("favorites"))

        {

            getActivity().setTitle("Favorites");
            byPop = false;
            byFav = true;

        }

        TextView textView = new TextView(getActivity());
        FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.container);

        if (byFav)
        {

            if (posterFav.size() == 0)
            {

                textView.setText("You have no favorites");
                if (frameLayout.getChildCount() == 1)

                    frameLayout.addView(textView);
                    gridViewMovies.setVisibility(GridView.GONE);

                } else {

                    gridViewMovies.setVisibility(GridView.VISIBLE);
                    frameLayout.removeView(textView);

                }
                if (posterFav != null && getActivity() != null) {

                    PosterAdapter posterAdapter = new PosterAdapter(getActivity(), posterFav, ancho);
                    gridViewMovies.setAdapter(posterAdapter);

                }

            } else {

                gridViewMovies.setVisibility(GridView.VISIBLE);
                frameLayout.removeView(textView);

            }


            if (isInternetConnection()) {

                //gridViewMovies.setVisibility(GridView.VISIBLE);
                new FetchMoviesTask().execute();
                textView.setText("");

            } else {

                TextView textView1 = new TextView(getActivity());
                FrameLayout frameLayout1 = (FrameLayout) getActivity().findViewById(R.id.container);
                textView1.setText("No internet connection");

                if (frameLayout1.getChildCount() == 1) {

                    frameLayout1.addView(textView1);

                }

                gridViewMovies.setVisibility(GridView.GONE);

            }


        }



    public boolean isInternetConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();

    }

    public class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<String>> {


        @Override
        protected ArrayList<String> doInBackground(Void... params) {

           //while (true) {

                try {

                    posterPopRat = new ArrayList(Arrays.asList(getPathsAPI(byPop)));
                    return posterPopRat;

                } catch (Exception e) {

                //   continue;

                }


          //  }

            return posterPopRat;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {

            if (result != null && getActivity() != null) {

                PosterAdapter adapter = new PosterAdapter(getActivity(), result, ancho);
                gridViewMovies.setAdapter(adapter);

            }
        }

        public String[] getPathsAPI(boolean byPop) {

        //    while (true) {

                HttpURLConnection urlConnection = null;
                BufferedReader bufferedReader = null;
                String JSONResultString;


                try {
                    String urlString = null;
                    if (byPop) {
                        urlString = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + API_KEY;

                    } else {

                        urlString = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=" + API_KEY;

                    }

                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();

                    if (inputStream == null) {

                        return null;

                    }

                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {

                        buffer.append(line + "\n");

                    }

                    if (buffer.length() == 0) {

                        return null;

                    }

                    JSONResultString = buffer.toString();

                    try {

                        titles = new ArrayList<String>(Arrays.asList(getStringsFromJSON(JSONResultString, "original_title")));
                        synopsis = new ArrayList<String>(Arrays.asList(getStringsFromJSON(JSONResultString, "overview")));
                        dates = new ArrayList<String>(Arrays.asList(getStringsFromJSON(JSONResultString, "release_date")));
                        rating = new ArrayList<String>(Arrays.asList(getStringsFromJSON(JSONResultString, "vote_average")));
                        ids = new ArrayList<String>(Arrays.asList(getStringsFromJSON(JSONResultString, "id")));

                      //  while(true) {

                            youtube1 = new ArrayList<String>(Arrays.asList(getYoutubeFromId(ids, 0)));
                            youtube2 = new ArrayList<String>(Arrays.asList(getYoutubeFromId(ids, 1)));

                            int nCount = 0;

                            for (int i = 0; i < youtube1.size(); i++) {

                                if (youtube1.get(i) == null) {

                                    nCount++;
                                    youtube1.set(i, "no video");
                                }
                            }

                            for (int i = 0; i < youtube2.size(); i++) {

                                if (youtube2.get(i) == null) {

                                    nCount++;
                                    youtube2.set(i, "no video");
                                }
                            }

                            if (nCount > 2) //continue;
                           // break;

               //         }

                        comments = getCommentsFromId(ids);
                        return getPathsJSON(JSONResultString);

                    } catch (JSONException e) {

                        return null;

                    }


                } catch (Exception e) {

                 //   continue;

                } finally {
                    if (urlConnection != null) {

                        urlConnection.disconnect();

                    }

                    if (bufferedReader != null) {

                        try {
                            bufferedReader.close();

                        } catch (final IOException e) {


                        }
                    }
                }
          //  }

            return null;


        }

        public String[] getYoutubeFromId(ArrayList<String> ids, int position)
        {

            String[] results = new String [ids.size()];

                for (int i=0; i<ids.size(); i++)
                {

                    HttpURLConnection urlConnection = null;
                    BufferedReader reader = null;
                    String JSONResultString;

                    try {

                        String urlString = null;
                        urlString = "http://api.themoviedb.org/3/movie/" + ids.get(i) + "/videos?api_key" + API_KEY;

                        URL url = new URL(urlString);
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.connect();

                        InputStream inputStream = urlConnection.getInputStream();
                        StringBuffer buffer = new StringBuffer();
                        if (inputStream == null)
                        {
                            return null;
                        }

                        reader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;

                        while ((line = reader.readLine())!=null)
                        {
                            buffer.append(line + "\n");
                        }

                        if (buffer.length()==0)
                        {
                            return null;

                        }

                        JSONResultString = buffer.toString();

                        try {

                            results[i] = getYoutubeFromJSON(JSONResultString, position);

                        }catch (JSONException e){

                            results[i]="no video";

                        }

                    } catch (Exception e){


                    } finally {

                        if(urlConnection != null) {

                            urlConnection.disconnect();

                        }

                        if (reader != null) {

                            try {

                                reader.close();

                            }catch (final IOException e){

                            }
                        }

                    }

                }

            return results;

        }

        public ArrayList<ArrayList<String>> getCommentsFromId(ArrayList<String> ids)
        {

         //   loop:
         //   while (true)
            {
                ArrayList<ArrayList<String>> results = new ArrayList<>();
                for (int i = 0; i<ids.size();i++)
                {
                    HttpURLConnection urlConnection = null;
                    BufferedReader reader = null;
                    String JSONResultString;

                    try
                    {
                        String urlString = null;
                        urlString = "http://api.themovie.org/3/movie/" + ids.get(i) + "/reviews?api_key=" + API_KEY;
                        URL url = new URL(urlString);
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.connect();

                        InputStream inputStream = urlConnection.getInputStream();
                        StringBuffer buffer = new StringBuffer();
                        if (inputStream == null)
                        {
                            return null;

                        }

                        reader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;

                        while ((line = reader.readLine())!=null)
                        {
                            buffer.append(line + "\n");

                        }
                        if(buffer.length() == 0)
                        {
                            return null;

                        }
                        JSONResultString = buffer.toString();

                        try{
                            results.add(getCommentsFromJSON(JSONResultString));
                        }catch (JSONException e){

                            return null;
                        }



                    }catch (Exception e){

                      //  continue loop;

                    }finally {

                        if(urlConnection != null){

                            urlConnection.disconnect();

                        }

                        if (reader != null){

                            try{
                                reader.close();

                            }catch (final IOException e){

                            }

                        }

                    }


                }

                return results;

            }

        }

        public ArrayList<String> getCommentsFromJSON(String JSONStringParam) throws JSONException {

            JSONObject jsonObject = new JSONObject(JSONStringParam);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            ArrayList<String> results = new ArrayList<>();

            if(jsonArray.length()==0)
            {
                results.add("No reviews");
                return results;

            }

            for (int i = 0; i<jsonArray.length(); i++)
            {

                JSONObject result = jsonArray.getJSONObject(i);
                results.add(result.getString("content"));

            }

            return results;


        }

        public String getYoutubeFromJSON(String JSONStringParam, int position) throws JSONException
        {
            JSONObject jsonObject = new JSONObject(JSONStringParam);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            JSONObject video;

            String result = "no videos";

            if(position==0)
            {
                video = jsonArray.getJSONObject(0);
                result = video.getString("key");

            }

            else if(position==1)
            {
                if(jsonArray.length()>1)
                {
                    video = jsonArray.getJSONObject(1);

                }
                else {

                    video = jsonArray.getJSONObject(0);

                }

                result = video.getString("key");

            }

            return result;


        }





        public String[] getStringsFromJSON(String JSONStringParam, String param) throws JSONException{

            JSONObject jsonObject = new JSONObject(JSONStringParam);

            JSONArray jsonArray = jsonObject.getJSONArray("results");
            String[] result = new String[jsonArray.length()];

            for(int i = 0; i<jsonArray.length();i++){

                JSONObject jsonMovie = jsonArray.getJSONObject(i);
                if(param.equals("vote_average")){

                    Double vote = jsonMovie.getDouble("vote_average");
                    String rating = Double.toString(vote)+"/10";
                    result[i]=rating;

                }
                else {

                    String data = jsonMovie.getString(param);
                    result[i] = data;

                }

            }

            return result;

        }

        public String[] getPathsJSON(String JSONStringParam) throws JSONException {

            JSONObject jsonObject = new JSONObject(JSONStringParam);

            JSONArray jsonArray = jsonObject.getJSONArray("results");
            String[] result = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject posterMovie = jsonArray.getJSONObject(i);
                String posterPath = posterMovie.getString("poster_path");
                result[i] = posterPath;

            }

            return result;


        }


    }

}











