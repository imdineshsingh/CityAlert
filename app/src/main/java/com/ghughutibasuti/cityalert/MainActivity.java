package com.ghughutibasuti.cityalert;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private EditText txtEmail,txtPassword;
    private Button btnLogin,btnRegister,btnForgotPassword;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initialize();
    }



  public void initialize()
    {
        tv= (TextView) findViewById(R.id.tv);
        txtEmail= (EditText) findViewById(R.id.txtEmail);
        txtPassword= (EditText) findViewById(R.id.txtPassword);
        btnLogin= (Button) findViewById(R.id.btnLogin);
        btnRegister= (Button) findViewById(R.id.btnRegister);
        btnForgotPassword= (Button) findViewById(R.id.btnForgotPassword);


        firebaseAuth=FirebaseAuth.getInstance();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null)
                {
                    //user is signed in
                    Log.d("TAG","onAuthStateChanged:Signedin"+user.getUid());

                }
                else
                {
                    //user is signed out
                    Log.d("TAG","onAuthStateChanged:signedout");
                }
            }
        };





        listeners();
    }


    public void listeners()
    {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
        //        getFragmentManager().beginTransaction().add(R.id.login_layout,new Register()).commit();



                if(validate())
                {
                    String email=txtEmail.getText().toString();
                    String password=txtPassword.getText().toString();

                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {

                                updateUI();
                                Toast.makeText(MainActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }



            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                //Register r=new Register();
/*
                FragmentManager fm=getFragmentManager();
                FragmentTransaction t=fm.beginTransaction();
                t.replace(R.id.login_layout,r);
                t.commit();
*/

//        getFragmentManager().beginTransaction().add(R.id.login_layout,new Register()).commit();

                Intent i=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(i);



            }
        });





        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
                View view1=getLayoutInflater().inflate(R.layout.dialog_forgot_password,null);

                 final EditText putEmail= (EditText) view1.findViewById(R.id.putEmail);

                Button btnReset=(Button) view1.findViewById(R.id.btnReset);

               // final String email=putEmail.getText().toString();

                adb.setCancelable(false);
                adb.setView(view1);

      // adb.show();
               final AlertDialog dialog=adb.create();
               dialog.show();

                btnReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FirebaseAuth.getInstance().sendPasswordResetEmail(putEmail.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(MainActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();

                                        }
                                    }
                                });
                    }
                });




            }
        });
    }

    private  boolean validate()
    {
            boolean b=true;
                if(txtEmail.getText().toString().trim().length()==0)
            {
                txtEmail.setError("Email cant be empty");
                b=false;
            }
            else if(txtPassword.getText().toString().trim().length()<=0)
            {
                txtPassword.setError("very Weak password");
                b=false;
            }
            return b;


    }


private void updateUI()
{
    Intent i=new Intent(MainActivity.this,HomeActivity.class);
    startActivity(i);
    finish();
}

    @Override
    protected void onStart() {
        super.onStart();

        if(firebaseAuth.getCurrentUser()!=null)
        {
            updateUI();
        }
    }
}
