package com.example.admin.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


/**
 * Created by Admin on 10/25/16.
 */
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailActivityFragment())
                    .commit();
        }

    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu){
            //Inflate the menu: this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);

            return true;

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item){
            //Handle action bar item clicks here. The action bar will
            //automatically handle clicks on the Home/up button, so lon as
            // you specify a parent activity in AndroidManifest.xm
            int id = item.getItemId();

            //noninspection SimplifiableIfStatement
            if (id == R.id.action_settings){

                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

                return true;
            }
            return super.onOptionsItemSelected(item);
        }

    public void favorite(View view){

        Button button = (Button)findViewById(R.id.favorite);
        if (button.getText().equals("FAVORITE")){

            button.setText("NO FAVORITE");
            button.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

        } else {

            button.setText("FAVORITE");
            button.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);

        }

    }

    public void trailer1(View view){


    }

    public void trailer2(View view){


    }




    }




