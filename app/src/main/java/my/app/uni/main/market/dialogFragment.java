package my.app.uni.main.market;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import my.app.uni.R;

public class dialogFragment extends DialogFragment {

    private String profileURL, first, last;
    private TextView textView;
    private Button dismissBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_dialog_profile_tmp, container, false);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        CircularImageView circularImageView = view.findViewById(R.id.popup_ProfilePic);
        profileURL = getArguments().getString("profileURL");
        if (profileURL!=null) {
            Glide.with(getContext()).load(profileURL).placeholder(R.drawable.ic_add_black_24dp).fitCenter()
                    .centerCrop().into(circularImageView);

        }
        else {
            circularImageView.setImageResource(R.drawable.ic_person_black_24dp);
        }
        first = getArguments().getString("first", "");
        last = getArguments().getString("last", "");

        textView = view.findViewById(R.id.popup_accountName);
        textView.setText(first+" " +last);


        return view;
    }
}
