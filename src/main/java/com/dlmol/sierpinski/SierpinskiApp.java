package com.dlmol.sierpinski;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

public class SierpinskiApp {

    private static final String EXTN = "png";
    private static final Color[] COLORS =
//            {Color.PINK, Color.CYAN, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.ORANGE, Color.WHITE};
            {Color.RED, Color.PINK, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.WHITE};

    public static void main(String[] args) {
        long startMs = System.currentTimeMillis();
        final String imagesDirName = "sierpinski_images";
        processImagesDir(imagesDirName);
        int maxIterations = 6;
        IntStream.rangeClosed(1, maxIterations).forEach(maxI -> {
            String filenameBase = imagesDirName + "/sierpinski_carpet_" + maxI;
            String filename = filenameBase + "." + EXTN;
            int size = (int) Math.pow(9, 3); // Size of the image (must be a power of 3)

            BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, size, size);
            g.setColor(Color.WHITE);
//            drawCarpet(g, 0, 0, size, maxI);
            drawCarpetReverse(g, 0, 0, size, maxI);
            g.dispose();

            try {
                final File file = new File(filename);
                System.out.println("Writing to file: " + file.getAbsolutePath());
                ImageIO.write(image, EXTN, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println("Done after " + (System.currentTimeMillis() - startMs) + " ms");
    }

    private static void processImagesDir(String imagesDirName) {
        final File imagesDirFile = new File(imagesDirName);
        if (!imagesDirFile.exists()) {
            if (!imagesDirFile.mkdirs()) {
                System.err.println("Failed to create directory: " + imagesDirFile.getAbsolutePath());
                System.exit(1);
            }
        }
    }
    private static void drawCarpet(Graphics2D g, int startX, int startY, int size, int maxIterations) {
        int newSize = size / 3;
        int i = 0;
        while (newSize >= 1 && i++ < maxIterations) {
            for (int x = 0; x < size; x += newSize) {
                for (int y = 0; y < size; y += newSize) {
                    if ((x / newSize) % 3 == 1 && (y / newSize) % 3 == 1) {
                        g.fillRect(startX + x, startY + y, newSize, newSize);
                    }
                }
            }
            newSize /= 3;
        }
    }

    private static void drawCarpetReverse(Graphics2D g, int startX, int startY, int size, int maxIterations) {
        int newSize = 1;
        int i = maxIterations;
        while (newSize <= size && i-- > 0) {
            for (int x = 0; x < size; x += newSize) {
                for (int y = 0; y < size; y += newSize) {
                    if ((x / newSize) % 3 == 1 && (y / newSize) % 3 == 1) {
                        g.setColor(COLORS[Math.min(i, COLORS.length - 1)]);
                        g.fillRect(startX + x, startY + y, newSize, newSize);
                    }
                }
            }
            newSize *= 3;
        }
    }
}