/*
 *    Copyright 2020 Criteo
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.criteo.publisher.samples.appbidding_mopub;

import static com.criteo.publisher.samples.appbidding_mopub.CriteoSampleApplication.MOPUB_BANNER_AD_UNIT_ID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

  private final Map<String, Class<? extends Activity>> samples = new LinkedHashMap<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    samples.put("Interstitial Sample", InterstitialActivity.class);

    initMoPub();
    initListView();
  }

  private void initMoPub() {
    SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(MOPUB_BANNER_AD_UNIT_ID).build();
    MoPub.initializeSdk(this, sdkConfiguration, () -> {

    });
  }

  private void initListView() {
    List<String> list = new ArrayList<>(samples.keySet());
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
        android.R.layout.simple_list_item_1, list);

    ListView listView = findViewById(R.id.listView);
    listView.setAdapter(adapter);

    listView.setOnItemClickListener((parent, view, position, id) -> {
      if (MoPub.isSdkInitialized()) {
        Intent intent = new Intent(this, samples.get(adapter.getItem(position)));
        startActivity(intent);
      } else {
        Toast.makeText(this, "MoPub SDK initialization isn't yet completed, please wait", Toast.LENGTH_SHORT).show();
      }
    });
  }
}
