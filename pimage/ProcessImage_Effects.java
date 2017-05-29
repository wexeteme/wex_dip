package com.wex.weffecto.main.pimage;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;

import com.wex.weffecto.main.cimage.captureImage;

import static com.wex.weffecto.main.activities.EffectsActivity.progressTextView;

import static com.wex.weffecto.main.pimage.ProcessImage.isBussy;
import static com.wex.weffecto.main.pimage.ProcessImage.onResultEffect;
import static com.wex.weffecto.main.pimage.ProcessImage.progress;
import static com.wex.weffecto.main.pimage.ProcessImage.total;

/**
 * Created by welle on 5/4/2017.
 */

public class ProcessImage_Effects {

    public static Bitmap doGreyscale(final Bitmap src, final boolean saveToDisk, final boolean OnResult) {

        isBussy = true;

        final Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                // constant factors
                final double GS_RED = 0.299;
                final double GS_GREEN = 0.587;
                final double GS_BLUE = 0.114;

                // get image size
                int width = src.getWidth();
                int height = src.getHeight();

                int pixels[] = new int[width * height];

                src.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                for (int i = 0; i < pixels.length; i++) {

                    int alpha = Color.alpha(pixels[i]);
                    int red = Color.red(pixels[i]);
                    int green = Color.green(pixels[i]);
                    int blue = Color.blue(pixels[i]);

                    int newColor = (int) (GS_RED * red + GS_GREEN * green + GS_BLUE * blue);

                    pixels[i] = Color.argb(alpha, newColor, newColor, newColor);
                }

                bmOut.setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                return bmOut;
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                if(OnResult) {
                    onResultEffect(onResult, false, "img");
                }else{
                    captureImage.imageBitmap_MODEed = onResult.copy(captureImage.imageBitmap_MODEed.getConfig(), true);
                    isBussy = false;
                }
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

    // do black and white

    public static Bitmap getBinaryBitmap(final Bitmap src, final boolean saveToDisk, final boolean OnResult) {

        isBussy = true;

        final Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                // get image size
                int width = src.getWidth();
                int height = src.getHeight();

                int pixels[] = new int[width * height];

                src.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                for (int i = 0; i < pixels.length; i++) {

                    double dwhite = getColorDistance(pixels[i], Color.WHITE);
                    double dblack = getColorDistance(pixels[i], Color.BLACK);

                    if (dwhite <= dblack) {
                        pixels[i] = Color.WHITE;
                    } else {
                        pixels[i] = Color.BLACK;
                    }

                }

                bmOut.setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                return bmOut;
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                if(OnResult) {
                    onResultEffect(onResult, false, "img");
                }else{
                    captureImage.imageBitmap_MODEed = onResult.copy(captureImage.imageBitmap_MODEed.getConfig(), true);
                    isBussy = false;
                }
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

    public static double getColorDistance(int c1, int c2) {

        int db = Color.blue(c1) - Color.blue(c2);
        int dg = Color.green(c1) - Color.green(c2);
        int dr = Color.red(c1) - Color.red(c2);

        double d = Math.sqrt(Math.pow(db, 2) + Math.pow(dg, 2) + Math.pow(dr, 2));
        return d;
    }

    // invert image

    public static Bitmap doInvert(final Bitmap src, final boolean saveToDisk, final boolean OnResult) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                // color info
                int A, R, G, B;

                // image size
                int height = src.getHeight();
                int width = src.getWidth();

                int pixels[] = new int[width * height];

                src.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                for (int i = 0; i < pixels.length; i++) {

                    // saving alpha channel
                    A = Color.alpha(pixels[i]);

                    // inverting byte for each R/G/B channel
                    R = 255 - Color.red  (pixels[i]);
                    G = 255 - Color.green(pixels[i]);
                    B = 255 - Color.blue (pixels[i]);

                    pixels[i] = Color.argb(A, R, G, B);

                }

                bmOut[0].setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                if(OnResult) {
                    onResultEffect(onResult, false, "img");
                }else{
                    captureImage.imageBitmap_MODEed = onResult.copy(captureImage.imageBitmap_MODEed.getConfig(), true);
                    isBussy = false;
                }
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

    // gemma correction

    public static Bitmap doGamma(final Bitmap src, final boolean saveToDisk, final double red, final double green, final double blue, final boolean OnResult) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                // get image size
                int width = src.getWidth();
                int height = src.getHeight();

                // color information
                int A, R, G, B;

                // constant value curve
                final int MAX_SIZE = 256;
                final double MAX_VALUE_DBL = 255.0;
                final int MAX_VALUE_INT = 255;
                final double REVERSE = 1.0;

                // gamma arrays
                int[] gammaR = new int[MAX_SIZE];
                int[] gammaG = new int[MAX_SIZE];
                int[] gammaB = new int[MAX_SIZE];

                // setting values for every gamma channels
                for (int i = 0; i < MAX_SIZE; ++i) {
                    gammaR[i] = (int) Math.min(MAX_VALUE_INT,
                            (int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / red)) + 0.5));
                    gammaG[i] = (int) Math.min(MAX_VALUE_INT,
                            (int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / green)) + 0.5));
                    gammaB[i] = (int) Math.min(MAX_VALUE_INT,
                            (int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / blue)) + 0.5));
                }

                int pixels[] = new int[width * height];

                src.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                for (int i = 0; i < pixels.length; i++) {

                    // saving alpha channel
                    A = Color.alpha(pixels[i]);

                    // look up gamma
                    R = gammaR[Color.red(pixels[i])];
                    G = gammaG[Color.green(pixels[i])];
                    B = gammaB[Color.blue(pixels[i])];

                    pixels[i] = Color.argb(A, R, G, B);

                }

                bmOut[0].setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                if(OnResult) {
                    onResultEffect(onResult, false, "img");
                }else{
                    captureImage.imageBitmap_MODEed = onResult.copy(captureImage.imageBitmap_MODEed.getConfig(), true);
                    isBussy = false;
                }
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

    // color Filtering

    public static Bitmap doColorFiltering(final Bitmap src, final boolean saveToDisk, final double red, final double green, final double blue, final  boolean OnResult) {

        // input parametras, R=0, G=0, B=1 samo plavata boja ja pushta

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                // image size
                int width = src.getWidth();
                int height = src.getHeight();
                // color information
                int A, R, G, B;

                int pixels[] = new int[width * height];

                src.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                for (int i = 0; i < pixels.length; i++) {

                    // saving alpha channel
                    A = Color.alpha(pixels[i]);

                    // look up gamma
                    R = (int) (Color.red(pixels[i]) * red);
                    G = (int) (Color.green(pixels[i]) * green);
                    B = (int) (Color.blue(pixels[i]) * blue);

                    pixels[i] = Color.argb(A, R, G, B);

                }

                bmOut[0].setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                if(OnResult) {
                    onResultEffect(onResult, false, "img");
                }else{
                    captureImage.imageBitmap_MODEed = onResult.copy(captureImage.imageBitmap_MODEed.getConfig(), true);
                    isBussy = false;
                }
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

    // contrast change

    public static Bitmap doContrast(final Bitmap src, final boolean saveToDisk, final double value, final boolean OnResult) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                int width = src.getWidth();
                int height = src.getHeight();

                // color information
                int A, R, G, B;

                // get contrast value
                double contrast = Math.pow((100 + value) / 100, 2);

                int pixels[] = new int[width * height];

                src.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                for (int i = 0; i < pixels.length; i++) {

                    A = Color.alpha(pixels[i]);
                    // apply filter contrast for every channel R, G, B
                    R = Color.red(pixels[i]);
                    R = (int) (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                    if (R < 0) {
                        R = 0;
                    } else if (R > 255) {
                        R = 255;
                    }

                    G = Color.green(pixels[i]);
                    G = (int) (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                    if (G < 0) {
                        G = 0;
                    } else if (G > 255) {
                        G = 255;
                    }

                    B = Color.blue(pixels[i]);
                    B = (int) (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                    if (B < 0) {
                        B = 0;
                    } else if (B > 255) {
                        B = 255;
                    }

                    pixels[i] = Color.argb(A, R, G, B);
                }

                bmOut[0].setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                if(OnResult) {
                    onResultEffect(onResult, false, "img");
                }else{
                    captureImage.imageBitmap_MODEed = onResult.copy(captureImage.imageBitmap_MODEed.getConfig(), true);
                    isBussy = false;
                }
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

    // doBrightness change

    public static Bitmap doBrightness(final Bitmap src, final boolean saveToDisk, final double value, final boolean OnResult) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                // image size
                int width = src.getWidth();
                int height = src.getHeight();

                // color information
                int A, R, G, B;

                int pixels[] = new int[width * height];

                src.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                for (int i = 0; i < pixels.length; i++) {

                    A = Color.alpha(pixels[i]);
                    R = Color.red(pixels[i]);
                    G = Color.green(pixels[i]);
                    B = Color.blue(pixels[i]);

                    // increase/decrease each channel
                    R += value;
                    if (R > 255) {
                        R = 255;
                    } else if (R < 0) {
                        R = 0;
                    }

                    G += value;
                    if (G > 255) {
                        G = 255;
                    } else if (G < 0) {
                        G = 0;
                    }

                    B += value;
                    if (B > 255) {
                        B = 255;
                    } else if (B < 0) {
                        B = 0;
                    }

                    pixels[i] = Color.argb(A, R, G, B);
                }

                bmOut[0].setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                if(OnResult) {
                    onResultEffect(onResult, false, "img");
                }else{
                    captureImage.imageBitmap_MODEed = onResult.copy(captureImage.imageBitmap_MODEed.getConfig(), true);
                    isBussy = false;
                }
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

    // color swap R with G change

    public static Bitmap doRswapG(final Bitmap src, final boolean saveToDisk, final boolean OnResult) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                // image size
                int width = src.getWidth();
                int height = src.getHeight();

                // color information
                int A, R, G, B;

                int pixels[] = new int[width * height];

                src.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                for (int i = 0; i < pixels.length; i++) {

                    A = Color.alpha(pixels[i]);

                    R = Color.red(pixels[i]);
                    G = Color.green(pixels[i]);
                    B = Color.blue(pixels[i]);

                    int temp = R;
                    R = G;
                    G = temp;

                    pixels[i] = Color.argb(A, R, G, B);
                }

                bmOut[0].setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                if(OnResult) {
                    onResultEffect(onResult, false, "img");
                }else{
                    captureImage.imageBitmap_MODEed = onResult.copy(captureImage.imageBitmap_MODEed.getConfig(), true);
                    isBussy = false;
                }
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

    // color swap R with B change

    public static Bitmap doRswapB(final Bitmap src, final boolean saveToDisk, final boolean OnResult) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                // image size
                int width = src.getWidth();
                int height = src.getHeight();

                // color information
                int A, R, G, B;

                int pixels[] = new int[width * height];

                src.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                for (int i = 0; i < pixels.length; i++) {

                    A = Color.alpha(pixels[i]);
                    R = Color.red(pixels[i]);
                    G = Color.green(pixels[i]);
                    B = Color.blue(pixels[i]);

                    int temp = R;
                    R = B;
                    B = temp;

                    pixels[i] = Color.argb(A, R, G, B);
                }

                bmOut[0].setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                if(OnResult) {
                    onResultEffect(onResult, false, "img");
                }else{
                    captureImage.imageBitmap_MODEed = onResult.copy(captureImage.imageBitmap_MODEed.getConfig(), true);
                    isBussy = false;
                }
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

    // color swap G with B change

    public static Bitmap doGswapB(final Bitmap src, final boolean saveToDisk, final boolean OnResult) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                // image size
                int width = src.getWidth();
                int height = src.getHeight();

                // color information
                int A, R, G, B;

                int pixels[] = new int[width * height];

                src.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                for (int i = 0; i < pixels.length; i++) {

                    A = Color.alpha(pixels[i]);
                    R = Color.red(pixels[i]);
                    G = Color.green(pixels[i]);
                    B = Color.blue(pixels[i]);

                    int temp = G;
                    G = B;
                    B = temp;

                    pixels[i] = Color.argb(A, R, G, B);
                }

                bmOut[0].setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                if(OnResult) {
                    onResultEffect(onResult, false, "img");
                }else{
                    captureImage.imageBitmap_MODEed = onResult.copy(captureImage.imageBitmap_MODEed.getConfig(), true);
                    isBussy = false;
                }
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

    // shading effect

    public static Bitmap doShading(final Bitmap src, final boolean saveToDisk, final float red, final float green, final float blue, final boolean OnResult) {

        // shadingColor =  int color = (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);

        int redd = Math.round(red);
        int greenn = Math.round(green);
        int bluee = Math.round(blue);

        final int shadingColor = android.graphics.Color.argb(255, redd, greenn, bluee);

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                // get image size
                int width = bmOut[0].getWidth();
                int height = bmOut[0].getHeight();

                int pixels[] = new int[width * height];

                src.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                for (int i = 0; i < pixels.length; i++) {

                    pixels[i] = pixels[i] & shadingColor;

                }

                bmOut[0].setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                if(OnResult) {
                    onResultEffect(onResult, false, "img");
                }else{
                    captureImage.imageBitmap_MODEed = onResult.copy(captureImage.imageBitmap_MODEed.getConfig(), true);
                    isBussy = false;
                }
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

    // dodecreaseColorDepth effect

    public static Bitmap doDecreaseColorDepth(final Bitmap src, final boolean saveToDisk, final float bitOffset_f, final boolean OnResult) {

        // dodecreaseColorDepth - decressing bits for the color codding - example from 8 bits to 6 or idk - looking in to int NUMBER 0 - 255

        // vlezot e od 0 do 8 bita a vo formulata imame offeset podolu od 0 255 pa, pravile 2^VLEZ
        float inverse = 8 - bitOffset_f;
        final int bitOffset = (int) Math.round(Math.pow(2, inverse));

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                // get image size
                int width  = src.getWidth();
                int height = src.getHeight();

                // color information
                int A, R, G, B;

                int pixels[] = new int[width * height];

                src.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                for (int i = 0; i < pixels.length; i++) {

                    A = Color.alpha(pixels[i]);
                    R = Color.red(pixels[i]);
                    G = Color.green(pixels[i]);
                    B = Color.blue(pixels[i]);

                    // round-off color offset
                    R = ((R + (bitOffset / 2)) - ((R + (bitOffset / 2)) % bitOffset) - 1);
                    if (R < 0) {
                        R = 0;
                    }
                    G = ((G + (bitOffset / 2)) - ((G + (bitOffset / 2)) % bitOffset) - 1);
                    if (G < 0) {
                        G = 0;
                    }
                    B = ((B + (bitOffset / 2)) - ((B + (bitOffset / 2)) % bitOffset) - 1);
                    if (B < 0) {
                        B = 0;
                    }

                    pixels[i] = Color.argb(A, R, G, B);

                }

                bmOut[0].setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                if(OnResult) {
                    onResultEffect(onResult, false, "img");
                }else{
                    captureImage.imageBitmap_MODEed = onResult.copy(captureImage.imageBitmap_MODEed.getConfig(), true);
                    isBussy = false;
                }
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

    // mean remove

    public static Bitmap applyMeanRemoval(final Bitmap src, final boolean saveToDisk, final boolean OnResult) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                double[][] MeanRemovalConfig = new double[][]{
                        {-1, -1, -1},
                        {-1,  9, -1},
                        {-1, -1, -1}
                };

                ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
                convMatrix.applyConfig(MeanRemovalConfig);
                convMatrix.Factor = 1;
                convMatrix.Offset = 0;

                bmOut[0] = ConvolutionMatrix.computeConvolution3x3(src, convMatrix, false, 255);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                if(OnResult) {
                    onResultEffect(onResult, false, "img");
                }else{
                    captureImage.imageBitmap_MODEed = onResult.copy(captureImage.imageBitmap_MODEed.getConfig(), true);
                    isBussy = false;
                }
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

    // gausian blue

    public static Bitmap applyBlur(final Bitmap src, final boolean saveToDisk, final double[][] BlurConfig, final double factor, final boolean OnResult) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
                convMatrix.applyConfig(BlurConfig);
                convMatrix.Factor = factor;
                convMatrix.Offset = 0;

                bmOut[0] = ConvolutionMatrix.computeConvolution3x3(src, convMatrix, false, 255);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                if(OnResult) {
                    onResultEffect(onResult, false, "img");
                }else{
                    captureImage.imageBitmap_MODEed = onResult.copy(captureImage.imageBitmap_MODEed.getConfig(), true);
                    isBussy = false;
                }
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

    // edge detection

    public static Bitmap doEdgeDetection(final Bitmap src, final boolean saveToDisk, final boolean OnResult) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                double[][] edgeConfig_ = new double[][]{
                        {-1,  -1,  -1 },
                        {-1,   8,  -1 },
                        {-1,  -1,  -1 }
                };

                ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
                convMatrix.applyConfig(edgeConfig_);
                convMatrix.Factor = 1.0f/16.0f;

                bmOut[0] = ConvolutionMatrix.computeConvolution3x3(src, convMatrix, true, 1);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                if(OnResult) {
                    onResultEffect(onResult, false, "img");
                }else{
                    captureImage.imageBitmap_MODEed = onResult.copy(captureImage.imageBitmap_MODEed.getConfig(), true);
                    isBussy = false;
                }
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

    // vintage effect

    public static Bitmap doVintage(final Bitmap src, final boolean saveToDisk, final boolean OnResult) {

        // vinrage effect, se pravi na sledniov nacin
        // R = se sto e pod 64 postavi na 64
        // G = se sto e pod 64 postavi na 64
        // B = se sto e nad 192 postavi na 192

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                // get image size
                int width  = src.getWidth();
                int height = src.getHeight();

                // color information
                int A, R, G, B;

                int pixels[] = new int[width * height];

                src.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                for (int i = 0; i < pixels.length; i++) {

                    A = Color.alpha(pixels[i]);
                    R = Color.red(pixels[i]);
                    G = Color.green(pixels[i]);
                    B = Color.blue(pixels[i]);

                    // round-off color offset
                    if (R <= 64) {
                        R =  64;
                    }
                    if (G <= 64) {
                        G =  64;
                    }
                    if (B >= 192) {
                        B =  192;
                    }

                    pixels[i] = Color.argb(A, R, G, B);

                }

                bmOut[0].setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                if(OnResult) {
                    onResultEffect(onResult, false, "img");
                }else{
                    captureImage.imageBitmap_MODEed = onResult.copy(captureImage.imageBitmap_MODEed.getConfig(), true);
                    isBussy = false;
                }
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

}
