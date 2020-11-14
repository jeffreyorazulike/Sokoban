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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.Optional;
import java.util.function.Consumer;

/**
 *
 * @author Jeffrey Orazulike
 */
public interface SokobanIO {

    default void save(File file, Object toSave, Optional<Consumer<File>> callback) {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(toSave);
            callback.ifPresent(action -> action.accept(file));
        } catch (IOException iOException) {
            //System.err.println(iOException.getMessage());
        }
    }

    default Object load(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            return input.readObject();
        }
    }

    default String loadTextFile(File file) {
        try {
            return Files.lines(file.toPath()).reduce("", (result, line) -> result.concat(line
            ).concat("\n"));
        } catch (IOException exception) {
            //System.err.println(exception.getMessage());
        }

        return "";
    }

}
