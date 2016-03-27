package tr.com.minder.petfeed.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tr.com.minder.petfeed.R;

/**
 * Created by cagri on 22.03.2016.
 */
public class SignUpTabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_signup_tab, container, false);
    }
}
