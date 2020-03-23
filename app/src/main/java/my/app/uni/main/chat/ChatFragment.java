package my.app.uni.main.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import my.app.uni.R;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private List<Object> chat_list;
    private ChatRecyclerAdapter chat_list_recycler_adapter;
    private TabLayout mTabLayout;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView chat_list_recycler;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_main_frag, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        chat_list = new ArrayList<>();

        chat_list_recycler = view.findViewById(R.id.chat_recyclerview);
        chat_list_recycler_adapter = new ChatRecyclerAdapter(chat_list, this);
        chat_list_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        chat_list_recycler.setAdapter(chat_list_recycler_adapter);

        chat_list_recycler_adapter.notifyItemInserted(0);

        firebaseFirestore.collection("California").document("UC Davis").collection("Users").document(firebaseAuth.getUid())
                .collection("Chat").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        chat_list.add(0, "hello?");
                        chat_list_recycler_adapter.notifyDataSetChanged();
                    }

                    if (doc.getType() == DocumentChange.Type.REMOVED) {

                    }
                }
            }

        });




        return view;
    }


}
