package com.example.android.movieposter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;

import java.util.LinkedList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

	private final static String LOG_TAG = "Main_Gridview_Fragment";

	private int mCurCheckPosition = 0;

	/**
	 * Fragment on create state
	 *
	 * @param savedInstanceState - saved instance state
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * Fragment on save instance state
	 *
	 * @param outState - out state
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(Constants.CURRENT_CHOICE, mCurCheckPosition);
	}

	public MainActivityFragment() {
	}

	/**
	 * Fragment's on create view, using Async Task to get the content from internet.
	 *
	 * @param inflater           a inflater
	 * @param container          a container
	 * @param savedInstanceState a saved instance state
	 * @return a view
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		final GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
		//gridView.setAdapter(new PosterImageAdapter(getActivity(), 0, Arrays.asList(PosterImage.posterImages)));
		if (null != savedInstanceState) {
			mCurCheckPosition = savedInstanceState.getInt(Constants.CURRENT_CHOICE);
		}
		final List<PosterImage> posterImages = new LinkedList<>();
		final PosterImageAdapter posterImageAdapter =
			new PosterImageAdapter(getActivity(), 0, posterImages);
		gridView.setAdapter(posterImageAdapter);

		AnimationSet animationSet = new AnimationSet(true);
		Animation animation = AnimationUtils.makeInChildBottomAnimation(getActivity());
		animationSet.addAnimation(animation);
		final GridLayoutAnimationController gridLayoutAnimationController =
			new GridLayoutAnimationController(animationSet, 1.0f, 1.0f);
		gridLayoutAnimationController.setDirection(GridLayoutAnimationController.DIRECTION_TOP_TO_BOTTOM);
		gridView.setLayoutAnimation(gridLayoutAnimationController);

		gridView.setOnItemClickListener(new PosterImageItemClickListener(getActivity(), gridView));
		gridView.setOnScrollListener(new PosterImageOnScrollListener(getActivity()) {
			@Override
			public void loadMore(final int currentPage) {
				Log.i(LOG_TAG, String.format("Start loading page %s ", currentPage));
				FetchMoviePosterTask t = new FetchMoviePosterTask(getActivity(), posterImageAdapter, rootView);
				t.setOnLoadMoreListener(new OnLoadMoreListener() {
					@Override
					public void loadMore() {
						setLoaded(currentPage);
					}
				});
				t.execute(currentPage);
			}
		});
		return rootView;
	}

	/**
	 * Create Animation Set.
	 *
	 * @return AnimationSet
	 */
	private AnimationSet getAnimationSet() {
		AnimationSet set = new AnimationSet(true);
		Animation animation = new TranslateAnimation(
			Animation.RELATIVE_TO_SELF, 0.0f,
			Animation.RELATIVE_TO_SELF, 0.0f,
			Animation.RELATIVE_TO_SELF, -1.0f,
			Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(500);
		set.addAnimation(animation);
		return set;
	}

}
