package com.teamstartup.startup.drrobo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.text.Html;
import android.text.Spanned;
import 	android.os.Handler;

public class MedicationActivity extends AppCompatActivity {

    //user details
    String nric = "";
    String userName = "";
    Double funds = 0.0;
    String dateModified = "";
    int userID = 0;
    String medicationData = "";

    TextView medicationText = null;
    JSONArray responseArray;
    Button medicationClaimButton = null;
    double totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);
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

        medicationText = (TextView)findViewById(R.id.medication_view);
        medicationClaimButton = (Button)findViewById(R.id.medication_button);
        //get details like name nric etc.
        Bundle bundles = getIntent().getExtras();
        if(bundles != null)
        {
            nric = bundles.getString("NRIC");
            userName = bundles.getString("USERNAME");
            funds = bundles.getDouble("FUNDS");
            dateModified = bundles.getString("DATE");
            userID = bundles.getInt("USERID");
        }

        //get data
        GetData();

    }

    protected void GetData(){
        GetUrlContentTask URLTask = new GetUrlContentTask();
        URLTask.delegate2 = this;
        URLTask.execute(new String[]{"http://asean100.ap-southeast-1.elasticbeanstalk.com/php/asean100/getmedicationdetails.php?id="+userID});


    }

    protected void processFinish(String output) {
        Toast.makeText(this, output,  Toast.LENGTH_SHORT).show();
        JSONObject responseJSON = null;
        responseArray = null;
        totalPrice = 0;
        try {
            responseArray = new JSONArray(output);
            for(int i=0; i<responseArray.length(); i++){
                medicationData += "<h1>1x " + responseArray.getJSONObject(i).getString("dosage_name")+"</h1>\n";
                medicationData += "<b>Price: $" + responseArray.getJSONObject(i).getDouble("dosage_price") + "</b>";
                medicationData += "<h3>Instructions</h3>\n";
                medicationData += responseArray.getJSONObject(i).getString("dosage_instructions") + "\n\n";
                totalPrice += responseArray.getJSONObject(i).getDouble("dosage_price");
            }
            medicationClaimButton.setText("Claim Medication (Pay $" + totalPrice + ")");
        } catch(Exception e) {
            Toast.makeText(this, "Error converting response to JSON: " + output, Toast.LENGTH_SHORT).show();
        }
        //convert to array of objects or strings
        Spanned htmlAsSpanned = Html.fromHtml(medicationData);
        medicationText.setText(htmlAsSpanned);
    }

    public void onClick_ClaimMedication(View view){
        Toast.makeText(this, "Claimed Medication", Toast.LENGTH_SHORT).show();
        //send to database
        for(int i=responseArray.length()-1; i>=0; i--) {
            //send to data base with url
            try {
                GetUrlContentTask URLTask = new GetUrlContentTask();
                String url = "http://asean100.ap-southeast-1.elasticbeanstalk.com/php/asean100/userpayment.php?uID=" + userID + "&iID=" + responseArray.getJSONObject(i).getInt("ID");
                URLTask.execute(new String[]{url});

                switch(responseArray.getJSONObject(i).getString("dosage_name")){
                    case "Cough Syrup":
                        (new Handler()).postDelayed(new Runnable() {
                            public void run() {
                                // yourMethod();
                                PlayCoughSyrup();
                            }
                        }, (responseArray.length() - i - 1)*9500);
                        break;

                    case "Aspirin":
                        (new Handler()).postDelayed(new Runnable() {
                            public void run() {
                                // yourMethod();
                                PlayAspirin();
                            }
                        }, (responseArray.length() - i - 1)*9500);
                        break;

                    case "Birth Control":
                        (new Handler()).postDelayed(new Runnable() {
                            public void run() {
                                // yourMethod();
                                PlayBrithControl();
                            }
                        }, (responseArray.length() - i - 1)*9500);
                        break;
                }



            } catch(JSONException e) {

            }

        }
        finish();

    }
    protected void PlayCoughSyrup(){
        MediaPlayer mp = MediaPlayer.create(this, R.raw.cough_syrup);
        mp.start();
    }

    protected void PlayAspirin(){
        MediaPlayer mp = MediaPlayer.create(this, R.raw.aspirin);
        mp.start();
    }

    protected void PlayBrithControl(){
        MediaPlayer mp = MediaPlayer.create(this, R.raw.birth_control);
        mp.start();
    }

}
