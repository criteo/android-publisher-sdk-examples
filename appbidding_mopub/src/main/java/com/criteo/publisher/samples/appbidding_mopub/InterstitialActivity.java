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

import static com.criteo.publisher.samples.appbidding_mopub.CriteoSampleApplication.CRITEO_INTERSTITIAL_AD_UNIT;
import static com.criteo.publisher.samples.appbidding_mopub.CriteoSampleApplication.MOPUB_INTERSTITIAL_AD_UNIT_ID;

import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.criteo.publisher.Criteo;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubInterstitial.InterstitialAdListener;

public class InterstitialActivity extends AppCompatActivity {

  private MoPubInterstitial moPubInterstitial;
  private Button displayInterstitialButton;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_interstitial);

    displayInterstitialButton = findViewById(R.id.displayInterstitialButton);
    displayInterstitialButton.setOnClickListener(view -> displayInterstitial());

    loadInterstitial();
  }

  @Override
  protected void onDestroy() {
    moPubInterstitial.destroy();
    super.onDestroy();
  }

  private void loadInterstitial() {
    moPubInterstitial = new MoPubInterstitial(this, MOPUB_INTERSTITIAL_AD_UNIT_ID);
    moPubInterstitial.setInterstitialAdListener(new InterstitialAdListener() {
      @Override
      public void onInterstitialLoaded(MoPubInterstitial interstitial) {
        displayInterstitialButton.setEnabled(true);
        displayInterstitialButton.setText("Display Interstitial");
      }

      @Override
      public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
        displayInterstitialButton.setEnabled(false);
        displayInterstitialButton.setText("Ad Failed to Load");
      }

      @Override
      public void onInterstitialShown(MoPubInterstitial interstitial) {

      }

      @Override
      public void onInterstitialClicked(MoPubInterstitial interstitial) {

      }

      @Override
      public void onInterstitialDismissed(MoPubInterstitial interstitial) {

      }
    });
    Criteo.getInstance().setBidsForAdUnit(moPubInterstitial, CRITEO_INTERSTITIAL_AD_UNIT);
    moPubInterstitial.load();
  }

  private void displayInterstitial() {
    if (moPubInterstitial.isReady()) {
      moPubInterstitial.show();
      displayInterstitialButton.setEnabled(false);
    }
  }
}
