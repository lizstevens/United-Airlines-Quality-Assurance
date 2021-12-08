package edu.msu.steve702.ua_quality_assurance_platform.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * ImageDisplayView Class
 * View for displaying the images for an audit
 */
public class ImageDisplayView extends View {

    /**
     * Constructor
     * @param context the application context
     */
    public ImageDisplayView(Context context) {
        super(context);
    }

    /**
     * Constructor
     * @param context the application context
     * @param attrs the atribute set
     */
    public ImageDisplayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructor
     * @param context the application context
     * @param attrs the attribute set
     * @param defStyleAttr the style attributes
     */
    public ImageDisplayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Constructor
     * @param context the application context
     * @param attrs the attribute set
     * @param defStyleAttr the attribute style
     * @param defStyleRes the style of attribute selected
     */
    public ImageDisplayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
