package com.captech.teegarden.awarenessdemo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.captech.teegarden.awarenessdemo.databinding.ActivityMainBinding;
import com.captech.teegarden.awarenessdemo.headphone.HeadphoneActivity;
import com.captech.teegarden.awarenessdemo.snapshot.SnapShotActivity;
import com.captech.teegarden.awarenessdemo.time.TimeActivity;
import com.captech.teegarden.awarenessdemo.weather.LocationActivity;

/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Created by teegarcs on 10/9/16.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.alarmView.setOnClickListener(this);
        binding.locationView.setOnClickListener(this);
        binding.headsetView.setOnClickListener(this);
        binding.instantView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.alarmView:
                intent = new Intent(this, TimeActivity.class);
                break;
            case R.id.locationView:
                intent = new Intent(this, LocationActivity.class);
                break;
            case R.id.headsetView:
                intent = new Intent(this, HeadphoneActivity.class);
                break;
            case R.id.instantView:
                intent = new Intent(this, SnapShotActivity.class);
                break;
        }
        startActivity(intent);
    }
}
