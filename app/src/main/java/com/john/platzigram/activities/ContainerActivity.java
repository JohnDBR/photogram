package com.john.platzigram.activities;

import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.john.platzigram.R;
import com.john.platzigram.fragments.HomeFragment;
import com.john.platzigram.fragments.ProfileFragment;
import com.john.platzigram.fragments.SearchFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContainerActivity extends AppCompatActivity {

    @BindView(R.id.bottomBar) BottomNavigationView bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        ButterKnife.bind(this);

        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search:
                        SearchFragment searchFragment = new SearchFragment();
                        setFragment(searchFragment);
                        return true;
                    case R.id.home:
                        HomeFragment homeFragment = new HomeFragment();
                        setFragment(homeFragment);
                        return true;
                    case R.id.me:
                        ProfileFragment profileFragment = new ProfileFragment();
                        setFragment(profileFragment);
                        return true;
                }
                return false;
            }
        });
        bottomBar.setSelectedItemId(R.id.home);
    }

    private void setFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null).commit();
    }
}
