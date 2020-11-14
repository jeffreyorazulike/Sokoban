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

import static com.jeffreyorazulike.sokoban.SokobanConstants.*;
import com.jeffreyorazulike.sokoban.model.GameModel;
import java.io.Serializable;
import java.util.function.BiFunction;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * <p>
 * Represents any object of the game</p>
 *
 * @author Jeffrey Orazulike <bit.ly/jeffreyorazulike>
 */
public class Actor extends Group implements Serializable {

    // The row and column of the actor in a grid/array
    private int row, column;

    // should the actor be able to move
    private final boolean isMovable;

    // the actor's image
    private transient Image image;

    private Actor(int row, int column, Image image, boolean movable) {
        setRow(row);
        setColumn(column);

        maxWidth(SPACE);
        maxHeight(SPACE);

        if (image != null)
            setImage(image);

        isMovable = movable;
    }

    public final void setRow(int row) {
        this.row = row;
        setLayoutY(row * SPACE);
    }

    public final int getRow() {
        return row;
    }

    public final void setColumn(int column) {
        this.column = column;
        setLayoutX(column * SPACE);
    }

    public final int getColumn() {
        return column;
    }

    public final void setImage(Image image) {
        this.image = image;
        ImageView imageView = new ImageView(image);

        getChildren().remove(0, getChildren().size());
        getChildren().add(imageView);

        setRow(row);
        setColumn(column);

        imageView.setX((SPACE - image.getWidth()) / 2);
        imageView.setY((SPACE - image.getHeight()) / 2);
    }

    public Image getImage() {
        return image;
    }

    public final boolean isMovable() {
        return isMovable;
    }

    /**
     *
     * @param model   the placeholder for the game data
     * @param keyCode The type of moveType the actor is to perform
     *
     */
    public void move(GameModel model, KeyCode keyCode) {
        if (!isMovable() || keyCode == null)
            return;

        int[] move = Actor.moveType(keyCode);

        if (move != null) {
            GameModel.Undo undo = new GameModel.Undo();

            if (move(model, undo, this, move))
                model.getUndo().push(undo);
        }
    }

    public void move(GameModel model, int[] move) {
        if (!isMovable())
            return;

        int movedRow = getRow() + move[0], movedColumn = getColumn() + move[1];

        Actor temp = model.getActors()[movedRow][movedColumn];
        model.getActors()[movedRow][movedColumn] = this;
        model.getActors()[getRow()][getColumn()] = temp;
        setRow(movedRow);
        setColumn(movedColumn);
    }

    private boolean move(GameModel model, GameModel.Undo undo, Actor actor, int[] move) {
        int thisRow = actor.getRow(), thisColumn = actor.getColumn();
        int movedRow = thisRow + move[0], movedColumn = thisColumn + move[1];

        // exit the method if the actor is going to collide with a wall
        if (model.getActors()[movedRow][movedColumn] instanceof Wall)
            return false;

        if (actor instanceof Baggage) {
            // exit the method if a baggage is colliding with another baggage
            if (model.getActors()[movedRow][movedColumn] instanceof Baggage)
                return false;

            actor.move(model, move);

            undo.baggage = new int[]{actor.getRow(), actor.getColumn()};

            return true;
        }

        if (actor instanceof Player) {
            if (model.getActors()[movedRow][movedColumn] instanceof Baggage) // moveType the baggage that is hit by the player but exit the method if the baggage hits another

                if (!move(model, undo, model.getActors()[movedRow][movedColumn], move))
                    return false;

            actor.move(model, move);

            undo.player = new int[]{actor.getRow(), actor.getColumn()};
            undo.setMove(move);
        }

        return true;
    }

    /**
     * @param AREA Where to check the actor collided, i.e. LEFT, RIGHT, UP, DOWN
     *
     * @return The moveType type for the received key code
     */
    public static int[] moveType(final KeyCode AREA) {

        switch (AREA) {
            case W:
                return UP_MOVE;
            case S:
                return DOWN_MOVE;
            case A:
                return LEFT_MOVE;
            case D:
                return RIGHT_MOVE;
            default:
                return null;
        }
    }

    /**
     *
     * @param character   the character representing the actor
     * @param row         the position of the actor on the board
     * @param column      the position of the actor on the board
     * @param returnEmpty the value to be returned when a non recognized or
     *                    empty actor is reached
     *
     * @return the requested actor
     */
    public static Actor getActor(char character, int row, int column, BiFunction<Integer, Integer, Empty> returnEmpty) {
        switch (character) {
            case WALL:
                return new Actor.Wall(row, column);
            case BAGGAGE:
                return new Actor.Baggage(row, column);
            case PLAYER:
                return new Actor.Player(row, column);
            case AREA:
                return new Actor.Area(row, column);
            default:
                return returnEmpty.apply(row, column);
        }
    }

    /*
     *
     */
    /**
     *
     * @param image  the image representing the actor
     * @param row    the position of the actor on the board
     * @param column the position of the actor on the board
     *
     *
     * @return the requested actor
     */
    public static Actor getActor(Image image, int row, int column) {
        if (image == WALL_IMAGE)
            return getActor(WALL, row, column, (r, c) -> null);
        else if (image == BAGGAGE_IMAGE)
            return getActor(BAGGAGE, row, column, (r, c) -> null);
        else if (image == AREA_IMAGE)
            return getActor(AREA, row, column, (r, c) -> null);
        else if (image == PLAYER_IMAGE)
            return getActor(PLAYER, row, column, (r, c) -> null);
        else
            return null;
    }

    /**
     *
     * @param actor the actor to check
     *
     * @return the character of the actor
     */
    public static char getCharacter(Actor actor) {
        if (actor instanceof Wall)
            return WALL;
        else if (actor instanceof Baggage)
            return BAGGAGE;
        else if (actor instanceof Player)
            return PLAYER;
        else if (actor instanceof Area)
            return AREA;
        else
            return EMPTY;
    }

    @Override
    public String toString() {
        return String.valueOf(getClass().getSimpleName()).toLowerCase();
    }

    /**
     * Represents the place where the baggage should be dropped
     */
    public static final class Area extends Actor {

        public Area(int row, int column) {
            super(row, column, AREA_IMAGE, false);
        }
    }

    /**
     * Represents the baggage to be moved by the player
     */
    public static final class Baggage extends Actor {

        public Baggage(int row, int column) {
            super(row, column, BAGGAGE_IMAGE, true);
        }
    }

    /**
     * Represents the player of the game
     */
    public static final class Player extends Actor {

        public Player(int row, int column) {
            super(row, column, PLAYER_IMAGE, true);
        }
    }

    /**
     * Represents a wall
     */
    public static final class Wall extends Actor {

        public Wall(int row, int column) {
            super(row, column, WALL_IMAGE, false);
        }
    }

    /**
     * Represents an empty area, used only during level building
     */
    public static final class Empty extends Actor {

        public Empty(int row, int column) {
            super(row, column, null, false);
            getChildren().add(new Rectangle(SPACE, SPACE, Color.TRANSPARENT));
        }
    }

}
