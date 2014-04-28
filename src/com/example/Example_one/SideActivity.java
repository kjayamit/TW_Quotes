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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

    public class SideActivity extends Activity implements View.OnClickListener {

        Button bt1;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.side);
            bt1 = (Button) findViewById(R.id.back);
            bt1.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            // TODO Auto-generated method stub
            Intent i = new Intent(getApplicationContext(),MyActivity.class);
            startActivity(i);
            setContentView(R.layout.main);
        }


}
