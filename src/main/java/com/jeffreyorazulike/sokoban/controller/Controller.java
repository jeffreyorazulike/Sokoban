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
package com.jeffreyorazulike.sokoban.controller;

import com.jeffreyorazulike.sokoban.interfaces.SokobanUI;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

/**
 *
 * @author Jeffrey Orazulike
 */
public abstract class Controller implements SokobanUI {

    private Parent root;

    protected Stage stage;

    protected MenuBar menuBar;

    @Override
    public Parent getRoot() {
        return root;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public MenuBar getMenuBar() {
        return menuBar;
    }

    public static Controller createController(String path) throws IOException {
        return createController(path, Optional.empty());
    }

    public static Controller createController(String path, Optional<Consumer<Controller>> perform) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Controller.class.getResource(path));
        Parent root = fxmlLoader.load();

        Controller controller = fxmlLoader.getController();
        controller.root = root;

        perform.ifPresent(action -> action.accept(controller));

        return controller;
    }
}
