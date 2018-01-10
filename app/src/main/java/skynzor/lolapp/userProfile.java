package skynzor.lolapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class userProfile extends AppCompatActivity  implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private LinearLayout prof_section;
    private Button SignOut;
    private TextView name,email;
    private ImageView prof_pic;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        prof_section = (LinearLayout)findViewById(R.id.prof_section);
        SignOut = (Button)findViewById(R.id.btn_logout);
        name = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.email);
        prof_pic = (ImageView)findViewById(R.id.prof_pic);
        SignOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
