package com.ghughutibasuti.cityalert;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener mAuthListener;

    private EditText firstName,lastName,phone,email,password,confirmPassword;
    private Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();


    }


    void initialize()
    {
        mAuth=FirebaseAuth.getInstance();
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


        firstName= (EditText) findViewById(R.id.firstName);
        lastName= (EditText) findViewById(R.id.lastName);
        phone= (EditText) findViewById(R.id.phone);
        email= (EditText) findViewById(R.id.email);
        password= (EditText) findViewById(R.id.password);
        confirmPassword= (EditText) findViewById(R.id.confirmPassword);
        register= (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

            signup();

            }
        });


    }


    void signup()
    {

        if(validate())
        {
            String e= email.getText().toString();
            String p=password.getText().toString();


                //Signed up new User
            mAuth.createUserWithEmailAndPassword(e,p)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d("TAG","createUserWithEmail:onComplete:"+task.isSuccessful());

                        if(task.isSuccessful())
                        {
                            updateUI();
                            Toast.makeText(RegisterActivity.this," Successfully Registered",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        }


    }


void updateUI()
{
    Intent i=new Intent(RegisterActivity.this, HomeActivity.class);
    startActivity(i);
    finish();
}


 private boolean validate()
 {


     String pass=password.getText().toString();
     String cpass=confirmPassword.getText().toString();

    boolean b=true;
    if(email.getText().toString().trim().length()==0)
    {
        email.setError("Email Can't be empty");
        b=false;
    }
    else if(password.getText().toString().trim().length()<0)
    {
        password.setError("Password should be more then 4 char");
        b=false;
    }

    else if( !pass.equals(cpass))
    {
        confirmPassword.setError("Not Matching with Password");
        b=false;
    }
    else if(firstName.getText().toString().trim().length()==0)
    {
        firstName.setError("FirstName can't be Empty");
        b=false;
    }

    else if(lastName.getText().toString().trim().length()==0)
    {
        firstName.setError("Last name cant be empty");
        b=false;
    }

    else if(phone.getText().toString().trim().length()<10 || phone.getText().toString().trim().length()>10)
    {
        phone.setError("PHone no. should be of 10 number");
        b=false;
    }


return b;
}




}
