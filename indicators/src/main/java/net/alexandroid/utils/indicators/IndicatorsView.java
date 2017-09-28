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
    private Rect mTempRect;

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
    private boolean mSmoothTransitionEnabled;
    private float mCurrentPositionOffset;
    private float mCurrentPosition;
    private float mDensity;

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

        mTempRect = new Rect();
    }

    private void setDefaults(Context context) {
        mDensity = getResources().getDisplayMetrics().density;

        mIndicatorSize = (int) (mIndicatorSize * mDensity);
        mPaddingBetweenIndicators = (int) (mPaddingBetweenIndicators * mDensity);

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
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        int desiredWidth = mIndicatorSize * mNumOfIndicators + mPaddingBetweenIndicators * (mNumOfIndicators - 1);
        int desiredHeight = mIndicatorSize;

        desiredWidth += getPaddingLeft() + getPaddingRight();
        desiredHeight += getPaddingTop() + getPaddingBottom();

        int width = desiredWidth;
        int height = desiredHeight;


        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;  // Must be this size (match_parent or exactly value)
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize); // (wrap_content)
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize; // Must be this size (match_parent or exactly value)
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);   // (wrap_content)
        }

        changeIndicatorSizeIfNecessary(width - getPaddingLeft() - getPaddingRight(), height - getPaddingTop() - getPaddingBottom());

        setMeasuredDimension(width, height);
    }

    private void changeIndicatorSizeIfNecessary(int width, int height) {
        boolean isIndicatorSizeChanged = false;

        // if width is not wide enough
        if (mIndicatorSize * mNumOfIndicators + (mNumOfIndicators - 1) * mPaddingBetweenIndicators > width) {
            width -= mPaddingBetweenIndicators * (mNumOfIndicators - 1);
            mIndicatorSize = width / mNumOfIndicators;
            isIndicatorSizeChanged = true;
        }

        // if height is not high enough
        if (mIndicatorSize > height) {
            mIndicatorSize = height;
            isIndicatorSizeChanged = true;
        }

        if (isIndicatorSizeChanged) {
            convertDrawablesToBitmaps();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int hCenter = (getWidth() - getPaddingLeft() - getPaddingRight()) / 2;
        final int vCenter = (getHeight() - getPaddingTop() - getPaddingBottom()) / 2;
        mTotalWidthWeNeed = mIndicatorSize * mNumOfIndicators + mPaddingBetweenIndicators * (mNumOfIndicators - 1);
        mLeftBound = hCenter - mTotalWidthWeNeed / 2 + getPaddingLeft();
        //mLeftBound = hCenter - mTotalWidthWeNeed / 2 + Math.round(getPaddingLeft() / mDensity);
        mTopBound = vCenter - mIndicatorSize / 2 + getPaddingTop();
        //mTopBound = vCenter - mIndicatorSize / 2 + Math.round(getPaddingTop() / mDensity);

        mRect.offsetTo(mLeftBound, mTopBound);
        for (int i = 0; i < mNumOfIndicators; i++) {
            if (i != mSelectedIndicator || mSmoothTransitionEnabled) {
                canvas.drawBitmap(mUnSelectedBitmap, null, mRect, null);
            } else {
                canvas.drawBitmap(mSelectedBitmap, null, mRect, null);
            }

            if (i == mCurrentPosition && mSmoothTransitionEnabled) {
                mTempRect.set(mRect);
            }

            mRect.offset(mIndicatorSize + mPaddingBetweenIndicators, 0);
        }

        if (mSmoothTransitionEnabled) {
            int offset = Math.round((mIndicatorSize + mPaddingBetweenIndicators) * mCurrentPositionOffset);
            mTempRect.offset(offset, 0);
            canvas.drawBitmap(mSelectedBitmap, null, mTempRect, null);
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
                || x > (mLeftBound + mTotalWidthWeNeed + mPaddingBetweenIndicators / 2)) {
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

    public void setSmoothTransition(boolean newValue) {
        mSmoothTransitionEnabled = newValue;
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
        if (mSmoothTransitionEnabled) {
            mCurrentPositionOffset = positionOffset;
            mCurrentPosition = position;
            invalidate();
        }
    }

    @Override
    public void onPageSelected(int position) {
        setSelectedIndicator(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setSelectedDrawable(Drawable drawable) {
        mSelectedDrawable = drawable;
        convertDrawablesToBitmaps();
        invalidate();
    }

    public void setUnSelectedDrawable(Drawable drawable) {
        mUnSelectedDrawable = drawable;
        convertDrawablesToBitmaps();
        invalidate();
    }

    // Click listener interface
    public interface OnIndicatorClickListener {
        void onClick(int indicatorNumber);
    }

}
