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

import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

/**
 *
 * @author Jeffrey Orazulike
 */
public interface SokobanUI {

    public MenuBar getMenuBar();

    public Stage getStage();

    public Parent getRoot();

    /*
     * <p>Make sure to define the values of the stage, root and menu bar in this
     * method</p>
     */
    public void initialize(Stage stage);

    public void loadToRoot();

    public void exit();
}
