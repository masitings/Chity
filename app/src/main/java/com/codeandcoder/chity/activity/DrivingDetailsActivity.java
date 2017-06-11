package com.codeandcoder.chity.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.codeandcoder.chity.R;
import com.codeandcoder.chity.extra.AllConstants;
import com.codeandcoder.chity.extra.AllURL;
import com.codeandcoder.chity.holder.DrivingDetails;
import com.codeandcoder.chity.model.DrivingTime;
import com.codeandcoder.chity.parser.DrivingDetailsParser;

public class DrivingDetailsActivity extends Activity {


    private Context con;
    private String pos = "";
    private TextView dName;

    private ProgressDialog pDialog;
    private DrivingTime DT;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving_details);

        dName = (TextView) findViewById(R.id.driveView);

        updateUI();
    }

    private void updateUI() {


        pDialog = ProgressDialog.show(this, "", "Loading..", false, false);

        final Thread d = new Thread(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub

                try {
                    if (DrivingDetailsParser.connect(con, AllURL
                            .drivingURL(AllConstants.UPlat, AllConstants.UPlng, AllConstants.Dlat, AllConstants.Dlng,
                                    AllConstants.apiKey))) {
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
                        try {

                            DT = DrivingDetails.getAlldrivingdetails()
                                    .elementAt(0);

                            dName.setText(DT.getTime().trim());


                            // ------Rating ---


                        } catch (Exception e) {
                            // TODO: handle exception
                        }


                    }
                });

            }
        });
        d.start();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.testmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
