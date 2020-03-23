package my.app.uni.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import my.app.uni.R;
import my.app.uni.main.Main;

public class RegisterFragment3 extends Fragment {

    private ImageButton nextBtn3;
    private CircularImageView profilePic;
    private UploadTask uploadTask;
    private DocumentReference docref;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_3_frag, container, false);
        
        nextBtn3 = view.findViewById(R.id.register_nextBtn3);
        nextBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent change2home = new Intent(getActivity(), Main.class);
                getActivity().startActivity(change2home);
                getActivity().finish();
                getActivity().overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_left);
            }
        });

        return view;

    }


}