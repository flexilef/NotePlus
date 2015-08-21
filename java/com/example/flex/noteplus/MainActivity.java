package com.example.flex.noteplus;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";

    private File root;
    private File notePlusDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        root = Environment.getExternalStorageDirectory();
        notePlusDir = new File(root.getAbsolutePath() + "/NotePlus");
        notePlusDir.mkdirs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}
        if(id == R.id.action_save)
        {
            showSaveAsDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSaveAsDialog()
    {
        DialogFragment newFragment = new SaveAsDialogFragment();
        newFragment.show(getSupportFragmentManager(), "dialog_save_as");
    }

    public void dialogSaveAsDoPositiveClick(Dialog dialog)
    {
        EditText editTextFile = (EditText) dialog.findViewById(R.id.edit_text_filename);
        EditText editTextPath = (EditText) dialog.findViewById(R.id.edit_text_location);
        EditText editTextBody = (EditText) findViewById(R.id.edit_body);

        String saveFileName = editTextFile.getText().toString();
        String saveFileLocation = editTextPath.getText().toString();
        String textToSave = editTextBody.getText().toString();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        if("".equals(saveFileName))
        {
            Toast toast = Toast.makeText(context, "Invalid Filename!", duration);
            toast.show();
            Log.e("save_as_dialog", "Empty name: " + saveFileName);
        }
        else if("".equals(saveFileLocation))
        {
            Toast toast = Toast.makeText(context, "Invalid Path!", duration);
            toast.show();
            Log.e("save_as_dialog", "Empty loc: " + saveFileLocation);
        }
        else
        {
            Log.e("save_as_dialog", "filename: " + saveFileName);
            Log.e("save_as_dialog", "pathname: " + saveFileLocation);

            saveAs(saveFileName, saveFileLocation, textToSave);
        }
    }

    public void dialogSaveAsDoNegativeClick(Dialog dialog)
    {
        dialog.cancel();
    }

    private void saveAs(String filename, String path, String text)
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        FileWriter outputStream = null;
        File file = new File(path, filename);

        try {
            outputStream = new FileWriter(file);
            outputStream.write(text);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.e("", "Error writing: \nfilename: " + filename + "\nlocation: " + path);
        }
        finally
        {
            try {
                if(outputStream != null)
                    outputStream.close();
            } catch (IOException e)
            {
                e.printStackTrace();
                Log.e("", "Error closing!");
            }
        }

        Toast toastSaved = Toast.makeText(context, "Saved", duration);
        toastSaved.show();
    }

    private boolean isExternalStorageWriteable()
    {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state))
            return true;

        return false;
    }
}
