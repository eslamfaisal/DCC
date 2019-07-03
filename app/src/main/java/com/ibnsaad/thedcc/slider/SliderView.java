package com.ibnsaad.thedcc.slider;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ibnsaad.thedcc.IndicatorView.PageIndicatorView;
import com.ibnsaad.thedcc.IndicatorView.animation.type.AnimationType;
import com.ibnsaad.thedcc.IndicatorView.draw.controller.DrawController;
import com.ibnsaad.thedcc.IndicatorView.draw.data.Orientation;
import com.ibnsaad.thedcc.R;
import com.ibnsaad.thedcc.Transformations.AntiClockSpinTransformation;
import com.ibnsaad.thedcc.Transformations.Clock_SpinTransformation;
import com.ibnsaad.thedcc.Transformations.CubeInDepthTransformation;
import com.ibnsaad.thedcc.Transformations.CubeInRotationTransformation;
import com.ibnsaad.thedcc.Transformations.CubeInScalingTransformation;
import com.ibnsaad.thedcc.Transformations.CubeOutDepthTransformation;
import com.ibnsaad.thedcc.Transformations.CubeOutRotationTransformation;
import com.ibnsaad.thedcc.Transformations.CubeOutScalingTransformation;
import com.ibnsaad.thedcc.Transformations.DepthTransformation;
import com.ibnsaad.thedcc.Transformations.FadeTransformation;
import com.ibnsaad.thedcc.Transformations.FanTransformation;
import com.ibnsaad.thedcc.Transformations.FidgetSpinTransformation;
import com.ibnsaad.thedcc.Transformations.GateTransformation;
import com.ibnsaad.thedcc.Transformations.HingeTransformation;
import com.ibnsaad.thedcc.Transformations.HorizontalFlipTransformation;
import com.ibnsaad.thedcc.Transformations.PopTransformation;
import com.ibnsaad.thedcc.Transformations.SimpleTransformation;
import com.ibnsaad.thedcc.Transformations.SpinnerTransformation;
import com.ibnsaad.thedcc.Transformations.TossTransformation;
import com.ibnsaad.thedcc.Transformations.VerticalFlipTransformation;
import com.ibnsaad.thedcc.Transformations.VerticalShutTransformation;
import com.ibnsaad.thedcc.Transformations.ZoomOutTransformation;

public class SliderView extends FrameLayout {

    private final Handler mHandler = new Handler();
    private boolean mIsAutoCycle = true;
    private int mScrollTimeInSec = 2;
    private CircularSliderHandle mCircularSliderHandle;
    private PageIndicatorView mPagerIndicator;
    private DataSetObserver mDataSetObserver;
    private PagerAdapter mPagerAdapter;
    private Runnable mSliderRunnable;
    private SliderPager mSliderPager;


    public SliderView(Context context) {
        super(context);
        setupSlideView(context);
    }

    public SliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupSlideView(context);
    }

    public SliderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupSlideView(context);
    }

    private void setupSlideView(Context context) {

        View wrapperView = LayoutInflater
                .from(context)
                .inflate(R.layout.slider_view, this, true);

        mSliderPager = wrapperView.findViewById(R.id.vp_slider_layout);
        mCircularSliderHandle = new CircularSliderHandle(mSliderPager);
        mSliderPager.addOnPageChangeListener(mCircularSliderHandle);

        mPagerIndicator = wrapperView.findViewById(R.id.pager_indicator);
        mPagerIndicator.setViewPager(mSliderPager);
    }

    public void setOnIndicatorClickListener(DrawController.ClickListener listener) {
        mPagerIndicator.setClickListener(listener);
    }

    public void setCurrentPageListener(CircularSliderHandle.CurrentPageListener listener) {
        mCircularSliderHandle.setCurrentPageListener(listener);
    }

    public void setSliderAdapter(final PagerAdapter pagerAdapter) {
        mPagerAdapter = pagerAdapter;
        registerDataObserver();
        mSliderPager.setAdapter(pagerAdapter);
        mSliderPager.setOffscreenPageLimit(pagerAdapter.getCount() - 1);
        //setup with indicator
        mPagerIndicator.setCount(pagerAdapter.getCount());
        mPagerIndicator.setDynamicCount(true);
    }

    private void registerDataObserver() {
        if (mDataSetObserver != null) {
            mPagerAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mSliderPager.setOffscreenPageLimit(mPagerAdapter.getCount() - 1);
            }
        };

        mPagerAdapter.registerDataSetObserver(mDataSetObserver);
    }

    public PagerAdapter getSliderAdapter() {
        return mPagerAdapter;
    }

    public boolean isAutoCycle() {
        return mIsAutoCycle;
    }

    public void setAutoCycle(boolean autoCycle) {
        this.mIsAutoCycle = autoCycle;
        if (!mIsAutoCycle && mSliderRunnable != null) {
            mHandler.removeCallbacks(mSliderRunnable);
            mSliderRunnable = null;
        }
    }

    public int getScrollTimeInSec() {
        return mScrollTimeInSec;
    }

    public void setScrollTimeInSec(int time) {
        mScrollTimeInSec = time;
    }

    public void setSliderTransformAnimation(SliderAnimations animation) {

        switch (animation) {
            case ANTICLOCKSPINTRANSFORMATION:
                mSliderPager.setPageTransformer(false, new AntiClockSpinTransformation());
                break;
            case CLOCK_SPINTRANSFORMATION:
                mSliderPager.setPageTransformer(false, new Clock_SpinTransformation());
                break;
            case CUBEINDEPTHTRANSFORMATION:
                mSliderPager.setPageTransformer(false, new CubeInDepthTransformation());
                break;
            case CUBEINROTATIONTRANSFORMATION:
                mSliderPager.setPageTransformer(false, new CubeInRotationTransformation());
                break;
            case CUBEINSCALINGTRANSFORMATION:
                mSliderPager.setPageTransformer(false, new CubeInScalingTransformation());
                break;
            case CUBEOUTDEPTHTRANSFORMATION:
                mSliderPager.setPageTransformer(false, new CubeOutDepthTransformation());
                break;
            case CUBEOUTROTATIONTRANSFORMATION:
                mSliderPager.setPageTransformer(false, new CubeOutRotationTransformation());
                break;
            case CUBEOUTSCALINGTRANSFORMATION:
                mSliderPager.setPageTransformer(false, new CubeOutScalingTransformation());
                break;
            case DEPTHTRANSFORMATION:
                mSliderPager.setPageTransformer(false, new DepthTransformation());
                break;
            case FADETRANSFORMATION:
                mSliderPager.setPageTransformer(false, new FadeTransformation());
                break;
            case FANTRANSFORMATION:
                mSliderPager.setPageTransformer(false, new FanTransformation());
                break;
            case FIDGETSPINTRANSFORMATION:
                mSliderPager.setPageTransformer(false, new FidgetSpinTransformation());
                break;
            case GATETRANSFORMATION:
                mSliderPager.setPageTransformer(false, new GateTransformation());
                break;
            case HINGETRANSFORMATION:
                mSliderPager.setPageTransformer(false, new HingeTransformation());
                break;
            case HORIZONTALFLIPTRANSFORMATION:
                mSliderPager.setPageTransformer(false, new HorizontalFlipTransformation());
                break;
            case POPTRANSFORMATION:
                mSliderPager.setPageTransformer(false, new PopTransformation());
                break;
            case SIMPLETRANSFORMATION:
                mSliderPager.setPageTransformer(false, new SimpleTransformation());
                break;
            case SPINNERTRANSFORMATION:
                mSliderPager.setPageTransformer(false, new SpinnerTransformation());
                break;
            case TOSSTRANSFORMATION:
                mSliderPager.setPageTransformer(false, new TossTransformation());
                break;
            case VERTICALFLIPTRANSFORMATION:
                mSliderPager.setPageTransformer(false, new VerticalFlipTransformation());
                break;
            case VERTICALSHUTTRANSFORMATION:
                mSliderPager.setPageTransformer(false, new VerticalShutTransformation());
                break;
            case ZOOMOUTTRANSFORMATION:
                mSliderPager.setPageTransformer(false, new ZoomOutTransformation());
                break;
            default:
                mSliderPager.setPageTransformer(false, new SimpleTransformation());

        }

    }

    public void setCustomSliderTransformAnimation(ViewPager.PageTransformer animation) {
        mSliderPager.setPageTransformer(false, animation);
    }

    public void setSliderAnimationDuration(int duration) {
        mSliderPager.setScrollDuration(duration);
    }

    public void setIndicatorAnimationDuration(long duration) {
        mPagerIndicator.setAnimationDuration(duration);
    }


    public void setIndicatorPadding(int padding) {
        mPagerIndicator.setPadding(padding);
    }

    public void setIndicatorOrientation(Orientation orientation) {
        mPagerIndicator.setOrientation(orientation);
    }

    public void setCurrentPagePosition(int position) {

        if (getSliderAdapter() != null) {
            mSliderPager.setCurrentItem(position, true);
        } else {
            throw new NullPointerException("Adapter not set");
        }
    }

    public int getCurrentPagePosition() {

        if (getSliderAdapter() != null) {
            return mSliderPager.getCurrentItem();
        } else {
            throw new NullPointerException("Adapter not set");
        }
    }

    public void setIndicatorAnimation(IndicatorAnimations animations) {

        switch (animations) {
            case DROP:
                mPagerIndicator.setAnimationType(AnimationType.DROP);
                break;
            case FILL:
                mPagerIndicator.setAnimationType(AnimationType.FILL);
                break;
            case NONE:
                mPagerIndicator.setAnimationType(AnimationType.NONE);
                break;
            case SWAP:
                mPagerIndicator.setAnimationType(AnimationType.SWAP);
                break;
            case WORM:
                mPagerIndicator.setAnimationType(AnimationType.WORM);
                break;
            case COLOR:
                mPagerIndicator.setAnimationType(AnimationType.COLOR);
                break;
            case SCALE:
                mPagerIndicator.setAnimationType(AnimationType.SCALE);
                break;
            case SLIDE:
                mPagerIndicator.setAnimationType(AnimationType.SLIDE);
                break;
            case SCALE_DOWN:
                mPagerIndicator.setAnimationType(AnimationType.SCALE_DOWN);
                break;
            case THIN_WORM:
                mPagerIndicator.setAnimationType(AnimationType.THIN_WORM);
                break;
        }
    }

    public void setPagerIndicatorVisibility(boolean visibility) {
        if (visibility) {
            mPagerIndicator.setVisibility(VISIBLE);
        } else {
            mPagerIndicator.setVisibility(GONE);
        }
    }

    public void startAutoCycle() {

        if (mSliderRunnable != null) {
            mHandler.removeCallbacks(mSliderRunnable);
            mSliderRunnable = null;
        }

        mSliderRunnable = new Runnable() {
            @Override
            public void run() {

                try {
                    // check is on auto scroll mode
                    if (!mIsAutoCycle) {
                        return;
                    }

                    int currentPosition = mSliderPager.getCurrentItem();

                    if (currentPosition == getSliderAdapter().getCount() - 1) {
                        // if is last item return to the first position
                        mSliderPager.setCurrentItem(0, true);
                    } else {
                        // continue smooth transition between pager
                        mSliderPager.setCurrentItem(++currentPosition, true);
                    }

                } finally {
                    mHandler.postDelayed(this, mScrollTimeInSec * 1000);
                }

            }
        };

        //Run the loop for the first time
        mHandler.postDelayed(mSliderRunnable, mScrollTimeInSec * 1000);
    }

    public int getSliderIndicatorRadius() {
        return mPagerIndicator.getRadius();
    }

    public void setSliderIndicatorRadius(int pagerIndicatorRadius) {
        this.mPagerIndicator.setRadius(pagerIndicatorRadius);
    }

    public void setSliderIndicatorSelectedColor(int color) {
        this.mPagerIndicator.setSelectedColor(color);
    }

    public int getSliderIndicatorSelectedColor() {
        return this.mPagerIndicator.getSelectedColor();
    }

    public void setSliderIndicatorUnselectedColor(int color) {
        this.mPagerIndicator.setUnselectedColor(color);
    }

    public int getSliderIndicatorUnselectedColor() {
        return this.mPagerIndicator.getUnselectedColor();
    }

}
