package com.teamstartup.startup.drrobo;

/**
 * Created by Jinwei-PC on 1/10/2017.
 */

import android.widget.ImageView;
import 	android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import android.os.AsyncTask;
import android.widget.Toast;


public class GetUrlImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public GetUrlImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            //Toast.makeText(this, "Error in loading image",  Toast.LENGTH_SHORT).show();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}