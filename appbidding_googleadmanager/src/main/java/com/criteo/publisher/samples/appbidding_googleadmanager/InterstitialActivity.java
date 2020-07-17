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

import static com.criteo.publisher.samples.appbidding_googleadmanager.CriteoSampleApplication.CRITEO_INTERSTITIAL_AD_UNIT;
import static com.criteo.publisher.samples.appbidding_googleadmanager.CriteoSampleApplication.GAM_INTERSTITIAL_AD_UNIT_ID;

import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.criteo.publisher.Criteo;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

public class InterstitialActivity extends AppCompatActivity {

  private PublisherInterstitialAd publisherInterstitialAd;
  private Button displayInterstitialButton;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_interstitial);

    displayInterstitialButton = findViewById(R.id.displayInterstitialButton);
    displayInterstitialButton.setOnClickListener(view -> displayInterstitial() );

    loadInterstitial();
  }

  private void loadInterstitial() {
    publisherInterstitialAd = new PublisherInterstitialAd(this);
    publisherInterstitialAd.setAdUnitId(GAM_INTERSTITIAL_AD_UNIT_ID);
    publisherInterstitialAd.setAdListener(new AdListener() {
      @Override
      public void onAdLoaded() {
        super.onAdLoaded();
        displayInterstitialButton.setEnabled(true);
        displayInterstitialButton.setText("Display Interstitial");
      }

      @Override
      public void onAdFailedToLoad(int i) {
        super.onAdFailedToLoad(i);
        displayInterstitialButton.setEnabled(false);
        displayInterstitialButton.setText("Ad Failed to Load");
      }
    });
    PublisherAdRequest.Builder builder = new PublisherAdRequest.Builder();

    Criteo.getInstance().setBidsForAdUnit(builder, CRITEO_INTERSTITIAL_AD_UNIT);

    PublisherAdRequest adRequest = builder.build();
    publisherInterstitialAd.loadAd(adRequest);
  }

  private void displayInterstitial() {
    if (publisherInterstitialAd.isLoaded()) {
      publisherInterstitialAd.show();
      displayInterstitialButton.setEnabled(false);
    }
  }
}
