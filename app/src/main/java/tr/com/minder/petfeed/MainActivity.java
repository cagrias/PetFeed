package tr.com.minder.petfeed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import tr.com.minder.petfeed.session.SessionManager;

public class MainActivity extends AppCompatActivity {

    private final MainActivity THIS = this;

    public final static String USERDATA = "tr.com.minder.petfeed.USERDATA";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

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
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.sign_in)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.sign_up)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final LoginPagerAdapter adapter = new LoginPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new LoginOnTabSelectedListener(viewPager));

        //savedInstanceState.getString(getString(R.string.shared_user_session_token))

    }

    /**
     * Called when the user signs in
     */
    public void signInAccount(View view) throws JSONException, IOException {

        String email = ((EditText) findViewById(R.id.email_signin)).getText().toString();
        String password = ((EditText) findViewById(R.id.password_signin)).getText().toString();

        String json = writeUser(email, password, null);
        System.out.println(json);
        new AsyncRestCaller().execute(getString(R.string.BASE_URL) + getString(R.string.SIGN_IN), json);
    }

    /**
     * Called when the user signs up
     */
    public void signUpAccount(View view) throws JSONException, IOException {

        String password = ((EditText) findViewById(R.id.password_signup)).getText().toString();
        String passwordAgain = ((EditText) findViewById(R.id.password_again_signup)).getText().toString();

        if (!password.equals(passwordAgain)) {
            Snackbar.make(findViewById(R.id.login_coordinator_layout), getString(R.string.passwords_do_not_match),
                    Snackbar.LENGTH_LONG)
                    .show();
            return;
        }

        String name = ((EditText) findViewById(R.id.name_signup)).getText().toString();
        String email = ((EditText) findViewById(R.id.email_signup)).getText().toString();

        String json = writeUser(email, password, name);
        System.out.println(json);
        new AsyncRestCaller().execute(getString(R.string.BASE_URL) + getString(R.string.SIGN_UP), json);

    }

    public String writeUser(String email, String password, String name) throws JSONException {

        JSONObject jsonObj = new JSONObject();

        jsonObj.put(getString(R.string.json_data_username), email);
        jsonObj.put(getString(R.string.json_data_password), password);

        if (name != null)
            jsonObj.put(getString(R.string.json_data_name), name);

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

    private class AsyncRestCaller extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, params[1]);
                Request request = new Request.Builder()
                        .url(params[0])
                        .post(body)
                        .build();
                Response response = null;
                response = client.newCall(request).execute();

                String resp = response.body().string();
                JSONObject jObj = new JSONObject(resp);

                return jObj;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            try {
                if (result == null) {
                    Snackbar.make(findViewById(R.id.login_coordinator_layout), getString(R.string.login_error),
                            Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }

                if (!result.getBoolean(getString(R.string.json_type)) || result.getString(getString(R.string.json_data)) == null) {
                    Snackbar.make(findViewById(R.id.login_coordinator_layout), result.getString(getString(R.string.json_message)),
                            Snackbar.LENGTH_LONG)
                            .show();
                    return;
                }

                // login successfull
                JSONObject user = result.getJSONObject(getString(R.string.json_data));

                // set user token to shared preferences
                SessionManager.getInstance().setToken(getApplicationContext(),
                        user.getString(getString(R.string.json_data_token)));

                startHomeActivity();
                return;


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(THIS, HomeActivity.class);
//                intent.putExtra(USERDATA, result.getString(getString(R.string.json_data)));
        startActivity(intent);
        finish();
    }


}
