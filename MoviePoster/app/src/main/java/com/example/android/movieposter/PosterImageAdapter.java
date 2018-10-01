package com.example.android.movieposter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * This is custom poster image adapter.
 */
public class PosterImageAdapter extends ArrayAdapter<PosterImage> {

	private static final String LOG_TAG = PosterImageAdapter.class.getSimpleName();

	/**
	 * Poster image adapter constructor
	 *
	 * @param context      - activity context
	 * @param resource     - resource
	 * @param posterImages - list of poster images
	 */
	public PosterImageAdapter(Context context, int resource, List<PosterImage> posterImages) {
		super(context, resource, posterImages);
	}

	/**
	 * Get image view into view.
	 *
	 * @param position    - position in view
	 * @param convertView - view
	 * @param parent      - parent
	 * @return view - view
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_view, parent, false);
		}
		PosterImage posterImage = getItem(position);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.image_picture_view);
		Picasso.with(getContext()).load(posterImage.getPath()).into(imageView);
		return convertView;
	}
}