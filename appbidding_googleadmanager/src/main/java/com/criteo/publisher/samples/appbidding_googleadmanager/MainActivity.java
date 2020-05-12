package com.criteo.publisher.samples.appbidding_googleadmanager;

import static com.criteo.publisher.samples.appbidding_googleadmanager.CriteoSampleApplication.CRITEO_BANNER_AD_UNIT;
import static com.criteo.publisher.samples.appbidding_googleadmanager.CriteoSampleApplication.CRITEO_INTERSTITIAL_AD_UNIT;
import static com.criteo.publisher.samples.appbidding_googleadmanager.CriteoSampleApplication.GAM_BANNER_AD_UNIT_ID;
import static com.criteo.publisher.samples.appbidding_googleadmanager.CriteoSampleApplication.GAM_INTERSTITIAL_AD_UNIT_ID;

import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.criteo.publisher.Criteo;
import com.google.android.gms.ads.AdSize;
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

    publisherAdView = new PublisherAdView(this);
    publisherAdView.setAdSizes(AdSize.BANNER);
    publisherAdView.setAdUnitId(GAM_BANNER_AD_UNIT_ID);
    FrameLayout publisherAdViewContainer = (FrameLayout)findViewById(R.id.publisherAdViewContainer);
    publisherAdViewContainer.addView(publisherAdView);

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
    PublisherAdRequest.Builder builder = new PublisherAdRequest.Builder();

    Criteo.getInstance().setBidsForAdUnit(builder, CRITEO_BANNER_AD_UNIT);

    PublisherAdRequest adRequest = builder.build();
    publisherAdView.loadAd(adRequest);
  }

  private void loadInterstitial() {
    publisherInterstitialAd = new PublisherInterstitialAd(this);
    publisherInterstitialAd.setAdUnitId(GAM_INTERSTITIAL_AD_UNIT_ID);
    PublisherAdRequest.Builder builder = new PublisherAdRequest.Builder();

    Criteo.getInstance().setBidsForAdUnit(builder, CRITEO_INTERSTITIAL_AD_UNIT);

    PublisherAdRequest adRequest = builder.build();
    publisherInterstitialAd.loadAd(adRequest);
  }

  private void displayInterstitial() {
    if (publisherInterstitialAd.isLoaded()) {
      publisherInterstitialAd.show();
    }
  }
}
