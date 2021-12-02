package com.example.timesheet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {

    public static final String KEY_NAME = "KEY_NAME";

    EditText userName;
    EditText edPsw;
    Button btnSubmit;
    //Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = this.<EditText>findViewById(R.id.userName);
        edPsw = findViewById(R.id.userName);
        btnSubmit = findViewById(R.id.btnSubmit);
        //btnSignIn = findViewById(R.id.btnSignIn);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        btnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(userName.getText())) {

                    userName.setError("Adja meg felhasználónevét!");

                } else if (TextUtils.isEmpty(edPsw.getText())) {

                    edPsw.setError("Írja be jelszavát!");
                } else {
                    ParseUser.logInInBackground(userName.getText().toString(), edPsw.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if (parseUser != null) {
                                Toast.makeText(MainActivity.this, getString(R.string.signin), Toast.LENGTH_LONG).show();
                                Intent i = new Intent(MainActivity.this, MainActivity2.class);

                                i.putExtra(KEY_NAME,
                                        userName.getText().toString());

                                startActivity(i);
                            } else {
                                ParseUser.logOut();
                                Toast.makeText(MainActivity.this, "Szívás!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

/*btnSignIn.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View v) {
        if (!userName.getText().toString().isEmpty() && !edPsw.getText().toString().isEmpty()){
            ParseUser user = new ParseUser();
            user.setUsername(userName.getText().toString());
            user.setPassword(edPsw.getText().toString());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null){
                        Toast.makeText(getApplicationContext(),"Sign Up Successful",Toast.LENGTH_LONG).show();
                        startMainActivity2();
                    }else {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
});*/

    }

    private void startMainActivity2() {
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intent);
    }

}
