package my.app.uni.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import my.app.uni.R;
import my.app.uni.main.Main;

public class Login extends AppCompatActivity {
    // initializing class variables
    private EditText email, password;
    private String emailInput, passwordInput;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Button loginBtn;
    private TextView registerBtn;
    private CardView login_cardview;
    private LottieAnimationView loadingAnimation;
    boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_act);
        // assigning variables
        login_cardview = findViewById(R.id.login_cardview);
        email = findViewById(R.id.Login_et_email);
        password = findViewById(R.id.login_et_password);
        doubleBackToExitPressedOnce = false;
        firebaseAuth = FirebaseAuth.getInstance();
        registerBtn = findViewById(R.id.login_registerBtn);
        loginBtn = findViewById(R.id.login_ib_loginBtn);
        loadingAnimation = findViewById(R.id.login_animation);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeRegistration(view);
            }
        });


    }

    public void userLogin(final View view){
        //initializing variables
        loadingAnimation.setVisibility(View.VISIBLE);
        login_cardview.setVisibility(View.INVISIBLE);

        emailInput = email.getText().toString().trim();
        passwordInput = password.getText().toString().trim();
        // error handling: checking if entry field is empty
        if (TextUtils.isEmpty(emailInput)){
            Snackbar.make(view,"Please enter your email address.", Snackbar.LENGTH_SHORT).show();
            loadingAnimation.setVisibility(View.INVISIBLE);
            login_cardview.setVisibility(View.VISIBLE);
            return;
        }
        else if (TextUtils.isEmpty(passwordInput)){
            Snackbar.make(view,"Please enter your password.", Snackbar.LENGTH_SHORT).show();
            loadingAnimation.setVisibility(View.INVISIBLE);
            login_cardview.setVisibility(View.VISIBLE);
            return;
        }
        // if success, change to home activity
        firebaseAuth.signInWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            preferences = getSharedPreferences("checkbox_remember", MODE_PRIVATE);
                            editor = preferences.edit();
                            editor.putString("remember", "true")
                                    .putString("email", emailInput)
                                    .putString("password", passwordInput)
                                    .apply();
                            changeNow();
                        }
                        else{
                            Snackbar.make(view,"Invalid email address or password.", Snackbar.LENGTH_SHORT).show();
                            loadingAnimation.setVisibility(View.INVISIBLE);
                            login_cardview.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    // changing activity to home
    public void changeNow(){
        Intent mainIntent = new Intent(Login.this, Main.class);
        Login.this.startActivity(mainIntent);
        Login.this.finish();
        this.overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_left);
    }

    // changing activity to register
    public void changeRegistration(View view){
        Intent mainIntent = new Intent(Login.this,RegisterMain.class);
        Login.this.startActivity(mainIntent);
        Login.this.finish();
        this.overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_left);
    }

    }
