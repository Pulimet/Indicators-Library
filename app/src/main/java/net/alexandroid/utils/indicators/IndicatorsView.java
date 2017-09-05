package net.alexandroid.utils.indicators;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created on 9/5/2017.
 */

public class IndicatorsView extends View implements ViewPager.OnPageChangeListener {

    private Drawable mSelectedDrawbale;
    private Drawable mUnSelectedDrawbale;

    private Bitmap mUnSelectedBitmap;
    private Bitmap mSelectedBitmap;

    private Rect mRect;

    // Default size
    private int mIndicatorSize = 13;

    // Default padding between indicators
    private int mPaddingBetweenIndicators = 7;

    private int mNumOfIndicators = 3;
    private int mSelectedIndicator = 0;

    public IndicatorsView(Context context) {
        this(context, null);
    }

    public IndicatorsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        setDefaults(context);
        getDataFromAttributes(context, attrs);
        convertDrawablesToBitmaps();
    }

    private void setDefaults(Context context) {
        float density = getResources().getDisplayMetrics().density;

        mIndicatorSize = (int) (mIndicatorSize * density);
        mPaddingBetweenIndicators = (int) (mPaddingBetweenIndicators * density);

        mSelectedDrawbale = ContextCompat.getDrawable(context, R.drawable.circle_selected);
        mUnSelectedDrawbale = ContextCompat.getDrawable(context, R.drawable.circle_unselected);
    }

    private void getDataFromAttributes(Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IndicatorsView, 0, 0);

            // Get indicators if exist
            Drawable selectedDrawable = a.getDrawable(R.styleable.IndicatorsView_selectedDrawable);
            if (selectedDrawable != null) {
                mSelectedDrawbale = selectedDrawable;
            }
            Drawable unSelectedDrawable = a.getDrawable(R.styleable.IndicatorsView_unSelectedDrawable);
            if (unSelectedDrawable != null) {
                mUnSelectedDrawbale = unSelectedDrawable;
            }

            // Get indicator size
            mIndicatorSize = (int) a.getDimension(R.styleable.IndicatorsView_indicatorSize, mIndicatorSize);

            // Get padding between indicators
            mPaddingBetweenIndicators = (int) a.getDimension(R.styleable.IndicatorsView_paddingBetweenIndicators, mPaddingBetweenIndicators);

            // Get number of indicators
            mNumOfIndicators = a.getInteger(R.styleable.IndicatorsView_numberOfIndicators, mNumOfIndicators);

            // Get selected indicator
            mSelectedIndicator = a.getInteger(R.styleable.IndicatorsView_selectedIndicator, mSelectedIndicator);

            a.recycle();
        }
    }

    private void convertDrawablesToBitmaps() {

        mRect = new Rect(0, 0, mIndicatorSize, mIndicatorSize);
        mUnSelectedBitmap = drawableToBitmap(mUnSelectedDrawbale, mIndicatorSize);
        mSelectedBitmap = drawableToBitmap(mSelectedDrawbale, mIndicatorSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean isIndicatorSizeChanged = false;

        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);


        if (mIndicatorSize * mNumOfIndicators + (mNumOfIndicators - 1) * mPaddingBetweenIndicators > width) {
            width -= mPaddingBetweenIndicators * (mNumOfIndicators - 1);
            mIndicatorSize = width / mNumOfIndicators;
            isIndicatorSizeChanged = true;
        }

        //MyLog.d("mIndicatorSize: " + mIndicatorSize);

        if (mIndicatorSize > height) {
            mIndicatorSize = height;
            isIndicatorSizeChanged = true;
        }

        //MyLog.d("mIndicatorSize: " + mIndicatorSize);
        if (isIndicatorSizeChanged) {
            convertDrawablesToBitmaps();
        }

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int hCenter = getWidth() / 2;
        final int vCenter = getHeight() / 2;
        final int totalWidthWeNeed = mIndicatorSize * mNumOfIndicators + mPaddingBetweenIndicators * (mNumOfIndicators - 1);
        final int leftBound = hCenter - totalWidthWeNeed / 2;
        final int topBound = vCenter - mIndicatorSize / 2;

        mRect.offsetTo(leftBound, topBound);
        for (int i = 0; i < mNumOfIndicators; i++) {
            canvas.drawBitmap(i == mSelectedIndicator ? mSelectedBitmap : mUnSelectedBitmap,
                    null, mRect, null);
            mRect.offset(mIndicatorSize + mPaddingBetweenIndicators, 0);
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable, int size) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        if (size < 1) {
            size = 1;
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    // Control

    public void setSelectedIndicator(int selectedIndicator) {
        mSelectedIndicator = selectedIndicator;
        invalidate();
    }

    public void setViewPager(ViewPager viewPager) {
        mNumOfIndicators = viewPager.getAdapter().getCount();
        mSelectedIndicator = viewPager.getCurrentItem();
        viewPager.addOnPageChangeListener(this);
        invalidate();
    }

    // ViewPager


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setSelectedIndicator(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
