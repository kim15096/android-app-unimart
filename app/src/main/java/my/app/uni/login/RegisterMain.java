package my.app.uni.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import my.app.uni.R;

public class RegisterMain extends AppCompatActivity {

    private RegisterFragment1 registerFragment1 = new RegisterFragment1();
    private RegisterFragment3 registerFragment3 = new RegisterFragment3();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main_act);


        getSupportFragmentManager().beginTransaction().add(R.id.regFragContainer, registerFragment1 ,"reg_1").show(registerFragment1).commit();

    }


    @Override
    public void onBackPressed() {

        if (getCurrentFocus() == null){

            Fragment CurrentFrag = getSupportFragmentManager().findFragmentById(R.id.regFragContainer);

            if (CurrentFrag == registerFragment1) {
                Intent mainIntent = new Intent(RegisterMain.this, Login.class);
                RegisterMain.this.startActivity(mainIntent);
                RegisterMain.this.finish();
                overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_right);
            }
        }
        else {
            getCurrentFocus().clearFocus();
        }


    }
}