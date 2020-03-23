package my.app.uni.main.account;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class SectionsPagerAdapter_Account extends FragmentPagerAdapter {


    public SectionsPagerAdapter_Account(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 1:
                Account_Listing account_listing = new Account_Listing();
                return account_listing;

            case 0:
                Account_Bought account_bought = new Account_Bought();
                return account_bought;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){

        return null;
    }
}
