package com.criteo.publisher;

import static com.criteo.publisher.ThreadingUtil.runOnMainThreadAndWait;
import static com.criteo.publisher.Util.CompletableFuture.completedFuture;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.support.test.InstrumentationRegistry;
import android.test.UiThreadTest;
import com.criteo.publisher.Util.MockedDependenciesRule;
import com.criteo.publisher.model.AdUnit;
import com.criteo.publisher.model.InterstitialAdUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CriteoInterstitialTest {
    @Rule
    public MockedDependenciesRule mockedDependenciesRule  = new MockedDependenciesRule();

    @Mock
    private CriteoInterstitialAdListener criteoInterstitialAdListener;

    private CriteoInterstitial criteoInterstitial;
    private InterstitialAdUnit interstitialAdUnit = new InterstitialAdUnit("/140800857/None");

    @Before
    @UiThreadTest
    public void setup() throws CriteoInitException {
        MockitoAnnotations.initMocks(this);

        List<AdUnit> cacheAdUnits = new ArrayList<>();
        cacheAdUnits.add(interstitialAdUnit);
        Application app =
                (Application) InstrumentationRegistry
                        .getTargetContext()
                        .getApplicationContext();
        Criteo.init(app, "B-056946", cacheAdUnits);
        criteoInterstitial = new CriteoInterstitial(InstrumentationRegistry.getContext(), interstitialAdUnit);
        criteoInterstitial.setCriteoInterstitialAdListener(criteoInterstitialAdListener);
    }

    @Test
    public void testInHouseLoadAdWithSameAdUnit() throws InterruptedException {
        UUID uuid1 = UUID.nameUUIDFromBytes("TEST_STRING1".getBytes());
        BidToken token1 = new BidToken(uuid1, interstitialAdUnit);
        criteoInterstitial.loadAd(token1);

        //wait for the loadAd process to be completed
        Thread.sleep(500);

        //Expected result , found no slot and called criteoInterstitialAdListener.onAdFetchFailed
        verify(criteoInterstitialAdListener, times(0)).onAdReceived();
        verify(criteoInterstitialAdListener, times(1))
                .onAdFailedToReceive(CriteoErrorCode.ERROR_CODE_NO_FILL);
    }

    @Test
    public void testInHouseLoadAdWithDifferentAdUnit() throws InterruptedException {
        UUID uuid1 = UUID.nameUUIDFromBytes("TEST_STRING1".getBytes());
        InterstitialAdUnit interstitialAdUnit2 = new InterstitialAdUnit("/140800857/None2");
        BidToken token1 = new BidToken(uuid1, interstitialAdUnit2);
        criteoInterstitial.loadAd(token1);

        //wait for the loadAd process to be completed
        Thread.sleep(500);

        //Expected result , found no slot and called criteoInterstitialAdListener.onAdFetchFailed
        verify(criteoInterstitialAdListener, never()).onAdReceived();
        verify(criteoInterstitialAdListener, never())
                .onAdFailedToReceive(CriteoErrorCode.ERROR_CODE_NO_FILL);
    }

    @Test
    public void testInHouseLoadAdWithEqualAdUnitButDifferentInstance() throws InterruptedException {
        UUID uuid = UUID.nameUUIDFromBytes("TEST_STRING1".getBytes());
        InterstitialAdUnit equalAdUnit = new InterstitialAdUnit("/140800857/None");
        BidToken bidToken = new BidToken(uuid, equalAdUnit);

        criteoInterstitial.loadAd(bidToken);

        //wait for the loadAd process to be completed
        Thread.sleep(500);

        //Expected result, found no slot and called criteoInterstitialAdListener.onAdFetchFailed
        verify(criteoInterstitialAdListener, never()).onAdReceived();
        verify(criteoInterstitialAdListener).onAdFailedToReceive(CriteoErrorCode.ERROR_CODE_NO_FILL);
    }

    @Test
    public void testNotifyListenerAsyncWithNullSlot() throws InterruptedException {
        criteoInterstitial.loadAd();

        //wait for the loadAd process to be completed
        Thread.sleep(1000);

        //Expected result , found no slot and called criteoInterstitialAdListener.onAdFetchFailed
        verify(criteoInterstitialAdListener, times(0)).onAdReceived();
        verify(criteoInterstitialAdListener, times(1))
                .onAdFailedToReceive(CriteoErrorCode.ERROR_CODE_NO_FILL);
    }

    @Test
    public void loadAdForStandaloneTwice_GivenOnlyNoBid_ShouldNotifyListenerTwiceForFailure() throws Exception {
        CriteoInterstitialAdListener listener = mock(CriteoInterstitialAdListener.class);

        Criteo criteo = mock(Criteo.class, Answers.RETURNS_DEEP_STUBS);
        when(criteo.getBidForAdUnit(interstitialAdUnit)).thenReturn(null);
        when(criteo.getDeviceInfo().getUserAgent()).thenReturn(completedFuture(""));

        AtomicReference<CriteoInterstitial> interstitial = new AtomicReference<>();

        runOnMainThreadAndWait(() -> {
            CriteoInterstitial interstitialObj = new CriteoInterstitial(
                InstrumentationRegistry.getContext(), interstitialAdUnit, criteo);

            interstitialObj.setCriteoInterstitialAdListener(listener);

            interstitial.set(interstitialObj);
        });

        runOnMainThreadAndWait(interstitial.get()::loadAd);
        ThreadingUtil.waitForAllThreads(mockedDependenciesRule.getTrackingCommandsExecutor());

        runOnMainThreadAndWait(interstitial.get()::loadAd);
        ThreadingUtil.waitForAllThreads(mockedDependenciesRule.getTrackingCommandsExecutor());

        verify(listener, times(2)).onAdFailedToReceive(CriteoErrorCode.ERROR_CODE_NO_FILL);
    }

}