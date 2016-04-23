package ru.avisprof.accounting.images;

import android.graphics.Bitmap;

public class ImageItem {
	private Bitmap mImage;
	private String mTitle;
	
	public ImageItem(Bitmap image, String title) {
		mImage = image;
		mTitle = title;
	}

	public Bitmap getImage() {
		return mImage;
	}

	public void setImage(Bitmap mImage) {
		this.mImage = mImage;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}
}
