package com.example.android.movieposter;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This is Fetch movie post task.
 */
public class FetchMoviePosterTask extends AsyncTask<Integer, Void, List<PosterImage>> {

	private static final String LOG_TAG = FetchMoviePosterTask.class.getSimpleName();

	private final Context mContext;
	private final PosterImageAdapter mPosterImageAdapter;
	private final ProgressBar mProgressBar;
	private OnLoadMoreListener mOnLoadMoreListener;

	/**
	 * A constructor with fields
	 *
	 * @param context            - activity context
	 * @param posterImageAdapter - post image array adapter
	 * @param view - view passed in from activity fragment
	 */
	public FetchMoviePosterTask(Context context, PosterImageAdapter posterImageAdapter, View view) {
		this.mContext = context;
		this.mPosterImageAdapter = posterImageAdapter;
		this.mProgressBar = (ProgressBar)view.findViewById(R.id.poster_images_progressbar);
	}

	/**
	 * Set on load more listener.
	 *
	 * @param onLoadMoreListener - on load more listener
	 */
	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
		this.mOnLoadMoreListener = onLoadMoreListener;
	}

	/**
	 * On pre execute
	 */
	@Override
	protected void onPreExecute() {
		mProgressBar.setIndeterminate(true);
		mProgressBar.setVisibility(View.VISIBLE);
	}

	/**
	 * https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=f1e1a774094f3fa69542ccd5fff19e51
	 *
	 * @param params - parameters
	 * @return - a list of post image JSON data
	 */
	@Override
	protected List<PosterImage> doInBackground(Integer... params) {
		final int FIRST_PAGE = 1;
		List<PosterImage> parseData;
		if (null != params && params.length > 0) {
			int pageIndex = params[0] > 0 ? params[0] : FIRST_PAGE;
			parseData = getPosterData(pageIndex);
		} else {
			parseData = getPosterData(FIRST_PAGE);
		}
		return parseData;
	}

	/**
	 * Put images in grid view.
	 *
	 * @param posterImages - list of poster images
	 */
	@Override
	protected void onPostExecute(List<PosterImage> posterImages) {
		mProgressBar.setVisibility(View.GONE);
		if (null == posterImages || posterImages.isEmpty()) {
			Toast.makeText(mContext, "Unable to obtain data", Toast.LENGTH_SHORT).show();
		} else {
			mPosterImageAdapter.addAll(posterImages);
			mPosterImageAdapter.notifyDataSetChanged();
			if (mOnLoadMoreListener != null) {
				mOnLoadMoreListener.loadMore();
			}
		}
	}

	/**
	 * @return Post JSON string
	 */
	private String getContent(Integer pageIndex) {

		String responseString = null;
		HttpURLConnection httpURLConnection = null;
		BufferedReader bufferedReader = null;

		final String APIKEY = "f1e1a774094f3fa69542ccd5fff19e51";
		final String BASEURL = "api.themoviedb.org";
		final String SORT_PARAM = "sort_by";
		final String SORT = "popularity.desc";
		final String APIKEY_PARAM = "api_key";
		final String PAGE_PARAM = "page";
		//final String PAGE = "1";
		final String PAGE = String.valueOf(pageIndex);
		final String HTTP_GET = "GET";

		Uri.Builder uriBuilder = new Uri.Builder();
		uriBuilder.scheme("https")
			.authority(BASEURL)
			.appendPath("3")
			.appendPath("discover")
			.appendPath("movie")
			.appendQueryParameter(SORT_PARAM, SORT)
			.appendQueryParameter(PAGE_PARAM, PAGE)
			.appendQueryParameter(APIKEY_PARAM, APIKEY)
			.build();

		Log.i(LOG_TAG, uriBuilder.toString());

		try {
			URL url = new URL(uriBuilder.toString());
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod(HTTP_GET);
			httpURLConnection.connect();

			InputStream inputStream = httpURLConnection.getInputStream();
			StringBuilder sb = new StringBuilder();

			if (null != inputStream) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
				}
				if (sb.length() > 0) {
					responseString = sb.toString();
				}
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error in reading ", e);
		} finally {
			if (null != httpURLConnection) {
				httpURLConnection.disconnect();
			}
			if (null != bufferedReader) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					Log.e(LOG_TAG, "Error in closing ", e);
				}
			}
		}
		return responseString;
	}

	/**
	 * @param page - a page number
	 * @return List<PosterImage> post images
	 */
	@Nullable
	private List<PosterImage> getPosterData(Integer page) {
		try {
			String data = getContent(page);
			if (null != data) {
				List<PosterImage> parseData = parseData(data);
				return parseData;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error in reading ", e);
		}
		return null;
	}

	/**
	 * @param content JSON content
	 * @return List<PosterImage> a list of data or empty
	 * @throws JSONException
	 */
	private List<PosterImage> parseData(String content) throws JSONException {
		final String RESULTS = "results";
		final String ID = "id";
		final String POSTER_PATH = "poster_path";
		final String TITLE = "title";
		final String RELASE_DATE = "release_date";
		final String OVERVIEW = "overview";
		final String POPULARITY = "popularity";
		final String VOTE_AVERAGE = "vote_average";

		JSONObject jsonObject = new JSONObject(content);
		JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);

		List<PosterImage> posterImages = new ArrayList<PosterImage>();

		for (int index = 0; index < jsonArray.length(); index++) {
			JSONObject inner = jsonArray.getJSONObject(index);
			String title = inner.getString(TITLE);
			String path = inner.getString(POSTER_PATH);
			String id = inner.getString(ID);
			String releaseDate = inner.getString(RELASE_DATE);
			String overview = inner.getString(OVERVIEW);
			double popularity = inner.getDouble(POPULARITY);
			double voteAverage = inner.getDouble(VOTE_AVERAGE);

			PosterImage posterImage = new PosterImage(path, title);
			posterImage.setMovieId(id);
			posterImage.setOverview(overview);
			posterImage.setReleaseDate(releaseDate);
			posterImage.setPopularity(popularity);
			posterImage.setVoteAverage(voteAverage);

			posterImages.add(posterImage);
		}
		return posterImages;
	}
}