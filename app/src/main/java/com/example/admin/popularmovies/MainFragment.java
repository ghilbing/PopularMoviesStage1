package com.example.admin.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.List;


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

    ArrayAdapter<String> mMoviesAdapter;

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.moviesfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Handle action bar item clicks here. The action bar will
        //automatically handle clicks on the Home/Up button, so long
        //as you specify a parent activity in AndroidManifest.xml

        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchMoviesTask moviesTask = new FetchMoviesTask();
            moviesTask.execute("1000");
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String[] data = {

                "Spiderman",
                "Batman returns",
                "Heroes",
                "Cinderela",
                "Walle",
                "Finding Dory",
                "Maleficent"
        };

        List<String> weekMovies = new ArrayList<String>(Arrays.asList(data));

        //Now we have some dummy movies data, create an ArrayAdapter.
        //The ArrayAdapter will take data froma source (like our dummy movies) and
        //use it to populate the ListView it's attached //


        mMoviesAdapter =
                new ArrayAdapter<String>(
                        getActivity(), //The current context (this activity)
                        R.layout.list_item_movies, //The name of the layout ID.
                        R.id.list_item_movies_textview, //The ID of the textview to populate
                        weekMovies);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_movies);
        listView.setAdapter(mMoviesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String movie = mMoviesAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, movie);
                startActivity(intent);
            }
        });

        return rootView;

    }

        //These two need to be declared outside the try/catch
        //so that they can be closed in the finally block

    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        /*Take the String representing the complete Movies in JSON Format, and
        pull out the data we need to construct the Strings needed for the wireframes.

        Fortunately parsing is easy: constructor takes the JSON string and converts it
        into an Object hierarchy for us.
         */

        private String[] getMoviesDataFromJson(String moviesJsonStr, int cant) throws JSONException{

            //These are the names of the JSON objects that need to be extracted.
            final String TMDB_PAGES = "page";
            final String TMDB_TOTAL_PAGES = "total_pages";
            final String TMDB_RESULTS = "results";
            final String TMDB_ID = "id";
            final String TMDB_ORIGINAL_TITLE = "original_title";
            final String TMDB_POSTER_PATH = "poster_path";
            final String TMDB_SYNOPSIS = "overview";
            final String TMDB_USER_RATING = "vote_count";
            final String TMDB_RELEASE_DATE = "release_date";

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(TMDB_RESULTS);

            //TMDB returns daily movies

            String[] resultStrs = new String[cant];
            for (int i = 0; i< moviesArray.length(); i++){

                String title;
                String poster;
                String synopsis;
                String rating;
                String date;

                //Get the JSON object representing the movie
                JSONObject movie = moviesArray.getJSONObject(i);

                //Title is in a child array called results, which is 1 element long

                title = movie.getString(TMDB_ORIGINAL_TITLE);

                poster = movie.getString(TMDB_POSTER_PATH);

                synopsis = movie.getString(TMDB_SYNOPSIS);

                rating = movie.getString(TMDB_USER_RATING);

                date = movie.getString(TMDB_RELEASE_DATE);


                resultStrs[i]= title + " - "+ poster + " - " + synopsis + " - " + rating + " - " + date;

            }
            for (String s: resultStrs){
                Log.i(LOG_TAG, "Movie entry: " + s);
            }

            return resultStrs;

        }

    @Override
    protected String[] doInBackground(String... params) {

        //If there's no movie, there's nothing to look up. Verify size of params.

        if (params.length == 0){
            return null;

        }




        //These two need to be declared outside the try/catch
        //so that they can be closed in the finally block


        HttpURLConnection urlConnectionPop = null;

        BufferedReader readerPop = null;


        //Will contain the raw JSON response as a string

        String moviesPopJsonStr = null;



        int cant = 20;



        try

        {
            //Construct the URLs for the MoviesDb querys
            //Possible parameters ara available at Movies API page at
            //POPULARITY
            //http://api.themoviedb.org/3/discover/movie?api_key=c0ce143817426b86ae199a8ede8d8775&sort_by=popularity.desc
            //RATED
            //http://api.themoviedb.org/3/discover/movie?api_key=c0ce143817426b86ae199a8ede8d8775&sort_by=vote_average.desc


            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("api_key", "c0ce143817426b86ae199a8ede8d8775")
                    .appendQueryParameter("sort_by", "popularity.desc");

            String myUrlPop = builder.build().toString();

            Log.i("Builder Uri", myUrlPop);


            String baseUrl = "http://api.themoviedb.org/3/discover/movie?api_key=c0ce143817426b86ae199a8ede8d8775";
            //String apiKey = "api_key=c0ce143817426b86ae199a8ede8d8775";
            String sortPop = "&sort_by=popularity.desc";


            URL urlPop = new URL(baseUrl.concat(sortPop));
            String verUrlPop = urlPop.toString();
            Log.i("urPopular", verUrlPop);


            //Create the request to MovieDB and open the connection

            urlConnectionPop = (HttpURLConnection) urlPop.openConnection();
            urlConnectionPop.setRequestMethod("GET");
            urlConnectionPop.connect();

            //Read the input stream into a String

            InputStream inputStreamPop = urlConnectionPop.getInputStream();

            StringBuffer bufferPop = new StringBuffer();


            if (inputStreamPop == null) {
                //Nothing to do
                return null;

            }

            readerPop = new BufferedReader(new InputStreamReader(inputStreamPop));


            String linePop;


            while ((linePop = readerPop.readLine()) != null) {
                //Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                //But it does make debugging a "lot" easier if you print out the completed
                //buffer for debugging

                bufferPop.append(linePop + "\n");

            }

            if (bufferPop.length() == 0) {
                //Stream was empty. No point in parsing
                return null;
            }

            moviesPopJsonStr = bufferPop.toString();
            Log.v(LOG_TAG, "Movies JSON String: " + moviesPopJsonStr);



        } catch (IOException e) {
            Log.e("MainFragment", "Error", e);
            //If the code didn't successfully get the movie data, there's no point attemping to parse it
            return null;


        } finally {
            if (urlConnectionPop != null)
                urlConnectionPop.disconnect();

        }

        if (readerPop != null) {
            try {
                readerPop.close();

            } catch (final IOException e) {
                Log.e("MainFragment", "Error closing stream", e);
            }

        }

    try {
        return getMoviesDataFromJson(moviesPopJsonStr, cant);
    }catch (JSONException e){
        Log.i(LOG_TAG, e.getMessage(), e);
        e.printStackTrace();
    }

        return null;
}

        @Override
        protected void onPostExecute (String[] result){

            if (result != null){
                mMoviesAdapter.clear();
                for(String movieStr: result){
                    mMoviesAdapter.add(movieStr);
                }
                //New data is back from the server.
            }
        }

}

}
