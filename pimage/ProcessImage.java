package com.wex.weffecto.main.pimage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wex.weffecto.R;
import com.wex.weffecto.main.activities.EffectsActivity;
import com.wex.weffecto.main.cimage.captureImage;
import com.wex.weffecto.main.common.Animations;
import com.wex.weffecto.main.debug.debugClass;

import java.util.ArrayList;

import static com.wex.weffecto.main.activities.EffectsActivity.context;
import static com.wex.weffecto.main.activities.EffectsActivity.mainImageDisplay_ORIGINAL;
import static com.wex.weffecto.main.activities.EffectsActivity.progressTextView;
import static com.wex.weffecto.main.common.CommonClass.showToast;
import static com.wex.weffecto.main.pimage.ProcessImage_Binary.dilateBmp;
import static com.wex.weffecto.main.pimage.ProcessImage_Binary.erodeBmp;
import static com.wex.weffecto.main.pimage.ProcessImage_Geometrical.applyReflection;
import static com.wex.weffecto.main.pimage.ProcessImage_Geometrical.doRotate;
import static com.wex.weffecto.main.pimage.ProcessImage_Geometrical.flipImage;

/**
 * Created by welle on 2/24/2017.
 * here, we create image processing tools
 */

public class ProcessImage {

    private static String tag = "wEffecto debug - ";

    public static Animations a = new Animations();

    public static boolean isBussy = false;
    public static boolean isBussy_FINAL = false;

    // input dialog parametars -----------------
    public static boolean statusDialog = false;
    // max input parametars is 3 :"C
    public static String parametars[] = {"0.0f", "0.0f", "0.0f"};
    public static int ids[] = {0, 0, 0};
    // input dialog parametars -----------------

    public static int progress = 0;
    public static int total = 0;

    public static ArrayList<String> effectsArray = new ArrayList<String>();

    public static int totalEffects = 26;
    public static String[][] CATEGORIES = {
            {"Geometrical",
                    "<",
                    "rotate",
                    "flip horizontally",
                    "flip vertically",
                    "reflection"},

            {"Effects",
                    "<",
                    "doGray",
                    "doInvert",
                    "doGamma",
                    "doColorFilteringR",
                    "doColorFilteringG",
                    "doColorFilteringB",
                    "swapR_B",
                    "swapR_G",
                    "swapG_B",
                    "shadingFilter",
                    "decColorDepth",
                    "doContrast",
                    "doBrightness",
                    "meanRemoval",
                    "gaussianBlur",
                    "sharpenImage",
                    "doEdgeDetection"},

            {"binaryEffects",
                    "<",
                    "binary image",
                    "dilate pixels",
                    "erode pixels"}
    };

    public static Bitmap callFunction(String name) {

        progressTextView.setText("processing");
        a.blinkAnimationFT(0, 1, 800, progressTextView);

        if (name.contentEquals("<")) {

            progressTextView.setText("-");
            a.cancelAnimation(progressTextView);

            EffectsActivity.initEffectsInterface();
        }

        // geometrical effects

        if (name.contentEquals("rotate")) {
            if (statusDialog) {
                // reset keeper
                statusDialog = false;
                return doRotate(captureImage.imageBitmap_MODEed, false, Float.parseFloat(parametars[0]));
            } else {
                String[] params = {"Rotation"};
                String title = "usually param is from 0 to 360*";
                popDialogInput(title, params, name);
                return null;
            }
        }
        if (name.contentEquals("flip vertically")) {
            return flipImage(captureImage.imageBitmap_MODEed, false, 1);
        }
        if (name.contentEquals("flip horizontally")) {
            return flipImage(captureImage.imageBitmap_MODEed, false, 2);
        }
        if (name.contentEquals("reflection")) {
            return applyReflection(captureImage.imageBitmap_MODEed, false);
        }
        // geometrical effects

        // color/pixels effects

        if (name.contentEquals("doGray")) {
            return doGreyscale(captureImage.imageBitmap_MODEed, false);
        }
        if (name.contentEquals("doInvert")) {
            return doInvert(captureImage.imageBitmap_MODEed, false);
        }
        if (name.contentEquals("doGamma")) {
            if (statusDialog) {
                // reset keeper
                statusDialog = false;
                return doGamma(captureImage.imageBitmap_MODEed, false,
                        Float.parseFloat(parametars[0]),
                        Float.parseFloat(parametars[1]),
                        Float.parseFloat(parametars[2]));
            } else {
                String[] params = {"Gamma Red", "Gamma Green", "Gamma Blue"};
                String title = "usually params are from 1.0 to 2.0";
                popDialogInput(title, params, name);
                return null;
            }
        }
        if (name.contentEquals("doColorFilteringR")) {
            return doColorFiltering(captureImage.imageBitmap_MODEed, false, 1, 0, 0);
        }
        if (name.contentEquals("doColorFilteringG")) {
            return doColorFiltering(captureImage.imageBitmap_MODEed, false, 0, 1, 0);
        }
        if (name.contentEquals("doColorFilteringB")) {
            return doColorFiltering(captureImage.imageBitmap_MODEed, false, 0, 0, 1);
        }
        if (name.contentEquals("swapR_G")) {
            return doRswapG(captureImage.imageBitmap_MODEed, false);
        }
        if (name.contentEquals("swapR_B")) {
            return doRswapB(captureImage.imageBitmap_MODEed, false);
        }
        if (name.contentEquals("swapG_B")) {
            return doGswapB(captureImage.imageBitmap_MODEed, false);
        }
        if (name.contentEquals("shadingFilter")) {
            if (statusDialog) {
                // reset keeper
                statusDialog = false;
                return doShading(captureImage.imageBitmap_MODEed, false, Float.parseFloat(parametars[0]),
                        Float.parseFloat(parametars[1]),
                        Float.parseFloat(parametars[2]));
            } else {
                String[] params = {"Shading Red", "Shading Green", "Shading Blue"};
                String title = "usually in range is 255 to 0";
                popDialogInput(title, params, name);
                return null;
            }
        }
        if (name.contentEquals("decColorDepth")) {
            if (statusDialog) {
                // reset keeper
                statusDialog = false;
                return doDecreaseColorDepth(captureImage.imageBitmap_MODEed, false, Float.parseFloat(parametars[0]));
            } else {
                String[] params = {"New Color Depth"};
                String title = "usually in range is 0 to 8 bits";
                popDialogInput(title, params, name);
                return null;
            }
        }
        if (name.contentEquals("doContrast")) {
            if (statusDialog) {
                // reset keeper
                statusDialog = false;
                return doContrast(captureImage.imageBitmap_MODEed, false, Float.parseFloat(parametars[0]));
            } else {
                String[] params = {"Contrast"};
                String title = "usually in range is 0 to 100";
                popDialogInput(title, params, name);
                return null;
            }
        }
        if (name.contentEquals("doBrightness")) {
            if (statusDialog) {
                // reset keeper
                statusDialog = false;
                return doBrightness(captureImage.imageBitmap_MODEed, false, Float.parseFloat(parametars[0]));
            } else {
                String[] params = {"Brightness"};
                String title = "usually in range is 0 to 255";
                popDialogInput(title, params, name);
                return null;
            }
        }
        if (name.contentEquals("meanRemoval")) {
            return applyMeanRemoval(captureImage.imageBitmap_MODEed, false);
        }
        if (name.contentEquals("gaussianBlur")) {
            return applyGaussianBlur(captureImage.imageBitmap_MODEed, false);
        }
        if (name.contentEquals("sharpenImage")) {
            if (statusDialog) {
                // reset keeper
                statusDialog = false;
                return sharpenImage(captureImage.imageBitmap_MODEed, false, Integer.parseInt(parametars[0]));
            } else {
                String[] params = {"Weight"};
                String title = "usually in range is 15 to 1";
                popDialogInput(title, params, name);
                return null;
            }
        }
        if (name.contentEquals("doEdgeDetection")) {
            return doEdgeDetection(captureImage.imageBitmap_MODEed, false);
        }
        // color/pixels effects

        // binary effects

        if (name.contentEquals("binary image")) {
            return getBinaryBitmap(captureImage.imageBitmap_MODEed, false);
        }
        if (name.contentEquals("dilate pixels")) {
            if (statusDialog) {
                // reset keeper
                statusDialog = false;
                return dilateBmp(captureImage.imageBitmap_MODEed, false, Integer.parseInt(parametars[0]));
            } else {
                String[] params = {"Pixels"};
                String title = "usually in range is 1 to 50";
                popDialogInput(title, params, name);
                return null;
            }
        }
        if (name.contentEquals("erode pixels")) {
            if (statusDialog) {
                // reset keeper
                statusDialog = false;
                return erodeBmp(captureImage.imageBitmap_MODEed, false, Integer.parseInt(parametars[0]));
            } else {
                String[] params = {"Pixels"};
                String title = "usually in range is 1 to 50";
                popDialogInput(title, params, name);
                return null;
            }
        }
        // binary effects

        return null;

    }

    // dialog for INPUT parametars

    public static void popDialogInput(String title, final String[] parametarsInput, final String functionName) {

        AlertDialog.Builder builder = new AlertDialog.Builder((Activity) context);
        builder.setMessage("Input Parametars - \n" + title);

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.input_parametars_dialog, null);
        builder.setView(dialogView);

        LinearLayout mainDialogView = (LinearLayout) (dialogView).findViewById(R.id.mainDialogView);

        // creating exact input params as much as required by the INPUT
        for (int i = 0; i < parametarsInput.length; i++) {
            EditText tv = new EditText(context);
            LinearLayout.LayoutParams params = null;
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            tv.setHint("" + parametarsInput[i]);
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(params);
            tv.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            ids[i] = View.generateViewId();
            tv.setId(ids[i]);

            mainDialogView.addView(tv, i);
        }

        // mainDialogView.setPadding(0, 0, 0, 25);

        builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                statusDialog = true;
                for (int i = 0; i < parametarsInput.length; i++) {
                    if (((EditText) (dialogView).findViewById(ids[i])).getText().toString().isEmpty()) {
                        parametars[i] = "1.0";
                    } else {
                        parametars[i] = ((EditText) (dialogView).findViewById(ids[i])).getText().toString();
                    }
                }

                callFunction(functionName);
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                statusDialog = false;
                progressTextView.setText("-");
                a.cancelAnimation(progressTextView);
            }
        });

        builder.setCancelable(false);
        AlertDialog alert = builder.create();

        alert.show();

        TextView messageView = (TextView) alert.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);

    }

    public static void onResultEffect(Bitmap onResult, boolean saveToDisk, String name) {
        if (saveToDisk) {
            captureImage.saveImageToFileModes(onResult, name);
        }
        isBussy = false;

        progressTextView.setText("-");
        a.cancelAnimation(progressTextView);

        captureImage.imageBitmap_MODEed = onResult.copy(captureImage.imageBitmap_MODEed.getConfig(), true);
        mainImageDisplay_ORIGINAL.setImageBitmap(captureImage.imageBitmap_MODEed);
        a.alphaAnimation(0, 1, 1000, mainImageDisplay_ORIGINAL);
    }

    public static void putOutFinalImage() {

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                return null;
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                showToast("Image saved, enjoy!");
                isBussy_FINAL = false;
                captureImage.saveImageToFile(captureImage.imageBitmap_MODEed, "output");
            }

            @Override
            protected void onPreExecute() {

                showToast("Processing ...");

            }

            @Override
            protected void onProgressUpdate(Void... values) {
            }

        }

        new LongOperation().execute();

    }

    // EFFECTS EFFECTS
    // EFFECTS EFFECTS
    // EFFECTS EFFECTS
    // EFFECTS EFFECTS

    // doGrayScaleImage

    public static Bitmap doGreyscale(final Bitmap src, final boolean saveToDisk) {

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
                onResultEffect(onResult, false, "grayImage");
            }

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onProgressUpdate(Void... values) {
                float status = (((float) progress) / ((float) total)) * 100;
                String x = String.format("%.2f", status);
                progressTextView.setText("" + x + " %");
            }
        }

        new LongOperation().execute();

        return bmOut;
    }

    // do black and white

    public static Bitmap getBinaryBitmap(final Bitmap src, final boolean saveToDisk) {

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
                onResultEffect(onResult, false, "binaryImage");
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

    public static Bitmap doInvert(final Bitmap src, final boolean saveToDisk) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                // color info
                int A, R, G, B;
                int pixelColor;
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
                onResultEffect(onResult, false, "invertImage");
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

    public static Bitmap doGamma(final Bitmap src, final boolean saveToDisk, final double red, final double green, final double blue) {

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
                int pixel;
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
                onResultEffect(onResult, false, "gemmaImage");
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

    public static Bitmap doColorFiltering(final Bitmap src, final boolean saveToDisk, final double red, final double green, final double blue) {

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
                int pixel;

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
                onResultEffect(onResult, false, "colorFilter");
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

    public static Bitmap doContrast(final Bitmap src, final boolean saveToDisk, final double value) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                int width = src.getWidth();
                int height = src.getHeight();

                // color information
                int A, R, G, B;
                int pixel;
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
                onResultEffect(onResult, false, "contrastImage");
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

    public static Bitmap doBrightness(final Bitmap src, final boolean saveToDisk, final double value) {

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
                int pixel;

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
                onResultEffect(onResult, false, "brigthnessImage");
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

    public static Bitmap doRswapG(final Bitmap src, final boolean saveToDisk) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                // image size
                int width = src.getWidth();
                int height = src.getHeight();

                // color information
                int R, G, B;
                int pixel;

                int pixels[] = new int[width * height];

                src.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                for (int i = 0; i < pixels.length; i++) {

                    R = Color.red(pixels[i]);
                    G = Color.green(pixels[i]);
                    B = Color.blue(pixels[i]);

                    int temp = R;
                    R = G;
                    G = temp;

                    pixels[i] = Color.rgb(R, G, B);
                }

                bmOut[0].setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                onResultEffect(onResult, false, "brigthnessImage");
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

    public static Bitmap doRswapB(final Bitmap src, final boolean saveToDisk) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                // image size
                int width = src.getWidth();
                int height = src.getHeight();

                // color information
                int R, G, B;
                int pixel;

                int pixels[] = new int[width * height];

                src.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                for (int i = 0; i < pixels.length; i++) {

                    R = Color.red(pixels[i]);
                    G = Color.green(pixels[i]);
                    B = Color.blue(pixels[i]);

                    int temp = R;
                    R = B;
                    B = temp;

                    pixels[i] = Color.rgb(R, G, B);
                }

                bmOut[0].setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                onResultEffect(onResult, false, "brigthnessImage");
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

    public static Bitmap doGswapB(final Bitmap src, final boolean saveToDisk) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                // image size
                int width = src.getWidth();
                int height = src.getHeight();

                // color information
                int R, G, B;
                int pixel;

                int pixels[] = new int[width * height];

                src.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                for (int i = 0; i < pixels.length; i++) {

                    R = Color.red(pixels[i]);
                    G = Color.green(pixels[i]);
                    B = Color.blue(pixels[i]);

                    int temp = G;
                    G = B;
                    B = temp;

                    pixels[i] = Color.rgb(R, G, B);
                }

                bmOut[0].setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                onResultEffect(onResult, false, "brigthnessImage");
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

    public static Bitmap doShading(final Bitmap src, final boolean saveToDisk, final float red, final float green, final float blue) {

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
                onResultEffect(onResult, false, "shadingImage");
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

    public static Bitmap doDecreaseColorDepth(final Bitmap src, final boolean saveToDisk, final float bitOffset_f) {

        // dodecreaseColorDepth - decressing bits for the color codding - example from 8 bits to 6 or idk - looking in to int NUMBER 0 - 255

        // vlezot e od 0 do 8 bita a vo formulata imame offeset podolu od 0 255 pa, pravile 2^VLEZ
        final int bitOffset = (int) Math.round(Math.pow(2, bitOffset_f));

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

                    pixels[i] = Color.rgb(R, G, B);

                }

                bmOut[0].setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                onResultEffect(onResult, false, "dodecreaseColorDepth");
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

    public static Bitmap applyMeanRemoval(final Bitmap src, final boolean saveToDisk) {

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

                bmOut[0] = ConvolutionMatrix.computeConvolution3x3(src, convMatrix);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                onResultEffect(onResult, false, "meanRemove");
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

    public static Bitmap applyGaussianBlur(final Bitmap src, final boolean saveToDisk) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                double[][] GaussianBlurConfig = new double[][]{
                        {1, 2, 1},
                        {2, 4, 2},
                        {1, 2, 1}
                };

                ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
                convMatrix.applyConfig(GaussianBlurConfig);
                convMatrix.Factor = 16;
                convMatrix.Offset = 0;

                bmOut[0] = ConvolutionMatrix.computeConvolution3x3(src, convMatrix);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                onResultEffect(onResult, false, "gaussianBlur");
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

    // sharpen the Image

    public static Bitmap sharpenImage(final Bitmap src, final boolean saveToDisk, final int weight) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                double[][] SharpConfig = new double[][]{
                        {0, -2,       0},
                        {-2, weight, -2},
                        {0, -2,       0}
                };
                ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
                convMatrix.applyConfig(SharpConfig);
                convMatrix.Factor = weight - 8;
                bmOut[0] = ConvolutionMatrix.computeConvolution3x3(src, convMatrix);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                onResultEffect(onResult, false, "sharpen");
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

    public static Bitmap doEdgeDetection(final Bitmap src, final boolean saveToDisk) {

        isBussy = true;

        final Bitmap[] bmOut = {Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig())};

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                double[][] edgeConfig = new double[][]{
                        {-1, -1, -1},
                        {-1,  8, -1},
                        {-1, -1, -1}
                };
                ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
                convMatrix.applyConfig(edgeConfig);
                convMatrix.Factor = 0;
                bmOut[0] = ConvolutionMatrix.computeConvolution3x3(src, convMatrix);

                return bmOut[0];
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                onResultEffect(onResult, false, "sharpen");
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
