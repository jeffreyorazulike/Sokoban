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

import com.jeffreyorazulike.sokoban.Actor;
import com.jeffreyorazulike.sokoban.SokobanConstants;
import static com.jeffreyorazulike.sokoban.SokobanConstants.*;
import com.jeffreyorazulike.sokoban.interfaces.ActorWorker;
import com.jeffreyorazulike.sokoban.interfaces.LevelWorker;
import com.jeffreyorazulike.sokoban.interfaces.SokobanIO;
import com.jeffreyorazulike.sokoban.model.GameModel;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Jeffrey Orazulike
 */
public class GameController extends Controller implements SokobanIO, ActorWorker, LevelWorker {

    private Actor.Player player;

    private File lastKnownFile;

    private Group display;

    private GameModel model;

    private FileChooser fileChooser;

    @FXML
    private MenuItem undo;

    @Override
    public void initialize(Stage stage) {
        this.stage = stage;
        menuBar = (MenuBar) (((BorderPane) getRoot()).getTop());
        ((BorderPane) getRoot()).setTop(null);

        try {
            model = (GameModel) load(new File(SokobanConstants.GAME_NAME.concat(SokobanConstants.GAME_EXTENSION)));
        } catch (IOException | ClassNotFoundException ex) {
        }

        Optional.ofNullable(model).ifPresentOrElse(m -> model = m, () -> model = (GameModel) decodeLevel(DEFAULT_LEVEL, ModelType.GAME));

        fileChooser = new FileChooser();
    }

    @FXML
    public void newGame(ActionEvent event) {
        model = (GameModel) decodeLevel(model.getLevel(), ModelType.GAME);
        ((BorderPane) getRoot()).setTop(null);
        loadToRoot();
    }

    @FXML
    public void open(ActionEvent event) throws IOException, ClassNotFoundException {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(GAME_NAME, GAME_EXTENSION));
        fileChooser.setTitle("Load Saved Game...");
        model = (GameModel) load(fileChooser.showOpenDialog(getStage()));
        loadToRoot();
    }

    @FXML
    public void loadLevel(ActionEvent event) throws IOException, ClassNotFoundException {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(LEVEL_BUILDER, LEVEL_BUILDER_EXTENSION));
        fileChooser.setTitle("Load Level...");
        String level = (String) load(fileChooser.showOpenDialog(getStage()));
        if (correctLevelStructure(decodeLevel(level, ModelType.BUILDER).getActors()))
            model = ((GameModel) decodeLevel(level, ModelType.GAME));

        ((BorderPane) getRoot()).setTop(null);
        loadToRoot();
    }

    @FXML
    public void save(ActionEvent event) throws IOException {
        if (lastKnownFile != null)
            save(lastKnownFile, model, Optional.of((file) -> lastKnownFile = file));
        else
            saveAs(event);
    }

    @FXML
    public void saveAs(ActionEvent event) throws IOException {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(GAME_NAME, GAME_EXTENSION));
        fileChooser.setTitle("Save Game As...");
        save(fileChooser.showSaveDialog(getStage()), model, Optional.of((file) -> lastKnownFile = file));
    }

    @FXML
    public void undo(ActionEvent event) {
        GameModel.Undo toUndo = model.getUndo().pop();

        Optional.ofNullable(toUndo).ifPresent(action -> {

            Optional.ofNullable(toUndo.player).ifPresent(actor -> {
                ((Actor.Player) (model.getActors()[actor[0]][actor[1]])).move(model, action.getMove());
            });

            Optional.ofNullable(toUndo.baggage).ifPresent(actor -> {
                ((Actor.Baggage) (model.getActors()[actor[0]][actor[1]])).move(model, action.getMove());
            });

        });
    }

    private boolean checkWin(Actor[][] actors, Actor.Area[] area) {
        for (Actor.Area currentArea : area)
            if (!(actors[currentArea.getRow()][currentArea.getColumn()] instanceof Actor.Baggage))
                return false;
        return true;
    }

    @Override
    public void loadToRoot() {
        undo.disableProperty().bind(model.getUndo().numberOfElementsProperty().isEqualTo(0));

        display = new Group();

        inflateActors(model.getArea(), Optional.of(actor -> display.getChildren().add(actor)));
        Arrays.stream(model.getActors()).forEach(actors -> inflateActors(actors, Optional.of(actor -> {
            if (actor instanceof Actor.Player)
                player = (Actor.Player) actor;
            display.getChildren().add(actor);
        })));

        display.setFocusTraversable(true);
        display.setOnKeyPressed((KeyEvent e) -> {
            player.move(model, e.getCode());
            if (checkWin(model.getActors(), model.getArea())) {
                Label label = new Label("Game Won");
                label.setFont(new Font(label.getFont().getName(), 25));
                label.setTextFill(Color.WHITE);
                ((BorderPane) getRoot()).setTop(label);
                display.setOnKeyPressed(null);
            }
        });

        ((BorderPane) getRoot()).setCenter(display);
        display.requestFocus();
    }

    @Override
    public void exit() {
        save(new File(GAME_NAME.concat(GAME_EXTENSION)), model, Optional.empty());
    }
}
