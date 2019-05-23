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
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.stream.Collectors;

public class ContentsFragment extends Fragment {
    public ContentsFragment() {
        // Required empty public constructor
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
        editText.setText(readFromFile(getActivity()), TextView.BufferType.NORMAL);
        Button button = view.findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SafeNote", "ON SAVE");
                writeToFile(editText.getText().toString(), getActivity());
            }
        });

        return view;
    }

    private static void writeToFile(final String data, final Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("data.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("SafeNote", "File write failed: " + e.toString());
        }
    }

    private static String readFromFile(final Context context) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput("data.txt");
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                ret = bufferedReader.lines().collect(Collectors.joining("\n"));
            }
        }
        catch (FileNotFoundException e) {
            Log.e("SafeNote", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("SafeNote", "Can not read file: " + e.toString());
        }
        return ret;
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
