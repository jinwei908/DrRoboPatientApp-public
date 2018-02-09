package com.teamstartup.startup.drrobo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class MainActivity extends AppCompatActivity {

    //get all the controls
    private TextView resultTxt;
    private EditText nricTxt;
    private int lastUpdated = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        resultTxt = (TextView)findViewById(R.id.person_details);
        nricTxt = (EditText)findViewById(R.id.editText);

    }

    public void onClick_InitializeScanBarcode(View view) {
        Intent myIntent = new Intent(this, BarcodeScanningActivity.class);
        startActivityForResult(myIntent, 1);
        //Toast.makeText(this, "Testing 1 2 3",  Toast.LENGTH_SHORT).show();
    }

    public void onClick_CheckServer(View view){
        //get data
        Intent myIntent = new Intent(this, ProfileInteractionActivity.class);
        myIntent.putExtra("NRIC", nricTxt.getText().toString());
        //myIntent.putExtra("name", nricTxt.getText().toString());

        startActivity(myIntent);
    }

    public void onClick_CheckUser(View view){
        /*GetUrlContentTask URLTask = new GetUrlContentTask();
        URLTask.delegate4 = this;
        URLTask.execute(new String[]{"http://asean100.ap-southeast-1.elasticbeanstalk.com/php/asean100/loginCheck.php?loginName="+nric + "&password=" + password});
        */

        //get data
        Intent myIntent = new Intent(this, ProfileInteractionActivity.class);
        myIntent.putExtra("NRIC", nricTxt.getText().toString());
        //myIntent.putExtra("name", nricTxt.getText().toString());

        startActivity(myIntent);
    }

    public void processFinish(String output) {
        Toast.makeText(this, output,  Toast.LENGTH_SHORT).show();
        //write on the te'xt field
        if(output.trim().equals("error")){
            resultTxt.setText("Error getting results");
        } else {
            resultTxt.setText("Results: " + output);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Toast.makeText(this, requestCode + " " + resultCode,  Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                nricTxt.setText(data.getStringExtra("BARCODE_NRIC"));
                //get data
                Intent myIntent = new Intent(this, ProfileInteractionActivity.class);
                myIntent.putExtra("NRIC", data.getStringExtra("BARCODE_NRIC"));
                startActivity(myIntent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
