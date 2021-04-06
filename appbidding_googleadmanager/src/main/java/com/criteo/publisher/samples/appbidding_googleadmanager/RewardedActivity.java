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

import static com.criteo.publisher.samples.appbidding_googleadmanager.CriteoSampleApplication.GAM_REWARDED_AD_UNIT_ID;

import android.util.Log;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class RewardedActivity extends AppCompatActivity {

  private RewardedAd rewardedAd;
  private Button displayRewardedButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rewarded);

    displayRewardedButton = findViewById(R.id.displayRewardedButton);
    displayRewardedButton.setOnClickListener(view -> displayRewarded() );

    loadRewarded();
  }

  private RewardedAdLoadCallback rewardedAdLoadCallback() {
    return new RewardedAdLoadCallback() {
      @Override
      public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
        RewardedActivity.this.rewardedAd = rewardedAd;
        displayRewardedButton.setEnabled(true);
        displayRewardedButton.setText("Display Rewarded Ads");
      }

      @Override
      public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
        Log.d("CriteoSDK", loadAdError.getMessage());

        RewardedActivity.this.rewardedAd = null;
        displayRewardedButton.setEnabled(false);
        displayRewardedButton.setText("Rewarded Ads Failed to Load");
      }
    };
  }

  private void loadRewarded() {
    RewardedAd.load(
        this,
        GAM_REWARDED_AD_UNIT_ID,
        new AdManagerAdRequest.Builder().build(), // TODO: Enrich AdRequest with Criteo keyvalues
        rewardedAdLoadCallback()
    );
  }

  private void displayRewarded() {
    if (rewardedAd != null) {
      rewardedAd.show(this, rewardItem -> {
        // User earned a reward
      });
      displayRewardedButton.setEnabled(false);
    }
  }
}