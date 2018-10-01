package com.example.android.movieposter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

/**
 * This is post image item detail fragment.
 */
public class PosterImageItemDetailFragment extends Fragment {

	private static final String LOG_TAG = PosterImageItemDetailFragment.class.getSimpleName();

//    private TextView mReviewTextView;
//    private ImageView mPosterImageView;
//    private TextView mTitle;
//    private TextView mReleaseDate;
//    private TextView mRuntime;
//    private TextView mTrailerInfo;

	public PosterImageItemDetailFragment() {
		super.setHasOptionsMenu(true);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.grid_item_detail_view, container, false);
		TextView mReviewTextView;
		ImageView mPosterImageView;
		TextView mTitle;
		TextView mReleaseDate;
		TextView mRuntime;
		TextView mTrailerInfo;
		RatingBar mRatingBar;

		mReviewTextView = (TextView) rootView.findViewById(R.id.review_text);
		mPosterImageView = (ImageView) rootView.findViewById(R.id.poster_image);
		mTitle = (TextView) rootView.findViewById(R.id.movie_title);
		mReleaseDate = (TextView) rootView.findViewById(R.id.movie_release_date);
		mRuntime = (TextView) rootView.findViewById(R.id.movie_runtime);
		mTrailerInfo = (TextView) rootView.findViewById(R.id.movie_trailer_info);
		mRatingBar = (RatingBar) rootView.findViewById(R.id.movie_vote_average);

		Intent intent = getActivity().getIntent();
		PosterImage posterImage = intent.getParcelableExtra(Constants.EXTRACT_TEXT);
		mReviewTextView.setText(posterImage.getOverview());
		Picasso.with(getContext()).load(posterImage.getPath()).into(mPosterImageView);
		mTitle.setText(posterImage.getTitle());
		mReleaseDate.setText(getReleaseYearOfDate(posterImage.getReleaseDate()));
		mRuntime.setText(getActivity().getString(R.string.movie_length, posterImage.getRunTime()));
		float rating = (float) posterImage.getVoteAverage() / 2;
		mRatingBar.setRating(rating);
		List<PosterTrailer> ts = posterImage.getPosterTrailers();
		if (ts.size() > 1) {
			mTrailerInfo.setText(getActivity().getString(R.string.movie_trailer,
				getActivity().getString(R.string.trailers)));
		} else {
			mTrailerInfo.setText(getActivity().getString(R.string.movie_trailer,
				getActivity().getString(R.string.trailer)));
		}
		addTrailer(rootView, inflater, container, ts);
		return rootView;
	}

	/**
	 * @param releaseDate
	 * @return year of release date
	 */
	private String getReleaseYearOfDate(String releaseDate) {
		String[] re = TextUtils.split(releaseDate, "-");
		return re[0];
	}

	/**
	 * Add trailer(s) to the root view.
	 *
	 * @param rootView
	 * @param inflater
	 * @param container
	 * @param ts
	 */
	private void addTrailer(View rootView, LayoutInflater inflater,
													ViewGroup container, List<PosterTrailer> ts) {
		LinearLayout trailers = (LinearLayout) rootView.findViewById(R.id.movie_trailer_listing);
		Iterator<PosterTrailer> trailersIterator = ts.iterator();
		PosterTrailer pt = trailersIterator.next();
		int index = 0;
		View item = inflater.inflate(R.layout.list_item_view, container, false);
		ImageView playImage = (ImageView) item.findViewById(R.id.image_player);
		playImage.setOnClickListener(
			new PosterTrailerClickListener(getActivity(), pt.getSource()));
		TextView name = (TextView) item.findViewById(R.id.movie_trailer_name);
		name.setText(pt.getName());
		trailers.addView(item, index++);
		while (trailersIterator.hasNext()) {
			pt = trailersIterator.next();
			View line = inflater.inflate(R.layout.horizontal_line, container, false);
			item = inflater.inflate(R.layout.list_item_view, container, false);
			playImage = (ImageView) item.findViewById(R.id.image_player);
			playImage.setOnClickListener(
				new PosterTrailerClickListener(getActivity(), pt.getSource()));
			name = (TextView) item.findViewById(R.id.movie_trailer_name);
			name.setText(pt.getName());
			trailers.addView(line, index++);
			trailers.addView(item, index++);
		}
	}
}
