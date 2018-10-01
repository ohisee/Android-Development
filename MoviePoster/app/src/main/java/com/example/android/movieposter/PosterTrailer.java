package com.example.android.movieposter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This is poster trailer.
 */
public class PosterTrailer implements Parcelable {

	private String name;
	private String size;
	private String source;
	private String type;

	/**
	 * A constructor with fields
	 *
	 * @param name
	 * @param size
	 * @param source
	 * @param type
	 */
	public PosterTrailer(String name, String size, String source, String type) {
		this.name = name;
		this.size = size;
		this.source = source;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static final Creator<PosterTrailer> CREATOR = new Creator<PosterTrailer>() {
		@Override
		public PosterTrailer createFromParcel(Parcel in) {
			return new PosterTrailer(in);
		}

		@Override
		public PosterTrailer[] newArray(int size) {
			return new PosterTrailer[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Write to parcel.
	 *
	 * @param dest
	 * @param flags
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(size);
		dest.writeString(source);
		dest.writeString(type);
	}

	/**
	 * De-parcel object, order matching write to parcel.
	 *
	 * @param in
	 */
	private PosterTrailer(Parcel in) {
		name = in.readString();
		size = in.readString();
		source = in.readString();
		type = in.readString();
	}

	@Override
	public String toString() {
		return "PosterTrailer{" +
			"name='" + name + '\'' +
			", size='" + size + '\'' +
			", source='" + source + '\'' +
			", type='" + type + '\'' +
			'}';
	}
}
