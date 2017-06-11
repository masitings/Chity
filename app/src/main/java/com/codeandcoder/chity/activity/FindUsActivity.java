package com.codeandcoder.chity.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.codeandcoder.chity.R;
import com.codeandcoder.chity.extra.AllConstants;

public class FindUsActivity extends Activity {
    private Context con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_us);
        con = this;
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("FIND US");
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

    public void btnFacebook(View v) {
        AllConstants.webUrl = "";

        Intent next = new Intent(con, DroidWebViewActivity.class);
        next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(next);
    }

    public void btnYoutube(View v) {
        AllConstants.webUrl = "";

        Intent next = new Intent(con, DroidWebViewActivity.class);
        next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(next);
    }

    public void btnLinkedin(View v) {
        AllConstants.webUrl = "";

        Intent next = new Intent(con, DroidWebViewActivity.class);
        next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(next);
    }

    public void btnGoogleplus(View v) {
        AllConstants.webUrl = "";

        Intent next = new Intent(con, DroidWebViewActivity.class);
        next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(next);
    }

    public void btnTwitter(View v) {
        AllConstants.webUrl = "";

        Intent next = new Intent(con, DroidWebViewActivity.class);
        next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(next);
    }

    public void btnPinterest(View v) {
        AllConstants.webUrl = "";

        Intent next = new Intent(con, DroidWebViewActivity.class);
        next.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(next);
    }

}
