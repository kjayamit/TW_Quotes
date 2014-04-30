package com.example.Example_one;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

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
        ArrayAdapter<String>adapter = new ArrayAdapter<String> (this,
                R.layout.spinner_layout,listList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt("Select Project");

        spinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.spinner_layout,
                        this));
        spinner.setOnItemSelectedListener(this);
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
        LinearLayout mainView = (LinearLayout)findViewById(R.id.right);
        mainView.removeAllViewsInLayout();
        String projectName = (String) spinner.getSelectedItem();
        String response = quotesServiceAdapters.quotesByProject(projectName);
        DisplayResponse(response);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

    public void DisplayResponse(String response) {
        LinearLayout mainView = (LinearLayout)findViewById(R.id.right);
        try {
            JSONArray responseArray = new JSONArray(response);
            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject jsonObject = responseArray.getJSONObject(i);
                TextView text2 = new TextView(getApplicationContext());
//                text2.setTextColor(android.R.color.black);
                text2.setTextColor(Color.rgb(139,137,137));
                text2.setText(jsonObject.getString("quote") + " by " + jsonObject.getString("by_name"));
                text2.setBackgroundResource(R.drawable.back);
                mainView.addView(text2);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}


