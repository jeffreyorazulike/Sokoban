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
package com.jeffreyorazulike.sokoban.interfaces;

import com.jeffreyorazulike.sokoban.Actor;
import static com.jeffreyorazulike.sokoban.Actor.getActor;
import static com.jeffreyorazulike.sokoban.Actor.getCharacter;
import com.jeffreyorazulike.sokoban.model.GameModel;
import com.jeffreyorazulike.sokoban.model.LevelBuilderModel;
import java.util.ArrayList;

/**
 *
 * @author Jeffrey Orazulike
 */
public interface LevelWorker {

    public enum ModelType {
        GAME, BUILDER
    }

    /**
     *
     * @param LEVEL     A string representing how the level should look like
     * @param modelType the type of model to be returned
     *
     * @return A sokoban model
     */
    default Model decodeLevel(final String LEVEL, ModelType modelType) {
        String[] level = LEVEL.split("\n");

        Actor[][] actors = new Actor[level.length][]; // holds the actors
        ArrayList<Actor.Area> area = new ArrayList<>(level.length * 2); // holds the area

        Actor actor;

        for (int actorRow = 0; actorRow < level.length; ++actorRow) {

            actors[actorRow] = new Actor[level[actorRow].length()];

            for (int actorColumn = 0; actorColumn < actors[actorRow].length; ++actorColumn) {

                if (modelType == ModelType.GAME)
                    actor = getActor(level[actorRow].charAt(actorColumn), actorRow, actorColumn, (r, c) -> null);
                else
                    actor = getActor(level[actorRow].charAt(actorColumn), actorRow, actorColumn, (r, c) -> new Actor.Empty(r, c));

                if (actor instanceof Actor.Area && modelType == ModelType.GAME)
                    area.add((Actor.Area) actor);
                else
                    actors[actorRow][actorColumn] = actor;
            }
        }

        if (modelType == ModelType.GAME)
            return new GameModel(actors, area.toArray(new Actor.Area[area.size()]), LEVEL);
        else
            return new LevelBuilderModel(actors);
    }

    default String encodeLevel(Actor[][] actors) {
        if (actors == null)
            return null;

        StringBuilder level = new StringBuilder(actors.length);

        for (int row = 0; row < actors.length; ++row) {
            for (int column = 0; column < actors[row].length; ++column)
                level.append(getCharacter(actors[row][column]));
            level.append("\n");
        }

        return level.toString();
    }

}
