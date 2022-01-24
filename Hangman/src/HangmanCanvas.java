/*
 * File: HangmanCanvas.java
 * ---------------------
 * This class holds the graphics elements to the Hangman game.
 * Author: Cobalt - M.Cabatuan
 * Date modified: 06/11/2019
 */


import acm.graphics.GCanvas;
import acm.graphics.GImage;

public class HangmanCanvas extends GCanvas {

    private static final int TEXT_HEIGHT = 20;   // you can modify this to suit your ascii art
    private static final int TEXT_X_OFFSET = 12;   // you can modify this to suit your ascii art
    private int textX = 10;
    private int textY;


    /**
     * Resets the display so that only the hangman scaffold appears */

    public void reset() {
        removeAll();
    }

    public void displayHangman(int guessCount) {
        reset();

        GImage hangman = new GImage("assets/life"+guessCount+".png");
        hangman.setSize(hangman.getWidth()/2, hangman.getHeight()/2);
        double centerX = (getWidth()-hangman.getWidth())/2;
        add(hangman,centerX,25);
    }
}
