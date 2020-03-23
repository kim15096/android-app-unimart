package my.app.uni.main.market;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import my.app.uni.R;

public class BasketRecyclerAdapter extends RecyclerView.Adapter<BasketRecyclerAdapter.ViewHolder> {
    public List<SellPostModel> basket_list;
    public MarketFragment marketFragment;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public BasketRecyclerAdapter(List<SellPostModel> basket_list, MarketFragment marketFragment){

        this.basket_list = basket_list;
        this.marketFragment = marketFragment;

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
    public void onBindViewHolder(@NonNull BasketRecyclerAdapter.ViewHolder holder, final int position) {
        holder.list_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] menuOptions = {"Save for later", "Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(menuOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                        }
                        else if (i == 1) {
                            removeAt(position);
                        }
                    }
                });
                builder.show();
            }
        });

        String desc_data = basket_list.get(position).getDesc();
        holder.setDescText(desc_data);

        String imageURL = basket_list.get(position).getImage_url();
        holder.setImageURL(imageURL);

        String title = basket_list.get(position).getTitle();
        holder.setTitle(title);

        Number price = basket_list.get(position).getPrice();
        holder.setPrice(price);



    }

    private void removeAt(int position) {
        basket_list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, basket_list.size());

    }


    @Override
    public int getItemCount() {
        return basket_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView list_card_view;
        private View mView;
        private TextView descView, priceView, timestampView, userID, title;
        private ImageView image1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            list_card_view = mView.findViewById(R.id.listing_cardview);


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
    }
}
