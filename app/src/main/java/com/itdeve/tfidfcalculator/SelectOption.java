package com.itdeve.tfidfcalculator;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.sql.Struct;
import java.util.HashMap;
import java.util.HashSet;

public class SelectOption extends AppCompatActivity implements View.OnClickListener {
    Button frequency;
    Button tf;
    Button idf;
    Button tfandidf;
    Button documentlength;
    Button s_score;
    String arr[];

    public double[][] getTf_idf() {
        return tf_idf;
    }

    public void setTf_idf(double[][] tf_idf) {
        this.tf_idf = tf_idf;
        for (int i = 0  ; i < arr.length;i++){
            for (int j =0 ; j< curpos.size();j++){
                tf_idf[i][j] = tfar[i][j] * idfar[i];
            }
        }
    }


    public double[] getIdfar() {
        return idfar;
    }

    public void setIdfar(double[] idf) {
        this.idfar = idf;
        int nsubi [] = getNi();
        int N = curpos.size();
        for (int i = 0; i <arr.length ; i++) {
            idfar[i] = Math.log(N/nsubi[i]);
        }
    }

    double idfar[];
    public int[] getNi() {
        return ni;
    }

    public void setNi(int[] ni) {
        this.ni = ni;
        for (int i = 0; i <arr.length ; i++) {
            String l = arr[i];
            for (int j = 1; j <=curpos.size(); j++) {
                if(curpos.get(j).contains(l)){
                    ni[i]++;
                    break;
                }
            }
        }

    }

    int ni [] ;
    HashMap<Integer, String> curpos;

    public double[][] getTfar() {
        return tfar;
    }

    public void setTfar(double[][] tfar) {
        this.tfar = tfar;
        int a [][] = getFrequencyar();
        tfar = new double[arr.length][curpos.size()];
        for (int i = 0; i <a.length ; i++) {
            for (int j = 0; j <a[i].length ; j++) {
                if(a[i][j]!=0)
                    tfar[i][j] =  (Math.log(a[i][j]) + 1);

            }
        }
    }

    double tfar[][] ;

    double tf_idf[][];
HashSet<String> set ;

    public int[][] getFrequencyar() {
        return frequencyar;
    }

    public void setFrequencyar(int[][] frequencyar) {
        this.frequencyar = frequencyar;
        for (int i = 0; i < arr.length; i++) {
            String letter = arr[i];
            for (int j = 1; j <=curpos.size() ; j++) {
                String doc[] = curpos.get(j).split(" ");
                int f = 0;

                for (int k = 0; k <doc.length ; k++) {
                    if(letter.equals(doc[k]))
                        f++;
                }
                frequencyar[i][j-1] =f;
            }

        }

    }

    int frequencyar [][] ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_option);
        Intent intent = getIntent();
        curpos = (HashMap<Integer, String>) intent.getSerializableExtra("map");
        frequency = (Button) findViewById(R.id.frequncy);
        tf = (Button) findViewById(R.id.tf);
        idf = (Button) findViewById(R.id.idf);
        tfandidf = (Button) findViewById(R.id.tfandidf);
        documentlength = (Button) findViewById(R.id.documentlength);
        s_score = (Button) findViewById(R.id.s_score);
        set = new HashSet<>();
        for(String doc : curpos.values()){
           if(set.add(doc)){
               Log.d("string", "added "+doc);
           }
        }
        arr = set.toArray(new String[set.size()]);
        for (int i = 0; i <arr.length ; i++) {
            Log.d("set",arr[i]);
        }
        frequency.setOnClickListener(this);
frequencyar =  new int[arr.length][curpos.size()];
        setFrequencyar(frequencyar);
        tfar = new double[arr.length][curpos.size()];
        setTfar(tfar);
        ni = new int [arr.length];
        setNi(ni);
        idfar = new double [arr.length];
        setIdfar(idfar);
        tf_idf = new double[arr.length][curpos.size()];
        setTf_idf(tf_idf);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_option, menu);
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

    @Override
    public void onClick(View v) {
    if(v.getId()==R.id.frequncy){
        int a [][] = getFrequencyar();
        int F[] = new int[arr.length];

        for (int i = 0; i <arr.length ; i++) {

            for (int j = 0; j <curpos.size() ; j++)
            {
                F[i]+=a[i][j];
            }
        }

Intent i = new Intent(SelectOption.this,ResultActivity.class);
        i.putExtra("t", arr);

        i.putExtra("F",F);
        i.putExtra("doc",curpos);
        i.putExtra("type","freq");
        startActivity(i);
    }
        else if(v.getId()==R.id.tf){
getTfar();
    }
        else if (v.getId()==R.id.idf){
    getIdfar();


    }
        else if(v.getId()==R.id.tfandidf){
        getTf_idf();
    }

    }
}
