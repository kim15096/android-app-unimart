package my.app.uni.main.account;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import my.app.uni.R;
import my.app.uni.main.market.SellPostModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class Account_Listing extends Fragment {

    private RecyclerView account_list_view;
    private Account_List_RecyclerAdapter account_list_recyclerAdapter;
    private List<SellPostModel> account_list;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth fireBaseAuth;
    private FloatingActionButton fab_add;
    private RecyclerView recyclerView;
    private ImageView imageView;
    private LottieAnimationView emptyBox;

    public Account_Listing() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.account_tab_listing, container, false);
        account_list = new ArrayList<>();

        account_list_view = view.findViewById(R.id.account_list_RecyclerVIew);
        account_list_recyclerAdapter = new Account_List_RecyclerAdapter(account_list);
        account_list_view.setLayoutManager(new LinearLayoutManager(getContext()));
        account_list_view.setAdapter(account_list_recyclerAdapter);
        emptyBox = view.findViewById(R.id.listing_emptyBox);

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("California").document("UC Davis").collection("Sell Posts").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()) {

                    if (doc.getDocument().get("image_url") == null) {

                    } else {

                        String user_id = doc.getDocument().getData().get("userID").toString();
                        if (fireBaseAuth.getInstance().getCurrentUser().getUid().equals(user_id)) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                SellPostModel blogPost = doc.getDocument().toObject(SellPostModel.class);
                                account_list.add(0, blogPost);
                                account_list_recyclerAdapter.notifyDataSetChanged();
                            }

                            if (doc.getType() == DocumentChange.Type.REMOVED) {

                            }
                        }
                        if (!account_list.isEmpty()) {
                            emptyBox.setVisibility(View.GONE);
                        } else {
                            emptyBox.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });



        // Inflate the layout for this fragment
        return view;
    }

}
