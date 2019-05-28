package com.digitalpurr.safenote;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

public class MainActivity extends Activity {
    public static MainActivity INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INSTANCE = this;
        setContentView(R.layout.activity_main);

        addFragment(R.id.content, new LoginFragment(), Consts.TAG_LOGIN_FRAGMENT);
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
                .addToBackStack(Consts.MAIN_BACKSTACK)
                .commit();
    }
}
