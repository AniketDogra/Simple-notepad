package com.example.android.n_otepad;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class Note_editor extends AppCompatActivity {

    Toolbar toolbar;
    EditText edit_title;
    EditText edit_content;
    Button btn_save;
    String title,content;
    DatabaseHelper db;
    static Integer flag=0;
    FloatingActionButton fab1;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private static Note mNote = null;

    private long mBackPressed;
    private static final int TIME = 2000;

    static String speech_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);


        toolbar = findViewById(R.id.toolbar_editor);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

      //  fab1 = findViewById(R.id.fab_speech);
        db = new DatabaseHelper(getApplicationContext());
        edit_title = findViewById(R.id.edit_text_title);
        edit_content = findViewById(R.id.edit_content);
        btn_save = findViewById(R.id.btn_save);




        getCurrentNote();
        if(mNote!=null)
        {
           // Toast.makeText(getApplicationContext(),"Added data",Toast.LENGTH_SHORT).show();
            addDataInFields();
        }

     //   speech_content = edit_content.getText().toString();


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(saveNote())
                {
                    if(mNote!=null)
                    {
                        Toast.makeText(getApplicationContext(),"Note updated",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Note saved",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    private void speechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to add note");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }
        catch (ActivityNotFoundException a)
        {}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case REQ_CODE_SPEECH_INPUT:
            {
                if(resultCode == RESULT_OK && null != data)
                {
                    ArrayList<String>  result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                     speech_content = (edit_content.getText().toString()).concat(" " + result.get(0));
                     edit_content.setText(speech_content);
                }
            }
        }
    }

    private void getCurrentNote()
    {
        if(NoteListFragment.noteId>0)
        {
            mNote = db.getSingleNote(NoteListFragment.noteId);
        }

    }

    private void addDataInFields()
    {
        edit_title.setText(mNote.getTitle());
        edit_content.setText(mNote.getContent());
    }

    @Override
    public void onBackPressed() {
//
        if(saveNote()) {
            Toast.makeText(getApplicationContext(), "Note saved", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Title or content cannot cannot be Empty",Toast.LENGTH_SHORT).show();
        }

            //super.onBackPressed();


     //   finish();

    }

    @Override
    protected void onStop() {
        mNote = null;
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(flag==1 ){
            edit_content.setText("");
            edit_title.setText("");
            flag=0;
            mNote=null;
        }
    }


    public boolean saveNote()
    {
        title = edit_title.getText().toString();
        content = edit_content.getText().toString();
        if(TextUtils.isEmpty(title))
        {
            edit_title.setError("Title is Required");
            return false;
        }
        if(TextUtils.isEmpty(content))
        {
            edit_content.setError("Content is required");
            return false;
        }
        if(mNote!=null)
        {
            mNote.setTitle(title);
            mNote.setContent(content);
            db.updateNote(mNote);
            mNote = null;
            Intent i =new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);

        }
        else
        {
            Note note = new Note(title,content);
            mNote = note;
            db.insertNote(note);
            Intent i =new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            mNote = null;

        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.speech:
                speechInput();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
