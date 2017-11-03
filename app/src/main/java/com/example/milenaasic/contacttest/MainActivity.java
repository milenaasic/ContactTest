package com.example.milenaasic.contacttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.milenaasic.contacttest.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements ContactFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar= (Toolbar) findViewById(R.id.mytoolbar);

       setSupportActionBar(myToolbar);

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
