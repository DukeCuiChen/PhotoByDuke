package com.duke.photobyduke.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class PageArgs implements Parcelable{

    private int x;
    private int y;
    private int height;

    public PageArgs() {
    }

    protected PageArgs(Parcel in) {
        x = in.readInt();
        y = in.readInt();
        height = in.readInt();
    }

    public static final Creator<PageArgs> CREATOR = new Creator<PageArgs>() {
        @Override
        public PageArgs createFromParcel(Parcel in) {
            return new PageArgs(in);
        }

        @Override
        public PageArgs[] newArray(int size) {
            return new PageArgs[size];
        }
    };

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight(){
        return height;
    }

    public void setHeight(int height){
        this.height = height;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeInt(height);
    }

    @Override
    public String toString() {
        return "PageArgs{" +
                "x=" + x +
                ", y=" + y +
                ", height" + height +
                '}';
    }
}
