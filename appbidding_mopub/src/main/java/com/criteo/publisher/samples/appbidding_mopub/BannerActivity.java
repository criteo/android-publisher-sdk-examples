package com.criteo.publisher.samples.appbidding_mopub;

import static com.criteo.publisher.samples.appbidding_mopub.CriteoSampleApplication.MOPUB_BANNER_AD_UNIT_ID;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.mopub.mobileads.MoPubView;
import com.mopub.mobileads.MoPubView.MoPubAdSize;

public class BannerActivity extends AppCompatActivity {

  private MoPubView moPubView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_banner);

    moPubView = (MoPubView)findViewById(R.id.mopubView);
    moPubView.setAdUnitId(MOPUB_BANNER_AD_UNIT_ID);
    moPubView.setAdSize(MoPubAdSize.HEIGHT_50);

    displayBanner();
  }

  @Override
  protected void onDestroy() {
    moPubView.destroy();
    super.onDestroy();
  }

  private void displayBanner() {
    moPubView.loadAd();
  }

}
