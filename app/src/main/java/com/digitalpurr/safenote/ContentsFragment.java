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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
        try {
            editText.setText(readFromFile(getActivity()), TextView.BufferType.NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Button button = view.findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SafeNote", "ON SAVE");
                try {
                    writeToFile(editText.getText().toString(), getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private static void writeToFile(final String data, final Context context) throws Exception {
        try {
            String filePath = context.getFilesDir().getPath() + File.separator + Consts.CONTAINER_FILE;
            FileOutputStream f = new FileOutputStream(new File(filePath));
            f.write(Encryption.encodeFile(data.getBytes()));
            f.flush();
            f.close();
        }
        catch (IOException e) {
            Log.e("SafeNote", "File write failed: " + e.toString());
        }
    }

    private static String readFromFile(final Context context) throws Exception {
        String ret = "";
        try {
            String filePath = context.getFilesDir().getPath() + File.separator + Consts.CONTAINER_FILE;
            File file = new File(filePath);
            byte[] fileData = new byte[(int) file.length()];
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            dis.readFully(fileData);
            dis.close();
            ret = new String(Encryption.decodeFile(fileData), "UTF-8");
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
