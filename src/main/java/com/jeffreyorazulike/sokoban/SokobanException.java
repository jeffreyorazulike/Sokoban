/*
 * Copyright (C) 2020 Jeffrey Orazulike <bit.ly/jeffreyorazulike>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeffreyorazulike.sokoban;

/**
 * An exception thrown by a method if something is wrong while preparing the
 * game
 *
 * @author Jeffrey Orazulike <bit.ly/jeffreyorazulike>
 */
public class SokobanException extends Exception {

    /**
     * The id used when serializing the class
     */
    private static final long serialVersionUID = -6931529667201729698L;

    /**
     * The default exception message
     */
    private static final String DEFAULT_MESSAGE = "Something is wrong with the game";

    public SokobanException(String message) {
        super(message);
    }

    public SokobanException() {
        this(DEFAULT_MESSAGE);
    }

}
