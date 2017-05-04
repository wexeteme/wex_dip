package com.wex.weffecto.main.pimage;

/**
 * Created by welle on 2/26/2017.
 * ConvolutionMatrix
 */

import android.graphics.Bitmap;
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

    public static Bitmap computeConvolution3x3(Bitmap src, ConvolutionMatrix matrix) {

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

        /*
        int pixelsAll[] = new int[width * height];
        src.getPixels(pixelsAll, 0, width, 1, 1, width - 1, height - 1);
        for (int i = 0; i < pixels.length; i++) {

        }
        result.setPixels(pixelsAll, 0, width, 1, 1, width - 1, height - 1);
        */

        // gi izminuvame site pikseli so toa sto ostavame 2 - oni na krajot nema so sto da se sporeduvaat!

        int pixels[] = new int[width * height];
        src.getPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

        for (int h = 0; h < height - 2; ++h) {
            for (int w = 0; w < width - 2; ++w) {

                for (int i = 0; i < SIZE; ++i) {
                    for (int j = 0; j < SIZE; ++j) {
                        int index = (w + i + j) + (h)*width;
                        pixelsMatrix[i][j] = pixels[index];
                    }
                }

                // get alpha of center pixel
                A = Color.alpha(pixelsMatrix[1][1]);

                // init color sum
                sumR = sumG = sumB = 0;

                // get sum of RGB on matrix
                for (int i = 0; i < SIZE; ++i) {
                    for (int j = 0; j < SIZE; ++j) {
                        sumR += (Color.red  (pixelsMatrix[i][j]) * matrix.Matrix[i][j]);
                        sumG += (Color.green(pixelsMatrix[i][j]) * matrix.Matrix[i][j]);
                        sumB += (Color.blue (pixelsMatrix[i][j]) * matrix.Matrix[i][j]);
                    }
                }

                // get final Red
                R = (int) (sumR / matrix.Factor + matrix.Offset);
                if (R < 0) {
                    R = 0;
                } else if (R > 255) {
                    R = 255;
                }

                // get final Green
                G = (int) (sumG / matrix.Factor + matrix.Offset);
                if (G < 0) {
                    G = 0;
                } else if (G > 255) {
                    G = 255;
                }

                // get final Blue
                B = (int) (sumB / matrix.Factor + matrix.Offset);
                if (B < 0) {
                    B = 0;
                } else if (B > 255) {
                    B = 255;
                }

                // apply new pixel
                pixels[(w) + (h)*width] = Color.argb(A, R, G, B);

            }
        }

        result.setPixels(pixels, 0, width, 1, 1, width - 1, height - 1);

        // final image
        return result;
    }
}