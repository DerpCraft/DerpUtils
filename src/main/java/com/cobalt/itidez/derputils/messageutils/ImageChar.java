/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.messageutils;

/**
 *
 * @author iTidez
 */
public enum ImageChar {
    BLOCK('\u2588'),
    DARK_SHADE('\u2593'),
    MEDIUM_SHADE('\u2592'),
    LIGHT_SHADE('\u2591');
    private char c;

    ImageChar(char c) {
        this.c = c;
    }

    public char getChar() {
        return c;
    }
}
