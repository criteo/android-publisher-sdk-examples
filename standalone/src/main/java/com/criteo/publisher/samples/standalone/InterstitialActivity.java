package com.criteo.publisher.samples.standalone;

import static com.criteo.publisher.samples.standalone.CriteoSampleApplication.CRITEO_INTERSTITIAL_AD_UNIT;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.criteo.publisher.CriteoErrorCode;
import com.criteo.publisher.CriteoInterstitial;
import com.criteo.publisher.CriteoInterstitialAdListener;

public class InterstitialActivity extends AppCompatActivity {

  private CriteoInterstitial interstitial;
  private Button displayInterstitialButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_interstitial);

    displayInterstitialButton = findViewById(R.id.displayInterstitialButton);
    displayInterstitialButton.setOnClickListener(view -> displayInterstitial());

    loadInterstitial();

  }


  private void loadInterstitial() {
    // Initialize interstitial with interstitial ad unit
    interstitial = new CriteoInterstitial(this.getBaseContext(), CRITEO_INTERSTITIAL_AD_UNIT);

    interstitial.setCriteoInterstitialAdListener(new CriteoInterstitialAdListener() {
      @Override
      public void onAdReceived() {
        // called when Criteo Ad is received, but may not necessarily ready to be displayed.
        // always use isAdLoaded() to check if Criteo Interstitial ad is ready to be displayed.
        displayInterstitialButton.setEnabled(true);
        displayInterstitialButton.setText("Display Interstitial");
      }

      @Override
      public void onAdFailedToReceive(CriteoErrorCode criteoErrorCode) {
        // called when Criteo returns no ad. The error description gives further details on the failure.
        displayInterstitialButton.setEnabled(false);
        displayInterstitialButton.setText("Ad Failed to Load");
      }

      @Override
      public void onAdClicked() {
        // called when user clicks on an opened Criteo Interstitial ad
      }

      @Override
      public void onAdOpened() {
        // called when Criteo Interstitial ad is opened and covering the screen
      }

      @Override
      public void onAdClosed() {
        // called when Criteo Interstitial ad is closed and user is back on the application
      }

      @Override
      public void onAdLeftApplication() {
        // called when clicking on Criteo ad result in user leaving your application
      }
    });

    // Load the interstitial with ad.
    interstitial.loadAd();
  }

  private void displayInterstitial() {
    if (interstitial.isAdLoaded()) {
      interstitial.show();
      displayInterstitialButton.setEnabled(false);
    }
  }

}
