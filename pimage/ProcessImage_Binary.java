package com.wex.weffecto.main.pimage;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import static com.wex.weffecto.main.pimage.ProcessImage.isBussy;
import static com.wex.weffecto.main.pimage.ProcessImage.onResultEffect;

/**
 * Created by welle on 3/14/2017.
 */

public class ProcessImage_Binary {

    public static String tag = "ProcessImage - ";

    // dilate Image

    public static Bitmap dilateBmp(final Bitmap src, final boolean saveToDisk, final int times) {

        isBussy = true;

        final Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                int width = src.getWidth();
                int height = src.getHeight();

                for (int i = 0; i < times; i++) {

                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {

                            if (bmOut.getPixel(x, y) == Color.BLACK) {

                                if (x > 0 && bmOut.getPixel(x - 1, y) == Color.WHITE) {
                                    bmOut.setPixel(x - 1, y, Color.BLUE);
                                }
                                if (y > 0 && src.getPixel(x, y - 1) == Color.WHITE) {
                                    bmOut.setPixel(x, y - 1, Color.BLUE);
                                }
                                if (x + 1 < width && src.getPixel(x + 1, y) == Color.WHITE) {
                                    bmOut.setPixel(x + 1, y, Color.BLUE);
                                }
                                if (y + 1 < height && src.getPixel(x, y + 1) == Color.WHITE) {
                                    bmOut.setPixel(x, y + 1, Color.BLUE);
                                }

                            }

                        }
                    }

                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {

                            if (bmOut.getPixel(x, y) == Color.BLUE) {
                                bmOut.setPixel(x, y, Color.BLACK);
                            }

                        }
                    }

                }

                return bmOut;
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                onResultEffect(onResult, false, "grayImage");
            }

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onProgressUpdate(Void... values) {

            }
        }

        new LongOperation().execute();

        return bmOut;
    }

    // erode image

    public static Bitmap erodeBmp(final Bitmap src, final boolean saveToDisk, final int times) {

        isBussy = true;

        final Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                int width = src.getWidth();
                int height = src.getHeight();

                for (int i = 0; i < times; i++) {

                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {

                            if (src.getPixel(x, y) == Color.WHITE) {

                                if (x > 0 && src.getPixel(x - 1, y) == Color.BLACK) {
                                    bmOut.setPixel(x - 1, y, Color.BLUE);
                                }
                                if (y > 0 && src.getPixel(x, y - 1) == Color.BLACK) {
                                    bmOut.setPixel(x, y - 1, Color.BLUE);
                                }
                                if (x + 1 < width && src.getPixel(x + 1, y) == Color.BLACK) {
                                    bmOut.setPixel(x + 1, y, Color.BLUE);
                                }
                                if (y + 1 < height && src.getPixel(x, y + 1) == Color.BLACK) {
                                    bmOut.setPixel(x, y + 1, Color.BLUE);
                                }

                            }

                        }
                    }

                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {

                            if (bmOut.getPixel(x, y) == Color.BLUE) {
                                bmOut.setPixel(x, y, Color.WHITE);
                            }

                        }
                    }

                }

                return bmOut;
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                onResultEffect(onResult, false, "grayImage");
            }

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onProgressUpdate(Void... values) {

            }
        }

        new LongOperation().execute();

        return bmOut;
    }

    // remove alone pixels

    public static Bitmap removeAlonePixels(Bitmap bm) {

        // remove alone pixels

        Log.w(tag, "removeAlonePixels!");

        Bitmap bitmap_new = bm.copy(bm.getConfig(), true);

        int width = bm.getWidth();
        int height = bm.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                if (bm.getPixel(x, y) == Color.BLACK) {

                    // we are checking if the area of that one pixel is WHITE
                    // if so - mark it for DELETING
                    if (x > 0 && bm.getPixel(x - 1, y) == Color.WHITE) {
                        if (y > 0 && bm.getPixel(x, y - 1) == Color.WHITE) {
                            if (x + 1 < width && bm.getPixel(x + 1, y) == Color.WHITE) {
                                if (y + 1 < height && bm.getPixel(x, y + 1) == Color.WHITE) {
                                    bitmap_new.setPixel(x, y, Color.BLUE);
                                }
                            }
                        }
                    }

                }

            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                if (bitmap_new.getPixel(x, y) == Color.BLUE) {
                    bitmap_new.setPixel(x, y, Color.WHITE);
                }

            }
        }

        return bitmap_new;

    }

    // fill alone pixels

    public static Bitmap fillAlonePixels(Bitmap bm) {

        // remove alone pixels

        Log.w(tag, "fillAlonePixels!");

        Bitmap bitmap_new = bm.copy(bm.getConfig(), true);

        int width = bm.getWidth();
        int height = bm.getHeight();

        int initRadius = 1;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                if (bm.getPixel(x, y) == Color.WHITE) {
                    // we are checking if the area of that one pixel is BLACK
                    // if so - mark it for CHANGE
                    if (x > 0 && bm.getPixel(x - initRadius, y) == Color.BLACK) {
                        if (y > 0 && bm.getPixel(x, y - initRadius) == Color.BLACK) {
                            if (x + 1 < width && bm.getPixel(x + initRadius, y) == Color.BLACK) {
                                if (y + 1 < height && bm.getPixel(x, y + initRadius) == Color.BLACK) {
                                    bitmap_new.setPixel(x, y, Color.WHITE);
                                }
                            }
                        }
                    }

                }

            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                if (bitmap_new.getPixel(x, y) == Color.BLUE) {
                    bitmap_new.setPixel(x, y, Color.WHITE);
                }

            }
        }

        return bitmap_new;
    }

}
