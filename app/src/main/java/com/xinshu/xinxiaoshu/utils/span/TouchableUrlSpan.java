package com.xinshu.xinxiaoshu.utils.span;

import android.content.res.ColorStateList;
import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.URLSpan;

/**
 * An extension to URLSpan which changes it's background & foreground color when clicked.
 * <p>
 * Derived from http://stackoverflow.com/a/20905824
 */
public class TouchableUrlSpan extends URLSpan {

    private static int[] STATE_PRESSED = new int[]{android.R.attr.state_pressed};
    private boolean isPressed;
    private int normalTextColor;
    private int pressedTextColor;
    private int pressedBackgroundColor;

    public TouchableUrlSpan(String url,
                            ColorStateList textColor,
                            int pressedBackgroundColor) {
        super(url);
        this.normalTextColor = textColor.getDefaultColor();
        this.pressedTextColor = textColor.getColorForState(STATE_PRESSED, normalTextColor);
        this.pressedBackgroundColor = pressedBackgroundColor;
    }

    public void setPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    @Override
    public void updateDrawState(TextPaint drawState) {
        drawState.setColor(isPressed ? pressedTextColor : normalTextColor);
        drawState.bgColor = isPressed ? pressedBackgroundColor : 0;
        drawState.setUnderlineText(!isPressed);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isPressed ? (byte) 1 : (byte) 0);
        dest.writeInt(this.normalTextColor);
        dest.writeInt(this.pressedTextColor);
        dest.writeInt(this.pressedBackgroundColor);
    }

    private TouchableUrlSpan(Parcel in) {
        super(in);
        this.isPressed = in.readByte() != 0;
        this.normalTextColor = in.readInt();
        this.pressedTextColor = in.readInt();
        this.pressedBackgroundColor = in.readInt();
    }

    public static final Creator<TouchableUrlSpan> CREATOR = new Creator<TouchableUrlSpan>() {
        @Override
        public TouchableUrlSpan createFromParcel(Parcel source) {
            return new TouchableUrlSpan(source);
        }

        @Override
        public TouchableUrlSpan[] newArray(int size) {
            return new TouchableUrlSpan[size];
        }
    };
}