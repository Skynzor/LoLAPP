package skynzor.lolapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainScreen extends AppCompatActivity implements View.OnClickListener{

    public Button goToProfile;



    public void init() {
        goToProfile = findViewById(R.id.btn_goProfile);

        goToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showProfile = new Intent(MainScreen.this, userProfile.class);

                startActivity(showProfile);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        init();


    }



    @Override
    public void onClick(View v) {



    }
}
