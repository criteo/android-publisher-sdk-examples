package com.criteo.publisher.samples.standalone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Map<String, Class<? extends Activity>> samples = new LinkedHashMap<>();
    samples.put("Banner Sample", BannerActivity.class);

    List<String> list = new ArrayList<>(samples.keySet());
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
        android.R.layout.simple_list_item_1, list);

    ListView listView = findViewById(R.id.listView);
    listView.setAdapter(adapter);

    listView.setOnItemClickListener((parent, view, position, id) -> {
      Intent intent = new Intent(this, samples.get(adapter.getItem(position)));
      startActivity(intent);
    });
  }

}
