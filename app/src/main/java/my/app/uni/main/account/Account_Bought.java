package my.app.uni.main.account;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import my.app.uni.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Account_Bought extends Fragment {


    public Account_Bought() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.account_tab_bought, container, false);
    }

}
