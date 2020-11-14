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
import static com.jeffreyorazulike.sokoban.SokobanConstants.AREA_IMAGE;
import static com.jeffreyorazulike.sokoban.SokobanConstants.BAGGAGE_IMAGE;
import static com.jeffreyorazulike.sokoban.SokobanConstants.PLAYER_IMAGE;
import static com.jeffreyorazulike.sokoban.SokobanConstants.WALL_IMAGE;
import com.jeffreyorazulike.sokoban.view.MessageBox;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 * @author Jeffrey Orazulike
 */
public interface ActorWorker {

    private static boolean checkLevel(Actor[][] actors) {
        // remove all null actors from the array
        List<List<Actor>> nonNullActors = Arrays.stream(actors).map(actorsRow -> Arrays.stream(actorsRow).filter(actor -> !(actor instanceof Actor.Empty)).collect(Collectors.toList())).collect(Collectors.toList());

        int player = 0, baggage = 0, area = 0;
        for (int row = 0; row < nonNullActors.size(); ++row) {
            List<Actor> actorsRow = nonNullActors.get(row);
            for (int column = 0; column < actorsRow.size(); ++column) {
                Actor actor = actorsRow.get(column);

                if ((column == 0 || column == actorsRow.size() - 1) && !(actor instanceof Actor.Wall))
                    return false;
                else if (actor instanceof Actor.Player)
                    ++player;
                else if (actor instanceof Actor.Baggage)
                    ++baggage;
                else if (actor instanceof Actor.Area)
                    ++area;
            }

            System.err.println();
        }

        return !(baggage != area || baggage == 0 || player != 1);
    }

    default boolean correctLevelStructure(Actor[][] actors) {
        if (checkLevel(actors))
            return true;
        else
            MessageBox.showDialog("The level is not built well");
        return false;
    }

    default Actor[] inflateActors(Actor[] actors, Optional<Consumer<Actor>> process) {
        Arrays.stream(actors).forEach(act -> {
            Optional.ofNullable(act).ifPresent(actor -> {
                if (actor instanceof Actor.Wall)
                    actor.setImage(WALL_IMAGE);
                else if (actor instanceof Actor.Baggage)
                    actor.setImage(BAGGAGE_IMAGE);
                else if (actor instanceof Actor.Player)
                    actor.setImage(PLAYER_IMAGE);
                else if (actor instanceof Actor.Area)
                    actor.setImage(AREA_IMAGE);
                process.ifPresent(action -> action.accept(act));
            });
        });
        return actors;
    }

}
