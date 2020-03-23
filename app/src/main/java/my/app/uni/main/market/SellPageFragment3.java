package my.app.uni.main.market;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.HashMap;
import java.util.Map;

import my.app.uni.R;

public class SellPageFragment3 extends Fragment
{
    private ImageView backBtn3, finalImage, prevImage;
    private Button sellBtn;
    private String current_user_id;
    private ViewPager viewPager;
    private EditText priceInput, sell_desc_Input, titleInput;
    private Long postCount, price;
    private TextView finalPrice, finalTitle, finalDesc, keyword;
    private DocumentReference docref;
    private String sell_desc = "this is post description", postImageURL = null;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private MaterialSpinner conditionSpinner, categorySpinner;
    private UploadTask uploadTask;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private LottieAnimationView loader;
    private static final String[] conditionList = {
            "Select condition",
            "New",
            "Used - Like new",
            "Used - Good",
            "Used - Fair"
    };

    //private PhotoView finalImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.post_page2_frag, container, false);

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        loader = view.findViewById(R.id.sellpage_loader);

        current_user_id = firebaseAuth.getCurrentUser().getUid();
        finalImage = view.findViewById(R.id.sellpage_imageFinal);
        keyword = view.findViewById(R.id.sellpage_keyword);

        titleInput = getParentFragment().getView().findViewById(R.id.sellpage_title);
        sell_desc_Input = getParentFragment().getView().findViewById(R.id.sellpage_description);
        prevImage = getParentFragment().getView().findViewById(R.id.sellpage_imageHolder1);


        finalImage.setImageURI(Uri.parse(prevImage.getTag().toString()));

        conditionSpinner = view.findViewById(R.id.sellpage_spinner);
        conditionSpinner.setItems(conditionList);
        conditionSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_SHORT).show();
            }
        });


        final String school = "UC Davis";

        firebaseFirestore.collection("California").document(school).collection("Users").document(current_user_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                postCount = documentSnapshot.getLong("post count");
            }
        });
        docref = firebaseFirestore.collection("California").document(school).collection("Users").document(current_user_id);

        backBtn3 = view.findViewById(R.id.sellpage_backBtn3);
        backBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getParentFragment().getChildFragmentManager().popBackStack();

            }
        });

        /*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2){
                    String priceStr = priceInput.getText().toString();
                    String titleStr = titleInput.getText().toString();
                    String descStr =  sell_desc_Input.getText().toString();
                    finalPrice.setText("$"+priceStr);
                    finalDesc.setText(descStr);
                    finalTitle.setText(titleStr);


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

        sellBtn = view.findViewById(R.id.sellpage_sellBtn);
        sellBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sellBtn.setClickable(false);
                sellBtn.setVisibility(View.INVISIBLE);
                loader.setVisibility(View.VISIBLE);

                sell_desc = sell_desc_Input.getText().toString();

                if (!TextUtils.isEmpty(sell_desc) && prevImage.getTag() != null) {

                    //price = Long.parseLong(priceInput.getText().toString());

                    final StorageReference filePath = storageRef.child("California").child(school).child(current_user_id).child(postCount.toString());
                    uploadTask = filePath.putFile(Uri.parse(prevImage.getTag().toString()));

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();

                            }
                            // Continue with the task to get the download URL
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                postImageURL = downloadUri.toString();
                                final FieldValue timestamp = FieldValue.serverTimestamp();

                                final Map<String, Object> postMap = new HashMap<>();
                                postMap.put("image_url", postImageURL);
                                postMap.put("title", titleInput.getText().toString());
                                postMap.put("description", sell_desc);
                                postMap.put("userID", current_user_id);
                                postMap.put("post_id", current_user_id + postCount);
                                postMap.put("category", "Electronics");
                                postMap.put("timestamp", timestamp);
                                postMap.put("keyword", keyword.getText().toString());
                                postMap.put("price", 0);

                                firebaseFirestore.collection("California").document(school).collection("Sell Posts").document(current_user_id + postCount)
                                        .set(postMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        postCount = postCount + 1;
                                        docref.update("post count", postCount).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(view, "Post has been added!", Snackbar.LENGTH_SHORT).show();
                                                getChildFragmentManager().popBackStack();
                                                getFragmentManager().popBackStack();

                                                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
                                                bottomNavigationView.animate().setInterpolator(new LinearInterpolator()).translationY(0).setDuration(150);
                                            }
                                        });

                                    }
                                });


                            } else {
                                Toast.makeText(getContext(), "DOWNLOAD URL DOESNT WORK FFS", Toast.LENGTH_SHORT).show();
                                loader.setVisibility(View.INVISIBLE);
                                sellBtn.setVisibility(View.VISIBLE);
                                sellBtn.setClickable(true);


                            }

                        }
                    });

                } else {
                    sellBtn.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.INVISIBLE);
                    Snackbar.make(view, "Please upload an image and enter description.", Snackbar.LENGTH_SHORT).show();
                    sellBtn.setClickable(true);

                }
            }}

        );


        return view;
    }

   /* private void loadImage() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String imageStr = preferences.getString("imageUri", "");

        Uri imageUri = Uri.parse(imageStr);
        finalImage.setImageURI(imageUri);
    }*/

}
