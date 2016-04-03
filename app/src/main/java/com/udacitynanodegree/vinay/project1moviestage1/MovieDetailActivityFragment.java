package com.udacitynanodegree.vinay.project1moviestage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rooView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        ImageView moviePoster = (ImageView) rooView.findViewById(R.id.movieposter);
        TextView movieRating = (TextView) rooView.findViewById(R.id.rating);
        TextView movieReleaseDate = (TextView) rooView.findViewById(R.id.releasedate);
        TextView overView = (TextView) rooView.findViewById(R.id.overview);

        Intent receivedIntent = getActivity().getIntent();
        int position = receivedIntent.getIntExtra("position", 0);

        String title = null, imagePath = null, releaseDate = null, rating = null, overview = null;

        try {
            JSONObject jsonObject = MyAdapter.myData;
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            JSONObject jsonObject1 = jsonArray.getJSONObject(position);
            title = jsonObject1.getString("title");
            imagePath = jsonObject1.getString("poster_path");
            releaseDate = jsonObject1.getString("release_date");
            rating = jsonObject1.getString("vote_average");
            overview = jsonObject1.getString("overview");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w500/" + imagePath).into(moviePoster);

        getActivity().setTitle(title);

        movieRating.setText("Rating: " + rating + "/10");
        movieReleaseDate.setText("Release Date : " + releaseDate);
        overView.setText(overview);


        return rooView;
    }
}
