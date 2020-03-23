package my.app.uni.main.market;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.viewpagerdots.DotsIndicator;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.app.uni.R;

public class SellRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int HEADER= 0;
    private static final int ITEMS= 1;
    private AlertDialog.Builder builder;
    public List<SellPostModel> sell_list;
    public Context context;
    private DocumentReference docref;
    private ImageView closeBtn, profilePic, itemView;
    private Button messageBtn;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private LottieAnimationView lottieAnimationView;
    private String first, last, user_id, profileURL, school;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    public MarketFragment marketFragment;
    private Integer card_position;
    private Toolbar item_toolbar;
    private TextView marketTitle, marketPrice, marketDesc, marketKeyword;
    private CardView popup_cardview;

    public SellRecyclerAdapter(List<SellPostModel> sell_list, MarketFragment marketFragment){

        this.sell_list = sell_list;
        this.marketFragment = marketFragment;

    }

    private class VIEW_TYPES {
        public static final int Header = 1;
        public static final int Normal = 2;
        public static final int Ads = 3;
    }



    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return VIEW_TYPES.Header;
        }
       /* else if(position % 5 == 0){
            return VIEW_TYPES.Ads;
        }*/
        else {
            return VIEW_TYPES.Normal;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_recycler_sellcard, parent, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        itemView = ((AppCompatActivity)context).findViewById(R.id.item_Image);
        item_toolbar = ((AppCompatActivity)context).findViewById(R.id.item_toolbar_bottom);
        marketTitle = ((AppCompatActivity)context).findViewById(R.id.marketItem_title);
        marketPrice = ((AppCompatActivity)context).findViewById(R.id.marketItem_price);
        marketDesc = ((AppCompatActivity)context).findViewById(R.id.marketItem_description);
        marketKeyword = ((AppCompatActivity)context).findViewById(R.id.marketItem_keyword);



        school = preferences.getString("school","");
        school = "UC Davis";

        View HeaderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_recycler_header, parent, false);
        View AdView = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_recycler_ad, parent, false);
        if (viewType == VIEW_TYPES.Header){
            return new ViewHolder2(HeaderView);
        }
        else if (viewType == VIEW_TYPES.Normal){

            return new ViewHolder0(view);
        }
        else if(viewType == VIEW_TYPES.Ads){
            return new ViewHolderAds(AdView);
        }
        else{
            return new ViewHolder0(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        switch(holder.getItemViewType()){

            case VIEW_TYPES.Ads:
                ViewHolderAds viewHolderAds = (ViewHolderAds) holder;
                break;
            case VIEW_TYPES.Header:
                ViewHolder2 viewHolder2 = (ViewHolder2)holder;
                break;
            case VIEW_TYPES.Normal:
                ViewHolder0 viewHolder0 = (ViewHolder0)holder;

                final String price = sell_list.get(position).getPrice().toString();
                viewHolder0.setPrice(price);

                final String desc_data = sell_list.get(position).getDesc();

                final String title = sell_list.get(position).getTitle();

                final String userID = sell_list.get(position).getUser_id();
                viewHolder0.setID(userID);

                final String pictureURL = sell_list.get(position).getProfileURL();
                viewHolder0.setImageURL(pictureURL);

                final String itemURL = sell_list.get(position).getImage_url();
                viewHolder0.setItemPicture(itemURL);

                final String keyword = sell_list.get(position).getKeyword();
                viewHolder0.setKeyword(keyword);

                viewHolder0.profilePic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder = new AlertDialog.Builder(context);
                        final View mView = ((AppCompatActivity)context).getLayoutInflater().inflate(R.layout.market_dialog_profile_tmp,null);
                        popup_cardview = mView.findViewById(R.id.popup_cardview);
                        popup_cardview.setVisibility(View.VISIBLE);
                        closeBtn = mView.findViewById(R.id.popup_close);
                        messageBtn = mView.findViewById(R.id.popup_messageBtn);
                        builder.setCancelable(true);
                        final AlertDialog dialog = builder.create();
                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.popup_profile_bg);
                        dialog.setView(mView);
                        dialog.getWindow().setWindowAnimations(R.style.Animation_Design_BottomSheetDialog);
                        dialog.getWindow().setGravity(Gravity.BOTTOM);
                        dialog.show();

                        messageBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                final Map<String, Object> chatMap = new HashMap<>();
                                chatMap.put("Sender", firebaseAuth.getCurrentUser().getDisplayName());

                                firebaseFirestore.collection("California").document(school).collection("Users")
                                        .document(firebaseAuth.getUid()).collection("Chat").document(userID).set(chatMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        /*ChatFragment chatFragment = new ChatFragment();
                                        ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().hide(marketFragment).show(chatFragment).commit();*/
                                        BottomNavigationView bottomNavigationView = ((AppCompatActivity)context).findViewById(R.id.bottom_navigation);
                                        View view = bottomNavigationView.findViewById(R.id.nav_chat);
                                        view.performClick();
                                        dialog.hide();




                                    }
                                });

                            }
                        });

                        closeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        firebaseFirestore.collection("California").document(school).collection("Users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String firstname = documentSnapshot.get("name").toString();
                                TextView firstName = mView.findViewById(R.id.popup_accountName);
                                profilePic = mView.findViewById(R.id.popup_ProfilePic);
                                if (documentSnapshot.get("image URL")!=null){
                                    String imageURL = documentSnapshot.get("image URL").toString();

                                    Glide.with(context).load(imageURL).centerCrop().addListener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            popup_cardview.setVisibility(View.VISIBLE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            popup_cardview.setVisibility(View.GONE);
                                            return false;
                                        }
                                    }).into(profilePic);


                                }

                                else{
                                    popup_cardview.setVisibility(View.GONE);
                                }
                                firstName.setText(firstname);

                            }
                        });
                    }
                });


                /*if(sell_list.get(position).getTimestamp() != null) {
                    Timestamp timestamp = (Timestamp) sell_list.get(position).getTimestamp();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
                    String Date = dateFormat.format(timestamp.toDate());
                    viewHolder0.setTimestamp(Date);
                }*/


                viewHolder0.card_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Glide.with(context).load(itemURL).into(itemView);
                        ScrollView scrollView = ((AppCompatActivity)context).findViewById(R.id.itemSlider_ScrollView);
                        scrollView.scrollTo(0, 0);
                        card_position = position;
                        marketTitle.setText(title);
                        marketPrice.setText("$" + price);
                        marketDesc.setText(desc_data);
                        marketKeyword.setText("#" + keyword);

                        FirebaseFirestore.getInstance().collection("California").document(school).collection("Users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.get("image URL") != null) {
                                    CircularImageView profilePic = ((AppCompatActivity) context).findViewById(R.id.marketItem_profilePic);
                                    String profileURL = documentSnapshot.get("image URL").toString();
                                    Glide.with(context).load(profileURL).into(profilePic);

                                }
                            }
                        });



                        if (sell_list.get(position).getUser_id().equals(firebaseAuth.getUid())){
                            //item_toolbar.setVisibility(View.GONE);

                        }
                        else{
                            item_toolbar.setVisibility(View.VISIBLE);
                        }
                        marketFragment.slidingLayer.openLayer(true);
                    }
                });
                break;
        }
    }

    public int getPosition(){
     return card_position;
    }

    @Override
    public int getItemCount() {
        return sell_list.size();
    }

    public class ViewHolder0 extends RecyclerView.ViewHolder{

        private CardView card_view, market_loadCard;
        private ImageView more_menu;
        private ImageView profilePic, sellImage;
        private ProgressBar progressBar;
        private LottieAnimationView loadingImage;
        private View mView;
        private TextView descView, timestampView, userID, priceView, title;
        private ImageView imageView;

        public ViewHolder0(@NonNull View itemView) {

            super(itemView);
            mView = itemView;
            sellImage = mView.findViewById(R.id.sell_image);
            card_view = mView.findViewById(R.id.cardView);
            more_menu = mView.findViewById(R.id.sellcard_more);
            profilePic = mView.findViewById(R.id.market_sellcard_profilepic);
            loadingImage = mView.findViewById(R.id.imageLoadingAnimation);
            market_loadCard = mView.findViewById(R.id.market_preloadCardView);

        }
        public void setDescText(String text){

            descView = mView.findViewById(R.id.sell_desc);
            descView.setText(text);
        }

       /* public void setTimestamp(String text){

            timestampView = mView.findViewById(R.id.sellcard_timestamp);
            timestampView.setText(text);
        }*/

        public void setTitle(String text){
            title = mView.findViewById(R.id.sellcard_Title);
            title.setText(text);

        }

        public void setPrice(String text){
            priceView = mView.findViewById(R.id.sellcard_pricetag);
            priceView.setText("$" + text);
        }

        public void setKeyword(String text){
            TextView keyword = mView.findViewById(R.id.sellcard_keyword);
            keyword.setText("#" + text);

        }

        public void setID(final String text){

            preferences = PreferenceManager.getDefaultSharedPreferences(context);

            school = "UC Davis";

            docref = FirebaseFirestore.getInstance().collection("California").document(school).collection("Users").document(text);

            docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    first = documentSnapshot.getData().get("name").toString();

                    CircularImageView circularImageView = mView.findViewById(R.id.market_sellcard_profilepic);
                    if (documentSnapshot.getData().get("image URL")!=null) {
                        profileURL = documentSnapshot.getData().get("image URL").toString();
                            Glide.with(context).load(profileURL).placeholder(new ColorDrawable(context.getResources().getColor(R.color.light_gray))).fitCenter()
                                    .centerCrop().into(circularImageView);
                    }
                    else{
                            circularImageView.setImageResource(R.drawable.ic_default_profilepic);
                        }


                }
            });
        }

        public void setItemPicture(String text) {
            imageView = mView.findViewById(R.id.sell_image);
            if (text != null) {
                Glide.with(context).load(text).placeholder(new ColorDrawable(context.getResources().getColor(R.color.fui_transparent))).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        market_loadCard.setVisibility(View.GONE);
                        return false;
                    }
                }).into(imageView);
            }
        }

        public void setImageURL(String text){

        }


    }

    public class ViewHolder2 extends RecyclerView.ViewHolder{
        private View mView;
        private ViewPager viewPager;
        private DotsIndicator dotsIndicator;

        public ViewHolder2(@NonNull View itemView) {

            super(itemView);
            mView = itemView;

            dotsIndicator = mView.findViewById(R.id.headerDotIndicator);
            viewPager = mView.findViewById(R.id.headerPager);
            viewPager.setAdapter(new ImageAdapter(context));
            dotsIndicator.attachViewPager(viewPager);






        }

    }

    public class ViewHolderAds extends RecyclerView.ViewHolder{
        private View mView;
        private RadioGroup radioGroup;

        public ViewHolderAds(@NonNull View itemView) {

            super(itemView);
            mView = itemView;

        }

    }

}
