package com.digitalpurr.safenote;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener((v) -> {
            Log.d("SafeNote", "ON SAVE");
            saveButton.setText("WRITING");
            saveButton.setEnabled(false);
            onSave(getActivity(), editText.getText().toString(), saveButton);
        });
        Button exportButton = view.findViewById(R.id.export_button);
        exportButton.setOnClickListener((v) -> {
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            intentShareFile.setType("application/octet-stream");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://com.digitalpurr.safenote.fileprovider/files/"+Consts.CONTAINER_FILE));
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "SafeNote data export");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "EXPORT TIME: "+sdf.format(new Date()));
            startActivity(Intent.createChooser(intentShareFile, "Export"));
        });
        Button importButton = view.findViewById(R.id.import_button);
        importButton.setOnClickListener((v) -> {
            Intent intent = new Intent()
                    .setType("application/octet-stream")
                    .setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == Activity.RESULT_OK) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        final FileInputStream in = (FileInputStream) getContext().getContentResolver().openInputStream(data.getData());
                        final String filePath = getContext().getFilesDir().getPath() + File.separator + Consts.CONTAINER_FILE;
                        FileOutputStream out = new FileOutputStream(new File(filePath));
                        FileChannel inChannel = in.getChannel();
                        FileChannel outChannel = out.getChannel();
                        inChannel.transferTo(0, inChannel.size(), outChannel);
                        in.close();
                        out.close();
                        getActivity().runOnUiThread(() -> getActivity().onBackPressed());
                    } catch (Exception e) {
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),"ERROR: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                        e.printStackTrace();
                    }
                }
            }.start();
        }
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
