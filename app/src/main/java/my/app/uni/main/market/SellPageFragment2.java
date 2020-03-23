package my.app.uni.main.market;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import my.app.uni.R;

import static android.app.Activity.RESULT_OK;

public class SellPageFragment2 extends Fragment
{
    private Button nextBtn2;
    private ImageView backBtn2,imageHolder1, uploadBtn, imageHolder2;
    private ViewPager viewPager;
    private SharedPreferences preferences;
    private AlertDialog.Builder builder;
    private TextView nextBtn, sellpage_pageTitle;
    private Uri sellImgUri;
    private MaterialSpinner categorySpinner;
    private static final String[] categoryList = {
            "Clothing",
            "Electronics",
            "Food",
            "Household",
            "Housing",
            "Miscellaneous",
            "School",
            "Vehicle"
    };
    private Toolbar sellpage_toolbar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.post_page1_frag, container, false);

        imageHolder1 = view.findViewById(R.id.sellpage_imageHolder1);
        nextBtn = view.findViewById(R.id.sellpage2_next);
        uploadBtn = view.findViewById(R.id.sellpage_uploadBtn);


        final BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);

        final ScrollView scrollView = view.findViewById(R.id.sellpage2_scrollView);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        sellpage_toolbar = view.findViewById(R.id.sellpage_toolbar);
        sellpage_pageTitle = view.findViewById(R.id.sellpage_createlisting);

        backBtn2 = view.findViewById(R.id.sellpage_backBtn2);
        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Are you sure you want to exit?");
                builder.setMessage("Changes will not be saved.");
                builder.setNegativeButton("Discard",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getFragmentManager().popBackStack();
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
        });

        categorySpinner = view.findViewById(R.id.sellpage_categorySpinner);
        categorySpinner.setItems(categoryList);
        categorySpinner.setBackgroundResource(R.drawable.sellpost_textbg);
        categorySpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner materialSpinner, int position, long id, String item) {
                if (position == 0){
                    LinearLayout clothingQ = view.findViewById(R.id.sellpage_clothingQ);
                    clothingQ.setVisibility(View.VISIBLE);
                }
            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment currentFrag = getChildFragmentManager().findFragmentByTag("sellpage");

                if (getChildFragmentManager().findFragmentByTag("sellpage2")== null){
                    SellPageFragment3 sellPageFragment3 = new SellPageFragment3();
                    getChildFragmentManager().beginTransaction().add(R.id.sellFragmentContainer, sellPageFragment3, "sellpage2").show(sellPageFragment3).addToBackStack(null).commit();



                }else{
                    Fragment sellPageFragment3 = getChildFragmentManager().findFragmentByTag("sellpage2");
                    getChildFragmentManager().beginTransaction().show(sellPageFragment3).addToBackStack(null).commit();


                }


            }
        });



        uploadBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                CropImage.activity()
                        .setActivityTitle("Edit")
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(256, 256)
                        .setAspectRatio(90,100)
                        .setInitialCropWindowPaddingRatio((float) 0.1)
                        .start(getContext(), SellPageFragment2.this);
            }
        });

        imageHolder2 = view.findViewById(R.id.sellpage_imageHolder2);

        return view;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                sellImgUri = result.getUri();
                if (imageHolder1.getTag()==null){
                    imageHolder1.setImageURI(sellImgUri);
                    imageHolder1.setTag(sellImgUri);
                }
                else if(imageHolder2.getTag()==null){
                    imageHolder2.setImageURI(sellImgUri);
                    imageHolder2.setTag(sellImgUri);
                }

                else if(uploadBtn.getTag()==null){
                    uploadBtn.setImageURI(sellImgUri);
                    uploadBtn.setTag(sellImgUri);
                }





            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}
