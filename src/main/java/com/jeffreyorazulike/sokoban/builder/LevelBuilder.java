/*
 * Copyright (C) 2020 Jeffrey Orazulike <bit.ly/jeffreyorazulike>
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
package com.jeffreyorazulike.sokoban.builder;

import com.jeffreyorazulike.sokoban.Actor;
import com.jeffreyorazulike.sokoban.Sokoban;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Jeffrey Orazulike <bit.ly/jeffreyorazulike>
 */
public class LevelBuilder extends JFrame {

    public static final String PROGRAM_NAME = "Sokoban Level Builder";
    private int row, column;

    private boolean toDrop;
    private Actor actor;

    public LevelBuilder(JFrame parent) {
        super(PROGRAM_NAME);

        new RowAndColumn(parent).dispose();

        ActorPanel[][] actors = new ActorPanel[row][column];

        JPanel actorsPanel = new JPanel();
        actorsPanel.setLayout(new GridLayout(row, column, 2, 2));
        actorsPanel.setBackground(Color.GRAY);

        for (int i = 0; i < actors.length; ++i) {
            for (int j = 0; j < actors.length; ++j) {
                actors[i][j] = new ActorPanel(null, true);
                actors[i][j].setFocusable(true);
                actors[i][j].setBackground(Sokoban.BACKGROUND_COLOR);
                actorsPanel.add(actors[i][j]);
            }
        }

        JPanel imagesPanel = new JPanel();
        imagesPanel.setLayout(new GridLayout(4, 1, 3, 3));
        imagesPanel.setMinimumSize(new Dimension(40, imagesPanel.getHeight()));
        ActorPanel[] images = new ActorPanel[4];

        images[0] = new ActorPanel(Actor.getActor(Actor.AREA, 0, 0), false);
        images[1] = new ActorPanel(Actor.getActor(Actor.BAGGAGE, 0, 0), false);
        images[2] = new ActorPanel(Actor.getActor(Actor.PLAYER, 0, 0), false);
        images[3] = new ActorPanel(Actor.getActor(Actor.WALL, 0, 0), false);

        for (ActorPanel image : images) {
            imagesPanel.add(image);
        }

        JButton save = new JButton("Save");

        setLayout(new BorderLayout(1, 3));

        add(actorsPanel, BorderLayout.CENTER);
        add(imagesPanel, BorderLayout.WEST);
        add(save, BorderLayout.SOUTH);

        setSize(640, 480);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private final class ActorPanel extends JPanel {

        private Actor actor;

        /**
         * @param actor the actor the panel holds
         * @param main  if the actor is part of the main area or adding area
         */
        public ActorPanel(Actor actor, boolean main) {
            setActor(actor);
            setMinimumSize(new Dimension(Actor.SPACE, Actor.SPACE));

            if (main) {
                addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                            setActor(null);
                            repaint();
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                    }
                });
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);

                        if (toDrop) {
                            setActor(LevelBuilder.this.actor);
                            toDrop = !toDrop;
                            repaint();
                        }
                    }

                });
            } else {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);

                        if (!toDrop) {
                            LevelBuilder.this.actor = getActor();
                            toDrop = !toDrop;
                        }
                    }

                });
            }
        }

        public void setActor(Actor actor) {
            this.actor = actor;
        }

        public Actor getActor() {
            return actor;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (actor != null) {
                int distance = actor.getImage().getWidth(this) / 2;
                g.drawImage(actor.getImage(), getX() / 2 - distance, getY() / 2 - distance, this);
            }

        }

    }

    private class RowAndColumn extends JDialog {

        JTextField rowField;
        JTextField columnField;

        public RowAndColumn(JFrame parent) {
            super(parent);
            setModal(true);

            setLocationRelativeTo(null);

            setLayout(new GridLayout(2, 1, 0, 5));

            JButton save = new JButton("Save");
            save.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (rowField.getText().length() != 0 && rowField.getText().length() != 0)
                        try {
                        row = Integer.valueOf(rowField.getText());
                        column = Integer.valueOf(columnField.getText());

                        if (row < 1 || column < 1)
                            throw new Exception();

                        RowAndColumn.this.dispose();
                    } catch (Exception exception) {
                    }
                }
            });

            JLabel rowLabel = new JLabel("Row:");
            rowField = new JTextField(3);

            JLabel columnLabel = new JLabel("Column:");
            columnField = new JTextField(3);

            JPanel topPanel = new JPanel();
            topPanel.add(rowLabel);
            topPanel.add(rowField);
            topPanel.add(columnLabel);
            topPanel.add(columnField);

            add(topPanel);
            add(save);

            pack();

            setVisible(true);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }

    }
}
