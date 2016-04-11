package tr.com.minder.petfeed.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import tr.com.minder.petfeed.MainActivity;
import tr.com.minder.petfeed.R;

/**
 * Created by cagri on 4.04.2016.
 */
public class SessionManager {

    private final static SessionManager INSTANCE = new SessionManager();

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        return INSTANCE;
    }

    public void setToken(Context ctx, String token) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(
                ctx.getString(R.string.shared_user_session), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ctx.getString(R.string.shared_user_session_token), token);
        editor.commit();

    }

    public String checkSession(Context ctx) {

        SharedPreferences sharedPref = ctx.getSharedPreferences(
                ctx.getString(R.string.shared_user_session), Context.MODE_PRIVATE);

        String token_shared = null;

        token_shared = sharedPref.getString(ctx.getString(R.string.shared_user_session_token), null);

        if (token_shared != null)
            System.out.println(token_shared);
        else
            startLoginActivity(ctx);

        return token_shared;
    }

    private void startLoginActivity(Context ctx) {

        Intent intent = new Intent(ctx, MainActivity.class);
        ctx.startActivity(intent);
    }

    public void unsetToken(Context ctx) {

        SharedPreferences sharedPref = ctx.getSharedPreferences(
                ctx.getString(R.string.shared_user_session), Context.MODE_PRIVATE);

        sharedPref.edit().remove(ctx.getString(R.string.shared_user_session_token)).commit();

        startLoginActivity(ctx);
    }
}
