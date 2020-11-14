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

import com.jeffreyorazulike.sokoban.interfaces.SokobanIO;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 *
 * @author Jeffrey Orazulike
 */
public class AboutController extends Controller implements SokobanIO {

    @FXML
    private TextArea licenseText;

    @Override
    public void initialize(Stage stage) {
        licenseText.setText((String) loadTextFile(new File("LICENSE")));
    }

    @Override
    public void loadToRoot() {

    }

    @Override
    public void exit() {

    }
}
