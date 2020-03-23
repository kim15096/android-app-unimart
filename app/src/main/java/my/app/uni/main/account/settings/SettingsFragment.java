package my.app.uni.main.account.settings;

import android.app.AlertDialog;
import android.app.Person;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import my.app.uni.R;
import my.app.uni.login.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {
    private ImageView backBtn;
    private ImageButton logoutBtn;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private AlertDialog.Builder builder;
    private CardView notiCard, personalCard, paymentCard, supportCard, addressCard, vouchCard, faqCard, tnsCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_main_frag, container, false);

        logoutBtn = view.findViewById(R.id.setting_logoutBtn);
        backBtn = view.findViewById(R.id.settings_backBtn);

        notiCard = view.findViewById(R.id.notification_card);
        personalCard = view.findViewById(R.id.setting_personal_info_card);
        paymentCard = view.findViewById(R.id.payment_card);
        supportCard = view.findViewById(R.id.support_card);
        addressCard = view.findViewById(R.id.addresses_card);
        vouchCard = view.findViewById(R.id.voucher_card);
        faqCard = view.findViewById(R.id.faq_card);
        tnsCard = view.findViewById(R.id.tns_card);

        final BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.animate().setInterpolator(new LinearInterpolator()).translationY(150).setDuration(150);

        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                getFragmentManager().popBackStack();
                bottomNavigationView.animate().setInterpolator(new LinearInterpolator()).translationY(0).setDuration(150);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Do you really want to sign out?");
                builder.setNegativeButton("Sign Out",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseAuth.signOut();
                                Intent change2Login = new Intent(SettingsFragment.this.getActivity(), Login.class);
                                SettingsFragment.this.startActivity(change2Login);
                                SettingsFragment.this.getActivity().finish();
                                getActivity().overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
                            }
                        });
                builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        personalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonalFrag personalFrag = new PersonalFrag();
                getChildFragmentManager().beginTransaction()
                        .add(R.id.setting_child_container, personalFrag).replace(R.id.setting_child_container, personalFrag).addToBackStack(null).commit();
            }
        });

        paymentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentFrag paymentFrag = new PaymentFrag();
                getChildFragmentManager().beginTransaction()
                        .add(R.id.setting_child_container, paymentFrag).replace(R.id.setting_child_container, paymentFrag).addToBackStack(null).commit();

            }
        });

        notiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationFrag notificationFrag = new NotificationFrag();
                getChildFragmentManager().beginTransaction()
                        .add(R.id.setting_child_container, notificationFrag).replace(R.id.setting_child_container, notificationFrag).addToBackStack(null).commit();

            }
        });

        supportCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SupportFrag supportFrag = new SupportFrag();
                getChildFragmentManager().beginTransaction()
                        .add(R.id.setting_child_container, supportFrag).replace(R.id.setting_child_container, supportFrag).addToBackStack(null).commit();

            }
        });

        faqCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FAQFrag faqFrag = new FAQFrag();
                getChildFragmentManager().beginTransaction()
                        .add(R.id.setting_child_container, faqFrag).replace(R.id.setting_child_container, faqFrag).addToBackStack(null).commit();

            }
        });

        tnsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TnSFrag tnSFrag = new TnSFrag();
                getChildFragmentManager().beginTransaction()
                        .add(R.id.setting_child_container, tnSFrag).replace(R.id.setting_child_container, tnSFrag).addToBackStack(null).commit();

            }
        });

        vouchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PromoFrag promoFrag = new PromoFrag();
                getChildFragmentManager().beginTransaction()
                        .add(R.id.setting_child_container, promoFrag).replace(R.id.setting_child_container, promoFrag).addToBackStack(null).commit();

            }
        });

        addressCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressFrag addressFrag = new AddressFrag();
                getChildFragmentManager().beginTransaction()
                        .add(R.id.setting_child_container, addressFrag).replace(R.id.setting_child_container, addressFrag).addToBackStack(null).commit();

            }
        });


        return view;
    }

}
