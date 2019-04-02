package com.dreamteam.dreamteam;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.dreamteam.dreamteam.Group.View.GroupsListFragment;
import com.dreamteam.dreamteam.User.View.UserViewController;

public class MainActivity extends AppCompatActivity {

    UserViewController userFragment = new UserViewController();
    GroupsListFragment groupsListFragment = new GroupsListFragment();
    BlankTest testFragment = new BlankTest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.groups:
                                changeFragment(groupsListFragment);
                                break;

                            case R.id.profile:
                                changeFragment(userFragment);
                                break;

                            case R.id.notification:
                                changeFragment(testFragment);
                        }
                        return true;
                    }
                });
        bottomNavigationView.setSelectedItemId(R.id.groups);

    }

    public void changeFragment (Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

}
