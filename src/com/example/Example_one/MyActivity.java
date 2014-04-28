package com.example.Example_one;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class MyActivity extends Activity implements AdapterView.OnItemSelectedListener {
    /**
     * Called when the activity is first created.
     */

    private Spinner spinner;
    List<String> listList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);

//        RelativeLayout layoutLeft = (RelativeLayout) this.findViewById(R.id.left);
//        ScrollView layoutRight = (ScrollView) this.findViewById(R.id.right);

        Button button = (Button)findViewById(R.id.submit);
        button.setOnClickListener(submitClickListener(this));

        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//        LinearLayout mainView = (LinearLayout)findViewById(R.id.mainLinear);
        String projects = getProjects();
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
                android.R.layout.simple_spinner_item,listList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        LinearLayout mainView = (LinearLayout)findViewById(R.id.right);
        mainView.removeAllViewsInLayout();
        String projectName = (String) spinner.getSelectedItem();
        String response = quotesByProject(projectName);
        DisplayResponse(response);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

    public String getProjects() {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://10.16.4.16:8765/projects_distinct");
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(MyActivity.class.toString(), "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public String recentQuotes() {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://10.16.4.16:8765/recent_10_quotes");
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(MyActivity.class.toString(), "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public String quotesByProject(String projectName) {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://10.16.4.16:8765/project/quotes?project=" + projectName);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(MyActivity.class.toString(), "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public void DisplayResponse(String response) {
        LinearLayout mainView = (LinearLayout)findViewById(R.id.right);
        try {
            JSONArray responseArray = new JSONArray(response);
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonObject = responseArray.getJSONObject(i);
                TextView text2 = new TextView(getApplicationContext());
                text2.setText(jsonObject.getString("quote") + " by " + jsonObject.getString("by_name"));
                text2.setBackgroundResource(R.drawable.back);
                mainView.addView(text2);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener submitClickListener(final Context context) {


        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LinearLayout mainView = (LinearLayout)findViewById(R.id.right);
//
//                String recentQuotes = recentQuotes();
//                try {
//                    JSONArray jsonArray = new JSONArray(recentQuotes);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        TextView text2 = new TextView(context);
//                        text2.setText(jsonObject.getString("quote") + " by " + jsonObject.getString("by_name"));
//                        text2.setBackgroundResource(R.drawable.back);
//                        mainView.addView(text2);
//                    }
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }

                Intent i = new Intent(getApplicationContext(),SideActivity.class);
                startActivity(i);
                setContentView(R.layout.side);
            }
        };
    }


}
