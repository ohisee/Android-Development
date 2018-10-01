package com.example.android.movieposter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * This is image adapter. Not in use
 */
public class ImageAdapter extends BaseAdapter {

    private static final String LOG_TAG = ImageAdapter.class.getSimpleName();

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private ImagePath[] mImagePaths = {
            new ImagePath("jjBgi2r5cRt36xF6iNUEhzscEcb.jpg", "Jurassic World"),
            new ImagePath("5aGhaIHYuQbqlHWvWYqMCnj40y2.jpg", "The Martian"),
            new ImagePath("q0R4crx2SehcEEQEkYObktdeFy.jpg", "Minions"),
            new ImagePath("7SGGUiTE6oc2fh9MjIk5M00dsQd.jpg", "Ant-Man"),
            new ImagePath("5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", "Terminator Genisys"),
            new ImagePath("aAmfIX3TT40zUHGcCKrlOZRKC7u.jpg", "Inside Out"),
            new ImagePath("1n9D32o30XOHMdMWuIT4AaA5ruI.jpg", "Spectre"),
            new ImagePath("z2sJd1OvAGZLxgjBdSnQoLCfn3M.jpg", "Rogue Nation"),
            new ImagePath("g23cs30dCMiG4ldaoVNP1ucjs6.jpg", "Fantastic Four"),
            new ImagePath("qey0tdcOp9kCDdEZuJ87yE3crSe.jpg", "San Andreas"),
            new ImagePath("69Cz9VNQZy39fUE2g0Ggth6SBTM.jpg", "Tomorrowland"),
            new ImagePath("kqjL17yufvn9OVLyXYpvtyrFfak.jpg", "Fury Road"),
            new ImagePath("ktyVmIqfoaJ8w0gDSZyjhhOPpD6.jpg", "Pixels"),
            new ImagePath("t90Y3G8UGQp0f0DrP60wRu9gfrH.jpg", "Age of Ultron"),
            new ImagePath("nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg", "Interstellar"),
            new ImagePath("vlTPQANjLYTebzFJM1G4KeON0cb.jpg", "Maze Runner"),
            new ImagePath("l3Lb8UWmqfXY9kr9YhJXvnTvf4I.jpg", "The Hobbit"),
            new ImagePath("5ttOaThDVmTpV8iragbrhdfxEep.jpg", "The Man from U.N.C.L.E."),
            new ImagePath("z3nGs7UED9XlqUkgWeT4jQ80m1N.jpg", "Southpaw"),
            new ImagePath("y31QB9kn3XSudA15tV7UWQ9XLuW.jpg", "Guardians of the Galaxy")
    };

    /**
     * Image adapter constructor.
     *
     * @param context activity context
     */
    public ImageAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mImagePaths.length;
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
        ImageViewHolder imageViewHolder;
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.grid_item_view, parent, false);
            imageViewHolder = new ImageViewHolder();
            imageViewHolder.setImageView((ImageView) convertView.findViewById(R.id.image_picture_view));
            convertView.setTag(imageViewHolder);
        } else {
            imageViewHolder = (ImageViewHolder) convertView.getTag();
        }
        //Log.i(LOG_TAG, mImagePaths[position].getPath());
        Picasso.with(mContext)
                .load(mImagePaths[position].getPath())
                .into(imageViewHolder.getImageView());
        return convertView;
    }

    /**
     * This is Image Path.
     */
    private static class ImagePath {
        private String path;
        private String title;

        private final String BASE_URL = "http://image.tmdb.org/t/p/w185/";

        public ImagePath(String path, String title) {
            this.path = String.format("%s%s", BASE_URL, path);
            this.title = title;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}