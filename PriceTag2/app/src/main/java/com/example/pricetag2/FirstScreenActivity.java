package com.example.pricetag2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import com.example.myapplication11114.R;

public class FirstScreenActivity extends AppCompatActivity {

    private SearchView searchView_firstScreenSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        InitVar();
        InitClickListener();
    }

    private void InitVar() {
        searchView_firstScreenSearch = findViewById(R.id.serarchView_first_screen);
    }

    private void InitClickListener() {
        searchView_firstScreenSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent();
                intent.putExtra("query", query);

                setResult(MainActivity.RESULT_CODE_FIRSTSCREENACTIVITY, intent);
                finish();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}