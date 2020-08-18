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
package com.jeffreyorazulike.sokoban;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * A simple sokoban game
 *
 * @author Jeffrey Orazulike <bit.ly/jeffreyorazulike>
 */
public class Sokoban extends JDialog {

    /**
     * The id used when serializing the class
     */
    private static final long serialVersionUID = -2168862794647094768L;

    private JFileChooser fileChooser;

    /**
     * Where the program's resources are kept
     */
    public static final String RESOURCES = "src/main/resources/";

    /**
     * How the game's world is arranged
     */
    private String level = """
                                     ######
                                     ##   #
                                     ##$  #
                                   ####  $##
                                   ##  $ $ #
                                 #### # ## #   ######
                                 ##   # ## #####  ..#
                                 ## $  $          ..#
                                 ###### ### #@##  ..#
                                     ##     #########
                                     ########
                                 """;

    /**
     * The name of the program
     */
    public static final String PROGRAM_NAME = "Sokoban";
    /**
     * The game's background color
     */
    public static final Color BACKGROUND_COLOR = new Color(250, 240, 170);

    private Board board;

    /**
     * Represents the game world where everything is going to take place
     */
    class Board extends JPanel implements KeyListener {

        private Actor[][] actors;
        private Actor.Area[] area;
        private Actor.Player player;
        private boolean isCompleted;

        /**
         * The id used when serializing the panel
         */
        private static final long serialVersionUID = 131524587658279935L;

        public Board() {
            initBoard();
        }

        private void initBoard() {
            setBackground(BACKGROUND_COLOR);

            newGame();

            setFocusable(true);
            addKeyListener(this);
        }

        private void newGame() {
            ActorsWithSize actorsWithSize = Actor.decodeLevel(level);
            actors = actorsWithSize.actors;
            area = actorsWithSize.area;

            for (Actor[] actor : actors) {
                for (Actor actor1 : actor) {
                    if (actor1 instanceof Actor.Player player1)
                        player = player1;
                }
            }

            Sokoban.this.setMinimumSize(new Dimension(actorsWithSize.width, actorsWithSize.height + 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawActors(g, actors, area, this);
            if (isCompleted)
                g.drawString("Completed", 10, 10);
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                newGame();
                repaint();
                return;
            }

            try {

                player.move(actors, e.getKeyCode());
                isCompleted = isCompleted(actors, area);
                repaint();
            } catch (SokobanException | ArrayIndexOutOfBoundsException exception) {
                System.out.println(exception.getMessage());
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

    }

    /**
     *
     * @param g      graphics object used for drawing
     * @param actors the actors of the game
     * @param board  the image observer
     */
    private void drawActors(Graphics g, Actor[][] actors, Actor.Area[] area, ImageObserver board) {
        for (int i = 0; i < area.length; ++i)
            area[i].draw(g, board);

        for (int i = 0; i < actors.length; ++i)
            for (int j = 0; j < actors[i].length; ++j)
                if (actors[i][j] != null)
                    actors[i][j].draw(g, board);
    }

    /**
     *
     * @param actors an array containing the actors
     * @param area   an array containing the area
     *
     * @return if the area has been filled up with baggage
     */
    public boolean isCompleted(Actor[][] actors, Actor.Area[] area) {
        for (Actor.Area currentArea : area) {
            if (!(actors[currentArea.getRow()][currentArea.getColumn()] instanceof Actor.Baggage))
                return false;
        }
        return true;
    }

    public void saveGame(File file) {

        try ( ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(board.actors);
            output.writeObject(board.area);
            output.writeObject(board.isCompleted);
            output.writeObject(Sokoban.this.getSize());
        } catch (IOException exception) {
            System.err.println(exception);
        }
    }

    public void loadGame(File file) {

        try ( ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            board.actors = (Actor[][]) input.readObject();
            board.area = (Actor.Area[]) input.readObject();
            board.isCompleted = (boolean) input.readObject();

            for (Actor[] actorObject : board.actors) {
                for (Actor actor : actorObject) {
                    if (actor instanceof Actor.Wall)
                        actor.setImage(Actor.WALL_IMAGE.getImage());
                    else if (actor instanceof Actor.Baggage)
                        actor.setImage(Actor.BAGGAGE_IMAGE.getImage());
                    else if (actor instanceof Actor.Player) {
                        board.player = (Actor.Player) actor;
                        actor.setImage(Actor.PLAYER_IMAGE.getImage());
                    }
                }
            }

            for (Actor.Area area : board.area) {
                area.setImage(Actor.AREA_IMAGE.getImage());
            }

            repaint();
            Sokoban.this.setMinimumSize((Dimension) input.readObject());
        } catch (IOException | ClassNotFoundException exception) {
            System.err.println(exception);
        }
    }

    /**
     * <p>
     * Initialize the sokoban game frame
     * </p>
     */
    private void initSokoban() throws SokobanException {
        JMenuBar menubar = new JMenuBar();

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        JMenu menu = new JMenu(PROGRAM_NAME);

        JMenuItem save = new JMenuItem("Save", 's');
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showSaveDialog(Sokoban.this);

                if (result == JFileChooser.CANCEL_OPTION)
                    return;

                File file = fileChooser.getSelectedFile();

                if (file != null && !file.getName().isEmpty())
                    saveGame(file);
            }
        });

        JMenuItem load = new JMenuItem("Load", 'l');
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog(Sokoban.this);

                if (result == JFileChooser.CANCEL_OPTION)
                    return;

                File file = fileChooser.getSelectedFile();

                if (file != null && !file.getName().isEmpty())
                    loadGame(file);
            }
        });

        menu.add(save);
        menu.add(load);

        menubar.add(menu);

        setJMenuBar(menubar);

        board = new Board();

        add(board);

        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    public Sokoban(JFrame parent) {
        super(parent, PROGRAM_NAME);

        try {
            initSokoban();
        } catch (SokobanException sokobanException) {
            System.err.println(sokobanException);
            System.exit(1);
        }
    }

}
