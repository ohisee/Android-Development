package com.example.android.movieposter;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * This is image view.
 */
public class ImageViewHolder {

	private ImageView imageView;
	private TextView textView;

	/**
	 * @return image view
	 */
	public ImageView getImageView() {
		return imageView;
	}

	/**
	 * Set image view
	 *
	 * @param imageView
	 */
	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	/**
	 * @return text view
	 */
	public TextView getTextView() {
		return textView;
	}

	/**
	 * @param textView
	 */
	public void setTextView(TextView textView) {
		this.textView = textView;
	}

}