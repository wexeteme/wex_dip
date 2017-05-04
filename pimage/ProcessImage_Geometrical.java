package com.wex.weffecto.main.pimage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.util.Log;

import static com.wex.weffecto.main.pimage.ProcessImage.isBussy;
import static com.wex.weffecto.main.pimage.ProcessImage.onResultEffect;

/**
 * Created by welle on 3/14/2017.
 * we do Geometrical effects here
 */

public class ProcessImage_Geometrical {

    public static String tag = "ProcessImage - ";

    public static final int FLIP_VERTICAL   = 1;
    public static final int FLIP_HORIZONTAL = 2;

    // rotate image

    public static Bitmap doRotate(final Bitmap src, final boolean saveToDisk, final float degree) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                // create new matrix
                Matrix matrix = new Matrix();
                // setup rotation degree
                matrix.postRotate(degree);

                // return new bitmap rotated using matrix
                bmOut[0] = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                onResultEffect(onResult, false, "rotateImage");
            }

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onProgressUpdate(Void... values) {

            }
        }

        new LongOperation().execute();

        return bmOut[0];
    }

    // flip/miroring image

    public static Bitmap flipImage(final Bitmap src, final boolean saveToDisk, final int type) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                // create new matrix for transformation
                Matrix matrix = new Matrix();
                // if vertical
                if(type == FLIP_VERTICAL) {
                    // y = y * -1
                    matrix.preScale(1.0f, -1.0f);
                }
                // if horizonal
                else if(type == FLIP_HORIZONTAL) {
                    // x = x * -1
                    matrix.preScale(-1.0f, 1.0f);
                    // unknown type
                } else {
                    return null;
                }

                // return transformed image
                bmOut[0] = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);


                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                onResultEffect(onResult, false, "flipImage_" + type);
            }

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onProgressUpdate(Void... values) {

            }
        }

        new LongOperation().execute();

        return bmOut[0];
    }

    // applyReflection

    public static Bitmap applyReflection(final Bitmap src, final boolean saveToDisk) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                final int reflectionGap = 4;

                int width = src.getWidth();
                int height = src.getHeight();

                Matrix matrix = new Matrix();
                matrix.preScale(1, -1);

                Bitmap reflectionImage = Bitmap.createBitmap(src, 0, height/2, width, height/2, matrix, false);

                Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height/2), Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(bitmapWithReflection);
                canvas.drawBitmap(src, 0, 0, null);
                Paint defaultPaint = new Paint();
                canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);

                canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

                // create a shader that is a linear gradient that covers the reflection
                Paint paint = new Paint();
                LinearGradient shader = new LinearGradient(0, src.getHeight(), 0,
                        bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff,
                        Shader.TileMode.REPEAT);

                paint.setShader(shader);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

                bmOut[0] = bitmapWithReflection;

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                onResultEffect(onResult, false, "applyReflection");
            }

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onProgressUpdate(Void... values) {

            }
        }

        new LongOperation().execute();

        return bmOut[0];
    }

    // scale the image

    public static Bitmap scaleBitmap(Bitmap bitmap, final boolean saveToDisk, int wantedWidth, int wantedHeight) {

        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());

        canvas.drawBitmap(bitmap, m, new Paint());

        isBussy = false;

        return output;
    }

    public static Bitmap scaleBitmapMax(Bitmap image, final boolean saveToDisk, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

}
