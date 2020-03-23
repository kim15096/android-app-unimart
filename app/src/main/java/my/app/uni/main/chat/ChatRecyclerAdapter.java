package my.app.uni.main.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import my.app.uni.R;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder> {
    public List<Object> chat_list;
    public ChatFragment chatFragment;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private BottomNavigationView bottomNavigationView;

    public ChatRecyclerAdapter(List<Object> chat_list, ChatFragment chatFragment){

        this.chat_list = chat_list;
        this.chatFragment = chatFragment;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recycler_card, parent, false);
        context = parent.getContext();

        bottomNavigationView = ((AppCompatActivity)context).findViewById(R.id.bottom_navigation);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRecyclerAdapter.ViewHolder holder, final int position) {
        holder.chat_list_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomNavigationView.animate().setInterpolator(new LinearInterpolator()).translationY(150).setDuration(150);
                ChatChildFragment chatChildFragment= new ChatChildFragment();
                chatFragment.getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_bottom_fast, 0).add(R.id.messages_childfrag_container, chatChildFragment).show(chatChildFragment).addToBackStack(null).commit();


            }
        });



    }

    @Override
    public int getItemCount() {
        return chat_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView chat_list_card;
        private View mView;
        private TextView chat_name;
        private CircularImageView profilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            chat_list_card = mView.findViewById(R.id.chat_cardview);

        }

        public void setName(String text){
            chat_name = mView.findViewById(R.id.chat_name);
            chat_name.setText(text);
        }

        public void setProfileImage(String text){
            profilePic = mView.findViewById(R.id.chat_profilepic);
            Glide.with(context).load(text).fitCenter()
                    .centerCrop().into(profilePic);
        }
    }
}
