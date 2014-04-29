package com.example.Example_one;

import android.app.Activity;

/**
 * Created with IntelliJ IDEA.
 * User: jayachk
 * Date: 4/25/14
 * Time: 5:34 PM
 * To change this template use File | Settings | File Templates.
 */


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SideActivity extends Activity implements View.OnClickListener {

        Button bt1;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.side);
            bt1 = (Button) findViewById(R.id.submit);
            bt1.setOnClickListener(this);
        }


        @Override
        public void onClick(View v)
        {

            EditText quote = (EditText) findViewById(R.id.quote);
            EditText name = (EditText) findViewById(R.id.name);
            EditText project = (EditText) findViewById(R.id.project);

            if(quote.getText().toString().length() < 1  || name.getText().toString().length() < 1 || project.getText().toString().length() < 1){
                Toast.makeText(getApplicationContext(), "Please enter all the details", Toast.LENGTH_SHORT).show();
            } else {

            postData(quote.getText().toString(), name.getText().toString(), project.getText().toString());

            Toast.makeText(getApplicationContext(), "Quote saved", Toast.LENGTH_SHORT).show();

            quote.setText("");
            name.setText("");
            project.setText("");

            Intent i = new Intent(getApplicationContext(),MyActivity.class);
            startActivity(i);
            setContentView(R.layout.main);
            }
        }

        public void postData(String quote, String name, String project) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://10.16.4.16:8765/quote");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("project", project));
                nameValuePairs.add(new BasicNameValuePair("by_name", name));
                nameValuePairs.add(new BasicNameValuePair("quote",quote));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_green:
                    return true;
                case R.id.menu_red:
                    Intent i = new Intent(getApplicationContext(),MyActivity.class);
                    startActivity(i);
                    setContentView(R.layout.main);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }


}
