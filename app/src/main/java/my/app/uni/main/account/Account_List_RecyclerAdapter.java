package my.app.uni.main.account;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import my.app.uni.R;
import my.app.uni.main.market.SellPostModel;
import my.app.uni.slidinglayer.SlidingLayer;

public class Account_List_RecyclerAdapter extends RecyclerView.Adapter<Account_List_RecyclerAdapter.ViewHolder>{

    public List<SellPostModel> account_list;
    public Context context;
    private DocumentReference docref;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public Account_List_RecyclerAdapter(List<SellPostModel> account_list){

        this.account_list = account_list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_tab_listing_card, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final SlidingLayer slidingLayer = ((AppCompatActivity)context).findViewById(R.id.AccountItemSlider);
        final TextView sliderHeader = ((AppCompatActivity)context).findViewById(R.id.account_slider_header);
        final ImageView delete_sellpost = ((AppCompatActivity)context).findViewById(R.id.sellpost_deleteBtn);

        holder.list_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                slidingLayer.openLayer(true);
                sliderHeader.setText("Selling #" + account_list.get(position).getKeyword());

                delete_sellpost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String[] menuOptions = {"Edit", "Delete"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setItems(menuOptions, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                }
                                else if (i == 1) {

                                    firebaseFirestore.collection("California").document("UC Davis").collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Long postCount = (Long) documentSnapshot.get("post count");
                                            postCount = postCount - 1;
                                            firebaseFirestore.collection("California").document("UC Davis").collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                                                    .update("post count", postCount).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    firebaseFirestore.collection("California").document("UC Davis").collection("Sell Posts").document(account_list.get(position).getPost_id()).delete();
                                                    removeAt(position);
                                                    slidingLayer.closeLayer(true);

                                                }
                                            });
                                        }
                                    });

                                }
                            }
                        });
                        builder.show();

                    }
                });

            }
        });

        String desc_data = account_list.get(position).getDesc();
        holder.setDescText(desc_data);

        String imageURL = account_list.get(position).getImage_url();
        holder.setImageURL(imageURL);

        String title = account_list.get(position).getTitle();
        holder.setTitle(title);

        Number price = account_list.get(position).getPrice();
        holder.setPrice(price);

        String item_tag = account_list.get(position).getKeyword();
        holder.setTag(item_tag);




    }

    private void removeAt(int position) {
        account_list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, account_list.size());

    }

    @Override
    public int getItemCount() {
        return account_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CardView list_card_view;
        private View mView;
        private TextView descView, priceView, timestampView, userID, title;
        private ImageView image1;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            list_card_view = (CardView) mView.findViewById(R.id.listing_cardview);

        }
        public void setDescText(String text){

            descView = mView.findViewById(R.id.account_list_desc);
            descView.setText(text);
        }

        public void setTitle(String text){
            title = mView.findViewById(R.id.account_postTitle);
            title.setText(text);

        }

        public void setPrice(Number text){
            priceView = mView.findViewById(R.id.listingcard_price);
            priceView.setText("$" + text.toString());
        }

        public void setImageURL(String text){
            image1 = mView.findViewById(R.id.listing_image1);
            Glide.with(context).load(text).fitCenter()
                    .centerCrop().into(image1);
        }

        public void setTag(String text){
            TextView tag_text = mView.findViewById(R.id.listingcard_tag);
            tag_text.setText("#"+text);
        }


    }
}
