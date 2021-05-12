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

package com.criteo.publisher.samples.appbidding_googleadmanager;

import static com.criteo.publisher.samples.appbidding_googleadmanager.CriteoSampleApplication.CRITEO_BANNER_AD_UNIT;
import static com.criteo.publisher.samples.appbidding_googleadmanager.CriteoSampleApplication.GAM_BANNER_AD_UNIT_ID;

import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.criteo.publisher.Bid;
import com.criteo.publisher.BidResponseListener;
import com.criteo.publisher.Criteo;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;

public class BannerActivity extends AppCompatActivity {

  private AdManagerAdView adManagerAdView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_banner);

    adManagerAdView = new AdManagerAdView(this);
    adManagerAdView.setAdSizes(AdSize.BANNER);
    adManagerAdView.setAdUnitId(GAM_BANNER_AD_UNIT_ID);
    FrameLayout publisherAdViewContainer = findViewById(R.id.publisherAdViewContainer);
    publisherAdViewContainer.addView(adManagerAdView);

    displayBanner();
  }

  @Override
  protected void onDestroy() {
    adManagerAdView.destroy();
    super.onDestroy();
  }

  private void displayBanner() {
    Criteo.getInstance().loadBid(CRITEO_BANNER_AD_UNIT, new BidResponseListener() {
      @Override
      public void onResponse(@Nullable Bid bid) {
        AdManagerAdRequest.Builder builder = new AdManagerAdRequest.Builder();

        if (bid != null) {
          Criteo.getInstance().enrichAdObjectWithBid(builder, bid);
        }

        AdManagerAdRequest request = builder.build();
        adManagerAdView.loadAd(request);
      }
    });
  }
}
