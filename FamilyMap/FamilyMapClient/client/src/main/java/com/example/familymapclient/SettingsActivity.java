package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;

import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    DataCache dataCache = DataCache.getInstance();
    Switch lifeLines;
    Switch treeLines;
    Switch spouseLines;
    Switch fatherSide;
    Switch motherSide;
    Switch maleEvents;
    Switch femaleEvents;
    LinearLayout logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        lifeLines = findViewById(R.id.lifeLineSwitch);
        treeLines = findViewById(R.id.treeLineSwitch);
        spouseLines = findViewById(R.id.spouseLineSwitch);
        fatherSide = findViewById(R.id.fatherSwitch);
        motherSide = findViewById(R.id.motherSwitch);
        maleEvents = findViewById(R.id.maleSwitch);
        femaleEvents = findViewById(R.id.femaleSwitch);
        logout = (LinearLayout) findViewById(R.id.logout);
        setDefaultChecks();

        lifeLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataCache.setLifeStoryLines(isChecked);
            }
        });

        treeLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataCache.setFamilyTreeLines(isChecked);
            }
        });

        spouseLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataCache.setSpouseLines(isChecked);
            }
        });

        fatherSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataCache.setFatherSide(isChecked);
            }
        });

        motherSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataCache.setMotherSide(isChecked);
            }
        });

        maleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataCache.setMaleEvents(isChecked);
            }
        });

        femaleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataCache.setFemaleEvents(isChecked);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCache.logout();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            dataCache.updateFilters();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

    private void setDefaultChecks(){
        lifeLines.setChecked(dataCache.isLifeStoryLines());
        treeLines.setChecked(dataCache.isFamilyTreeLines());
        spouseLines.setChecked(dataCache.isSpouseLines());
        fatherSide.setChecked(dataCache.isFatherSide());
        motherSide.setChecked(dataCache.isMotherSide());
        maleEvents.setChecked(dataCache.isMaleEvents());
        femaleEvents.setChecked(dataCache.isFemaleEvents());
    }
}