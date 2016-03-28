package tr.com.minder.petfeed.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import tr.com.minder.petfeed.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        SharedPreferences sharedPref1 = this.getSharedPreferences(
                "10", Context.MODE_PRIVATE);
        System.out.println(sharedPref1.getInt("token", 3));

        SharedPreferences sharedPref2 = this.getPreferences(Context.MODE_PRIVATE);
        System.out.println(sharedPref2.getInt("token", 4));

        Context context2 = getApplicationContext();
        SharedPreferences sharedPref3 = context2.getSharedPreferences(
                "12", Context.MODE_PRIVATE);
        System.out.println(sharedPref3.getInt("token", 5));
    }

}
