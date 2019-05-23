package com.digitalpurr.safenote;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;

public class MainActivity extends Activity {

    public final static String MAIN_BACKSTACK = "MAIN_BACKSTACK";
    public final static String TAG_LOGIN_FRAGMENT = "TAG_LOGIN_FRAGMENT";
    public final static String TAG_CONTENTS_FRAGMENT = "TAG_CONTENTS_FRAGMENT";
    public static MainActivity INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INSTANCE = this;
        setContentView(R.layout.activity_main);

        addFragment(R.id.content, new LoginFragment(), TAG_LOGIN_FRAGMENT);
    }

    public void addFragment(@IdRes int containerViewId,
                               @NonNull Fragment fragment,
                               @NonNull String fragmentTag) {
        getFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }

    public void replaceFragment(@IdRes int containerViewId,
                                   @NonNull Fragment fragment,
                                   @NonNull String fragmentTag) {
                                   //@Nullable String backStackStateName) {
        getFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                //.addToBackStack(backStackStateName)
                .addToBackStack(MAIN_BACKSTACK)
                .commit();
    }
}
