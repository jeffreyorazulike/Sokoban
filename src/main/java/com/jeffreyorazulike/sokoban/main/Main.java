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
package com.jeffreyorazulike.sokoban.main;

import com.jeffreyorazulike.sokoban.Sokoban;
import com.jeffreyorazulike.sokoban.builder.LevelBuilder;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Jeffrey Orazulike <bit.ly/jeffreyorazulike>
 */
public class Main extends JFrame {

    private Sokoban sokoban;
    private LevelBuilder levelBuilder;

    public Main() {
        super(Sokoban.PROGRAM_NAME);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException exception) {
            System.err.println(exception);
        }

        JButton sokobanButton = new JButton("Play ".concat(Sokoban.PROGRAM_NAME));
        sokobanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sokoban != null)
                    sokoban.dispose();

                sokoban = new Sokoban(Main.this);
            }
        });
        JButton levelBuilderButton = new JButton(LevelBuilder.PROGRAM_NAME);
        levelBuilderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (levelBuilder != null)
                    levelBuilder.dispose();

                levelBuilder = new LevelBuilder(Main.this);
            }
        });

        setLayout(new GridLayout(1, 2, 5, 5));
        add(sokobanButton);
        add(levelBuilderButton);

        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center the JFrame on the screen
    }

    public static void main(String[] args) {
        new Main();
    }
}
