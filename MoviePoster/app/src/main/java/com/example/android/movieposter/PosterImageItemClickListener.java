package com.example.android.movieposter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * This is post image click listener.
 */
public class PosterImageItemClickListener implements AdapterView.OnItemClickListener {

	private static final String LOG_TAG = PosterImageItemClickListener.class.getSimpleName();

	private final GridView mGridView;
	private final Context mContext;

	public PosterImageItemClickListener(Context context, GridView gridView) {
		this.mContext = context;
		this.mGridView = gridView;
	}

	/**
	 * Poster image item click listener.
	 *
	 * @param parent - parent view
	 * @param view - a view
	 * @param position - item's position
	 * @param id - id
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final PosterImage posterImage = (PosterImage) mGridView.getItemAtPosition(position);
		FetchMovieDetailTask fetchMovieDetailTask = new FetchMovieDetailTask(mContext);
		fetchMovieDetailTask.execute(posterImage.getMovieId());
		fetchMovieDetailTask.setFetchMovieDetailTaskListener(
			new FetchMovieDetailTask.FetchMovieDetailTaskListener() {
				@Override
				public void setPosterImageDetail(PosterImageDetail posterImageDetail) {
					if (null != posterImageDetail) {
						posterImage.setRunTime(posterImageDetail.getRunTime());
						posterImage.setPosterTrailers(posterImageDetail.getPosterTrailers());
						startPosterImageDetailActivity(posterImage);
					} else {
						Log.i(LOG_TAG, "Poster detail is empty");
					}
				}
			});
	}

	/**
	 * Start poster image detail activity
	 *
	 * @param posterImage
	 */
	private void startPosterImageDetailActivity(PosterImage posterImage) {
		Intent intent = new Intent(mContext, PosterImageItemDetailActivity.class);
		intent.putExtra(Constants.EXTRACT_TEXT, posterImage);
		mContext.startActivity(intent);
	}

}

