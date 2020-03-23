package my.app.uni.main;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import my.app.uni.main.chat.ChatFragment;
import my.app.uni.main.explore.ExploreFragment;
import my.app.uni.main.market.MarketFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import my.app.uni.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import my.app.uni.main.account.AccountFragment;
import my.app.uni.main.market.SellPageFragment2;

public class Main extends AppCompatActivity {
    // SellFragment test = new SellFragment();

    private TextView accountName, vouchText;
    private ImageView settingBtn;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private FirebaseAuth mAuth;
    private static final String TAG = "MyActivity";
    private String vouchNo;
    private FirebaseUser firebaseUser;
    private ViewPager viewPager;
    private FragmentManager fm = getSupportFragmentManager();
    private FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    private final MarketFragment marketFragment = new MarketFragment();
    private final AccountFragment accountFragment = new AccountFragment();
    private ExploreFragment exploreFragment = new ExploreFragment();
    private ChatFragment chatFragment = new ChatFragment();
    private BottomNavigationView bottomNavigationView;
    private ImageView backBtn, basketBackBtn;
    private View DarkView;
    private AlertDialog.Builder builder;
    private  final Fragment[] currentFragment = {marketFragment};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);

        backBtn = findViewById(R.id.market_item_backBtn);
        basketBackBtn = findViewById(R.id.basket_backBtn);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, exploreFragment, "explore").hide(exploreFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, accountFragment, "account").hide(accountFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, chatFragment, "chat").hide(chatFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, marketFragment, "market").show(marketFragment).commit();

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_market);
            // change to whichever id should be default
        }
        final Window window = getWindow();
        final View decorView = getWindow().getDecorView();



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){


                    case R.id.nav_rewards:
                        getSupportFragmentManager().beginTransaction().hide(currentFragment[0]).show(exploreFragment).commit();
                        currentFragment[0] = exploreFragment;

                        bottomNavigationView.animate().setInterpolator(new LinearInterpolator()).translationY(0).setDuration(150);

                        return true;



                    case R.id.nav_chat:
                        getSupportFragmentManager().beginTransaction().hide(currentFragment[0]).show(chatFragment).commit();
                        currentFragment[0] = chatFragment;



                        return true;


                    case R.id.nav_add:

                        bottomNavigationView.animate().setInterpolator(new LinearInterpolator()).translationY(150).setDuration(150);

                        final SellPageFragment2 sellPage = new SellPageFragment2();

                        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, sellPage, "sellpage")
                                .setCustomAnimations(R.anim.slide_in_bottom,R.anim.fade_out).show(sellPage).hide(currentFragment[0]).addToBackStack(null).commit();

                        break;


                    case R.id.nav_market:
                        getSupportFragmentManager().beginTransaction().hide(currentFragment[0]).show(marketFragment).commit();
                        currentFragment[0] = marketFragment;


                        return true;

                    case R.id.nav_account:
                        getSupportFragmentManager().beginTransaction().hide(currentFragment[0]).show(accountFragment).commit();
                        currentFragment[0] = accountFragment;

                        bottomNavigationView.animate().setInterpolator(new LinearInterpolator()).translationY(0).setDuration(150);


                        return true;

                }
                return false;
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marketFragment.slidingLayer.closeLayer(true);
            }
        });

        basketBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marketFragment.basketSliding.closeLayer(true);
            }
        });


    }



    boolean doubleBackToExitPressedOnce = false;





    @Override
    public void onBackPressed() {

        if (marketFragment.slidingLayer.isOpened()) {
            marketFragment.slidingLayer.closeLayer(true);
            return;
        }

        if (currentFragment[0] == accountFragment && accountFragment.accountItemSlider.isOpened()){
            accountFragment.accountItemSlider.closeLayer(true);
            return;
        }

        if (marketFragment.basketSliding.isOpened()){
            marketFragment.basketSliding.closeLayer(true);
            return;
        }

        if (currentFragment[0] != accountFragment && getSupportFragmentManager().getBackStackEntryCount() > 0 ) {


            builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Are you sure you want to exit?");
            builder.setMessage("Changes will not be saved.");
            builder.setNegativeButton("Discard",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getSupportFragmentManager().popBackStack();

                            bottomNavigationView.animate().setInterpolator(new LinearInterpolator()).translationY(0).setDuration(150);

                        }
                    });
            builder.setPositiveButton("Keep Writing", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }

        else {
            getSupportFragmentManager().popBackStack();
            bottomNavigationView.animate().setInterpolator(new LinearInterpolator()).translationY(0).setDuration(150);
        }

        if (chatFragment.getChildFragmentManager().getBackStackEntryCount() == 1){
            chatFragment.getChildFragmentManager().popBackStack();
            bottomNavigationView.animate().setInterpolator(new LinearInterpolator()).translationY(0).setDuration(150);
        }


        if (marketFragment.getChildFragmentManager().getBackStackEntryCount() == 1){
            marketFragment.getChildFragmentManager().popBackStack();
            bottomNavigationView.animate().setInterpolator(new LinearInterpolator()).translationY(0).setDuration(150);
            }

        if (marketFragment.getChildFragmentManager().getBackStackEntryCount() == 2){
            marketFragment.getChildFragmentManager().popBackStack();
        }


        if(accountFragment.getChildFragmentManager().getBackStackEntryCount() == 1){
            accountFragment.getChildFragmentManager().popBackStack();
            bottomNavigationView.animate().setInterpolator(new LinearInterpolator()).translationY(0).setDuration(150);

        }

        if(accountFragment.getChildFragmentManager().getBackStackEntryCount() == 2){
            accountFragment.getChildFragmentManager().popBackStack();

        }


/*        if (getSupportFragmentManager().getBackStackEntryCount() == 0 && getFragmentManager().getBackStackEntryCount()==0
                && marketFragment.getChildFragmentManager().getBackStackEntryCount() == 0 && accountFragment.getChildFragmentManager().getBackStackEntryCount() ==0 && chatFragment.getChildFragmentManager().getBackStackEntryCount() == 0){
            super.onBackPressed();
            return;
        }*/


        if (getCurrentFocus() != null) {

            getCurrentFocus().clearFocus();

        }



    }

}

