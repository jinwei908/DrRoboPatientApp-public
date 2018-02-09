package com.teamstartup.startup.drrobo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;
import 	android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

public class ProfileInteractionActivity extends AppCompatActivity {

    TextView personDetails = null;
    TextView[] userDetails = new TextView[5];
    private ProgressBar spinner;
    private FrameLayout progressBarHolder;
    private LinearLayout personDetailsHolder;
    private LinearLayout newUserHolder;
    ImageView userImage = null;
    EditText newUser = null;

    Button medicationBtn = null;
    Button button_profile1 = null;
    Button button_profile3 = null;


    //user details
    String nric = "";
    String userName = "";
    Double funds = 0.0;
    String dateModified = "";
    int userID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_interaction);
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

        //init user details
        userImage = (ImageView)findViewById(R.id.person_image);
        userDetails[0] = (TextView)findViewById(R.id.person_name);
        userDetails[1] = (TextView)findViewById(R.id.person_nric);
        userDetails[2] = (TextView)findViewById(R.id.person_funds);
        userDetails[3] = (TextView)findViewById(R.id.date_modified);
        userDetails[4] = (TextView)findViewById(R.id.new_nric);

        personDetailsHolder = (LinearLayout)findViewById(R.id.person_details_frame);
        personDetailsHolder.setVisibility(View.GONE);
        newUserHolder = (LinearLayout)findViewById(R.id.add_account);
        newUser = (EditText)findViewById(R.id.new_username);

        //personDetails  = (TextView)findViewById(R.id.person_details_in);
        Bundle bundles = getIntent().getExtras();
        if(bundles != null)
        {
            nric = bundles.getString("NRIC");
        }
        //personDetails.setText("Loading Profile Data: "  + nric);
        medicationBtn = (Button)findViewById(R.id.medication_btn);
        button_profile1 = (Button)findViewById(R.id.button_profile1);
        button_profile3 = (Button)findViewById(R.id.button_profile3);


        spinner = (ProgressBar)findViewById(R.id.loading_bar);
        progressBarHolder = (FrameLayout) findViewById(R.id.loading_bar_frame);
        //spinner.setVisibility(View.VISIBLE);
        progressBarHolder.setVisibility(View.VISIBLE);

        GetUrlContentTask URLTask = new GetUrlContentTask();
        URLTask.delegate = this;
        URLTask.execute(new String[]{"http://asean100.ap-southeast-1.elasticbeanstalk.com/php/asean100/getuser.php?nric="+nric});

    }

    public void processFinish(String output){
        //personDetails.setText(output);
        Toast.makeText(this, output,  Toast.LENGTH_SHORT).show();
        progressBarHolder.setVisibility(View.GONE);
        newUserHolder.setVisibility(View.GONE);
        personDetailsHolder.setVisibility(View.GONE);
        JSONObject responseJSON = null;
        try {
            responseJSON = new JSONObject(output);
        } catch(Exception e){
            Toast.makeText(this, "Error converting response to JSON: " + output,  Toast.LENGTH_SHORT).show();
        }
        //Process Finish
        if(!output.trim().equals("error")){
            personDetailsHolder.setVisibility(View.VISIBLE);
            userImage.setVisibility(View.VISIBLE);
            medicationBtn.setVisibility(View.VISIBLE);
            button_profile1.setVisibility(View.VISIBLE);
            button_profile3.setVisibility(View.VISIBLE);
            try {
                userName = responseJSON.getString("personName");
                funds = responseJSON.getDouble("funds");
                dateModified = responseJSON.getString("dateMod");
                userID = responseJSON.getInt("ID");

                userDetails[0].setText("Name: " + userName);
                userDetails[1].setText("NRIC: " + responseJSON.getString("personNRIC"));
                userDetails[2].setText("Funds: " + funds);
                userDetails[3].setText("Date Modified: " + dateModified);
                medicationBtn.setText("Get Medication (" + responseJSON.getInt("patientMed") + ")");
                if (!responseJSON.getString("image").equals("")) {
                    new GetUrlImageTask(userImage).execute(new String[]{responseJSON.getString("image")});
                    Toast.makeText(this, "Set User Image", Toast.LENGTH_SHORT).show();
                } else {
                    Bitmap bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.default_image);
                    userImage.setImageBitmap(bImage);
                    userImage.invalidate();
                    Toast.makeText(this, "Set Default Image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
                Toast.makeText(this, "Error converting response to JSON",  Toast.LENGTH_SHORT).show();
            }
        } else {
            //ask for account creation
            Toast.makeText(this, "Set to visible, creating user...",  Toast.LENGTH_SHORT).show();
            newUserHolder.setVisibility(View.VISIBLE);
            medicationBtn.setVisibility(View.GONE);
            button_profile1.setVisibility(View.GONE);
            button_profile3.setVisibility(View.GONE);
            //personDetailsHolder.setVisibility(View.GONE);
            userDetails[4].setText("New NRIC: " + nric);
        }
    }

    public void onClick_AddUserToServer(View view){
        Toast.makeText(this, "Adding User: Name: " + newUser.getText() + " | NRIC: " + nric, Toast.LENGTH_SHORT).show();
        GetUrlContentTask URLTask = new GetUrlContentTask();
        URLTask.delegate = this;
        URLTask.execute(new String[]{"http://asean100.ap-southeast-1.elasticbeanstalk.com/php/asean100/adduser.php?nric="+nric+"&name="+newUser.getText()});
        //newUserHolder.setVisibility(View.GONE);
    }

    public void onClick_GetMedication(View view){
        Toast.makeText(this, "Getting Medication For: " + userName, Toast.LENGTH_SHORT).show();
        Intent MedicationA = new Intent(this, MedicationActivity.class);
        MedicationA.putExtra("NRIC", nric);
        MedicationA.putExtra("USERNAME", userName);
        MedicationA.putExtra("FUNDS", funds);
        MedicationA.putExtra("DATE", dateModified);
        MedicationA.putExtra("USERID", userID);
        startActivityForResult(MedicationA,1);

        //newUserHolder.setVisibility(View.GONE);
    }

    public void onClick_ConsultADoctor(View view){
        Toast.makeText(this, "Consult a Doctor", Toast.LENGTH_SHORT).show();
        /*Intent myIntent = new Intent(this, VideoCallActivity.class);
        myIntent.putExtra("NRIC", nric);
        myIntent.putExtra("Username", userName);
        startActivity(myIntent);*/

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.sinch.android.rtc.sample.video");
        if (launchIntent != null) {
            startActivity(launchIntent);//null pointer check in case package name was not found
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Toast.makeText(this, requestCode + " " + resultCode,  Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            GetUrlContentTask URLTask = new GetUrlContentTask();
            URLTask.delegate = this;
            URLTask.execute(new String[]{"http://asean100.ap-southeast-1.elasticbeanstalk.com/php/asean100/getuser.php?nric="+nric});


        }
    }

    public void Finish(){

    }


}

