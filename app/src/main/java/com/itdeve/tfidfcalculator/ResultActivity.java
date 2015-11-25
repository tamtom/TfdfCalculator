package com.itdeve.tfidfcalculator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle e = getIntent().getExtras();
        HashMap<Integer, String> m = (HashMap<Integer, String>) e.getSerializable("doc");
        String[] t = e.getStringArray("t");
        int freq[][] = new int[t.length][m.size()];
        for (int i = 0; i < t.length; i++) {
            String letter = t[i];
            Log.d("the letter", letter);
            for (int j = 1; j <= m.size(); j++) {
                String doc[] = m.get(j).split(" ");
                int f = 0;

                for (int k = 0; k < doc.length; k++) {

                    if (letter.equals(doc[k]))
                        f++;
                }
                freq[i][j - 1] = f;
            }

        }
            String type = e.getString("type");
            switch (type) {
                case "freq": {
                    String[] column = {"Term", "document#", "Frequency"};

                    int rl = t.length;
                    int cl = column.length;

                    ScrollView sv = new ScrollView(this);

                    TableLayout tableLayout = createTableLayoutTerm(t, column, rl, cl, m, freq);
                    HorizontalScrollView hsv = new HorizontalScrollView(this);

                    hsv.addView(tableLayout);

                    sv.addView(hsv);

                    setContentView(sv);
                }
                break;
                case "TF": {
                    int a[][] = freq;

                    double[][] tfar = new double[t.length][m.size()];
                    for (int i = 0; i < a.length; i++) {
                        for (int j = 0; j < a[i].length; j++) {
                            if (a[i][j] != 0)
                                if (e.getBoolean("log"))
                                    tfar[i][j] = (Math.log(a[i][j]) + 1);
                                else {
                                    tfar[i][j] = (Math.log10(a[i][j]) / Math.log10(2) + 1);
                                }

                        }
                    }


                    int rl = t.length;
                    int cl = m.size();

                    ScrollView sv = new ScrollView(this);

                    TableLayout tableLayout = createTableLayoutTF(t, rl, cl, m, tfar);
                    HorizontalScrollView hsv = new HorizontalScrollView(this);

                    hsv.addView(tableLayout);

                    sv.addView(hsv);

                    setContentView(sv);
                }
                break;
                case "IDF": {
                    int ni[] = new int[t.length];
                    for (int i = 0; i < t.length; i++) {

                        String l = t[i];
                        Log.d("term", l);
                        for (int j = 1; j <= m.size(); j++) {
                            Log.d("check term", l + "does it in?" + m.get(j).contains(l));
                            if (m.get(j).contains(l)) {

                                ni[i]++;

                            }
                        }
                    }
                    int N = m.size();
                    double idfar[] = new double[t.length];
                    for (int i = 0; i < t.length; i++) {
                        if (e.getBoolean("log"))
                            idfar[i] = Math.log(N / ni[i]);
                        else {
                            idfar[i] = (Math.log10(N / ni[i]) / Math.log10(2));
                        }


                    }

                    int rl = t.length;
                    int cl = m.size();

                    ScrollView sv = new ScrollView(this);

                    TableLayout tableLayout = createTableLayoutIDF(t, rl, cl, m, idfar, ni);
                    HorizontalScrollView hsv = new HorizontalScrollView(this);

                    hsv.addView(tableLayout);

                    sv.addView(hsv);

                    setContentView(sv);
                }
                case "TF-IDF": {
                    int a[][] = freq;
                    double[][] tfar = new double[t.length][m.size()];
                    for (int i = 0; i < a.length; i++) {
                        for (int j = 0; j < a[i].length; j++) {
                            if (a[i][j] != 0)
                                if (e.getBoolean("log"))
                                    tfar[i][j] = (Math.log(a[i][j]) + 1);
                                else {
                                    tfar[i][j] = (Math.log10(a[i][j]) / Math.log10(2) + 1);
                                }

                        }
                    }
                    int ni[] = new int[t.length];
                    for (int i = 0; i < t.length; i++) {

                        String l = t[i];
                        Log.d("term", l);
                        for (int j = 1; j <= m.size(); j++) {
                            Log.d("check term", l + "does it in?" + m.get(j).contains(l));
                            if (m.get(j).contains(l)) {

                                ni[i]++;

                            }
                        }
                    }
                    int N = m.size();
                    double idfar[] = new double[t.length];
                    for (int i = 0; i < t.length; i++) {
                        if (e.getBoolean("log"))
                            idfar[i] = Math.log(N / ni[i]);
                        else {
                            idfar[i] = (Math.log10(N / ni[i]) / Math.log10(2));
                        }
                    }
                    double[][] Tfand = new double[t.length][m.size()];
                    for (int i = 0; i < t.length; i++) {
                        for (int j = 0; j < m.size(); j++) {
                            Tfand[i][j] = tfar[i][j] * idfar[i];
                        }
                    }
                    int rl = t.length;
                    int cl = m.size();

                    ScrollView sv = new ScrollView(this);

                    TableLayout tableLayout = createTableLayoutTFandIDF(t, m, Tfand);
                    HorizontalScrollView hsv = new HorizontalScrollView(this);

                    hsv.addView(tableLayout);

                    sv.addView(hsv);

                    setContentView(sv);
                }

            }
    }

    private TableLayout createTableLayoutTFandIDF(String[] rv, HashMap<Integer, String> m, double[][] freq) {
        // 1) Create a tableLayout and its params
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setBackgroundColor(Color.BLACK);

        // 2) create tableRow params
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1, 1, 1, 1);
        tableRowParams.weight = 1;
        TableRow tableRow = new TableRow(this);
        tableRow.setBackgroundColor(Color.BLACK);

        //create header
        for (int j = 0; j <= m.size(); j++) {
            // 4) create textView
            TextView textView = new TextView(this);
            //  textView.setText(String.valueOf(j));
            textView.setBackgroundColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            if (j == 0)
                textView.setText("Vocabulary");
            else
                textView.setText("(d" + j + ")");


            // 5) add textView to tableRow
            tableRow.addView(textView, tableRowParams);
        }

        // 6) add tableRow to tableLayout
        tableLayout.addView(tableRow, tableLayoutParams);

        for (int i = 0; i < rv.length; i++) {
            // 3) create tableRow

            TableRow tableRow2 = new TableRow(this);
            tableRow2.setBackgroundColor(Color.BLACK);


            for (int j = -1; j < m.size(); j++) {
                // 4) create textView
                TextView textView = new TextView(this);
                //  textView.setText(String.valueOf(j));
                textView.setBackgroundColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER);
                if (j == -1)
                    textView.setText(rv[i]);
                else {
                    textView.setText(freq[i][j] + "");
                }


                // 5) add textView to tableRow
                tableRow2.addView(textView, tableRowParams);
            }

            // 6) add tableRow to tableLayout
            tableLayout.addView(tableRow2, tableLayoutParams);


        }

        return tableLayout;
    }
    public void makeCellEmpty(TableLayout tableLayout, int rowIndex, int columnIndex) {
        // get row from table with rowIndex
        TableRow tableRow = (TableRow) tableLayout.getChildAt(rowIndex);

        // get cell from row with columnIndex
        TextView textView = (TextView)tableRow.getChildAt(columnIndex);

        // make it black
        textView.setBackgroundColor(Color.BLACK);
    }
    public void setHeaderTitle(TableLayout tableLayout, int rowIndex, int columnIndex){

        // get row from table with rowIndex
        TableRow tableRow = (TableRow) tableLayout.getChildAt(rowIndex);

        // get cell from row with columnIndex
        TextView textView = (TextView)tableRow.getChildAt(columnIndex);

        textView.setText("Hello");
    }

    private TableLayout createTableLayoutIDF(String[] rv, int rowCount, int columnCount, HashMap<Integer, String> m, double[] idfar, int[] ni) {
        // 1) Create a tableLayout and its params
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setBackgroundColor(Color.BLACK);

        // 2) create tableRow params
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1, 1, 1, 1);
        tableRowParams.weight = 1;
        TableRow tableRow = new TableRow(this);
        tableRow.setBackgroundColor(Color.BLACK);

        //create header
        for (int j = 0; j < 3; j++) {
            // 4) create textView
            TextView textView = new TextView(this);
            //  textView.setText(String.valueOf(j));
            textView.setBackgroundColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            if (j == 0)
                textView.setText("term");
            if (j == 1)
                textView.setText("ni");
            if (j == 2) {
                textView.setText("idf=log(N/ni");
            }


            // 5) add textView to tableRow
            tableRow.addView(textView, tableRowParams);
        }

        // 6) add tableRow to tableLayout
        tableLayout.addView(tableRow, tableLayoutParams);

        for (int i = 0; i < rv.length; i++) {
            // 3) create tableRow

            TableRow tableRow2 = new TableRow(this);
            tableRow2.setBackgroundColor(Color.BLACK);


            for (int j = 0; j < 3; j++) {
                // 4) create textView
                TextView textView = new TextView(this);
                //  textView.setText(String.valueOf(j));
                textView.setBackgroundColor(Color.WHITE);
                textView.setGravity(Gravity.CENTER);

                if (j == 0)
                    textView.setText(rv[i]);
                if (j == 1)
                    textView.setText(ni[i] + "");
                if (j == 2) {
                    textView.setText(idfar[i] + "");
                }

                // 5) add textView to tableRow
                tableRow2.addView(textView, tableRowParams);
            }

            // 6) add tableRow to tableLayout
            tableLayout.addView(tableRow2, tableLayoutParams);

        }


        return tableLayout;
    }

    private TableLayout createTableLayoutTF(String[] rv, int rowCount, int columnCount, HashMap<Integer, String> m, double[][] freq) {
        // 1) Create a tableLayout and its params
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setBackgroundColor(Color.BLACK);

        // 2) create tableRow params
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1, 1, 1, 1);
        tableRowParams.weight = 1;
        TableRow tableRow = new TableRow(this);
        tableRow.setBackgroundColor(Color.BLACK);

        //create header
        for (int j = 0; j <= m.size(); j++) {
            // 4) create textView
            TextView textView = new TextView(this);
            //  textView.setText(String.valueOf(j));
            textView.setBackgroundColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            if (j == 0)
                textView.setText("Vocabulary");
            else
                textView.setText("tf(i" + j + ")");


            // 5) add textView to tableRow
            tableRow.addView(textView, tableRowParams);
        }

        // 6) add tableRow to tableLayout
        tableLayout.addView(tableRow, tableLayoutParams);

        for (int i = 0; i < rv.length; i++) {
            // 3) create tableRow
            for (int cu = 0; cu < m.size(); cu++) {
                TableRow tableRow2 = new TableRow(this);
                tableRow2.setBackgroundColor(Color.BLACK);


                for (int j = -1; j < m.size(); j++) {
                    // 4) create textView
                    TextView textView = new TextView(this);
                    //  textView.setText(String.valueOf(j));
                    textView.setBackgroundColor(Color.WHITE);
                    textView.setGravity(Gravity.CENTER);
                    if (j == -1)
                        textView.setText(rv[i]);
                    else {
                        textView.setText(freq[i][j] + "");
                    }


                    // 5) add textView to tableRow
                    tableRow2.addView(textView, tableRowParams);
                }

                // 6) add tableRow to tableLayout
                tableLayout.addView(tableRow2, tableLayoutParams);

            }

        }

        return tableLayout;
    }

    private TableLayout createTableLayoutTerm(String[] rv, String[] cv, int rowCount, int columnCount, HashMap<Integer, String> m, int[][] freq) {
        // 1) Create a tableLayout and its params
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setBackgroundColor(Color.BLACK);

        // 2) create tableRow params
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1, 1, 1, 1);
        tableRowParams.weight = 1;
        TableRow tableRow = new TableRow(this);
        tableRow.setBackgroundColor(Color.BLACK);


        for (int j= 0; j < columnCount; j++) {
            // 4) create textView
            TextView textView = new TextView(this);
            //  textView.setText(String.valueOf(j));
            textView.setBackgroundColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            textView.setText(cv[j]);


            // 5) add textView to tableRow
            tableRow.addView(textView, tableRowParams);
        }

        // 6) add tableRow to tableLayout
        tableLayout.addView(tableRow, tableLayoutParams);

        for (int i = 0; i < rowCount; i++) {
            // 3) create tableRow
            for (int cu = 0; cu <m.size() ; cu++) {
                TableRow tableRow2 = new TableRow(this);
                tableRow2.setBackgroundColor(Color.BLACK);


                for (int j= 0; j < columnCount; j++) {
                    // 4) create textView
                    TextView textView = new TextView(this);
                    //  textView.setText(String.valueOf(j));
                    textView.setBackgroundColor(Color.WHITE);
                    textView.setGravity(Gravity.CENTER);
                    if(j==0)
                        textView.setText(rv[i]);
                    if(j==1)
                        textView.setText("d"+cu);
                    if(j==2)
                        textView.setText(freq[i][cu]
                                + "");


                    // 5) add textView to tableRow
                    tableRow2.addView(textView, tableRowParams);
                }

                // 6) add tableRow to tableLayout
                tableLayout.addView(tableRow2, tableLayoutParams);

            }

        }

        return tableLayout;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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
