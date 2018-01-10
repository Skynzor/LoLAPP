package skynzor.lolapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class userProfile extends AppCompatActivity  implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    public LinearLayout prof_section;
    public Button SignOut;
    public SignInButton SignIn;
    public TextView name,email;
    public ImageView prof_pic;
    public GoogleApiClient googleApiClient;
    public static final int REQ_CODE = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        prof_section = (LinearLayout)findViewById(R.id.prof_section);
        SignOut = (Button)findViewById(R.id.btn_logout);
        SignIn = (SignInButton) findViewById(R.id.btn_login);
        SignIn.setOnClickListener(this);
        SignOut.setOnClickListener(this);
        name = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.email);
        prof_pic = (ImageView)findViewById(R.id.prof_pic);
        prof_section.setVisibility(View.GONE);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                signIn();
                break;
            case R.id.btn_logout:
                signOut();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_CODE){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    public void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    public void handleResult(GoogleSignInResult result){

        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            String username = account.getDisplayName();
            String useremail = account.getEmail();
            String img_url = account.getPhotoUrl().toString();
            name.setText(username);
            email.setText(useremail);
            Glide.with(this).load(img_url).into(prof_pic);
            updateUI(true);
        } else {
            updateUI(false);
        }
    }

    public void updateUI(boolean isLogin){
        if(isLogin){
            prof_section.setVisibility(View.VISIBLE);
            SignIn.setVisibility(View.GONE);
        } else {
            prof_section.setVisibility(View.GONE);
            SignIn.setVisibility(View.VISIBLE);
        }
    }

    public void signOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }


}
