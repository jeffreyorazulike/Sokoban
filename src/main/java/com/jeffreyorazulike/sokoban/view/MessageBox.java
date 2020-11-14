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
package com.jeffreyorazulike.sokoban.view;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Jeffrey Orazulike
 */
public class MessageBox {

    public static void showDialog(String messsage) {
        final Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label(messsage);
        label.setAlignment(Pos.CENTER);

        Button button = new Button("Ok");
        button.setAlignment(Pos.CENTER);
        button.setOnAction((e) -> stage.close());

        FlowPane root = new FlowPane();
        root.getChildren().addAll(label, button);
        root.setAlignment(Pos.CENTER);
        root.setOrientation(Orientation.VERTICAL);
        root.setColumnHalignment(HPos.CENTER);
        root.setRowValignment(VPos.CENTER);
        root.setMaxHeight(50);

        stage.setScene(new Scene(root));

        stage.showAndWait();
    }

}
