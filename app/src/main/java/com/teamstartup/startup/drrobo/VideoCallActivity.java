package com.teamstartup.startup.drrobo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.sinch.android.rtc.SinchError;

import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.video.VideoController;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;

public class VideoCallActivity extends BaseActivity {
    SinchClient sinchClient = null;
    private String nric = "";
    private String userName = "";
    private ProgressDialog mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        Bundle bundles = getIntent().getExtras();
        if(bundles != null)
        {
            nric = bundles.getString("NRIC");
            userName = bundles.getString("Username");
        }

        android.content.Context context = this.getApplicationContext();
        sinchClient = Sinch.getSinchClientBuilder().context(context)
                .applicationKey("73ee6f6b-d519-47ac-ab14-d3bfebc953ce")
                .applicationSecret("nvrlKu+Q+0C6YKQo0AQKPw==")
                .environmentHost("clientapi.sinch.com")//("sandbox.sinch.com")
                .userId("patient")
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();

        sinchClient.addSinchClientListener(new SinchClientListener() {
            public void onClientStarted(SinchClient client) { }
            public void onClientStopped(SinchClient client) { }
            public void onClientFailed(SinchClient client, SinchError error) { }
            public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration registrationCallback) { }
            public void onLogMessage(int level, String area, String message) { }
        });
        sinchClient.start();

        CallDoctor();
    }

    protected void TerminateSinchClient(){
        sinchClient.stopListeningOnActiveConnection();
        sinchClient.terminate();
    }

    @Override
    protected void onServiceConnected() {

    }

    protected void CallDoctor(){

        try {
            if (!getSinchServiceInterface().isStarted()) {
                getSinchServiceInterface().startClient(userName);
                showSpinner();
            } else {

            }


            String userName = "doctor908";
            if (userName.isEmpty()) {
                Toast.makeText(this, "Please enter a user to call", Toast.LENGTH_LONG).show();
                return;
            }

            Call call = getSinchServiceInterface().callUserVideo(userName);
            String callId = call.getCallId();

            Intent callScreen = new Intent(this, CallScreenActivity.class);
            callScreen.putExtra(SinchService.CALL_ID, callId);
            startActivity(callScreen);
        } catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //call.addCallListener(...);
    }

    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
    }
}
