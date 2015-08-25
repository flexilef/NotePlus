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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SaveAsDialogFragment.SaveAsDialogEditListener{

    private static final String TAG = "MyActivity";

    String filenameCurrent;
    String filepathCurrent;
    boolean changesMade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        File root = Environment.getExternalStorageDirectory();
        File notePlusDir = new File(root.getAbsolutePath() + "/NotePlus");
        notePlusDir.mkdirs();

        filenameCurrent = "untitled.txt";
        filepathCurrent = notePlusDir.getAbsolutePath();
        setTitle("NotePlus - " + filenameCurrent);
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
        else if(id == R.id.action_new)
        {
            clearEditTextBody();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onFinishEditDialog(String name, String path)
    {
        EditText editTextBody = (EditText) findViewById(R.id.edit_body);
        String textToSave = editTextBody.getText().toString();

        Log.e("save_as_dialog", "filename: " + name);
        Log.e("save_as_dialog", "pathname: " + path);

        saveAs(name, path, textToSave);
    }

    private void clearEditTextBody() {
        //check if we want to save first
        //if not then
        //clear
        EditText editTextBody = (EditText) findViewById(R.id.edit_body);
        editTextBody.setText("");
    }

    private void showSaveAsDialog()
    {
        DialogFragment newFragment = new SaveAsDialogFragment();
        newFragment.show(getSupportFragmentManager(), "dialog_save_as");
    }

    private void saveAs(String filename, String path, String text)
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        boolean success = true;

        FileWriter outputStream = null;
        File location = new File(path);
        if(!location.mkdirs())
            location.mkdirs();

        File file = new File(path, filename);

        try {
            outputStream = new FileWriter(file);
            outputStream.write(text);
        }
       catch(IOException e)
        {
            success = false;
            Toast saveErrorToast = Toast.makeText(context, "Save Failed!", duration);
            saveErrorToast.show();

            e.printStackTrace();
            Log.e("", "Error writing: \nfilename: " + filename + "\nlocation: " + path);
        }
        finally
        {
            try {
                if(outputStream != null)
                    outputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Log.e("", "Error closing: \nfilename: " + filename + "\nlocation: " + path);
            }
        }

        if(success)
        {
            Toast toastSaved = Toast.makeText(context, "Saved", duration);
            toastSaved.show();
            filenameCurrent = filename;
            setTitle("NotePlus - " + filenameCurrent);
        }
    }

    private boolean isExternalStorageWriteable()
    {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state))
            return true;

        return false;
    }
}
