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

import com.jeffreyorazulike.sokoban.SokobanConstants;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author Jeffrey Orazulike
 */
public class SokobanController extends Controller {

    @FXML
    private Label label;

    private Optional<Controller> gameController = Optional.empty();
    private Optional<Controller> levelBuilderController = Optional.empty();
    private Optional<Controller> aboutController = Optional.empty();

    private Controller getView(String path) {
        try {
            Controller controller = Controller.createController(path);
            controller.initialize(getStage());
            controller.loadToRoot();
            return controller;
        } catch (IOException ex) {
        }
        return null;
    }

    @FXML
    private BorderPane mainPane;

    @FXML
    public void play(ActionEvent event) {
        gameController.ifPresentOrElse(controller -> {
            changeView(controller);
            ((BorderPane) controller.getRoot()).getCenter().requestFocus();
        }, () -> {
            gameController = Optional.ofNullable(getView("/ui/Game.fxml"));
            play(event);
        });
    }

    @FXML
    public void buildLevel(ActionEvent event) {
        levelBuilderController.ifPresentOrElse(controller -> {
            changeView(controller);
        }, () -> {
            levelBuilderController = Optional.ofNullable(getView("/ui/LevelBuilder.fxml"));
            buildLevel(event);
        });
    }

    @FXML
    public void about(ActionEvent event) {
        aboutController.ifPresentOrElse(controller -> {
            changeView(controller);
        }, () -> {
            aboutController = Optional.ofNullable(getView("/ui/About.fxml"));
            about(event);
        });
    }

    @FXML
    public void exit(ActionEvent event) {
        exit();
        Platform.exit();
    }

    @Override
    public void initialize(Stage stage) {
        Platform.runLater(() -> {
            this.stage = stage;
            stage.setMinWidth(700);
            stage.setMinHeight(480);
            stage.setTitle(SokobanConstants.GAME_NAME);
            stage.setResizable(false);
            stage.setOnCloseRequest((window) -> exit());
            label.setText(label.getText().concat(String.valueOf(LocalDate.now().get(ChronoField.YEAR))));
            label.setFont(new Font(label.getFont().getName(), 30));
        });
    }

    private void changeView(Controller controller) {
        mainPane.setCenter(controller.getRoot());
        mainPane.setTop(controller.getMenuBar());
    }

    @Override
    public void loadToRoot() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exit() {
        List.of(aboutController, gameController, levelBuilderController).stream().filter(Optional::isPresent).map(Optional::get).forEach(controller -> controller.exit());
    }
}
