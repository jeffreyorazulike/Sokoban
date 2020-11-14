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
import static com.jeffreyorazulike.sokoban.SokobanConstants.*;
import com.jeffreyorazulike.sokoban.interfaces.ActorWorker;
import com.jeffreyorazulike.sokoban.interfaces.LevelWorker;
import com.jeffreyorazulike.sokoban.interfaces.SokobanIO;
import com.jeffreyorazulike.sokoban.model.LevelBuilderModel;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Jeffrey Orazulike
 */
public class LevelBuilderController extends Controller implements SokobanIO, ActorWorker, LevelWorker {

    private final int MAX_HEIGHT = 12;
    private final int MAX_WIDTH = MAX_HEIGHT * 2;

    private Image currentImage = WALL_IMAGE;

    private final int INITIAL_LENGTH = 5;

    private LevelBuilderModel model;

    private Group group;

    private File lastKnownFile;

    @FXML
    private FlowPane actorImagesPane;

    @FXML
    private TextField rowTextField;

    @FXML
    private TextField columnTextField;

    @Override
    public void initialize(Stage stage) {
        this.stage = stage;
        menuBar = (MenuBar) (((BorderPane) getRoot()).getTop());
        ((BorderPane) getRoot()).setTop(null);

        actorImagesPane.setMaxHeight(SPACE);
        addToImagesPane(AREA_IMAGE);
        addToImagesPane(BAGGAGE_IMAGE);
        addToImagesPane(PLAYER_IMAGE);
        addToImagesPane(WALL_IMAGE);

        try {
            model = (LevelBuilderModel) decodeLevel((String) load(new File(GAME_NAME.concat(LEVEL_BUILDER_EXTENSION))), ModelType.BUILDER);
        } catch (IOException | ClassNotFoundException e) {
        }

        Optional.ofNullable(model).ifPresentOrElse(m -> model = m, () -> model = new LevelBuilderModel(INITIAL_LENGTH, INITIAL_LENGTH));

        rowTextField.setText(String.valueOf(model.getActors().length));
        columnTextField.setText(String.valueOf(model.getActors()[0].length));

        changeLevelStructure(null);
    }

    private void addToImagesPane(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setOnMouseClicked((MouseEvent e) -> currentImage = imageView.getImage());
        actorImagesPane.getChildren().add(imageView);
    }

    @FXML
    public void changeLevelStructure(ActionEvent actionEvent) {
        try {
            int row = Integer.parseInt(rowTextField.getText());
            int column = Integer.parseInt(columnTextField.getText());
            if (row <= 0 || column <= 0 || row > MAX_HEIGHT || column > MAX_WIDTH)
                return;
            if (model.update(row, column))
                loadToRoot();
        } catch (NumberFormatException e) {
        }
    }

    @FXML
    public void max(ActionEvent actionEvent) {
        rowTextField.setText(String.valueOf(MAX_HEIGHT));
        columnTextField.setText(String.valueOf(MAX_WIDTH));
        changeLevelStructure(actionEvent);
    }

    @FXML
    public void load(ActionEvent event) throws IOException, ClassNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(LEVEL_BUILDER, LEVEL_BUILDER_EXTENSION));
        fileChooser.setTitle("Load Level...");
        model = (LevelBuilderModel) decodeLevel((String) load(fileChooser.showOpenDialog(getStage())), ModelType.BUILDER);
        loadToRoot();
    }

    @FXML
    public void save(ActionEvent event) {
        if (correctLevelStructure(model.getActors()))
            if (lastKnownFile != null)
                save(lastKnownFile, encodeLevel(model.getActors()), Optional.of((file) -> lastKnownFile = file));
            else
                saveAs(event);
    }

    @FXML
    public void saveAs(ActionEvent event) {
        if (correctLevelStructure(model.getActors())) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(LEVEL_BUILDER, LEVEL_BUILDER_EXTENSION));
            fileChooser.setTitle("Save Level As...");
            save(fileChooser.showSaveDialog(getStage()), encodeLevel(model.getActors()), Optional.of((file) -> lastKnownFile = file));
        }
    }

    @Override
    public void loadToRoot() {
        group = new Group();

        for (int row = 0; row < model.getActors().length; ++row)
            for (int column = 0; column < model.getActors()[row].length; ++column) {
                final Actor actor = model.getActors()[row][column];

                actor.setOnMouseClicked(e -> {
                    model.getActors()[actor.getRow()][actor.getColumn()] = Actor.getActor(currentImage, actor.getRow(), actor.getColumn());
                    loadToRoot();
                });
                actor.setOnMouseDragged(e -> {
                    model.getActors()[actor.getRow()][actor.getColumn()] = new Actor.Empty(actor.getRow(), actor.getColumn());
                    loadToRoot();
                });

                group.getChildren().add(actor);
            }

        ((BorderPane) getRoot()).setCenter(group);
    }

    @Override
    public void exit() {
        save(new File(GAME_NAME.concat(LEVEL_BUILDER_EXTENSION)), encodeLevel(model.getActors()), Optional.empty());
    }

}
