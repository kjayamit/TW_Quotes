package com.example.Example_one;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.example.Resources.NothingSelectedSpinnerAdapter;
import com.example.Resources.QuotesServiceAdapters;
import org.json.JSONArray;
import org.json.JSONObject;

public class MyActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private final QuotesServiceAdapters quotesServiceAdapters = new QuotesServiceAdapters();
    /**
     * Called when the activity is first created.
     */

    private Spinner spinner;
    List<String> listList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);

        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String projects = quotesServiceAdapters.getProjects();
        try {
            JSONArray jsonArray = new JSONArray(projects);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                listList.add(jsonObject.getString("project"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        spinner = (Spinner) findViewById(R.id.spinner);
        final ArrayAdapter<String>adapter = new ArrayAdapter<String> (this,
                R.layout.spinner_layout,listList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Select Project");

        spinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_layout,
                        this));
        spinner.setOnItemSelectedListener(this);

        final EditText edittext = (EditText) findViewById(R.id.search);
        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    LinearLayout mainView = (LinearLayout)findViewById(R.id.right);
                    mainView.removeAllViews();

                    spinner.setAdapter(
                            new NothingSelectedSpinnerAdapter(
                                    adapter,
                                    R.layout.spinner_layout,
                                    MyActivity.this));

                    String response = null;
                    try {
                        response = quotesServiceAdapters.searchFor(edittext.getText().toString().trim());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    DisplayResponse(response);

                    return true;
                }
                return false;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
        String response = quotesServiceAdapters.recentQuotes();
        DisplayResponse(response);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.menu_red);
        item.setVisible(false);
        this.invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_green:
                Intent i = new Intent(getApplicationContext(),SideActivity.class);
                startActivity(i);
                setContentView(R.layout.side);
                return true;
            case R.id.menu_red:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        if(position > 0) {
            EditText edittext = (EditText) findViewById(R.id.search);
            edittext.setText("");
        LinearLayout mainView = (LinearLayout)findViewById(R.id.right);
        mainView.removeAllViewsInLayout();
        String projectName = (String) spinner.getSelectedItem();
        String response = quotesServiceAdapters.quotesByProject(projectName);
        DisplayResponse(response);
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Back disabled in App", Toast.LENGTH_SHORT).show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

    public void DisplayResponse(String response) {
        LinearLayout mainView = (LinearLayout)findViewById(R.id.right);
        try {
            JSONArray responseArray = new JSONArray(response);
            for (int i = 0; i < responseArray.length(); i++) {


                RelativeLayout lLayout = new RelativeLayout(getApplicationContext());

                ImageView imgView = new ImageView(getApplicationContext());
                imgView.setImageResource(R.drawable.elephant_icon);
                RelativeLayout.LayoutParams params_2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                params_2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                imgView.setLayoutParams(params_2);
                lLayout.addView(imgView);


                JSONObject jsonObject = responseArray.getJSONObject(i);
                TextView text2 = new TextView(getApplicationContext());
                text2.setTextColor(Color.rgb(128, 255, 0));
                text2.setText(jsonObject.getString("quote") + " by " + jsonObject.getString("by_name"));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                text2.setLayoutParams(params);
                text2.setBackgroundResource(R.drawable.brown_2);
                lLayout.addView(text2);

                mainView.addView(lLayout);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}


