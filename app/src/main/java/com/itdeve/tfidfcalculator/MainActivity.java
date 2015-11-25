package com.itdeve.tfidfcalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText doc;
    Button enter;
 private int N;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doc = (EditText) findViewById(R.id.doc);
        enter = (Button) findViewById(R.id.enter);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);

        alert.setTitle("Enter  the number of documents");

        alert.setView(edittext);

        alert.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                N = Integer.parseInt(edittext.getText().toString());


            }
        });

        alert.show();
        final int[] c = {1};
        final HashMap<Integer,String> corpus = new HashMap<>();
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                corpus.put(c[0], doc.getText().toString().toLowerCase());
                c[0]++;

                doc.setText("");
                if(c[0]-1 ==N){
                    Intent intent = new Intent(MainActivity.this, SelectOption.class);
                    for(String n : corpus.values()){
                        Log.d("lett1",n);
                    }
                    intent.putExtra("map", corpus);
                    startActivity(intent);
                }
                }


        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
