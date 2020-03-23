package my.app.uni.login;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.HashMap;
import java.util.Map;

import my.app.uni.R;

public class RegisterFragment1 extends Fragment {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ViewPager viewPager;
    private ImageButton nextBtn, nextBtn2;
    private String email, full_name, password, passwordConfirm;
    private EditText editTextEmail, editTextFname, editTextPassword, editTextPasswordConfirm;
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private MaterialSpinner spinner;
    private SharedPreferences preferences;
    private Long totalUsers;
    private String selectedSchool;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.register_1_frag, container, false);

        nextBtn = view.findViewById(R.id.register_nextBtn1);
        editTextEmail = view.findViewById(R.id.Registration_et_email);
        editTextFname = view.findViewById(R.id.registration_fname);
        editTextPassword = view.findViewById(R.id.register_password);
        editTextPasswordConfirm = view.findViewById(R.id.register_confirmPassword);

        //School List

        final Map<String, String> map = new HashMap<>();
        map.put("ucdavis", "UC Davis");
        map.put("berkeley", "UC Berkeley");
        map.put("stanford", "Stanford U");

        //testing
        map.put("gmail", "UC Davis");

        //

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                email = editTextEmail.getText().toString().trim();
                full_name = editTextFname.getText().toString().trim();
                password = editTextPassword.getText().toString().trim();
                passwordConfirm = editTextPasswordConfirm.getText().toString().trim();


                if (TextUtils.isEmpty(full_name)) {
                    Snackbar.make(view, "Please enter your name", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Snackbar.make(view, "Please enter your school email", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                else {
                    firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if (task.isSuccessful()) {
                                int a = task.getResult().getSignInMethods().size();
                                if (a == 1) {
                                    Snackbar.make(view, "Already registered email", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    if (!email.contains(".edu")) {
                                        Snackbar.make(view, "Please use a valid school email", Snackbar.LENGTH_SHORT).show();
                                        return;
                                    } else {
                                        for (Map.Entry<String, String> entry : map.entrySet()) {
                                            System.out.println(entry.getKey());
                                            if (email.contains(entry.getKey())) {
                                                selectedSchool = entry.getValue();
                                                validSchool();
                                                return;
                                            }
                                        }
                                        Snackbar.make(view, "Your School is not supported yet!", Snackbar.LENGTH_SHORT).show();
                                        return;
                                    }

                                }
                            }
                        }
                    });
                }
            }

                private void validSchool(){
                    if (TextUtils.isEmpty(password)) {
                        Snackbar.make(view, "Please enter a password", Snackbar.LENGTH_SHORT).show();
                        return;
                    } else if (password.length() < 8) {
                        Snackbar.make(view, "Password must be at least 8 characters", Snackbar.LENGTH_SHORT).show();
                        return;
                    } else if (TextUtils.isEmpty(passwordConfirm)) {
                        Snackbar.make(view, "Please confirm your password", Snackbar.LENGTH_SHORT).show();
                        return;
                    } else if (!password.equals(passwordConfirm)) {
                        Snackbar.make(view, "Passwords do not match", Snackbar.LENGTH_SHORT).show();
                        return;
                    } else if (selectedSchool != null){
                        change_now();
                    }
                }

                // changing activity to registration2
                private void change_now() {
                    final ProgressDialog dialog = new ProgressDialog(getContext());
                    dialog.show();
                    dialog.setMessage("Creating account");
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // only store information when registration success
                                        FirebaseFirestore.getInstance().collection("California").document(selectedSchool).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                totalUsers = (long) documentSnapshot.get("Total Users");
                                                totalUsers = totalUsers + 1;
                                                FirebaseFirestore.getInstance().collection("California").document(selectedSchool).update("Total Users", totalUsers);
                                            }
                                        });

                                        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                                        preferences.edit().putString("school", selectedSchool).apply();

                                        firebaseUser = firebaseAuth.getCurrentUser();
                                        firebaseUser.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            dialog.hide();

                                                            RegisterFragment3 registerFragment3 = new RegisterFragment3();
                                                            RegisterFragment2 registerFragment2 = new RegisterFragment2();
                                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                                    .add(R.id.regFragContainer, registerFragment3, "reg_3").hide(registerFragment3).commit();
                                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                                    .add(R.id.regFragContainer, registerFragment2, "reg_2").setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left).show(registerFragment2).commit();

                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(getContext(), "Try again.", Toast.LENGTH_SHORT).show();
                                        dialog.hide();
                                    }
                                }
                            });
                }

            });

        return view;
    }
}
