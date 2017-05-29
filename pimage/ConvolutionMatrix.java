package com.wex.weffecto.main.pimage;

/**
 * Created by welle on 2/26/2017.
 * ConvolutionMatrix
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

public class ConvolutionMatrix {

    private static String tag = "ConvolutionMatrix debug";

    public static final int SIZE = 3;

    public double[][] Matrix;
    public double Factor = 1;
    public double Offset = 1;

    public ConvolutionMatrix(int size) {
        Matrix = new double[size][size];
    }

    public void setAll(double value) {
        for (int x = 0; x < SIZE; ++x) {
            for (int y = 0; y < SIZE; ++y) {
                Matrix[x][y] = value;
            }
        }
    }

    public void applyConfig(double[][] config) {
        for (int x = 0; x < SIZE; ++x) {
            for (int y = 0; y < SIZE; ++y) {
                Matrix[x][y] = config[x][y];
            }
        }
    }

    public static Bitmap computeConvolution3x3(Bitmap src, ConvolutionMatrix matrix, final boolean GrayScale, int threshold) {

        // zemi golemini na slika
        int width = src.getWidth();
        int height = src.getHeight();

        // inicializiaziraj resultat novata slika - produktot
        Bitmap result = Bitmap.createBitmap(width, height, src.getConfig());

        // init na promenlivi
        int A, R, G, B;

        // sumi na boite od matriciti koga gi mnozime/sobirame
        int sumR, sumG, sumB;

        // matrica koja zema 3x3 piksela od slika za mnozenje
        int[][] pixelsMatrix = new int[SIZE][SIZE];

        // gi izminuvame site pikseli so toa sto ostavame 2 - oni na krajot nema so sto da se sporeduvaat!

        int pixels[] = new int[width * height];
        src.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

        for (int h = 0; h < height; ++h) {
            for (int w = 0; w < width; ++w) {

                // epa, ovde ke go sredimo slucajot koj doaga do rab na slikata

                for (int i = 0; i < SIZE; ++i) {
                    for (int j = 0; j < SIZE; ++j) {
                        int index = (w + i + j) + (h) * width;
                        try {
                            pixelsMatrix[i][j] = pixels[index];
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                            pixelsMatrix[i][j] = Color.WHITE;
                        }
                    }
                }

                // ja zemame alpha od centralniot pixel - odnosno momentalniot
                A = Color.alpha(pixelsMatrix[1][1]);

                // init color sum
                sumR = sumG = sumB = 0;

                if (GrayScale) {

                    // se koristie za edge detection

                    // get sum of RGB on matrix
                    for (int i = 0; i < SIZE; ++i) {
                        for (int j = 0; j < SIZE; ++j) {
                            sumR += (Color.red(pixelsMatrix[i][j]) * matrix.Matrix[i][j]);
                        }
                    }

                    // get final Red
                    R = (int) (sumR / matrix.Factor);

                    if (R < 0) {
                        R = 0;
                    } else if (R > 255) {
                        R = 255;
                    }

                    R = R;
                    G = R;
                    B = R;

                } else {
                    // get sum of RGB on matrix
                    for (int i = 0; i < SIZE; ++i) {
                        for (int j = 0; j < SIZE; ++j) {
                            sumR += (Color.red(pixelsMatrix[i][j]) * matrix.Matrix[i][j]);
                            sumG += (Color.green(pixelsMatrix[i][j]) * matrix.Matrix[i][j]);
                            sumB += (Color.blue(pixelsMatrix[i][j]) * matrix.Matrix[i][j]);
                        }
                    }

                    // get final Red
                    R = (int) (sumR / matrix.Factor);
                    if (R < 0) {
                        R = 0;
                    } else if (R > 255) {
                        R = 255;
                    }

                    // get final Green
                    G = (int) (sumG / matrix.Factor);
                    if (G < 0) {
                        G = 0;
                    } else if (G > 255) {
                        G = 255;
                    }

                    // get final Blue
                    B = (int) (sumB / matrix.Factor);
                    if (B < 0) {
                        B = 0;
                    } else if (B > 255) {
                        B = 255;
                    }

                }

                // apply new pixel
                pixels[(w) + (h) * width] = Color.argb(A, R, G, B);
            }

        }

        result.setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

        // crop out 3 pixels - gi secem ovie zatoa sto pri konvolucija 3x3 nemame so sto da pravime
        // konvolucija so poslednite pikseli
        result = Bitmap.createBitmap(result, 0, 0, result.getWidth()-5, result.getHeight());

        // final image
        return result;
    }
}