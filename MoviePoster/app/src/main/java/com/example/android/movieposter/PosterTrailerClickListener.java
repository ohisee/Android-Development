package com.example.android.movieposter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

/**
 * This is poster trailer click listener.
 */
public class PosterTrailerClickListener implements View.OnClickListener {

	private static final String LOG_TAG = PosterTrailerClickListener.class.getSimpleName();

	private final Context mContext;
	private final String mSource;

	/**
	 * A constructor with field
	 *
	 * @param source - source of trailer ID
	 */
	public PosterTrailerClickListener(Context context, String source) {
		mContext = context;
		mSource = source;
	}

	/**
	 * To create an intent to play streaming media player.
	 *
	 * @param v - a view
	 */
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(Intent.ACTION_VIEW,
			Uri.parse("vnd.youtube:" + mSource));
		if (intent.resolveActivity(mContext.getPackageManager()) != null) {
			try {
				mContext.startActivity(intent);
			} catch (Exception e) {
				Log.i(LOG_TAG, "No client is found " + e.getMessage());
			}
		}
	}
}
