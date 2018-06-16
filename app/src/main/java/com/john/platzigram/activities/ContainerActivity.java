package com.john.platzigram.activities;

import android.app.FragmentTransaction;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.john.platzigram.R;
import com.john.platzigram.fragments.HomeFragment;
import com.john.platzigram.fragments.ProfileFragment;
import com.john.platzigram.fragments.SearchFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContainerActivity extends AppCompatActivity {

    @BindView(R.id.bottomBar) BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        ButterKnife.bind(this);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.search:
                        SearchFragment searchFragment = new SearchFragment();
                        setFragment(searchFragment);
                        break;
                    case R.id.home:
                        HomeFragment homeFragment = new HomeFragment();
                        setFragment(homeFragment);
                        break;
                    case R.id.me:
                        ProfileFragment profileFragment = new ProfileFragment();
                        setFragment(profileFragment);
                        break;
                }
            }
        });
        bottomBar.setDefaultTab(R.id.home);
    }

    private void setFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
//                .addToBackStack(null).commit();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            if (getFragmentManager().getFragment())
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

//        Fragment f = getActivity().getFragmentManager().findFragmentById(R.id.fragment_container);
//        if(f instanceof CustomFragmentClass)
//            // do something with f
//            ((CustomFragmentClass) f).doSomething();

        if (bottomBar.getCurrentTabId() ==  R.id.search || bottomBar.getCurrentTabId() == R.id.me){
//                HomeFragment homeFragment = new HomeFragment();
//                setFragment(homeFragment);
                bottomBar.selectTabWithId(R.id.home);
        } else {
            super.onBackPressed();
        }
    }
}
