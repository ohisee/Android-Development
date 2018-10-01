package com.example.android.movieposter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * This is poster trailer adapter.
 */
public class PosterTrailerAdapter extends ArrayAdapter<PosterTrailer> {

	/**
	 * A constructor with fields
	 *
	 * @param context - activity context
	 * @param resource - resource
	 * @param objects - poster trailer object holding trailer data
	 */
	public PosterTrailerAdapter(Context context, int resource, List<PosterTrailer> objects) {
		super(context, resource, objects);
	}

	/**
	 * To populate list item
	 *
	 * @param position - position Id
	 * @param convertView - view
	 * @param parent - view group parent
	 * @return a view
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PosterTrailer posterTrailer = getItem(position);
		if (null == convertView) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_view, parent, false);
		}
		TextView trailNameView = (TextView) convertView.findViewById(R.id.movie_trailer_name);
		trailNameView.setText(posterTrailer.getName());
		return convertView;
	}
}
