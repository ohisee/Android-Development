package com.example.android.movieposter;

import android.content.Context;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.GridLayoutAnimationController;
import android.widget.AbsListView;
import android.widget.Toast;

/**
 * This is poster image grid view on scroll listener.
 */
public abstract class PosterImageOnScrollListener implements AbsListView.OnScrollListener {

	private static final String LOG_TAG = PosterImageOnScrollListener.class.getSimpleName();

	private boolean loading = false;
	private int currentPage = 1;
	private int itemCount = 0;
	private final int MAX_PAGE = 3;
	private final int visibleThreshold = 3;
	private final Context mContext;

	public PosterImageOnScrollListener(Context context) {
		mContext = context;
	}

	/**
	 * @param view        - list view
	 * @param scrollState - scroll state
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	/**
	 * @param view             - list view
	 * @param firstVisibleItem - the index of the first visible cell (ignore if visibleItemCount == 0)
	 * @param visibleItemCount - the number of visible cells
	 * @param totalItemCount   - the number of items in the (list) adaptor
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		Log.i(LOG_TAG, "Listener on scroll");
		Log.i(LOG_TAG,
			String.format("loading %s, itemCount %s, currentPage %s, firstVisibleItem %s, visibleItemCount %s, totalItemCount %s",
				loading, itemCount, currentPage, firstVisibleItem, visibleItemCount, totalItemCount));
//        if (totalItemCount < itemCount) {
//            this.itemCount = totalItemCount;
//            if (totalItemCount == 0) {
//                this.loading = true;
//            }
//        }
//        if (loading && (totalItemCount > itemCount)) {
//            loading = false;
//            itemCount = totalItemCount;
//            currentPage = currentPage + 1;
//        }
		// (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)
		//Log.i(LOG_TAG, "Animation : " + mAnimation.isDone());
		if (!loading && ((totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold))) {
			Log.i(LOG_TAG,
				String.format("loading %s, visibleItemCount + visibleThreshold %s, totalItemCount %s",
					loading, visibleItemCount + visibleThreshold, totalItemCount));
			if (currentPage <= MAX_PAGE) {
				Log.i(LOG_TAG, "Listener to load more");
				loadMore(currentPage);
				currentPage = currentPage + 1;
			}
			loading = true;
			Log.i(LOG_TAG, String.format("Listener to load more %s", loading));
		}
	}

	/**
	 * Decide whether to proceed calling task.
	 *
	 * @param firstVisibleItem - the index of the first visible cell (ignore if visibleItemCount == 0)
	 * @param visibleItemCount - the number of visible cells
	 * @param totalItemCount   - the number of items in the (list) adaptor
	 * @return true to proceed loading
	 */
	private boolean proceedLoading(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		boolean c = firstVisibleItem == 0 && visibleItemCount == 0 && totalItemCount == 0;
		boolean d = firstVisibleItem == 0 &&
			(visibleItemCount > 0 && totalItemCount > 0);
		return c;
	}

	/**
	 * Set loading to false.
	 */
	public void setLoaded(final int page) {
		Log.i(LOG_TAG, String.format("Loaded page %s ", page));
		Log.i(LOG_TAG, "Set loading to false");
		loading = false;
	}

	/**
	 * Load additional data.
	 */
	public abstract void loadMore(int currentPage);

}
