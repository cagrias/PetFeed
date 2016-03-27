package tr.com.minder.petfeed;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonWriter;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tr.com.minder.petfeed.home.HomeActivity;
import tr.com.minder.petfeed.login.LoginPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private final MainActivity THIS = this;

    public final static String USERDATA = "tr.com.minder.petfeed.USERDATA";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String URL = "http://192.168.1.9:8000/rs/login/authenticate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Sign-in"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign-up"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final LoginPagerAdapter adapter = new LoginPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new LoginOnTabSelectedListener(viewPager));
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * Called when the user signs in
     */
    public void signInAccount(View view) throws JSONException, IOException {

        String email = ((EditText) findViewById(R.id.email_signin)).getText().toString();
        String password = ((EditText) findViewById(R.id.password_signin)).getText().toString();

        String json = writeUser(email, password);
        System.out.println(json);
        new AsyncRestCaller().execute(json);


    }

    /**
     * Called when the user signs up
     */
    public void signUpAccount(View view) {

        String password = ((EditText) findViewById(R.id.password_signup)).getText().toString();
        String passwordAgain = ((EditText) findViewById(R.id.password_again_signup)).getText().toString();

        //TODO passwords do not match
        if (!password.equals(passwordAgain))
            return;

        String email = ((EditText) findViewById(R.id.email_signup)).getText().toString();

//        Intent intent = new Intent(this, DisplayMessageActivity.class);
//        EditText editText = (EditText) findViewById(R.id.email);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
    }

    public String writeUser(String email, String password) throws JSONException {

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("username", email);
        jsonObj.put("password", password);
        return jsonObj.toString();
    }

    private class LoginOnTabSelectedListener implements TabLayout.OnTabSelectedListener {

        private ViewPager viewPager;

        public LoginOnTabSelectedListener(ViewPager viewPager) {
            this.viewPager = viewPager;
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }

    private class AsyncRestCaller extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, params[0]);
                Request request = new Request.Builder()
                        .url(URL)
                        .post(body)
                        .build();
                Response response = null;
                response = client.newCall(request).execute();

                String resp = response.body().string();
                JSONObject jObj = new JSONObject(resp);

                if (jObj.getBoolean("type") && jObj.getString("data") != null) {
                    // operation successfull
                    System.out.println(Boolean.TRUE);
                    Intent intent = new Intent(THIS, HomeActivity.class);
                    intent.putExtra(USERDATA, jObj.getString("data"));
                    startActivity(intent);
                } else
                    System.out.println(Boolean.FALSE);

                return null;
                
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
