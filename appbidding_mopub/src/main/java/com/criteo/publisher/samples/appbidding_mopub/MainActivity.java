package com.criteo.publisher.samples.appbidding_mopub;

import static com.criteo.publisher.samples.appbidding_mopub.CriteoSampleApplication.MOPUB_BANNER_AD_UNIT_ID;
import static com.criteo.publisher.samples.appbidding_mopub.CriteoSampleApplication.MOPUB_INTERSTITIAL_AD_UNIT_ID;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;
import com.mopub.mobileads.MoPubView.MoPubAdSize;

public class MainActivity extends AppCompatActivity {

  private MoPubView moPubView;
  private MoPubInterstitial moPubInterstitial;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initMoPub();

    findViewById(R.id.displayBannerButton).setOnClickListener(v -> displayBanner());
    findViewById(R.id.displayInterstitialButton).setOnClickListener(v -> displayInterstitial());
  }

  @Override
  protected void onDestroy() {
    moPubInterstitial.destroy();
    moPubView.destroy();
    super.onDestroy();
  }

  private void initMoPub() {
    moPubView = (MoPubView)findViewById(R.id.mopubView);
    moPubView.setAdUnitId(MOPUB_BANNER_AD_UNIT_ID);
    moPubView.setAdSize(MoPubAdSize.HEIGHT_50);

    moPubInterstitial = new MoPubInterstitial(this, MOPUB_INTERSTITIAL_AD_UNIT_ID);

    SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(MOPUB_BANNER_AD_UNIT_ID).build();
    MoPub.initializeSdk(this, sdkConfiguration, () -> {
      moPubInterstitial.load();
    });
  }

  private void displayBanner() {
    moPubView.loadAd();
  }

  private void displayInterstitial() {
    if (moPubInterstitial.isReady()) {
      moPubInterstitial.show();
    }
  }
}
