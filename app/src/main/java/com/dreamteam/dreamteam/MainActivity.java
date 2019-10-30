package com.dreamteam.dreamteam;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.dreamteam.dreamteam.EditProfile.View.EditProfileController;
import com.dreamteam.dreamteam.Group.Protocols.ActivityAction;
import com.dreamteam.dreamteam.Group.View.GroupController;
import com.dreamteam.dreamteam.GroupList.View.GroupsListController;
import com.dreamteam.dreamteam.User.Entity.UserData.User;
import com.dreamteam.dreamteam.User.View.UserViewController;

public class MainActivity extends AppCompatActivity implements ActivityAction {

    GroupController groupController;

    EditProfileController editProfileController;

    BottomNavigationView bottomNavigationView;

    private final String BLANK_TEST = "BlankTest";
    private final String GROUP_LIST = "GroupList";
    private final String USER_FRAGMENT = "UserFragment";
    private final String GROUP_CONTROLLER = "GroupController";
    private final String EDIT_PROFILE_CONTROLLER = "EditProfileController";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        switch (menuItem.getItemId()) {

                            case R.id.groups:
                                clearMainActivity();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GroupsListController()).commit();
                                break;

                            case R.id.profile:
                                clearMainActivity();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserViewController()).commit();
                                break;

                            case R.id.notification:
                                clearMainActivity();
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BlankTest()).commit();
                                break;
                        }
                        return true;
                    }
                });
    }

    public void clearMainActivity(){

        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i <= (fm.getBackStackEntryCount() + 1); ++i) {
            fm.popBackStack();
        }
        for (Fragment fragment:getSupportFragmentManager().getFragments()) {//TODO: если нажата страница группы, то не удаляет список
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    public void changeFragment(Fragment fragment, String type) {
        if(getSupportFragmentManager().findFragmentByTag(type)== null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, type)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, getSupportFragmentManager().findFragmentByTag(type)).commit();
        }

        System.gc();
    }

    public void changeFragmentWitchBackstack(Fragment fragment, String type) {
        if(getSupportFragmentManager().findFragmentByTag(type)== null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, type)
                    .addToBackStack(null)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, type).addToBackStack(null).commit();
        }
    }

    @Override
    public void getGroup(String id) {
        groupController = new GroupController(id);
        if (getSupportFragmentManager().findFragmentByTag(GROUP_CONTROLLER) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, groupController, GROUP_CONTROLLER)
                    .addToBackStack(null)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, groupController, GROUP_CONTROLLER).addToBackStack(null).commit();
        }
    }

    public void editProfile(User user, Bitmap bitmap){
        editProfileController = new EditProfileController(user, bitmap);
        if (getSupportFragmentManager().findFragmentByTag(GROUP_CONTROLLER) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, editProfileController, EDIT_PROFILE_CONTROLLER)
                    .addToBackStack(null)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, editProfileController, EDIT_PROFILE_CONTROLLER).addToBackStack(null).commit();
        }
    }
}
