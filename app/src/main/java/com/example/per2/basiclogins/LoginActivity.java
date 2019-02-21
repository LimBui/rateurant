package com.example.per2.basiclogins;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.per2.basiclogins.CreateAccountActivity;
import com.example.per2.basiclogins.Credentials;
import com.example.per2.basiclogins.R;
import com.example.per2.basiclogins.RestaurantListActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextusername;
    private EditText editTextpassword;
    private Button login;
    private TextView createAccount;

    public static final String EXTRA_SENT_MESSAGE = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        wireWidgets();

        //initialize Backendless connection
        Backendless.initApp(this, Credentials.APP_ID, Credentials.APP_KEY);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginToBackendless();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //when createAccount textView is clicked, goes to Create Account page
                Intent createAccountPage = new Intent(LoginActivity.this, CreateAccountActivity.class);

                //get the text from the editText
                String username = editTextusername.getText().toString();

                //package the text into the intent
                createAccountPage.putExtra(EXTRA_SENT_MESSAGE, username);

                //start the new activity
                startActivityForResult(createAccountPage, 1234);
            }
        });
    }

    private void loginToBackendless() {
        String login = editTextusername.getText().toString();
        String password = editTextpassword.getText().toString();
        Backendless.UserService.login(login, password,
                new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        //Start the new activity here because this
                        //method is called when the login is complete
                        //and successful
                        Toast.makeText(LoginActivity.this, response.getEmail() + " Logged In", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, RestaurantListActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(LoginActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void wireWidgets() {
        editTextusername = findViewById(R.id.edittext_login_username);
        editTextusername.setText(getIntent().getStringExtra(CreateAccountActivity.EXTRA_USER_INFO));
        editTextpassword = findViewById(R.id.edittext_login_password);
        login = findViewById(R.id.button_login_login);
        createAccount = findViewById(R.id.textview_login_createaccount);
    }
}
