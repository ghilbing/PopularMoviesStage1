package com.example.admin.popularmovies;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
 * {@link MainFragment.OnFragmentInteractionListener} interface
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        //
        //
        // TODO: 10/20/16

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

        //These two need to be declared outside the try/catch
        //so that they can be closed in the finally block

        HttpURLConnection urlConnectionPop = null;
        HttpURLConnection urlConnectionRat = null;
        BufferedReader readerPop = null;
        BufferedReader readerRat = null;

        //Will contain the raw JSON response as a string

        String moviesPopularityJsonStr = null;
        String moviesRatedJsonStr = null;

        try {
            //Construct the URLs for the MoviesDb querys
            //Possible parameters ara available at Movies API page at
            //POPULARITY
            //http://api.themoviedb.org/3/discover/movie?api_key=c0ce143817426b86ae199a8ede8d8775&sort_by=popularity.desc
            //RATED
            //http://api.themoviedb.org/3/discover/movie?api_key=c0ce143817426b86ae199a8ede8d8775&sort_by=vote_average.desc

            String baseUrl = "http://api.themoviedb.org/3/discover/movie?";
            String apiKey = "api_key=c0ce143817426b86ae199a8ede8d8775";
            String sortPopular = "&sort_by=popularity.desc";
            String sortRated = "&sort_by=vote_average.desc";

            URL urlPopular = new URL(baseUrl.concat(apiKey).concat(sortPopular));
            URL urlRated = new URL (baseUrl.concat(apiKey).concat(sortRated));

            //Create the request to MovieDB and open the connection

            urlConnectionPop = (HttpURLConnection) urlPopular.openConnection();
            urlConnectionPop.setRequestMethod("GET");
            urlConnectionPop.connect();

            urlConnectionRat = (HttpURLConnection) urlRated.openConnection();
            urlConnectionRat.setRequestMethod("GET");
            urlConnectionRat.connect();

            //Read the input stream into a String

            InputStream inputStreamPop = urlConnectionPop.getInputStream();
            InputStream inputStreamRat = urlConnectionRat.getInputStream();

            StringBuffer bufferPop = new StringBuffer();
            StringBuffer bufferRat = new StringBuffer();

            if (inputStreamPop == null){
                //Nothing to do
                return null;

            }

            if (inputStreamRat == null){
                //Nothing to do
                return null;

            }

            readerPop = new BufferedReader(new InputStreamReader(inputStreamPop));
            readerRat = new BufferedReader(new InputStreamReader(inputStreamRat));

            String linePop;
            String lineRat;

            while ((linePop = readerPop.readLine())!=null){
                //Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                //But it does make debugging a "lot" easier if you print out the completed
                //buffer for debugging

                bufferPop.append(linePop + "\n");

            }

            while ((lineRat = readerRat.readLine())!=null){
                //Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                //But it does make debugging a "lot" easier if you print out the completed
                //buffer for debugging

                bufferRat.append(lineRat + "\n");
            }

            if (bufferPop.length() == 0){
                //Stream was empty. No point in parsing
                return null;
            }

            if (bufferRat.length() == 0){
                //Stream was empty. No point in parsing
                return null;
            }

            moviesPopularityJsonStr = bufferPop.toString();
            moviesRatedJsonStr = bufferRat.toString();




        }catch (IOException e){
            Log.e("MainFragment", "Error", e);
            //If the code didn't successfully get the movie data, there's no point attemping to parse it
            return null;


        }finally {
            if (urlConnectionPop!=null && urlConnectionRat!=null){
                urlConnectionPop.disconnect();
                urlConnectionRat.disconnect();
            }
            if (readerPop !=null && readerRat != null){
                try {
                    readerPop.close();
                    readerRat.close();
                }catch (final IOException e){
                    Log.e("MainFragment", "Error closing stream",e);
                }

            }
        }

        return rootView;

        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_main, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */


}
