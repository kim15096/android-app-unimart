package my.app.uni.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import my.app.uni.R;

public class RegisterFragment2 extends Fragment {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String full_name_text;
    private EditText full_name;
    private SharedPreferences preferences;
    private LottieAnimationView reg_dot_1, reg_dot_2, reg_dot_3;
    private ProgressBar progressBar1, progressBar2, progressBar3;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_2_frag, container, false);
        reg_dot_1 = getActivity().findViewById(R.id.reg_dot_1);
        reg_dot_2 = getActivity().findViewById(R.id.reg_dot_2);
        reg_dot_3 = getActivity().findViewById(R.id.reg_dot_3);
        progressBar1 = getActivity().findViewById(R.id.reg_progress_1);
        progressBar2 = getActivity().findViewById(R.id.reg_progress_2);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!firebaseAuth.getCurrentUser().isEmailVerified()) {
                    FirebaseAuth.getInstance().getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            checkVerified();

                        }

                    });
                    handler.postDelayed(this, 5000);

                }

            }
        }, 5000);

        progressBar1.setProgress(100);
        reg_dot_1.loop(false);
        reg_dot_2.setAlpha(1f);
        reg_dot_2.playAnimation();



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAuth.getInstance().getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                checkVerified();
            }
        });
    }

    private void checkVerified() {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.isEmailVerified()) {
                    full_name = getActivity().findViewById(R.id.registration_fname);
                    full_name_text = full_name.getText().toString().trim();
                    EditText email = getActivity().findViewById(R.id.Registration_et_email);
                    String emailStr = email.getText().toString().trim();

                    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(full_name_text).build();

                    firebaseUser.updateProfile(profileUpdates);
                    String userID = firebaseUser.getUid();
                    final String region = "California";

                    preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

                    final String school = preferences.getString("school", "");

                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> userString = new HashMap<>();
                    userString.put("name", full_name_text);
                    userString.put("userID", userID);
                    userString.put("school", school);
                    userString.put("image URL", null);

                    db.collection(region).document("UC Davis").collection("Users").document(firebaseUser.getUid())
                            .set(userString)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    db.collection(region).document(school).collection("Users").document(firebaseUser.getUid()).update("post count", 0);
                                    db.collection(region).document(school).collection("Users").document(firebaseUser.getUid()).update("vouch", 0);
                                    db.collection(region).document(school).collection("Users").document(firebaseUser.getUid()).update("sold", 0);
                                    db.collection(region).document(school).collection("Users").document(firebaseUser.getUid()).update("bought", 0).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    Fragment registerFragment3 = getActivity().getSupportFragmentManager().findFragmentByTag("reg_3");
                                                    Fragment currentFrag = getActivity().getSupportFragmentManager().findFragmentByTag("reg_2");
                                                    reg_dot_3.playAnimation();
                                                    progressBar2.setProgress(100);
                                                    reg_dot_2.loop(false);
                                                    reg_dot_3.setAlpha(1f);

                                                    handler.removeCallbacksAndMessages(null);

                                                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left).hide(currentFrag).show(registerFragment3).commit();

                                                }
                                            });

                                }
                            });



        }
    }
}