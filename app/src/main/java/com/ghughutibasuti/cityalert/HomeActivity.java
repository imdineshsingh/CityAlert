package com.ghughutibasuti.cityalert;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.ghughutibasuti.cityalert.R.layout.activity_home;

public class HomeActivity extends AppCompatActivity {
    private Button logout,panicButton,btnEmergency,editDetail;
    private TelephonyManager telephonyManager;



    EditText msg,cont1,cont2,cont3,cont4;
    public static final String DEFAULT="N/A";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_home);

        initialize();
        permissions();
        listeners();

        location();
        geoloc();






        Button loc= (Button) findViewById(R.id.loc);
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(HomeActivity.this,MyLocation.class);
                startActivity(i);

            }
        });







    }

    public void location()
    {
        ActivityCompat.requestPermissions(HomeActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},123);

        TextView longitude= (TextView) findViewById(R.id.longitude);
        TextView latitude= (TextView) findViewById(R.id.latitude);

        GPSTracker g=new GPSTracker(getApplicationContext());
        Location l=g.getLocation();

        if(l!=null)
        {

            double lati=l.getLatitude();
            double longi=l.getLongitude();
            longitude.setText("Longitude :"+longi);
            latitude.setText("Latitude :"+lati);

            Toast.makeText(getApplicationContext(), lati+","+longi, Toast.LENGTH_SHORT).show();
          


        }

    }


    private void geoloc()
    {
        Double latitude=77.31344666666668;
        Double longitude=28.529526666666666;

        Geocoder geocoder;
        List<Address> addresses;

        TextView textView= (TextView) findViewById(R.id.textView);

        geocoder=new Geocoder(this, Locale.getDefault());
        try {
            addresses=geocoder.getFromLocation(latitude,longitude,1);

            if(addresses!=null )
            {
                String add = addresses.get(0).getAddressLine(0);

                String area = addresses.get(0).getLocality();
                String city = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();

                String fullAddress = add + "" + area + "" + city + "" + country + "" + postalCode;
                textView.setText(fullAddress);
            }
            Toast.makeText(this, "addresses is null", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.setting:
                break;
            case R.id.logout :
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

                break;
            case R.id.about:

                Intent i=new Intent(HomeActivity.this,About.class);
                startActivity(i);
                break;
            case R.id.exit:
                HomeActivity.this.finish();
                break;
        }


        return true;
    }

    private void initialize()
    {
        logout = (Button) findViewById(R.id.logout);
        panicButton= (Button) findViewById(R.id.panicButton);
        btnEmergency= (Button) findViewById(R.id.btnEmergency);

        editDetail= (Button) findViewById(R.id.editDetail);

    }




    private void listeners()
    {

        /*
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
*/


        editDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

               // editDetail.setBackgroundColor(getResources().getColor(R.color.onClick));

                AlertDialog.Builder adb=new AlertDialog.Builder(HomeActivity.this);
                View view1=getLayoutInflater().inflate(R.layout.customdialog_input,null);

                 msg= (EditText) view1.findViewById(R.id.msg);
                 cont1=(EditText)view1.findViewById(R.id.cont1);
                 cont2=(EditText)view1.findViewById(R.id.cont2);
                 cont3=(EditText)view1.findViewById(R.id.cont3);
                 cont4=(EditText)view1.findViewById(R.id.cont4);
                Button btnSaveDetail= (Button) view1.findViewById(R.id.btnSaveDetail);

                //Priinting Detail on EditTexts ..that is saved in shared preference
                SharedPreferences sharedPreferences=getSharedPreferences("MyData",MODE_PRIVATE);
                msg.setText(sharedPreferences.getString("message",DEFAULT));
                cont1.setText(sharedPreferences.getString("cont1",DEFAULT));
                cont2.setText(sharedPreferences.getString("cont2",DEFAULT));
                cont3.setText(sharedPreferences.getString("cont3",DEFAULT));
                cont4.setText(sharedPreferences.getString("cont4",DEFAULT));

                adb.setCancelable(true);
                adb.setView(view1);

//        adb.show();
                final AlertDialog dialog=adb.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                String m=msg.getText().toString();
                String c1=cont1.getText().toString();
                String c2=cont2.getText().toString();
                String c3=cont3.getText().toString();
                String c4=cont4.getText().toString();


                msg.setText(m);
                cont1.setText(c1);
                cont2.setText(c2);
                cont3.setText(c3);
                cont4.setText(c4);


                btnSaveDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               //Saving DAta to Shared Preferences

                SharedPreferences sharedPreferences=getSharedPreferences("MyData",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("message",msg.getText().toString());
                editor.putString("cont1",cont1.getText().toString());
                editor.putString("cont2",cont2.getText().toString());
                editor.putString("cont3",cont3.getText().toString());
                editor.putString("cont4",cont4.getText().toString());
                 // editor.commit();
                editor.apply();

                Toast.makeText(HomeActivity.this, "Your Detail Saved Successfully", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });





            }
        });



        panicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                 //panicButton.setBackgroundColor(getResources().getColor(R.color.onClick));

                SharedPreferences sharedPreferences=getSharedPreferences("MyData",MODE_PRIVATE);
                String m=sharedPreferences.getString("message",DEFAULT);
                String a=sharedPreferences.getString("cont1",DEFAULT);
                String b=sharedPreferences.getString("cont2",DEFAULT);
                String c=sharedPreferences.getString("cont3",DEFAULT);
                String d=sharedPreferences.getString("cont4",DEFAULT);

                SmsManager smsManager=SmsManager.getDefault();

                smsManager.sendTextMessage(a,null,m,null,null);
                smsManager.sendTextMessage(b,null,m,null,null);
                smsManager.sendTextMessage(c,null,m,null,null);
                smsManager.sendTextMessage(d,null,m,null,null);
                //   txtMessage.setText("");
                // txtMessage.requestFocus();
                Toast.makeText(HomeActivity.this,"SMS Sent to "+a+","+b+","+c+","+d+"",Toast.LENGTH_SHORT).show();
            }
        });


        btnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                //btnEmergency.setBackgroundColor(getResources().getColor(R.color.onClick));


                AlertDialog.Builder adb=new AlertDialog.Builder(HomeActivity.this);
                View view1=getLayoutInflater().inflate(R.layout.custom_dialog,null);


                Button btnCallPolice= (Button) view1.findViewById(R.id.btnCallPolice);
                Button btnCallAmbulance= (Button) view1.findViewById(R.id.btnCallAmbulance);
                Button btnCallFire=(Button) view1.findViewById(R.id.btnCallFire);

                adb.setCancelable(true);
                adb.setView(view1);

//        adb.show();
                final AlertDialog dialog=adb.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                btnCallPolice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ContextCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(HomeActivity.this,new String[] {Manifest.permission.CALL_PHONE},400);
                        }
                       else
                        {

                            Intent i=new Intent(Intent.ACTION_CALL);
                            i.setData(Uri.parse("tel:8447709787"));
                            startActivity(i);

                        }
                    }
                });

                btnCallAmbulance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(ContextCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(HomeActivity.this,new String[] {Manifest.permission.CALL_PHONE},400);
                        }
                        else
                        {
                            Intent i=new Intent(Intent.ACTION_CALL);
                            i.setData(Uri.parse("tel:8447709787"));
                            startActivity(i);

                        }
                    }
                });

                    btnCallFire.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(ContextCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED)
                            {
                                ActivityCompat.requestPermissions(HomeActivity.this,new String[] {Manifest.permission.CALL_PHONE},400);
                            }
                            else
                            {
                                Intent i=new Intent(Intent.ACTION_CALL);
                                i.setData(Uri.parse("tel:8447709787"));
                                startActivity(i);

                            }

                        }
                    });


            }
        });


    }



    private void permissions()
    {

        telephonyManager= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED)
            {

                requestPermissions(new String[] {Manifest.permission.SEND_SMS},100);
            }

            else if(checkSelfPermission(Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[] {Manifest.permission.CALL_PHONE},200);
            }


            else
            {
                listeners();
            }


        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100)
        {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                //add Event on Panic Button
                listeners();
            }

            else
            {
                Toast.makeText(this,"not able to send",Toast.LENGTH_SHORT).show();
            }

        }

        if(requestCode==200)
        {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                listeners();
            }
            else
            {
                Toast.makeText(this, "not able to call", Toast.LENGTH_SHORT).show();
            }
        }




    }


                /*String m=msg.getText().toString();
                String c1=cont1.getText().toString();
                String c2=cont2.getText().toString();
                String c3=cont3.getText().toString();
                String c4=cont4.getText().toString();
*/




}
