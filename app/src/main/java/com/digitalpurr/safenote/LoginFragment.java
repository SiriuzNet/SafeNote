package com.digitalpurr.safenote;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

public class LoginFragment extends Fragment {
    public LoginFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        final EditText passwordField = view.findViewById(R.id.login_password);
        final Button button = view.findViewById(R.id.login_button);

        File file = new File(Consts.CONTAINER_FILE);
        if(!file.exists()) {
            button.setText("CREATE");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SafeNote", "ON LOGIN");
                try {
                    Encryption.generateKey(passwordField.getText().toString());
                    MainActivity.INSTANCE.replaceFragment(R.id.content, new ContentsFragment(), Consts.TAG_CONTENTS_FRAGMENT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
