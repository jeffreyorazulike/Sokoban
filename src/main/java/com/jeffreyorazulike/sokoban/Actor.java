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

import static com.jeffreyorazulike.sokoban.Sokoban.RESOURCES;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * <p>
 * Represents any object of the game</p>
 *
 * @author Jeffrey Orazulike <bit.ly/jeffreyorazulike>
 */
public class Actor implements Serializable {

    /**
     * The key for moving up
     */
    public static final int UP = KeyEvent.VK_W;
    public static final int[] UP_MOVE = {-1, 0};
    /**
     * The key for moving down
     */
    public static final int DOWN = KeyEvent.VK_S;
    public static final int[] DOWN_MOVE = {1, 0};
    /**
     * The key for moving left
     */
    public static final int LEFT = KeyEvent.VK_A;
    public static final int[] LEFT_MOVE = {0, -1};
    /**
     * The key for moving right
     */
    public static final int RIGHT = KeyEvent.VK_D;
    public static final int[] RIGHT_MOVE = {0, 1};

    /**
     * Represents a wall
     */
    public static final char WALL = '#';
    public static transient final ImageIcon WALL_IMAGE = new ImageIcon(RESOURCES
            + "wall.png");
    /**
     * Represents a baggage
     */
    public static final char BAGGAGE = '$';
    public static transient final ImageIcon BAGGAGE_IMAGE = new ImageIcon(RESOURCES
            + "baggage.png");
    /**
     * Represents the player
     */
    public static final char PLAYER = '@';
    public static transient final ImageIcon PLAYER_IMAGE = new ImageIcon(RESOURCES
            + "sokoban.png");
    /**
     * Represents a final area
     */
    public static final char AREA = '.';
    public static transient final ImageIcon AREA_IMAGE = new ImageIcon(RESOURCES
            + "area.png");

    /**
     * The size of the wall and area
     */
    public static final int SPACE = new ImageIcon(RESOURCES + "area.png").
            getIconHeight();

    /**
     * The position of the actor
     */
    private int row, column;
    /**
     * The position of the actor on the board
     */
    private int x, y;
    /**
     * Is the actor movable
     */
    private final boolean isMovable;
    /**
     * The image of the actor
     */
    private transient Image image;

    /**
     * The size of the actor
     */
    private int size;

    /**
     *
     * @param row     the row of the actor
     * @param column  the column of the actor
     * @param image   the actor's image
     * @param movable if the actor is movable
     */
    private Actor(int row, int column, ImageIcon image, boolean movable) {
        setPosition(row, column);

        if (image != null) {
            setImage(image.getImage());
            size = image.getIconWidth();
        }

        isMovable = movable;
    }

    /**
     *
     * @param actor the actor object to use and make the new actor object
     */
    public Actor(Actor actor) {
        setPosition(actor.getX(), actor.getY());
        image = actor.getImage();
        size = actor.size;
        isMovable = actor.isMovable();
    }

    private Actor() {
        this(0, 0, null, false);
    }

    /**
     *
     * @param row    the position of the actor on the board
     * @param column the position of the actor on the board
     */
    public final void setPosition(int row, int column) {
        setRow(row);
        setColumn(column);
    }

    /**
     * @return the row of the actor on the board
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row the row of the actor on the board
     */
    public void setRow(int row) {
        this.row = row;
        y = SPACE * row;
    }

    /**
     * @return the column of the actor on the board
     */
    public int getColumn() {
        return column;
    }

    /**
     * @param column the column of the actor on the board
     */
    public void setColumn(int column) {
        this.column = column;
        x = SPACE * column;
    }

    /**
     * @return the horizontal position of the actor
     */
    public int getX() {
        return x;
    }

    /**
     * @return the vertical position of the actor
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @param image the actor's image
     */
    public final void setImage(Image image) {
        this.image = image;
    }

    /**
     *
     * @return the actor's image
     */
    public Image getImage() {
        return image;
    }

    /**
     *
     * @return the size of the image
     */
    public final int getSize() {
        return size;
    }

    /**
     *
     * @return is the actor movable
     */
    public final boolean isMovable() {
        return isMovable;
    }

    public void draw(Graphics g, ImageObserver observer) {
        int distance = (SPACE - getSize()) / 2;
        g.drawImage(getImage(), getX() + distance, getY() + distance, observer);
    }

    @Override
    public String toString() {
        return String.format("%s is on (%d,%d)", getClass().
                getSimpleName(), getX(), getY());
    }

    /**
     *
     * @param level A string representing how the level should look like
     *
     * @return An object with the actors and the width and height of the play
     *         ground
     */
    static ActorsWithSize decodeLevel(String LEVEL) {
        String[] level = LEVEL.split("\n");

        Actor[][] actors = new Actor[level.length][]; // holds the actors
        ArrayList<Actor.Area> area = new ArrayList<>(level.length * 2); // holds the area

        int x, width = 0, height = 0;
        Actor actor;

        for (int actorRow = 0; actorRow < level.length; ++actorRow) {

            x = 0;
            actors[actorRow] = new Actor[level[actorRow].length()];

            for (int actorColumn = 0; actorColumn < actors[actorRow].length; ++actorColumn) {

                actor = getActor(level[actorRow].charAt(actorColumn), actorRow, actorColumn);

                if (actor instanceof Area)
                    area.add((Actor.Area) actor);
                else
                    actors[actorRow][actorColumn] = actor;

                x += SPACE;
            }

            if (x > width)
                width = x;

            height += SPACE;
        }

        return new ActorsWithSize(actors, area.toArray(new Area[area.size()]), width + SPACE, height + (SPACE * 2));
    }

    /**
     *
     * @param character The character representing the object
     * @param row       the position of the actor on the board
     * @param column    the position of the actor on the board
     *
     * @return The requested actor or null if the actor doesn't exist
     */
    public static Actor getActor(char character, int row, int column) {
        return switch (character) {
            case WALL ->
                new Actor().new Wall(row, column);
            case BAGGAGE ->
                new Actor().new Baggage(row, column);
            case PLAYER ->
                new Actor().new Player(row, column);
            case AREA ->
                new Actor().new Area(row, column);
            default ->
                null;
        };
    }

    /**
     * @param AREA Where to check the actor collided, i.e. LEFT, RIGHT, UP, DOWN
     *
     * @return The move type for the received key code
     */
    public static int[] move(final int AREA) {

        switch (AREA) {
            case UP -> {
                return UP_MOVE;
            }
            case DOWN -> {
                return DOWN_MOVE;
            }
            case LEFT -> {
                return LEFT_MOVE;
            }
            case RIGHT -> {
                return RIGHT_MOVE;
            }
        }
        return null;
    }

    /**
     *
     * @param actors the placeholder for actors
     * @param actor  the actor to move
     * @param move   the amount of rows and columns to move the actor
     *
     * @throws SokobanException               if one of the parameters was null
     *                                        or one of the moves values didn't
     *                                        have 1 or the number of values in
     *                                        the move array is less than 2
     * @throws ArrayIndexOutOfBoundsException if the moves makes the actor fall
     *                                        outside the board
     */
    public static boolean move(Actor[][] actors, Actor actor, int[] move) throws
            SokobanException, ArrayIndexOutOfBoundsException {

        /**
         * if an invalid move was passed or the move passed is not adequate or
         * the actor is not movable or the actor or actors is not valid or the
         * one of the values of the moves is not one then throw a sokoban
         * exception
         */
        if (move == null || move.length < 2 || actor == null || !actor.isMovable() || actors == null || !(Math.abs(move[0]) == 1 || Math.abs(move[1]) == 1))
            throw new SokobanException("Something went wrong while trying to move the actor, maybe the actor is not movable");

        int row = actor.getRow(), column = actor.getColumn();

        // exit the method if the actor is going to collide with a wall
        if (actors[row + move[0]][column + move[1]] instanceof Wall)
            return false;

        if (actor instanceof Baggage) {
            // exit the method if a baggage is colliding with another baggage
            if (actors[row + move[0]][column + move[1]] instanceof Baggage)
                return false;

            Actor temp = actors[row + move[0]][column + move[1]];
            actors[row + move[0]][column + move[1]] = actor;
            actor.setPosition(row + move[0], column + move[1]);
            actors[row][column] = temp;
            return true;
        }

        if (actor instanceof Player) {
            if (actors[row + move[0]][column + move[1]] instanceof Baggage)
                // move the baggage that is hit by the player but exit the method if the baggage hits another
                if (!move(actors, actors[row + move[0]][column + move[1]], move))
                    return false;

            Actor temp = actors[row + move[0]][column + move[1]];
            actors[row + move[0]][column + move[1]] = actor;
            actor.setPosition(row + move[0], column + move[1]);
            actors[row][column] = temp;
        }

        return true;
    }

    /**
     * Represents the place where the baggage should be dropped
     */
    public final class Area extends Actor {

        public Area(int row, int column) {
            super(row, column, AREA_IMAGE, false);
        }
    }

    /**
     * Represents the baggage to be moved by the player
     */
    public final class Baggage extends Actor {

        public Baggage(int row, int column) {
            super(row, column, BAGGAGE_IMAGE, true);
        }
    }

    /**
     * Represents the player of the game
     */
    public final class Player extends Actor {

        public Player(int row, int column) {
            super(row, column, PLAYER_IMAGE, true);
        }

        public void move(Actor[][] actors, int keyCode) throws SokobanException, ArrayIndexOutOfBoundsException {
            int[] move = Actor.move(keyCode);

            if (move != null)
                Actor.move(actors, this, move);
        }

    }

    /**
     * Represents a wall
     */
    public final class Wall extends Actor {

        public Wall(int row, int column) {
            super(row, column, WALL_IMAGE, false);
        }
    }

}

/**
 * An class containing the list of actors and the size of the board after the
 * level has been decoded
 */
class ActorsWithSize {

    Actor[][] actors;
    Actor.Area[] area;
    int width, height;

    public ActorsWithSize(Actor[][] actors, Actor.Area[] area, int width, int height) {
        this.actors = actors;
        this.area = area;
        this.width = width;
        this.height = height;
    }

}
