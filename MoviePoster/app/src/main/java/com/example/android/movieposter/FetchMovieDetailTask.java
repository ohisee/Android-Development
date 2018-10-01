package com.example.android.movieposter;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
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
 * This is an async task to fetch details.
 */
public class FetchMovieDetailTask extends AsyncTask<String, Void, PosterImageDetail> {

	private static final String LOG_TAG = FetchMovieDetailTask.class.getSimpleName();

	private final Context mContext;
	private FetchMovieDetailTaskListener mFetchMovieDetailTaskListener;

	/**
	 * A constructor
	 *
	 * @param context
	 */
	public FetchMovieDetailTask(Context context) {
		this.mContext = context;
	}

	/**
	 * Callback interface
	 */
	public interface FetchMovieDetailTaskListener {
		public void setPosterImageDetail(PosterImageDetail posterImageDetail);
	}

	/**
	 * Set fetch movie detail task listener.
	 *
	 * @param fetchMovieDetailTaskListener
	 */
	public void setFetchMovieDetailTaskListener(
		FetchMovieDetailTaskListener fetchMovieDetailTaskListener) {
		this.mFetchMovieDetailTaskListener = fetchMovieDetailTaskListener;
	}

	/**
	 * https://api.themoviedb.org/3/movie/102899?api_key=f1e1a774094f3fa69542ccd5fff19e51&append_to_response=releases,trailers
	 *
	 * @param ids a list of ids?
	 * @return
	 */
	@Override
	protected PosterImageDetail doInBackground(String... ids) {
		if (null != ids || ids.length > 1) {
			String id = ids[0];
			return getPosterImageDetailDate(id);
		}
		return null;
	}

	@Override
	protected void onPostExecute(PosterImageDetail posterImageDetail) {
		if (null == posterImageDetail) {
			Toast.makeText(mContext, "Unable to obtain data", Toast.LENGTH_SHORT);
		} else {
			if (null != mFetchMovieDetailTaskListener) {
				mFetchMovieDetailTaskListener.setPosterImageDetail(posterImageDetail);
			} else {
				Log.i(LOG_TAG, "Fetch Movie Detail Task Listener is empty");
			}
		}
	}

	/**
	 * @return Json result
	 */
	private String getContent(String id) {

		String responseString = null;
		HttpURLConnection httpURLConnection = null;
		BufferedReader bufferedReader = null;

		final String APIKEY = "f1e1a774094f3fa69542ccd5fff19e51";
		final String BASEURL = "api.themoviedb.org";
		final String APIKEY_PARAM = "api_key";
		final String APPEND_TO_RESPONSE = "append_to_response";
		final String APPEND_TO_RESPONSE_PARAM = "releases,trailers";
		final String HTTP_GET = "GET";

		Uri.Builder uriBuilder = new Uri.Builder();
		uriBuilder.scheme("https")
			.authority(BASEURL)
			.appendPath("3")
			.appendPath("movie")
			.appendPath(id)
			.appendQueryParameter(APIKEY_PARAM, APIKEY)
			.appendQueryParameter(APPEND_TO_RESPONSE, APPEND_TO_RESPONSE_PARAM)
			.build();

		Log.i(LOG_TAG, uriBuilder.toString());

		try {
			URL url = new URL(uriBuilder.toString());
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod(HTTP_GET);
			httpURLConnection.connect();

			InputStream inputStream = httpURLConnection.getInputStream();
			StringBuffer sb = new StringBuffer();

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
	 * @param id post Id
	 * @return PosterImageDetail
	 */
	@Nullable
	private PosterImageDetail getPosterImageDetailDate(String id) {
		try {
			String data = getContent(id);
			if (null != data) {
				PosterImageDetail parseData = parseData(data);
				return parseData;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error in reading ", e);
		}
		return null;
	}

	/**
	 * @param content
	 * @return PosterImageDetail
	 * @throws JSONException
	 */
	private PosterImageDetail parseData(String content) throws JSONException {
		final String ID = "id";
		final String POSTER_PATH = "poster_path";
		final String TITLE = "title";
		final String RELASE_DATE = "release_date";
		final String OVERVIEW = "overview";
		final String POPULARITY = "popularity";
		final String RUNTIME = "runtime";
		final String TRAILERS = "trailers";
		final String NAME = "name";
		final String SIZE = "size";
		final String SOURCE = "source";
		final String TYPE = "type";
		final String VOTEAVERAGE = "vote_average";
		final String YOUTUBE = "youtube";

		JSONObject jsonObject = new JSONObject(content);

		PosterImageDetail posterImageDetail = new PosterImageDetail();
		String title = jsonObject.getString(TITLE);
		String path = jsonObject.getString(POSTER_PATH);
		String id = jsonObject.getString(ID);
		String releaseDate = jsonObject.getString(RELASE_DATE);
		String overview = jsonObject.getString(OVERVIEW);
		String length = jsonObject.getString(RUNTIME);
		String voteAverage = jsonObject.getString(VOTEAVERAGE);
		double popularity = jsonObject.getDouble(POPULARITY);

		posterImageDetail.setReleaseDate(releaseDate);
		posterImageDetail.setPostId(id);
		posterImageDetail.setPostPath(path);
		posterImageDetail.setRunTime(length);
		posterImageDetail.setVoteAverage(voteAverage);
		posterImageDetail.setOverview(overview);
		posterImageDetail.setTitle(title);

		JSONObject trailers = jsonObject.getJSONObject(TRAILERS);
		JSONArray jsonArray = trailers.getJSONArray(YOUTUBE);

		List<PosterTrailer> posterTrailers = new ArrayList<PosterTrailer>();

		for (int index = 0; index < jsonArray.length(); index++) {
			JSONObject inner = jsonArray.getJSONObject(index);
			PosterTrailer posterTrailer = new PosterTrailer(
				inner.getString(NAME), inner.getString(SIZE),
				inner.getString(SOURCE), inner.getString(TYPE));
			posterTrailers.add(posterTrailer);
		}

		posterImageDetail.setPosterTrailers(posterTrailers);

		return posterImageDetail;
	}

}
