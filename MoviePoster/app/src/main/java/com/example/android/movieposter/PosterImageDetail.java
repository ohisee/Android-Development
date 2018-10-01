package com.example.android.movieposter;

import java.util.List;

/**
 * This is poster image detail.
 */
public class PosterImageDetail {

	private String postId;
	private String voteAverage;
	private String postPath;
	private String releaseDate;
	private String runTime;
	private String overview;
	private String title;
	private List<PosterTrailer> posterTrailers;

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getVoteAverage() {
		return voteAverage;
	}

	public void setVoteAverage(String voteAverage) {
		this.voteAverage = voteAverage;
	}

	public String getPostPath() {
		return postPath;
	}

	public void setPostPath(String postPath) {
		this.postPath = postPath;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<PosterTrailer> getPosterTrailers() {
		return posterTrailers;
	}

	public void setPosterTrailers(List<PosterTrailer> posterTrailers) {
		this.posterTrailers = posterTrailers;
	}
}
