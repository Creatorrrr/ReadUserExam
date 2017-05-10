package com.example.kosta.readuserexam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by kosta on 2017-05-10.
 */

public class UserAdapter extends BaseAdapter {
    private Context context;
    private List<User> users;
    private LayoutInflater inflater;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;

        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView no = (TextView)convertView.findViewById(R.id.no);
        TextView name = (TextView)convertView.findViewById(R.id.name);

        ImageView img = (ImageView)convertView.findViewById(R.id.img);

        no.setText(users.get(position).getNo() + "");
        name.setText(users.get(position).getName());

        new ImageLoadingTask(img).execute(users.get(position).getImgURL());

        return convertView;
    }

    private class ImageLoadingTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;

        public ImageLoadingTask(ImageView img) {
            imageViewReference = new WeakReference<ImageView>(img);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            URL url = null;

            try {
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return getRemoteImage(url);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageView = imageViewReference.get();
            if(imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private Bitmap getRemoteImage(final URL url) {
        Bitmap bitmap = null;

        URLConnection conn;

        try {
            conn = url.openConnection();
            conn.connect();

            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
