/*
 * Copyright (C) 2020 Jeffrey Orazulike
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

import com.jeffreyorazulike.sokoban.model.GameModel;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

/**
 *
 * @author Jeffrey Orazulike
 */
public abstract class SokobanConstants {

    /**
     * How the game's world is arranged
     */
    public static final String DEFAULT_LEVEL
            = "    ######\n"
            + "    ##   #\n"
            + "    ##$  #\n"
            + "  ####  $##\n"
            + "  ##  $ $ #\n"
            + "#### # ## #   ######\n"
            + "##   # ## #####  ..#\n"
            + "## $  $          ..#\n"
            + "###### ### #@##  ..#\n"
            + "    ##     #########\n"
            + "    ########\n";

    /**
     * The name of the game
     */
    public static final String GAME_NAME = "Sokoban";

    /**
     * The name of the level builder
     */
    public static final String LEVEL_BUILDER = "Level Builder";

    /**
     * The extension of the game save file
     */
    public static final String GAME_EXTENSION = ".sokobansave";

    /**
     * The extension of the level save file
     */
    public static final String LEVEL_BUILDER_EXTENSION = ".sokobanlevel";

    /**
     * The key for moving up
     */
    public static final KeyCode UP = KeyCode.W;
    public static final int[] UP_MOVE = {-1, 0};

    /**
     * The key for moving down
     */
    public static final KeyCode DOWN = KeyCode.S;
    public static final int[] DOWN_MOVE = {1, 0};

    /**
     * The key for moving left
     */
    public static final KeyCode LEFT = KeyCode.A;
    public static final int[] LEFT_MOVE = {0, -1};

    /**
     * The key for moving right
     */
    public static final KeyCode RIGHT = KeyCode.D;
    public static final int[] RIGHT_MOVE = {0, 1};

    /**
     * Represents a wall
     */
    public static final char WALL = '#';
    public static final transient Image WALL_IMAGE = new Image(GameModel.class.getResource("/wall.png").toString());

    /**
     * Represents a baggage
     */
    public static final char BAGGAGE = '$';
    public static final transient Image BAGGAGE_IMAGE = new Image(GameModel.class.getResource("/baggage.png").toString());

    /**
     * Represents the player
     */
    public static final char PLAYER = '@';
    public static final transient Image PLAYER_IMAGE = new Image(GameModel.class.getResource("/sokoban.png").toString());

    /**
     * Represents a final area
     */
    public static final char AREA = '.';
    public static final transient Image AREA_IMAGE = new Image(GameModel.class.getResource("/area.png").toString());

    /*
     * Represents an empty area
     */
    public static final char EMPTY = ' ';

    /**
     * The size of the wall and area
     */
    public static final int SPACE = (int) AREA_IMAGE.getWidth();

}
