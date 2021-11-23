package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button mtruebutton;
    private static final String FILE_NAME = "example.txt";
    Button mfalsebutton;
    TextView mscoreview;
    SharedPreferences sp;
    TextView mQuestiontextview;
    ProgressBar mProgressBar;
    String mystr;
    private final static String STORETEXT="storetext.txt";
    int mIndex;
    int mfinal = 0;
    int mScore=0;
    int FinalScore = 0;
    int score;
    TrueFalse[] QuestionBank= new TrueFalse[]{
            new TrueFalse(R.string.question_1,false),
            new TrueFalse(R.string.question_2,true),
            new TrueFalse(R.string.question_3,true),
            new TrueFalse(R.string.question_4,false),
            new TrueFalse(R.string.question_5,false),
    };
    final int progress_bar_increment = (int) Math.ceil(100.0 / QuestionBank.length);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("Myuserpref",MODE_PRIVATE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));

        if(savedInstanceState!=null){
            mScore = savedInstanceState.getInt("Score");
            mIndex = savedInstanceState.getInt("Index");


        }else{
            mScore  =0 ;
            mIndex = 0;
        }
        mtruebutton = findViewById(R.id.true_button);
        mfalsebutton = findViewById(R.id.false_button);
        mQuestiontextview = findViewById(R.id.question_view);
        mProgressBar = findViewById(R.id.progressBar);
        mscoreview = findViewById(R.id.score);
        mscoreview.setText("Score:"+FinalScore+"/"+QuestionBank.length);
        TrueFalse first_question = QuestionBank[mIndex];
        int questionRID = first_question.getmQuestionID();
        mQuestiontextview.setText(questionRID);
        mystr = mQuestiontextview.getText().toString();
        replaceFragment(new Fragment1());
        Collections.shuffle(Arrays.asList(QuestionBank));
        mtruebutton.setOnClickListener(v -> {
            checkAnswer(true);
            mystr = mQuestiontextview.getText().toString();
            updateQuestion();
            score++;
        });
        mfalsebutton.setOnClickListener(v -> {
            checkAnswer(false);
            mystr = mQuestiontextview.getText().toString();
            updateQuestion();
            score++;
        });
        score += QuestionBank.length;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ic,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                load();
                return true;
            case R.id.item2:
                return true;
            case R.id.item3:
                Reset();
                save();
                return true;
            case R.id.item4:
                ChangeLanguage();
                return true;
        }
        return true;
    }

    private void Reset() {
        mScore =0;
        FinalScore =0 ;
        mfinal = 0;
    }

    private void ChangeLanguage() {
        final String[] listItems = {"हिंदी","English"};
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(MainActivity.this);
        mbuilder.setTitle("Choose language");
        mbuilder.setSingleChoiceItems(listItems, 1, (dialogInterface, i) -> {
            if(i == 0) {
                setLocale("hi");
                recreate();
            }
            else if(i == 1) {
                setLocale("en");
                recreate();
            }
            dialogInterface.dismiss();
        });
        AlertDialog mDialog = mbuilder.create();
        mDialog.show();
    }

    private void setLocale(String hi) {
        Locale locale = new Locale(hi);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor  = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_lang",hi);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String lang = prefs.getString("My_lang","");
        setLocale(lang);
    }


    @SuppressLint("SetTextI18n")
    public void updateQuestion(){
        int random = (int) (Math.random()*2 + 1);
        if(random == 1) {
            replaceFragment(new Fragment1());
        }else if(random == 2){
            replaceFragment(new Fragment2());
        }
        mIndex = ((mIndex+1) %QuestionBank.length);
        if (mIndex==0){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Game is finished");
            alert.setCancelable(false).setMessage("You scored "+ FinalScore + " points out of " +QuestionBank.length ).setPositiveButton("Ignore", (dialog, which) -> finish()).setNegativeButton("Save", (dialog, which) -> {
                save();
                mProgressBar.setProgress(0);
                recreate();
                score+=5;
            });
            alert.show();
        }
        int questionRID =  QuestionBank[mIndex].getmQuestionID();
        mQuestiontextview.setText(questionRID);
        mProgressBar.incrementProgressBy(progress_bar_increment);
        mscoreview.setText("Score:"+FinalScore+"/"+QuestionBank.length);
        score++;
    }



    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();

    }


    public void checkAnswer(boolean userSelection){
        boolean correctAnswer = QuestionBank[mIndex].ismAnswer();
        if (userSelection==correctAnswer){
            mScore++;
            FinalScore++;
            score++;
            Toast.makeText(getApplicationContext(),R.string.correct_toast,Toast.LENGTH_SHORT).show();
        }
        else {
            score++;
            Toast.makeText(getApplicationContext(),R.string.incorrect_toast,Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("Score",mScore);
        outState.putInt("Index",mIndex);

    }
    public void save() {
        try {
            OutputStreamWriter out= new OutputStreamWriter(openFileOutput(STORETEXT, 0));
            out.write(String.valueOf(mScore));
            out.close();
            Toast.makeText(this, "The contents are saved in the file.", Toast.LENGTH_LONG).show();
        }
        catch (Throwable t) {
            Toast.makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG).show();
        }
    }



    private void load() {
        try {
            InputStream in = openFileInput(STORETEXT);
            if (in != null) {
                InputStreamReader tmp=new InputStreamReader(in);
                BufferedReader reader=new BufferedReader(tmp);
                String str;
                StringBuilder buf=new StringBuilder();
                while ((str = reader.readLine()) != null) {
                    buf.append(str);
                }
                in.close();
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(MainActivity.this);
                mbuilder.setTitle("You scored "+ buf.toString()+ " points out of " + score);
                AlertDialog mDialog = mbuilder.create();
                mDialog.show();
                }
        }
        catch (java.io.FileNotFoundException e) {
        }
        catch (Throwable t) {
            Toast.makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG).show();

        }

    }


    public String getMyData() {
        return mystr;
    }
}
