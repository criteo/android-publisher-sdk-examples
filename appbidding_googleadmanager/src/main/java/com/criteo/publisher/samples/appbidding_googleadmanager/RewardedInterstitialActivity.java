/*
 *    Copyright 2021 Criteo
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

import static com.criteo.publisher.samples.appbidding_googleadmanager.CriteoSampleApplication.GAM_REWARDED_INTERSTITIAL_AD_UNIT_ID;

import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;

public class RewardedInterstitialActivity extends AppCompatActivity {

  private RewardedInterstitialAd rewardedInterstitialAd;
  private Button displayRewardedButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rewarded_interstitial);

    displayRewardedButton = findViewById(R.id.displayRewardedButton);
    displayRewardedButton.setOnClickListener(view -> displayRewarded() );

    loadRewarded();
  }

  private RewardedInterstitialAdLoadCallback rewardedInterstitialAdLoadCallback() {
    return new RewardedInterstitialAdLoadCallback() {
      @Override
      public void onAdLoaded(@NonNull RewardedInterstitialAd rewardedInterstitialAd) {
        RewardedInterstitialActivity.this.rewardedInterstitialAd = rewardedInterstitialAd;
        displayRewardedButton.setEnabled(true);
        displayRewardedButton.setText("Display Rewarded Interstitial");
      }

      @Override
      public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
        RewardedInterstitialActivity.this.rewardedInterstitialAd = null;
        displayRewardedButton.setEnabled(false);
        displayRewardedButton.setText("Rewarded Interstitial Failed to Load");
      }
    };
  }

  private void loadRewarded() {
    RewardedInterstitialAd.load(
        this,
        GAM_REWARDED_INTERSTITIAL_AD_UNIT_ID,
        new AdManagerAdRequest.Builder().build(), // TODO: Enrich AdRequest with Criteo keyvalues
        rewardedInterstitialAdLoadCallback()
    );
  }

  private void displayRewarded() {
    if (rewardedInterstitialAd != null) {
      rewardedInterstitialAd.show(this, rewardItem -> {
        // User earned a reward
      });
      displayRewardedButton.setEnabled(false);
    }
  }
}