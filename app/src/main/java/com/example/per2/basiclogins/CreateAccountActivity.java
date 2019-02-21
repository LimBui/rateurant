package com.example.per2.basiclogins;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextEmail;
    private Button buttonCreateAccount;
    public static final String EXTRA_NAME_INFO = "nameinfo";
    public static final String EXTRA_USER_INFO = "logininfo";
    public static final String EXTRA_PW_INFO = "pwinfo";
    public static final String EXTRA_CPW_INFO = "cpwinfo";
    public static final String EXTRA_EMAIL_INFO = "emailinfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        wireWidgets();
        prefillUserName();
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccountOnBackendless();
            }
        });
    }

    private void registerAccountOnBackendless() {
        //verfiy all the field are filled out & passwords are same
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        String username = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();
        String name = editTextName.getText().toString();

        if(allFieldsValid(password, confirmPassword, username, email, name)){
            // do not forget to call Backendless.initApp when your app initializes

            BackendlessUser user = new BackendlessUser();
            user.setProperty("name", name);
            user.setProperty("username", username);
            user.setProperty( "email", email );
            user.setPassword( password );

            Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>()
            {
                public void handleResponse( BackendlessUser registeredUser )
                {
                    Toast.makeText(CreateAccountActivity.this, registeredUser.getUserId()+ " has registered.", Toast.LENGTH_SHORT ).show();;
                    finish();
                    //would be nie to turn the username to the lognactivity
                    // user has been registered and now can login
                }

                public void handleFault( BackendlessFault fault )
                {
                    // an error has occurred, the error code can be retrieved with fault.getCode()
                }
            } );

        }
        //make the registration call

        //return to the LoginActivity
    }

    private boolean allFieldsValid(String password, String confirmPassword, String username, String email, String name) {
        return password.equals(confirmPassword) && username.length() > 0;
    }

    private void prefillUserName() {
        String username = getIntent().getStringExtra(LoginActivity.EXTRA_SENT_MESSAGE);
        if(username != null) {
            editTextUsername.setText(username);
        }
    }

    private void wireWidgets() {
        editTextUsername = findViewById(R.id.edittext_create_username);
        editTextName = findViewById(R.id.edittext_create_name);
        editTextPassword = findViewById(R.id.edittext_create_password);
        editTextConfirmPassword = findViewById(R.id.edittext_create_confirmpassword);
        editTextEmail = findViewById(R.id.edittext_create_email);
        buttonCreateAccount = findViewById(R.id.button_create_account);
    }
}