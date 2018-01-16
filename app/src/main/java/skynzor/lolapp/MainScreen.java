package skynzor.lolapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainScreen extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    public SignInButton SignIn;
    public Button SignOut;
    public ImageButton goProfile;

    public TextView name,email;
    public ImageView avatar;
    public GoogleApiClient googleApiClient;
    public static final int REQ_CODE = 9001;




    public void goProfile() {
        goProfile = findViewById(R.id.btn_goProfile);

        goProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showProfile = new Intent(MainScreen.this, userProfile.class);

                startActivity(showProfile);
            }
        });}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        goProfile();

        SignIn = findViewById(R.id.btn_googlePlusSignIn);
        SignIn.setOnClickListener(this);
        SignOut = findViewById(R.id.btn_signOut);
        SignOut.setOnClickListener(this);
        name = findViewById(R.id.txt_userLoggedInAs);
        email = findViewById(R.id.txt_emailUserLoggedInAs);
        avatar = findViewById(R.id.img_avatar);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

    }



    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_googlePlusSignIn:
                signIn();
                break;
            case R.id.btn_signOut:
                signOut();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_CODE){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    public void handleResult(GoogleSignInResult result){

        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Fetching Google+ Sign in...");
            progressDialog.show();

            String username = account.getDisplayName();
            String useremail = account.getEmail();
            String img_url = account.getPhotoUrl().toString();
            name.setText(username);
            email.setText(useremail);
            Glide.with(this).load(img_url).into(avatar);
            updateUI(true);
            progressDialog.dismiss();
        } else {
            updateUI(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly. We can try and retrieve an
            // authentication code.
            //Log.d(TAG, "Got cached sign-in");
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Fetching Google+ Sign in...");
            progressDialog.show();

            GoogleSignInResult result = opr.get();
            handleResult(result);

            progressDialog.dismiss();
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Checking sign in state...");
            progressDialog.show();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    progressDialog.dismiss();
                    handleResult(googleSignInResult);
                }
            });
        }
    }

    public void updateUI(boolean isLogin){
        if(isLogin){
            SignIn.setVisibility(View.GONE);
            SignOut.setVisibility(View.VISIBLE);
            goProfile.setVisibility(View.VISIBLE);
        } else {
            SignIn.setVisibility(View.VISIBLE);
            SignOut.setVisibility(View.GONE);
            name.setText(R.string.txt_userLoggedInAs);
            email.setText(R.string.txt_emailUserLoggedInAs);
            avatar.setImageDrawable(getResources().getDrawable(R.drawable.defaultavatar, getApplicationContext().getTheme()));
            goProfile.setVisibility(View.GONE);
        }
    }

    public void signOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }
}
