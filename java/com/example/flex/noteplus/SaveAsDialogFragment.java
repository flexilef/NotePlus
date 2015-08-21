package com.example.flex.noteplus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.app.Dialog;

/**
 * Created by Flex on 8/19/2015.
 */
public class SaveAsDialogFragment extends DialogFragment
{
    public SaveAsDialogFragment()
    {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_save_as, null);
        //editTextFile = (EditText) view.findViewById(R.id.edit_text_filename);
        //editTextPath = (EditText) view.findViewById(R.id.edit_text_location);

        builder.setView(view)
                .setTitle("Save as...")
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ((MainActivity)getActivity()).dialogSaveAsDoNegativeClick(getDialog());
                            }
                        }
                )
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ((MainActivity)getActivity()).dialogSaveAsDoPositiveClick(getDialog());
                            }
                        }
                );

        return builder.create();
    }
}
