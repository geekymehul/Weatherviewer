package com.developinggeek.weatherviewer;

/**
 * Created by DELL-PC on 12/29/2016.
 */
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeatherArrayAdapter extends ArrayAdapter<Weather> {

    private static class ViewHolder{
        ImageView conditionImageView;
        TextView dayTextView;
        TextView hiTextview;
        TextView lowTextView;
        TextView humidityTextView;
    }

    private Map<String, Bitmap> bitmaps = new HashMap<>();

    public WeatherArrayAdapter(Context context, List<Weather> forecast) {
        super(context, -1, forecast);
    }

    @Override
    public View getView(int position , View convertview , ViewGroup parent){
        Weather day = getItem(position);

        ViewHolder viewHolder;

        if(convertview==null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertview = inflater.inflate(R.layout.list_item,parent,false);
            viewHolder.conditionImageView=(ImageView)convertview.findViewById(R.id.conditionImageview);
            viewHolder.dayTextView=(TextView)convertview.findViewById(R.id.dayTextView);
            viewHolder.hiTextview=(TextView)convertview.findViewById(R.id.hitextView);
            viewHolder.lowTextView=(TextView)convertview.findViewById(R.id.lowtextView);
            viewHolder.humidityTextView=(TextView)convertview.findViewById(R.id.humiditytextView);
            convertview.setTag(viewHolder);
        }

        else
        {
            viewHolder = (ViewHolder)convertview.getTag();
        }

        if(bitmaps.containsKey(day.iconURL)){
            viewHolder.conditionImageView.setImageBitmap(bitmaps.get(day.iconURL));
        }

        else{
            new LoadImageTask(viewHolder.conditionImageView).execute(day.iconURL);
        }

        Context context = getContext();
        viewHolder.dayTextView.setText(context.getString(R.string.day_description,day.dayofweek,day.description));
        viewHolder.lowTextView.setText(context.getString(R.string.low_temp,day.mintemp));
        viewHolder.hiTextview.setText(context.getString(R.string.high_temp,day.maxtemp));
        viewHolder.humidityTextView.setText(context.getString(R.string.humidity,day.humidity));

        return  convertview;
    }

    private class LoadImageTask extends AsyncTask<String,Void,Bitmap> {
        private ImageView imageView;
        public LoadImageTask(ImageView imageView)
        {
            this.imageView=imageView;
        }
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap=null;
            HttpURLConnection connection=null;
            try {
                URL url = new URL(params[0]);
                connection=(HttpURLConnection) url.openConnection();
                try(InputStream inputStream =connection.getInputStream()){
                    bitmap= BitmapFactory.decodeStream(inputStream);
                    bitmaps.put(params[0],bitmap);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                connection.disconnect();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
