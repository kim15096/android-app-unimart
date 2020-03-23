package my.app.uni.main.account;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.hdodenhof.circleimageview.CircleImageView;
import my.app.uni.R;
import my.app.uni.main.account.settings.SettingsFragment;
import my.app.uni.slidinglayer.SlidingLayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.*;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import android.widget.TextView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.widget.Toast;
import android.net.Uri;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment {

    private TextView accountName, vouchText;
    private ImageView settingBtn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private static final String TAG = "MyActivity";
    private FirebaseUser firebaseUser;
    private CircleImageView circularImageView, profilepic_2, marketProfilePic;
    private Integer vouchNo;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    StorageReference imagesRef = storageRef.child("sell_posts");
    private RecyclerView recyclerView;
    private String imageURL_STR = null, school;
    private LottieAnimationView profileLoading;
    private ProgressDialog dialog;
    private ViewPager viewPager;
    private SectionsPagerAdapter_Account mSectionsAdapter;
    private TabLayout mTabLayout;
    private DocumentReference docref;
    private UploadTask uploadTask;
    private FloatingActionButton fab_add;
    private SharedPreferences preferences;
    public SlidingLayer accountItemSlider;
    private Long postCount, boughtCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        school = preferences.getString("school", "");
        school = "UC Davis";
        firebaseUser = mAuth.getInstance().getCurrentUser();
        docref = db.collection("California").document(school).collection("Users").document(firebaseUser.getUid());

        View view = inflater.inflate(R.layout.account_frag, container, false);

        accountItemSlider = view.findViewById(R.id.AccountItemSlider);

        String DB_name = firebaseUser.getDisplayName();
        accountName = view.findViewById(R.id.account_Name);
        accountName.setText(DB_name);

        //TextView myText = view.findViewById(R.id.account_uniname);

        Animation anim = new ScaleAnimation(
                1f, 1.1f, // Start and end values for the X axis scaling
                1f, 1.1f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling// Needed to keep the result of the animation
        anim.setDuration(750);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        //myText.startAnimation(anim);

        mTabLayout = view.findViewById(R.id.account_TabLayout);
        
        settingBtn = view.findViewById(R.id.account_SettingsBtn);
        profileLoading = view.findViewById(R.id.profilepic_animation);
        circularImageView = view.findViewById(R.id.account_profilePic);


        if (getActivity()!=null && isAdded()) {
            docref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot.get("image URL") != null) {
                        String imageURL = documentSnapshot.get("image URL").toString();
                        Picasso.get().load(imageURL).into(circularImageView);
                    }

                }
            });
        }

        db.collection("California").document(school).collection("Sell Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (final DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                    docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.get("image URL") != null) {
                                imageURL_STR = documentSnapshot.get("image URL").toString();

                            }

                            postCount = (Long) documentSnapshot.get("post count");
                            boughtCount = (Long) documentSnapshot.get("bought");

                            mTabLayout.getTabAt(1).setText("Sell  " + postCount.toString());
                            mTabLayout.getTabAt(0).setText("Buy  " + boughtCount.toString());

                        }
                    });

                }
            }
        });

        //Tabs
        viewPager = view.findViewById(R.id.account_Viewpager);
        mSectionsAdapter = new SectionsPagerAdapter_Account(getChildFragmentManager());
        viewPager.setAdapter(mSectionsAdapter);
        viewPager.setCurrentItem(0);

        mTabLayout.setupWithViewPager(viewPager);
        int tabIconColor = ContextCompat.getColor(getContext(), R.color.main_theme);
        mTabLayout.setTabTextColors(getResources().getColor(R.color.dark_gray),
                getResources().getColor(R.color.main_theme));

        circularImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String[] menuOptions = {"Change Profile Picture", "Remove Profile Picture"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        builder.setItems(menuOptions, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    CropImage.activity()
                                            .setActivityTitle("Edit")
                                            .setCropMenuCropButtonTitle("Save")
                                            .setGuidelines(CropImageView.Guidelines.ON)
                                            .setCropShape(CropImageView.CropShape.OVAL)
                                            .setMinCropResultSize(256, 256) // update image quality
                                            .setAspectRatio(1, 1)
                                            .setInitialCropWindowPaddingRatio((float) 0.1)
                                            .start(getContext(), AccountFragment.this);


                                } else if (which == 1) {
                                    docref.update("image URL",null).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            circularImageView.setImageResource(R.drawable.ic_default_profilepic);
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.show();

                    }
                });

        settingBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Fragment settingsFragment = new SettingsFragment();
                                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right)
                                        .replace(R.id.fragment_container, settingsFragment).addToBackStack(null).commit();
                            }
                        });


        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                profileLoading.setVisibility(View.VISIBLE);

                Uri resultUri = result.getUri();
                final StorageReference filepath = storageRef.child("California").child(school).child(firebaseUser.getUid()).child("Profile Image.jpg");
                uploadTask = filepath.putFile(resultUri);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        // Continue with the task to get the download URL
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            imageURL_STR = downloadUri.toString();
                            profileLoading.setVisibility(View.GONE);
                            docref.update("image URL", imageURL_STR);
                        } else {
                            Toast.makeText(getContext(), "DOWNLOAD URL DOESNT WORK FFS", Toast.LENGTH_SHORT).show();
                            profileLoading.setVisibility(View.GONE);
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                profileLoading.setVisibility(View.GONE);
            }
        }
    }


}
