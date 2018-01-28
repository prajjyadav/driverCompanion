package myname.driver;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends Activity  implements LocationListener {

    AudioManager audioManager;
    int speed;
    TextToSpeech t1;

    @TargetApi(Build.VERSION_CODES.DONUT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioManager = (AudioManager) getSystemService(getApplicationContext().AUDIO_SERVICE);
        Button location = (Button) findViewById(R.id.show_location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        final Button driver = (Button) findViewById(R.id.driver_mode);
        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(driver.getText().toString().equalsIgnoreCase("ACTIVATE DRIVER'S MODE")) {

                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    Toast.makeText(MainActivity.this, "Driver's Mode Activated", Toast.LENGTH_LONG).show();
                    driver.setText("DEACTIVATE DRIVER'S MODE");


                    LocationManager lm = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, MainActivity.this);
                    MainActivity.this.onLocationChanged(null);
                }

                else if(driver.getText().toString().equalsIgnoreCase("DEACTIVATE DRIVER'S MODE")){
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    Toast.makeText(MainActivity.this, "Driver's Mode Deactivated", Toast.LENGTH_LONG).show();
                    driver.setText("ACTIVATE DRIVER'S MODE");
                }

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.DONUT)
    @Override
    public void onLocationChanged(Location location) {
        if (location==null){
            // if you can't get speed because reasons :)
            speed=0;
        }
        else{
            //int speed=(int) ((location.getSpeed()) is the standard which returns meters per second. In this example i converted it to kilometers per hour

            speed=(int) ((location.getSpeed()*3600)/1000);
            if(speed>80)
            {
                String toSpeak = "You are overSpeeding, Your current speed is"+speed+". Please Take care";
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
