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
package com.jeffreyorazulike.sokoban.model;

import com.jeffreyorazulike.sokoban.Actor;
import static com.jeffreyorazulike.sokoban.SokobanConstants.*;
import com.jeffreyorazulike.sokoban.interfaces.Model;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Jeffrey Orazulike
 */
public class LevelBuilderModel implements Model, Serializable {

    private Actor[][] actors;

    public LevelBuilderModel(int row, int column) {
        actors = new Actor[row][column];

        for (int i = 0; i < row; ++i)
            for (int j = 0; j < column; ++j)
                actors[i][j] = Actor.getActor(WALL, i, j, null);
    }

    public LevelBuilderModel(Actor[][] actors) {
        this.actors = actors;
    }

    @Override
    public Actor[][] getActors() {
        return actors;
    }

    public boolean update(int row, int column) {
        if (actors.length == row && actors[0].length == column)
            return false;

        Actor[][] update = new Actor[row][];

        for (int updateRow = 0, formerColumn; updateRow < row && updateRow < actors.length; ++updateRow) {
            formerColumn = actors[updateRow].length - 1;
            update[updateRow] = Arrays.copyOf(actors[updateRow], column);

            for (; formerColumn < column; ++formerColumn)
                update[updateRow][formerColumn] = Actor.getActor(WALL, updateRow, formerColumn, (r, c) -> new Actor.Empty(r, c));
        }

        for (int updateRow = actors.length - 1; updateRow < row; ++updateRow) {

            update[updateRow] = new Actor[column];
            for (int updateColumn = 0; updateColumn < column; ++updateColumn)
                update[updateRow][updateColumn] = Actor.getActor(WALL, updateRow, updateColumn, (r, c) -> new Actor.Empty(r, c));
        }

        actors = update;

        return true;
    }

}
