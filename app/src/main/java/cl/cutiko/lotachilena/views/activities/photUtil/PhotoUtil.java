package cl.cutiko.lotachilena.views.activities.photUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PhotoUtil {

	public static int calculateInSampleSize(BitmapFactory.Options options, int size) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	    if (height > size || width > size) {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	        
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > size
	                && (halfWidth / inSampleSize) > size) {
	            inSampleSize *= 2;
	        }
	    }
    	return inSampleSize;
	}

	
	public static Bitmap getBitmapFromPath(String path){
        System.gc();
    	BitmapFactory.Options options = new BitmapFactory.Options();
    	options.inJustDecodeBounds = true;
    	BitmapFactory.decodeFile(path, options);
    	options.inSampleSize = PhotoUtil.calculateInSampleSize(options,200);
    	options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path,options);
	}

}
