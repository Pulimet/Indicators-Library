package net.alexandroid.utils.indicators;

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created on 9/5/2017.
 */

public class IndicatorsView extends View implements ViewPager.OnPageChangeListener {

    private Drawable mSelectedDrawable;
    private Drawable mUnSelectedDrawable;

    private Bitmap mUnSelectedBitmap;
    private Bitmap mSelectedBitmap;

    private Rect mRect;

    // Default size
    private int mIndicatorSize = 13;

    // Default padding between indicators
    private int mPaddingBetweenIndicators = 7;

    private int mNumOfIndicators = 3;
    private int mSelectedIndicator = 0;

    private int mLeftBound;
    private int mTopBound;
    private int mTotalWidthWeNeed;
    private boolean mIndicatorsClickChangePage;
    private ViewPager mViewPager;

    private OnIndicatorClickListener mOnIndicatorClickListener;

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

        mSelectedDrawable = ContextCompat.getDrawable(context, R.drawable.circle_selected);
        mUnSelectedDrawable = ContextCompat.getDrawable(context, R.drawable.circle_unselected);
    }

    private void getDataFromAttributes(Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IndicatorsView, 0, 0);

            // Get indicators if exist
            Drawable selectedDrawable = a.getDrawable(R.styleable.IndicatorsView_selectedDrawable);
            if (selectedDrawable != null) {
                mSelectedDrawable = selectedDrawable;
            }
            Drawable unSelectedDrawable = a.getDrawable(R.styleable.IndicatorsView_unSelectedDrawable);
            if (unSelectedDrawable != null) {
                mUnSelectedDrawable = unSelectedDrawable;
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
        mUnSelectedBitmap = drawableToBitmap(mUnSelectedDrawable, mIndicatorSize);
        mSelectedBitmap = drawableToBitmap(mSelectedDrawable, mIndicatorSize);
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
        mTotalWidthWeNeed = mIndicatorSize * mNumOfIndicators + mPaddingBetweenIndicators * (mNumOfIndicators - 1);
        mLeftBound = hCenter - mTotalWidthWeNeed / 2;
        mTopBound = vCenter - mIndicatorSize / 2;

        mRect.offsetTo(mLeftBound, mTopBound);
        for (int i = 0; i < mNumOfIndicators; i++) {
            canvas.drawBitmap(i == mSelectedIndicator ? mSelectedBitmap : mUnSelectedBitmap,
                    null, mRect, null);
            mRect.offset(mIndicatorSize + mPaddingBetweenIndicators, 0);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mIndicatorsClickChangePage && mOnIndicatorClickListener != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    int indicatorNumberClicked = getNumberOfCLickedIndicator(x, y);
                    //Log.d("ZAQ", "x: " + x + "  y: " + y);
                    Log.d("ZAQ", "Clicked on: " + indicatorNumberClicked);
                    if (indicatorNumberClicked > -1) {
                        onIndicatorClick(indicatorNumberClicked);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            return true;
        }
        return false;
    }

    private int getNumberOfCLickedIndicator(int x, int y) {
        if (y < mTopBound - mIndicatorSize
                || y > (mTopBound + mIndicatorSize)
                || x < mLeftBound - mPaddingBetweenIndicators / 2
                || x > (mLeftBound + mTotalWidthWeNeed)) {
            return -1;
        }

        int x2 = x - mLeftBound;
        final int firstIndicatorWidth = mIndicatorSize + mPaddingBetweenIndicators / 2;

        if (x2 < firstIndicatorWidth) {
            return 0;
        }

        int count = 1;
        x2 -= firstIndicatorWidth;
        final int indicatorWidth = mIndicatorSize + mPaddingBetweenIndicators;

        while (x2 > indicatorWidth) {
            count++;
            x2 -= indicatorWidth;
        }

        return count;
    }

    private void onIndicatorClick(int indicatorNumberClicked) {
        if (mIndicatorsClickChangePage && mViewPager != null) {
            mViewPager.setCurrentItem(indicatorNumberClicked);
        }

        if (mOnIndicatorClickListener != null) {
            mOnIndicatorClickListener.onClick(indicatorNumberClicked);
        }
    }

    // Helper
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
        mViewPager = viewPager;

        mNumOfIndicators = mViewPager.getAdapter().getCount();
        mSelectedIndicator = mViewPager.getCurrentItem();
        mViewPager.addOnPageChangeListener(this);

        invalidate();
    }

    public void setIndicatorsClickChangePage(boolean newValue) {
        mIndicatorsClickChangePage = newValue;
    }

    public void setIndicatorsClickListener(OnIndicatorClickListener listener) {
        mOnIndicatorClickListener = listener;
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

    // Click listener interface
    public interface OnIndicatorClickListener {
        void onClick(int indicatorNumber);
    }

}
