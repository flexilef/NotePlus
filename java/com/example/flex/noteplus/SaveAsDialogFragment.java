package com.example.flex.noteplus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.app.Dialog;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Flex on 8/19/2015.
 */
public class SaveAsDialogFragment extends DialogFragment {
    public interface SaveAsDialogEditListener {
        void onFinishEditDialog(String filename, String filepath);
    }

    EditText editTextFile;
    EditText editTextPath;

    public SaveAsDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_save_as, null);

        editTextFile = (EditText) view.findViewById(R.id.edit_text_filename);
        editTextPath = (EditText) view.findViewById(R.id.edit_text_location);

        editTextFile.setText(".txt");
        editTextFile.setSelection(1); //not sure why i have to set setSelection() to num != 0
        editTextFile.setSelection(0); //for setSelection(0) to work
        editTextPath.setText(Environment.getExternalStorageDirectory().getAbsolutePath() + "/NotePlus/");

        builder.setView(view)
                .setTitle("Save as...")
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //((MainActivity)getActivity()).dialogSaveAsDoNegativeClick(getDialog());
                            }
                        }
                )
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //((MainActivity) getActivity()).dialogSaveAsDoPositiveClick(getDialog());
                            }
                        }
                );

        final AlertDialog aDialog = builder.create();
        aDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button buttonPositive = aDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean validFilename = true;
                        boolean validFilepath = true;
                        String saveFileName = editTextFile.getText().toString();
                        String saveFileLocation = editTextPath.getText().toString();
                        Context context = getActivity().getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;
                        SaveAsDialogEditListener activity = (SaveAsDialogEditListener) getActivity();

                        if (isInvalidFilename(saveFileName))
                            validFilename = false;
                        else if (isInvalidFilepath(saveFileLocation))
                            validFilepath = false;
                        else {
                            //pass input to main activity
                            activity.onFinishEditDialog(saveFileName, saveFileLocation);
                        }

                        if (validFilename && validFilepath)
                            aDialog.dismiss();
                    }
                });
            }
        });

        return aDialog;
    }

    private boolean isInvalidFilename(String name)
    {
        Context context = getActivity().getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        final String reservedChars = "|\\?*<\":>+[]/'";

        if ("".equals(name))
        {
            Toast toast = Toast.makeText(context, "Invalid Filename: empty!", duration);
            toast.show();
            Log.e("save_as_dialog", "Empty name: " + name);
            return true;
        }

        int length = reservedChars.length();
        for(int i = 0; i < length; i++) {
            if (name.indexOf(reservedChars.charAt(i)) > -1) {
                char match = reservedChars.charAt(i);
                Toast toast = Toast.makeText(context, "Invalid Filename: " + match, duration);
                toast.show();
                Log.e("save_as_dialog", "Invalid Filename: " + match);
                return true;
            }
        }

        return false;
    }

    private boolean isInvalidFilepath(String path)
    {
        Context context = getActivity().getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        final String reservedChars = "|\\?*<\":>+[]'";

        if ("".equals(path)) {
            Toast toast = Toast.makeText(context, "Invalid Path: empty!", duration);
            toast.show();
            Log.e("save_as_dialog", "Empty loc: " + path);
            return true;
        }

        int length = reservedChars.length();
        for(int i = 0; i < length; i++) {
            if (path.indexOf(reservedChars.charAt(i)) > -1) {
                char match = reservedChars.charAt(i);
                Toast toast = Toast.makeText(context, "Invalid Filename: " + match, duration);
                toast.show();
                Log.e("save_as_dialog", "Invalid Filename: " + match);
                return true;
            }
        }

        return false;
    }
}
