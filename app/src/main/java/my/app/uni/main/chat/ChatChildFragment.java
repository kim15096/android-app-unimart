package my.app.uni.main.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import my.app.uni.R;

public class ChatChildFragment extends Fragment {
    private ViewPager viewPager;
    private ImageView backBtn;
    private Toolbar mToolbar;
    private List<Object> chat_list;
    private ChatRecyclerAdapter chat_list_recycler_adapter;
    private TabLayout mTabLayout;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView chat_list_recycler;
    private FirebaseAuth firebaseAuth;
    private BottomNavigationView bottomNavigationView;
    private CircleImageView userProfile, sellerProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_childfrag, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        backBtn = view.findViewById(R.id.chat_backbtn);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        userProfile = view.findViewById(R.id.chat_userProfilePic);


        firebaseFirestore.collection("California").document("UC Davis").collection("Users").document(firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.get("image URL") != null){
                    String userProfileStr = documentSnapshot.get("image URL").toString();
                    Glide.with(getContext()).load(userProfileStr).into(userProfile);
                }

            }
        });


        firebaseFirestore.collection("California").document("UC Davis").collection("Users").document(firebaseAuth.getUid())
                .collection("Chat").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                    if (doc.getType() == DocumentChange.Type.ADDED) {

                    }

                    if (doc.getType() == DocumentChange.Type.REMOVED) {

                    }
                }
            }

        });



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomNavigationView.animate().setInterpolator(new LinearInterpolator()).translationY(0).setDuration(150);
                getParentFragment().getChildFragmentManager().popBackStack();
            }
        });




        return view;
    }


}
