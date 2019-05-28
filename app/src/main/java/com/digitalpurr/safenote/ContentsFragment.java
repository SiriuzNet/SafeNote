package com.digitalpurr.safenote;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ContentsFragment extends Fragment {
    private String data;

    public ContentsFragment() {
        // Required empty public constructor
    }

    public ContentsFragment setData(String data) {
        this.data = data;
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_contents, container, false);
        final EditText editText = view.findViewById(R.id.content_text);
        editText.setText(data, TextView.BufferType.NORMAL);
        data = null;
        Button button = view.findViewById(R.id.save_button);
        button.setOnClickListener((v) -> {
            Log.d("SafeNote", "ON SAVE");
            button.setText("WRITING");
            button.setEnabled(false);
            onSave(getActivity(), editText.getText().toString(), button);
        });
        return view;
    }

    private void onSave(final Activity activity, final String data, final Button button) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Encryption.writeToFile(data, activity);
                } catch (Exception e) {
                    activity.runOnUiThread(() -> Toast.makeText(activity,"ERROR: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                    e.printStackTrace();
                }
                activity.runOnUiThread(() -> {
                    Toast.makeText(activity,"DONE", Toast.LENGTH_SHORT).show();
                    button.setText("SAVE");
                    button.setEnabled(true);
                });
            }
        }.start();
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
