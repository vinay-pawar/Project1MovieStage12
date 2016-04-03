package com.udacitynanodegree.vinay.project1moviestage1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by somesh on 04/01/16.
 */
public class MyAdapter extends BaseAdapter {
    private Context mContext;

    public static JSONObject myData;


    public MyAdapter(Context c) {
        mContext = c;

    }

    public int getCount() {

        int count = 0;

        try {
            if (myData != null)
                count = myData.getJSONArray("results").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (count >= 10)
            return 10;
        else
            return 0;

    }


    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

       {

           // gridView = new View(mContext);

            gridView = inflater.inflate(R.layout.movie_poster_view, null);


            ImageView imageViewNew = (ImageView) gridView
                    .findViewById(R.id.myImageView);

            String imagePath = null;

            try {
                JSONObject jsonObject = myData;
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                JSONObject jsonObject1 = jsonArray.getJSONObject(position);
                imagePath = jsonObject1.getString("poster_path");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + imagePath).into(imageViewNew);


        }

        return gridView;
    }


}
