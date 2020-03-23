package my.app.uni.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import my.app.uni.R;
import my.app.uni.main.Main;

public class Splash extends AppCompatActivity {

    // initializing variables

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    //private final int SPLASH_DISPLAY_LENGTH = 2500;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            splash_screen_toHome();
        }
        else if (currentUser != null && !currentUser.isEmailVerified()) {
            //splash_to_verification();
        }
        else {
            splash_screen_toLogin();
        }
    }
    // New Handler to start the Menu-Activity and close this Splash-Screen after some seconds.

    private void splash_screen_toHome() {

        Intent mainIntent = new Intent(Splash.this, Main.class);
        Splash.this.startActivity(mainIntent);
        Splash.this.finish();
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
    }

    private void splash_screen_toLogin() {

        Intent mainIntent = new Intent(Splash.this, Login.class);
        Splash.this.startActivity(mainIntent);
        Splash.this.finish();
        overridePendingTransition(R.transition.fade_in, R.transition.fade_out);

    }
}