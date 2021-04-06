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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.criteo.publisher.Bid;
import com.criteo.publisher.BidResponseListener;
import com.criteo.publisher.Criteo;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback;

public class InterstitialActivity extends AppCompatActivity {

  private AdManagerInterstitialAd adManagerInterstitialAd;
  private Button displayInterstitialButton;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_interstitial);

    displayInterstitialButton = findViewById(R.id.displayInterstitialButton);
    displayInterstitialButton.setOnClickListener(view -> displayInterstitial() );

    loadInterstitial();
  }

  private AdManagerInterstitialAdLoadCallback adManagerInterstitialAdLoadCallback() {
    return new AdManagerInterstitialAdLoadCallback() {
      @Override
      public void onAdLoaded(@NonNull AdManagerInterstitialAd adManagerInterstitialAd) {
        InterstitialActivity.this.adManagerInterstitialAd = adManagerInterstitialAd;
        displayInterstitialButton.setEnabled(true);
        displayInterstitialButton.setText("Display Interstitial");
      }

      @Override
      public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
        displayInterstitialButton.setEnabled(false);
        displayInterstitialButton.setText("Ad Failed to Load");
      }
    };
  }

  private void loadInterstitial() {

    Criteo.getInstance().loadBid(CRITEO_INTERSTITIAL_AD_UNIT, new BidResponseListener() {
      @Override
      public void onResponse(@Nullable Bid bid) {
        AdManagerAdRequest.Builder builder = new AdManagerAdRequest.Builder();

        if (bid != null) {
          Criteo.getInstance().enrichAdObjectWithBid(builder, bid);
        }

        AdManagerAdRequest adManagerAdRequest = builder.build();

        AdManagerInterstitialAd.load(
            InterstitialActivity.this,
            GAM_INTERSTITIAL_AD_UNIT_ID,
            adManagerAdRequest,
            adManagerInterstitialAdLoadCallback());
      }

    });
  }

  private void displayInterstitial() {
    if (adManagerInterstitialAd != null) {
      adManagerInterstitialAd.show(this);
      displayInterstitialButton.setEnabled(false);
    }
  }
}
