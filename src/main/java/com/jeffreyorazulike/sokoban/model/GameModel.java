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
import com.jeffreyorazulike.sokoban.interfaces.Model;
import java.io.Serializable;
import java.util.ArrayDeque;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Jeffrey Orazulike
 */
public class GameModel implements Serializable, Model {

    private transient FixedStack<Undo> undo;

    private final Actor[][] actors;
    private final Actor.Area[] area;
    private final String level;

    public GameModel(Actor[][] actors, Actor.Area[] area, String level) {
        this.actors = actors;
        this.area = area;
        this.level = level;
    }

    @Override
    public Actor[][] getActors() {
        return actors;
    }

    public Actor.Area[] getArea() {
        return area;
    }

    public String getLevel() {
        return level;
    }

    public FixedStack<Undo> getUndo() {
        if (undo == null)
            undo = new FixedStack<>();

        return undo;
    }

    public static final class Undo {

        public int[] baggage;
        public int[] player;
        private int[] move;

        public void setMove(int[] move) {
            this.move = new int[2];
            this.move[0] = move[0] * -1;
            this.move[1] = move[1] * -1;
        }

        public int[] getMove() {
            return move;
        }
    }

    public static final class FixedStack<T> {

        private final int CAPACITY;
        private IntegerProperty numberOfElements;
        private ArrayDeque<T> deque;

        public FixedStack() {
            this(10);
        }

        public FixedStack(final int capacity) {
            numberOfElements = new SimpleIntegerProperty();
            CAPACITY = capacity;
            deque = new ArrayDeque<>(CAPACITY);
        }

        public void push(T element) {
            if (numberOfElements.get() >= CAPACITY) {
                deque.pollLast();
                numberOfElements.set(numberOfElements.get() - 1);
            }
            deque.push(element);
            numberOfElements.set(numberOfElements.get() + 1);
        }

        public T pop() {
            numberOfElements.set(numberOfElements.get() - 1);
            return deque.poll();
        }

        public ReadOnlyIntegerProperty numberOfElementsProperty() {
            return numberOfElements;
        }

        public final int getCapacity() {
            return CAPACITY;
        }
    }
}
