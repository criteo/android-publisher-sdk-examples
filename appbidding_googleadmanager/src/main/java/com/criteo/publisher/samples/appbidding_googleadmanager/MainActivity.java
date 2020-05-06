package com.criteo.publisher.samples.appbidding_googleadmanager;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

public class MainActivity extends AppCompatActivity {

  private PublisherAdView publisherAdView;
  private PublisherInterstitialAd publisherInterstitialAd;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    publisherAdView = findViewById(R.id.publisherAdView);

    loadInterstitial();

    findViewById(R.id.displayBannerButton).setOnClickListener(v -> displayBanner());
    findViewById(R.id.displayInterstitialButton).setOnClickListener(v -> displayInterstitial());
  }

  @Override
  protected void onDestroy() {
    publisherAdView.destroy();
    super.onDestroy();
  }

  private void displayBanner() {
    PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
    publisherAdView.loadAd(adRequest);
  }

  private void loadInterstitial() {
    publisherInterstitialAd = new PublisherInterstitialAd(this);
    publisherInterstitialAd.setAdUnitId("/6499/example/interstitial");
    PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
    publisherInterstitialAd.loadAd(adRequest);
  }

  private void displayInterstitial() {
    if (publisherInterstitialAd.isLoaded()) {
      publisherInterstitialAd.show();
    }
  }
}
