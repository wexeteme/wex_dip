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
import static com.wex.weffecto.main.pimage.ProcessImage_Effects.applyBlur;
import static com.wex.weffecto.main.pimage.ProcessImage_Effects.applyMeanRemoval;
import static com.wex.weffecto.main.pimage.ProcessImage_Effects.doBrightness;
import static com.wex.weffecto.main.pimage.ProcessImage_Effects.doColorFiltering;
import static com.wex.weffecto.main.pimage.ProcessImage_Effects.doContrast;
import static com.wex.weffecto.main.pimage.ProcessImage_Effects.doDecreaseColorDepth;
import static com.wex.weffecto.main.pimage.ProcessImage_Effects.doEdgeDetection;
import static com.wex.weffecto.main.pimage.ProcessImage_Effects.doGamma;
import static com.wex.weffecto.main.pimage.ProcessImage_Effects.doGreyscale;
import static com.wex.weffecto.main.pimage.ProcessImage_Effects.doGswapB;
import static com.wex.weffecto.main.pimage.ProcessImage_Effects.doInvert;
import static com.wex.weffecto.main.pimage.ProcessImage_Effects.doRswapB;
import static com.wex.weffecto.main.pimage.ProcessImage_Effects.doRswapG;
import static com.wex.weffecto.main.pimage.ProcessImage_Effects.doShading;
import static com.wex.weffecto.main.pimage.ProcessImage_Effects.doVintage;
import static com.wex.weffecto.main.pimage.ProcessImage_Effects.getBinaryBitmap;
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
    public static boolean isBussy_MAJOR = false;
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

    public static int totalEffects = 25;
    public static String[][] CATEGORIES = {
            {"Geometrical",
                    "<",
                    "Rotate",
                    "Flip horizontally",
                    "Flip vertically",
                    "Reflection"},

            {"Artistic",
                    "<",
                    "Black & White",
                    "Invert",
                    "Binary",
                    "Summer",
                    "Winter",
                    "Punch",
                    "Blur",
                    "Box Blur",
                    "Cartoonize",
                    "Vintage",
                    "Edge Up",
                    "Sharpen",
                    "Red",
                    "Blue",
                    "Green",
                    "Brighter",
                    "Darker",
                    "Magic 1",
                    "Magic 2"},
    };

    // OVDE SE PRAVAT EFEKTITE !_!_!_!

    public static void inProgressDisplay(){
        progressTextView.setText("processing");
        a.blinkAnimationFT(0, 1, 800, progressTextView);
    }

    public static void callFunction(String name) {

        inProgressDisplay();

        if (name.contentEquals("<")) {

            progressTextView.setText("-");
            a.cancelAnimation(progressTextView);

            EffectsActivity.initEffectsInterface();
        }

        // geometrical effects

        if (name.contentEquals("Rotate")) {
            if (statusDialog) {
                // reset keeper
                statusDialog = false;
                doRotate(captureImage.imageBitmap_MODEed, false, Float.parseFloat(parametars[0]));
            } else {
                String[] params = {"Rotation"};
                String title = "usually param is from 0 to 360*";
                popDialogInput(title, params, name);
            }
        }
        if (name.contentEquals("Flip vertically")) {
            flipImage(captureImage.imageBitmap_MODEed, false, 1);
        }
        if (name.contentEquals("Flip horizontally")) {
            flipImage(captureImage.imageBitmap_MODEed, false, 2);
        }
        if (name.contentEquals("Reflection")) {
            applyReflection(captureImage.imageBitmap_MODEed, false);
        }
        // geometrical effects

        // color/pixels effects

        if (name.contentEquals("Black & White")) {
            doGreyscale(captureImage.imageBitmap_MODEed, false, true);
        }
        if (name.contentEquals("Invert")) {
            doInvert(captureImage.imageBitmap_MODEed, false, true);
        }
        if (name.contentEquals("Binary")) {
            getBinaryBitmap(captureImage.imageBitmap_MODEed, false, true);
        }
        if (name.contentEquals("Summer")) {
            doGamma(captureImage.imageBitmap_MODEed, false,
                    1.8f,
                    1.0f ,
                    1.0f , true);
        }
        if (name.contentEquals("Winter")) {
            doGamma(captureImage.imageBitmap_MODEed, false,
                    1.0f,           // R
                    1.0f ,          // G
                    1.8f , true);   // B
        }
        if (name.contentEquals("Punch")) {
            doContrast(captureImage.imageBitmap_MODEed, false, 10, true);
        }
        if (name.contentEquals("Blur")) {
            double[][] Config = new double[][]{
                    {1, 2, 1},
                    {2, 4, 2},
                    {1, 2, 1}
            };
            applyBlur(captureImage.imageBitmap_MODEed, false, Config, 16, true);
        }
        if (name.contentEquals("Box Blur")) {
            double[][] Config = new double[][]{
                    {1.0f/9, 1.0f/9, 1.0f/9},
                    {1.0f/9, 1.0f/9, 1.0f/9},
                    {1.0f/9, 1.0f/9, 1.0f/9}
            };
            applyBlur(captureImage.imageBitmap_MODEed, false, Config, 1, true);
        }
        if (name.contentEquals("Cartoonize")) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    double[][] Config = new double[][]{
                            {1.0f/9, 1.0f/9, 1.0f/9},
                            {1.0f/9, 1.0f/9, 1.0f/9},
                            {1.0f/9, 1.0f/9, 1.0f/9}
                    };
                    applyBlur(captureImage.imageBitmap_MODEed, false, Config, 1, false);
                    while(isBussy){}
                    doDecreaseColorDepth(captureImage.imageBitmap_MODEed, false, 3.0f, false);
                    while(isBussy){}
                    applyBlur(captureImage.imageBitmap_MODEed, false, Config, 1, true);
                    while(isBussy){}

                }
            };
            new Thread(runnable).start();
        }
        if (name.contentEquals("Vintage")) {
            doVintage(captureImage.imageBitmap_MODEed, false, true);
        }
        if (name.contentEquals("Edge Up")) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    double[][] Config = new double[][]{
                            {1.0f/9, 1.0f/9, 1.0f/9},
                            {1.0f/9, 1.0f/9, 1.0f/9},
                            {1.0f/9, 1.0f/9, 1.0f/9}
                    };
                    applyBlur(captureImage.imageBitmap_MODEed, false, Config, 1, false);
                    while(isBussy){}
                    doGreyscale(captureImage.imageBitmap_MODEed, false, false);
                    while(isBussy){}
                    doEdgeDetection(captureImage.imageBitmap_MODEed, false, true);
                    while(isBussy){}
                }
            };
            new Thread(runnable).start();
        }
        if (name.contentEquals("Sharpen")) {
            applyMeanRemoval(captureImage.imageBitmap_MODEed, false, true);
        }
        if (name.contentEquals("Red")) {
            doColorFiltering(captureImage.imageBitmap_MODEed, false, 1   , 0.25, 0.25, true);
        }
        if (name.contentEquals("Green")) {
            doColorFiltering(captureImage.imageBitmap_MODEed, false, 0.25 , 1  , 0.25, true);
        }
        if (name.contentEquals("Blue")) {
            doColorFiltering(captureImage.imageBitmap_MODEed, false, 0.25, 0.25, 1   , true);
        }
        if (name.contentEquals("Brighter")) {
            doBrightness(captureImage.imageBitmap_MODEed, false,  20, true);
        }
        if (name.contentEquals("Darker")) {
            doBrightness(captureImage.imageBitmap_MODEed, false, -20, true);
        }
        if (name.contentEquals("Magic 1")) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    doRswapG(captureImage.imageBitmap_MODEed, false, false);
                    while(isBussy){}
                    doContrast(captureImage.imageBitmap_MODEed, false, 5, true);
                    while(isBussy){}
                }
            };
            new Thread(runnable).start();
        }
        if (name.contentEquals("Magic 2")) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    doGswapB(captureImage.imageBitmap_MODEed, false, false);
                    while(isBussy){}
                    doContrast(captureImage.imageBitmap_MODEed, false, 5, true);
                    while(isBussy){}
                }
            };
            new Thread(runnable).start();
        }
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

        progressTextView.setText("-");
        a.cancelAnimation(progressTextView);

        captureImage.imageBitmap_MODEed = onResult.copy(captureImage.imageBitmap_MODEed.getConfig(), true);
        mainImageDisplay_ORIGINAL.setImageBitmap(captureImage.imageBitmap_MODEed);
        a.alphaAnimation(0, 1, 1000, mainImageDisplay_ORIGINAL);

        isBussy = false;
    }

    public static void putOutFinalImage() {

        class LongOperation extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(Void... params) {

                return null;
            }

            @Override
            protected void onPostExecute(Bitmap onResult) {
                showToast(context.getString(R.string.imageSaved));
                isBussy_FINAL = false;
                captureImage.saveImageToFile(captureImage.imageBitmap_MODEed, "output");
            }

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onProgressUpdate(Void... values) {
            }

        }

        new LongOperation().execute();

    }

}
