package com.meiqia.meiqiasdk.model;

import java.util.ArrayList;

/**
 * 浣滆��:鐜嬫旦 閭欢:bingoogolapple@gmail.com
 * 鍒涘缓鏃堕棿:16/2/24 涓嬪崍6:28
 * 鎻忚堪:
 */
public class ImageFolderModel {
    public String name;
    public String coverPath;
    private ArrayList<String> mImages = new ArrayList<String>();
    private boolean mTakePhotoEnabled;

    public ImageFolderModel(boolean takePhotoEnabled) {
        mTakePhotoEnabled = takePhotoEnabled;
        if (takePhotoEnabled) {
            // 鎷嶇収
            mImages.add("");
        }
    }

    public ImageFolderModel(String name, String coverPath) {
        this.name = name;
        this.coverPath = coverPath;
    }

    public boolean isTakePhotoEnabled() {
        return mTakePhotoEnabled;
    }

    public void addLastImage(String imagePath) {
        mImages.add(imagePath);
    }

    public ArrayList<String> getImages() {
        return mImages;
    }

    public int getCount() {
        return mImages.size();
    }
}