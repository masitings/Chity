package com.codeandcoder.chity.activity;


import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codeandcoder.chity.R;
import com.codeandcoder.chity.extra.AlertMessage;
import com.codeandcoder.chity.extra.AllConstants;
import com.codeandcoder.chity.extra.AllURL;
import com.codeandcoder.chity.extra.CacheImageDownloader;
import com.codeandcoder.chity.extra.PrintLog;
import com.codeandcoder.chity.extra.SharedPreferencesHelper;
import com.codeandcoder.chity.holder.AllCityMenu;
import com.codeandcoder.chity.model.CityMenuList;
import com.codeandcoder.chity.parser.CityMenuParser;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class ListActivity extends Activity{

	private ListView list;
	private Context con;
	private Bitmap defaultBit;
	private RestaurantAdapter adapter;
	private ProgressDialog pDialog;
	private CacheImageDownloader downloader;



	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;


	AdView adView;
	AdRequest bannerRequest, fullScreenAdRequest;
	InterstitialAd fullScreenAdd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.listlayout);
		// get the action bar
		setTheme(R.style.HomeTheme);
		aBar();
		con = this;
		initUI();
		enableAd();

	 }


	private void aBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		getActionBar().setTitle("AROUND ME");

	}





	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {


			case android.R.id.home:
				this.finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}

	private void enableAd() {
		// TODO Auto-generated method stub
		adView = (AdView) findViewById(R.id.adView);
		bannerRequest = new AdRequest.Builder().build();
		adView.loadAd(bannerRequest);
		fullScreenAdd = new InterstitialAd(this);
		fullScreenAdd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
		fullScreenAdRequest = new AdRequest.Builder().build();
		fullScreenAdd.loadAd(fullScreenAdRequest);

		fullScreenAdd.setAdListener(new AdListener() {

			@Override
			public void onAdLoaded() {

				Log.i("FullScreenAdd", "Loaded successfully");
				fullScreenAdd.show();

			}

			@Override
			public void onAdFailedToLoad(int errorCode) {

				Log.i("FullScreenAdd", "failed to Load");
			}
		});

		// TODO Auto-generated method stub

	}

	private void initUI() {
		// TODO Auto-generated method stub

		list = (ListView) findViewById(R.id.menuListView);
		downloader = new CacheImageDownloader();
		parseQuery();

		PrintLog.myLog("Query in activity : ", AllConstants.query);

	}

	private void parseQuery() {
		// TODO Auto-generated method stub
		if (!SharedPreferencesHelper.isOnline(con)) {
			AlertMessage.showMessage(con, "Error", "No internet connection");
			return;
		}

		pDialog = ProgressDialog.show(this, "", "Loading..", false, false);

		final Thread d = new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				try {
					if (CityMenuParser.connect(con, AllURL.nearByURL(
							AllConstants.UPlat, AllConstants.UPlng,
							AllConstants.query, AllConstants.apiKey))) {

						PrintLog.myLog("Size of City : ", AllCityMenu
								.getAllCityMenu().size()
								+ "");

					}

				} catch (final Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				runOnUiThread(new Runnable() {

					public void run() {
						// TODO Auto-generated method stub
						if (pDialog != null) {
							pDialog.cancel();
						}
						if (AllCityMenu.getAllCityMenu().size() == 0) {

						} else {

							adapter = new RestaurantAdapter(con);
							list.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						}

					}
				});

			}
		});
		d.start();
	}

	class RestaurantAdapter extends ArrayAdapter<CityMenuList> {
		private final Context con;

		public RestaurantAdapter(Context context) {
			super(context, R.layout.rowlist, AllCityMenu.getAllCityMenu());
			con = context;
			// TODO Auto-generated constructor stub

		}




		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				final LayoutInflater vi = (LayoutInflater) con
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.rowlist, null);
			}

			if (position < AllCityMenu.getAllCityMenu().size()) {
				final CityMenuList CM = AllCityMenu.getCityMenuList(position);


				final TextView address = (TextView) v
						.findViewById(R.id.rowAddress);

				try {
					address
							.setText(CM.getVicinity().toString()
									.trim());
				} catch (Exception e) {
					// TODO: handle exception
				}

				try {

					AllConstants.photoReferrence = CM.getPhotoReference()
							.toString().trim();
					PrintLog
							.myLog("PPRRRef", AllConstants.photoReferrence + "");
				} catch (Exception e) {
					// TODO: handle exception
				}


				final ImageView icon = (ImageView) v
						.findViewById(R.id.rowImageView);
				
				try {

					AllConstants.iconUrl = CM.getIcon()
							.toString().trim();
					PrintLog
							.myLog("iconURL:", AllConstants.iconUrl + "");
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				try {

					String imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=190&photoreference="
							+ AllConstants.photoReferrence
							+ "&sensor=true&key=" + AllConstants.apiKey;

					if (AllConstants.photoReferrence.length() != 0) {

						downloader.download(imgUrl.trim(), icon);

						AllConstants.cPhotoLink = imgUrl.replaceAll(" ", "%20");
					}

					else {

						icon.setImageResource(R.drawable.not_found_banner);


					}

				} catch (Exception e) {
					// TODO: handle exception
				}

				// ------Rating ---
				final RatingBar listRatings = (RatingBar) v
						.findViewById(R.id.ratingBarList);

				String rating = CM.getRating();

				try {

					Float count = Float.parseFloat(rating);

					listRatings.setRating(count);

				} catch (Exception e) {

				}

				// ----Name----

				final TextView name = (TextView) v.findViewById(R.id.rowName);
				try {
					name.setText(CM.getName().toString().trim());
//					name.setTypeface(title);
				} catch (Exception e) {
					// TODO: handle exception
				}
				v.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						AllConstants.referrence = CM.getReference().toString()
								.trim();
						try {
							AllConstants.photoReferrence = CM
									.getPhotoReference().toString().trim();
						} catch (Exception e) {
							// TODO: handle exception
						}

						try {
							AllConstants.Dlat = CM
									.getdLat().toString().trim();
							AllConstants.Dlng = CM
									.getdLan().toString().trim();


						} catch (Exception e) {
							// TODO: handle exception
						}
						PrintLog.myLog("DDDLatLng : ", CM
								.getdLat().toString().trim()+"  "+CM
								.getdLan().toString().trim());

						try {
							AllConstants.detailsiconUrl = CM
									.getIcon().toString().trim();
						} catch (Exception e) {
							// TODO: handle exception
						}
						final Intent iii = new Intent(con,
								ListDetailsActivity.class);
						iii.putExtra("POSITION", position);
						iii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(iii);

						// }

					}
				});

			}

			// TODO Auto-generated method stub
			return v;
		}

	}

	public void btnHome(View v) {

		Intent next = new Intent(con, MainActivity.class);
		next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(next);

	}

	public void btnBack(View v) {
		finish();

	}
}
