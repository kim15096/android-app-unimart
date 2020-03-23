package my.app.uni.main.market;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import my.app.uni.slidinglayer.SlidingLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.app.uni.R;
import my.app.uni.main.account.Account_List_RecyclerAdapter;

import android.widget.TextView;


public class MarketFragment extends Fragment {
    private FloatingActionButton fab_add;
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private Boolean isOpen = false;
    private ImageView chatBtn;
    private CircleImageView marketProfilePic;
    private FirebaseAuth fireBaseAuth = FirebaseAuth.getInstance();
    private String email;
    private FirebaseUser firebaseUser = fireBaseAuth.getCurrentUser();
    public Context context;
    private DocumentReference docref;
    private RecyclerView.LayoutManager mLayoutManager;
    private SellRecyclerAdapter sellRecyclerAdapter;
    private Account_List_RecyclerAdapter account_list_recyclerAdapter;
    private List<SellPostModel> sell_list, account_list, basket_list;
    private RecyclerView sell_list_view, account_list_view, basketRview;
    private SwipeRefreshLayout swipeRefreshLayout;
    private androidx.appcompat.widget.SearchView search_view;
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    private RadioGroup radioGroup;
    private FragmentManager fm = getFragmentManager();
    private DocumentReference user_docref;
    private String current_user_id;
    private CardView cardView;
    private Button addToBasketBtn;
    private ArrayList<Object> index_list;
    private Toolbar mainToolbar;
    private SlidingPaneLayout slidingDrawer;
    private TextView logoHeader, greetText;
    private ImageView basketBtn;
    public SlidingLayer slidingLayer, basketSliding, marketItemSlider;
    public BottomNavigationView bottomNavigationView;
    private SwipeRefreshLayout refreshLayout;
    private BasketRecyclerAdapter basketRecyclerAdapter;
    private LottieAnimationView heart_anim;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.market_main_frag, container, false);
        View view2 = inflater.inflate(R.layout.market_recycler_sellcard, container, false);
        final CircularImageView circularImageView = view2.findViewById(R.id.market_sellcard_profilepic);
        //
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        index_list = new ArrayList<>();
        index_list.add("category");
        sell_list = new ArrayList<>();
        basket_list = new ArrayList<>();

        addToBasketBtn = getActivity().findViewById(R.id.addToBasket);
        heart_anim = getActivity().findViewById(R.id.item_saveforlater);
        basketRview = getActivity().findViewById(R.id.basketRecycler);

        basketRecyclerAdapter = new BasketRecyclerAdapter(basket_list, this);
        basketRview.setAdapter(basketRecyclerAdapter);
        basketRview.setLayoutManager(new LinearLayoutManager(getActivity()));

        refreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        refreshLayout.setProgressViewOffset(false, 100, 175);
        int c1 = getResources().getColor(R.color.colorPrimary);
        refreshLayout.setColorSchemeColors(c1);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                sellRecyclerAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });

        basketBtn = view.findViewById(R.id.marketBasketBtn);
        //radioGroup = view.findViewById(R.id.market_radioGroup);
        sell_list_view = view.findViewById(R.id.sell_list_view);
        sellRecyclerAdapter = new SellRecyclerAdapter(sell_list, this);
        current_user_id = fireBaseAuth.getCurrentUser().getUid();

        slidingLayer = getActivity().findViewById(R.id.itemSlider);
        basketSliding = getActivity().findViewById(R.id.basketSlider);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(position){
                    case 0:
                        return 2;
                    default:
                        return 1;
                }
            }
        });


        docref = firebaseFirestore.collection("California").document("UC Davis").collection("Users").document(firebaseUser.getUid());

        if (getActivity()!=null && isAdded()) {
            docref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot.get("image URL") != null) {
                        marketProfilePic = view.findViewById(R.id.market_profilePic);
                        String imageURL = documentSnapshot.get("image URL").toString();
                        Picasso.get().load(imageURL).into(marketProfilePic);
                    }

                }
            });
        }

        sell_list_view.setLayoutManager(gridLayoutManager);
        sell_list_view.setAdapter(sellRecyclerAdapter);
        sell_list_view.setHasFixedSize(true);


       /* sortConditionBtn = view.findViewById(R.id.market_sortConditionBtn);
        sortPriceBtn = view.findViewById(R.id.market_sortPriceBtn);
        sortDateBtn = view.findViewById(R.id.market_sortDateBtn);*/

       basketBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               basketSliding.openLayer(true);

           }
       });


        logoHeader = view.findViewById(R.id.logoHeader);

        search_view = view.findViewById(R.id.searchView);
        search_view.setIconifiedByDefault(false);

        EditText txtSearch = search_view.findViewById(androidx.appcompat.R.id.search_src_text);
        txtSearch.setHint("What are you looking for?");
        txtSearch.setTextSize(16);
        txtSearch.setBackgroundResource(R.color.fui_transparent);





        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String savedLayout = preferences.getString(KEY_LAYOUT_MANAGER, "");
        // final String school = preferences.getString("school", "");
        final String school = "UC Davis";


        /*if (savedLayout.equals("LINEAR_LAYOUT_MANAGER")){
            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
            radioGroup.check(R.id.linear_rb);

        }
        else if (savedLayout.equals("GRID_LAYOUT_MANAGER")){
            mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
            radioGroup.check(R.id.grid_rb);
        }
        else{
            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
            radioGroup.check(R.id.linear_rb);

        }*/

        /*search_view.setQueryHint("What are you looking for?");
        EditText searchEditText = search_view.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setBackgroundResource(R.drawable.btn_bg);*/





        /*mLinearLayoutRadioButton = view.findViewById(R.id.linear_rb);
        mLinearLayoutRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
                Vibrator a = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
                a.vibrate(25);
            }
        });

        mGridLayoutRadioButton = view.findViewById(R.id.grid_rb);
        mGridLayoutRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER);
                Vibrator a = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
                a.vibrate(25);

            }
        });*/

        firebaseFirestore.collection("California").document(school).collection("Sell Posts").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                    SellPostModel blogPost = doc.getDocument().toObject(SellPostModel.class);


                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        if (doc.getDocument().get("category").toString().equals("header")){
                            sell_list.add(0, blogPost);
                            sellRecyclerAdapter.notifyItemInserted(0);
                        }
                        else {
                            // check for bug
                            index_list.add(1, doc.getDocument().get("post_id"));
                            sell_list.add(1, blogPost);
                            sellRecyclerAdapter.notifyItemInserted(1);
                            sellRecyclerAdapter.notifyDataSetChanged();


                        }
                    }
// error here
                    if (doc.getType() == DocumentChange.Type.REMOVED){
                        Number index = index_list.indexOf(doc.getDocument().get("post_id"));
                        sell_list.remove((int) index);
                        index_list.remove((int) index);
                        /*List<SellPostModel> sell_list2 = sell_list;
                        sell_list.clear();
                        sell_list.addAll(sell_list2);*/
                        sellRecyclerAdapter.notifyDataSetChanged();

                    }
                }

            }
        });



        addToBasketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToBasketBtn.setClickable(false);
                final Integer position = sellRecyclerAdapter.getPosition();
                String post_id = sell_list.get(position).getPost_id();

                if (basket_list.contains(sell_list.get(position))) {
                    Snackbar.make(view, "Item is already in your basket!", Snackbar.LENGTH_SHORT).show();
                    addToBasketBtn.setClickable(true);
                } else {

                    Map<String, Object> data = new HashMap<>();
                    data.put("Saved for later", false);
                    data.put("Post ID", post_id);


                    firebaseFirestore.collection("California").document("UC Davis")
                            .collection("Users").document(firebaseUser.getUid())
                            .collection("Basket").document(post_id).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            basket_list.add(sell_list.get(position));
                            basketRecyclerAdapter.notifyDataSetChanged();
                            addToBasketBtn.setClickable(true);

                        }
                    });

                }
            }
        });
        heart_anim.setMinAndMaxProgress(0.4f, 0.9f);
        heart_anim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heart_anim.playAnimation();

            }
        });



        /*firebaseFirestore.collection("California").document("UC Davis").collection("Users").document(current_user_id)
                .collection("Basket").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                    SellPostModel basketPost = doc.getDocument().toObject(SellPostModel.class);

                    basket_list.add(basketPost);
                    basketRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });*/


        mainToolbar = view.findViewById(R.id.market_toolbar);

        sell_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                /*switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        new Handler().postDelayed(new Runnable(){
                            @Override
                            public void run() {
                                fab_add.show();
                            }
                        }, 500);
                        break;

                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        break;

                }*/

            }
            private static final int HIDE_THRESHOLD = 20;
            private int scrolledDistance = 0;
            private boolean controlsVisible = true;


            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                /*if (dy>0){
                }
                    if (dy > 50) {
                        cardView.setVisibility(View.GONE);
                    } else if (dy < -25) {
                        cardView.setVisibility(View.VISIBLE);
                    }
*/
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    controlsVisible = false;
                    mainToolbar.animate().setInterpolator(new LinearInterpolator()).translationY(-150).setDuration(175);
                    bottomNavigationView.animate().setInterpolator(new LinearInterpolator()).translationY(150).setDuration(150);
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    controlsVisible = true;
                    mainToolbar.animate().setInterpolator(new LinearInterpolator()).translationY(0).setDuration(175);
                    bottomNavigationView.animate().setInterpolator(new LinearInterpolator()).translationY(0).setDuration(150);

                    scrolledDistance = 0;
                }

                if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
                    scrolledDistance += dy;
                }


            }


        });

        logoHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sell_list_view.smoothScrollToPosition(0);
            }
        });

        // could potentially change marketprofile to show statistics of money saved etc.
        marketProfilePic = view.findViewById(R.id.market_profilePic);
        marketProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setSelectedItemId(R.id.nav_account);

                for (Fragment fragment : getActivity().getSupportFragmentManager().getFragments()) {
                    getActivity().getSupportFragmentManager().beginTransaction().hide(fragment).commit();
                }
                Fragment accountFragment = getActivity().getSupportFragmentManager().findFragmentByTag("account");
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, 0).show(accountFragment).commit();
            }
        });


        return view;
    }


}





