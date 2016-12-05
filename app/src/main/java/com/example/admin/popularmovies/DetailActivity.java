package com.example.admin.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


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

                return true;
            }
            return super.onOptionsItemSelected(item);
        }





    }




