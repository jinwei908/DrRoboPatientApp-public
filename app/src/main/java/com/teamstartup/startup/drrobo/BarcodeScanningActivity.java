package com.teamstartup.startup.drrobo;

import android.app.Activity;
import android.content.Intent;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeScanningActivity extends Activity {

    Fragment fragmentEmpty = new Fragment();
    IntentIntegrator scanIntegrator = new IntentIntegrator(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanning);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction;

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment1 = new BarcodeScanningFragment();
        fragmentTransaction.add(R.id.frameLayout11, fragment1);
        fragmentTransaction.commit();


        scanIntegrator.initiateScan(1);



        // note: the following codes are added to remove any previously
        //       displayed fragment2, so that when you get from landscape mode
        //       to portrait mode, it won't show fragment2

        //fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.frameLayout12, fragmentEmpty);
        //fragmentTransaction.commit();


        /*if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment2 = new Fragment2();
            fragmentTransaction.add(R.id.frameLayout12, fragment2);
            fragmentTransaction.commit();
        }*/
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //we have a result
            Toast.makeText(this, "Welcome " + scanningResult.getContents(), Toast.LENGTH_SHORT).show();
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

        //start intent
        Intent newIntent = new Intent(this, MainActivity.class);
        newIntent.putExtra("BARCODE_NRIC", scanningResult.getContents());
        setResult(RESULT_OK, newIntent);
        finish();

        /*Intent newIntent = new Intent(this, MainActivity.class);
        newIntent.putExtra("BARCODE_NRIC", scanningResult.getContents());
        startActivity(newIntent);*/

    }
}
