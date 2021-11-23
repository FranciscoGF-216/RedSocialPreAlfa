package com.example.uaqychat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.uaqychat.R;
import com.example.uaqychat.fragments.ChatsFragment;
import com.example.uaqychat.fragments.FiltersFragment;
import com.example.uaqychat.fragments.HomeFragment;
import com.example.uaqychat.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(new HomeFragment());

    }



    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                 if (item.getItemId()== R.id.itemHome){
                     //Fragment Home
                     openFragment(new HomeFragment());
                 }else if(item.getItemId()==R.id.itemChat){
                     //Fragment Chat
                     openFragment(new ChatsFragment());

                 }else if(item.getItemId()==R.id.itemFilters){
                     //Fragment Filters
                     openFragment(new FiltersFragment());

                 }else if(item.getItemId()==R.id.itemProfile){
                     //Fragment Profile
                     openFragment(new ProfileFragment());

                 }
                    return true;
                }
            };
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finishAffinity();
    }


}

