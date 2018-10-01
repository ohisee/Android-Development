package com.example.android.movieposter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * This is Poster Image item detail activity.
 */
public class PosterImageItemDetailActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		if (null == savedInstanceState) {
			PosterImageItemDetailFragment posterImageItemDetailFragment =
				new PosterImageItemDetailFragment();
			getSupportFragmentManager().beginTransaction()
				.add(R.id.poster_detail_container, posterImageItemDetailFragment).commit();
		}
	}
}
