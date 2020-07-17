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

package com.criteo.publisher.samples.standalone;

import static com.criteo.publisher.samples.standalone.CriteoSampleApplication.CRITEO_BANNER_AD_UNIT;

import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.criteo.publisher.CriteoBannerView;

public class BannerActivity extends AppCompatActivity {

  private CriteoBannerView bannerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_banner);

    bannerView = new CriteoBannerView(this.getBaseContext(), CRITEO_BANNER_AD_UNIT);

    FrameLayout criteoBannerViewContainer = findViewById(R.id.criteoBannerViewContainer);
    criteoBannerViewContainer.addView(bannerView);

    displayBanner();
  }


  @Override
  protected void onDestroy() {
    bannerView.destroy();
    super.onDestroy();
  }

  private void displayBanner() {
    // Load the bannerView with ad.
    bannerView.loadAd();
  }

}
