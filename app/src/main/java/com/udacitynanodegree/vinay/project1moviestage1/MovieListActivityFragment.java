package com.udacitynanodegree.vinay.project1moviestage1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListActivityFragment extends Fragment {


    GridView gridview;
    static JSONObject jsonObject;
    static String orderPref,check_pref;
    SharedPreferences sharedPref;


    public MovieListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        gridview = (GridView) rootView.findViewById(R.id.gridview);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        check_pref = sharedPref.getString("pref_orderKey", "");


        updateDataFromNetwork();


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent detailActivity = new Intent(getActivity(), MovieDetailActivity.class).putExtra("position", position);
                startActivity(detailActivity);

            }
        });


        return rootView;
    }

    public void updateDataFromNetwork()
    {
        new DataFetchFromNetwork().execute();
    }

    public void setAdapter() {
        gridview.setAdapter(new MyAdapter(getActivity()));
    }


    @Override
    public void onPause() {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        check_pref = sharedPref.getString("pref_orderKey", "");
        super.onPause();
    }

    @Override
    public void onResume() {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(!check_pref.equals(sharedPref.getString("pref_orderKey", "")))
            updateDataFromNetwork();
        super.onResume();
    }


    public class DataFetchFromNetwork extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... params) {

                // These two need to be declared outside the try/catch
                // so that they can be closed in the finally block.
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                // Will contain the raw JSON response as a string.
                String forecastJsonStr = null;
                String uri,apiKey,order;

                uri = "http://api.themoviedb.org/3/discover/movie?";
                apiKey = "&api_key=" + "your moviedb api key";

                sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                orderPref = sharedPref.getString("pref_orderKey", "");

                order = orderPref;

                try {
                    // Construct the URL for the OpenWeatherMap query
                    // Possible parameters are available at OWM's forecast API page, at
                    // http://openweathermap.org/API#forecast

                    URL url = new URL(uri + order + apiKey);

                    // Create the request to OpenWeatherMap, and open the connection
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        forecastJsonStr = null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        forecastJsonStr = null;
                    }
                    forecastJsonStr = buffer.toString();

                    try {
                        jsonObject = new JSONObject(forecastJsonStr);
                        MyAdapter.myData = jsonObject;



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    Log.e("PlaceholderFragment", "Error ", e);
                    // If the code didn't successfully get the data, there's no point in attempting
                    // to parse it.
                    forecastJsonStr = null;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e("PlaceholderFragment", "Error closing stream", e);                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {


                if (MyAdapter.myData != null) {
                    MyAdapter myAdapter = new MyAdapter(getActivity());
                      setAdapter();
                      myAdapter.notifyDataSetChanged();

                } else
                    Toast.makeText(getContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();

                super.onPostExecute(aVoid);
            }

        }


    }
