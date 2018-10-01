package com.example.android.movieposter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * This is post image class.
 */
public class PosterImage implements Parcelable {

	private final static String BASE_URL = "http://image.tmdb.org/t/p/w185/";
	private String path;
	private String title;
	private String overview;
	private String releaseDate;
	private String movieId;
	private String runTime;
	private double popularity;
	private double voteAverage;
	private List<PosterTrailer> posterTrailers;

	public PosterImage(String path, String title) {
		this.path = String.format("%s%s", BASE_URL, path);
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public double getPopularity() {
		return popularity;
	}

	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}

	public double getVoteAverage() {
		return voteAverage;
	}

	public void setVoteAverage(double voteAverage) {
		this.voteAverage = voteAverage;
	}

	public List<PosterTrailer> getPosterTrailers() {
		if (null == posterTrailers) {
			posterTrailers = new ArrayList<PosterTrailer>();
		}
		return posterTrailers;
	}

	public void setPosterTrailers(List<PosterTrailer> posterTrailers) {
		this.posterTrailers = posterTrailers;
	}

	public static final Parcelable.Creator<PosterImage> CREATOR = new Parcelable.Creator<PosterImage>() {
		@Override
		public PosterImage createFromParcel(Parcel source) {
			return new PosterImage(source);
		}

		@Override
		public PosterImage[] newArray(int size) {
			return new PosterImage[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Write to parcel
	 *
	 * @param dest
	 * @param flags
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(path);
		dest.writeString(title);
		dest.writeString(overview);
		dest.writeString(releaseDate);
		dest.writeString(movieId);
		dest.writeString(runTime);
		dest.writeDouble(popularity);
		dest.writeDouble(voteAverage);
		dest.writeTypedList(posterTrailers);
	}

	/**
	 * De-parcel object, order matching write to parcel.
	 *
	 * @param in
	 */
	private PosterImage(Parcel in) {
		path = in.readString();
		title = in.readString();
		overview = in.readString();
		releaseDate = in.readString();
		movieId = in.readString();
		runTime = in.readString();
		popularity = in.readDouble();
		voteAverage = in.readDouble();
		in.readTypedList(getPosterTrailers(), PosterTrailer.CREATOR);
	}

	public static PosterImage[] posterImages = {
		new PosterImage("jjBgi2r5cRt36xF6iNUEhzscEcb.jpg", "Jurassic World"),
		new PosterImage("5aGhaIHYuQbqlHWvWYqMCnj40y2.jpg", "The Martian"),
		new PosterImage("q0R4crx2SehcEEQEkYObktdeFy.jpg", "Minions"),
		new PosterImage("7SGGUiTE6oc2fh9MjIk5M00dsQd.jpg", "Ant-Man"),
		new PosterImage("5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", "Terminator Genisys"),
		new PosterImage("aAmfIX3TT40zUHGcCKrlOZRKC7u.jpg", "Inside Out"),
		new PosterImage("1n9D32o30XOHMdMWuIT4AaA5ruI.jpg", "Spectre"),
		new PosterImage("z2sJd1OvAGZLxgjBdSnQoLCfn3M.jpg", "Rogue Nation"),
		new PosterImage("g23cs30dCMiG4ldaoVNP1ucjs6.jpg", "Fantastic Four"),
		new PosterImage("qey0tdcOp9kCDdEZuJ87yE3crSe.jpg", "San Andreas"),
		new PosterImage("69Cz9VNQZy39fUE2g0Ggth6SBTM.jpg", "Tomorrowland"),
		new PosterImage("kqjL17yufvn9OVLyXYpvtyrFfak.jpg", "Fury Road"),
		new PosterImage("ktyVmIqfoaJ8w0gDSZyjhhOPpD6.jpg", "Pixels"),
		new PosterImage("t90Y3G8UGQp0f0DrP60wRu9gfrH.jpg", "Age of Ultron"),
		new PosterImage("nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg", "Interstellar"),
		new PosterImage("vlTPQANjLYTebzFJM1G4KeON0cb.jpg", "Maze Runner"),
		new PosterImage("l3Lb8UWmqfXY9kr9YhJXvnTvf4I.jpg", "The Hobbit"),
		new PosterImage("5ttOaThDVmTpV8iragbrhdfxEep.jpg", "The Man from U.N.C.L.E."),
		new PosterImage("z3nGs7UED9XlqUkgWeT4jQ80m1N.jpg", "Southpaw"),
		new PosterImage("y31QB9kn3XSudA15tV7UWQ9XLuW.jpg", "Guardians of the Galaxy")
	};
}
