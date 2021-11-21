package main;

import controller.Controller;
import model.Model;
import view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class TriviaMazeFrame extends JFrame {

    public static final String SAVE_FILE = "save.ser";

    private JMenuBar myMenuBar;

    private JMenu myFileMenu;

    private JMenu myHelpMenu; //Consider making this local.

    private JMenuItem mySaveGameMenuItem;

    private JMenuItem myLoadGameMenuItem;

    private JMenuItem myNewGameMenuItem;

    private JMenuItem myExitMenuItem;

    private JMenuItem myAboutMenuItem;

    private JMenuItem myHowToPlayMenuItem;

    private Model myModel;

    private View myView;

    private Controller myController;

    private TriviaMazeFrame() {
        //Does nothing..
    }

    public TriviaMazeFrame(final Model theModel) {
        super();
        myModel = theModel;
        myView = new View(myModel);
        myController = new Controller(myModel);
        startUpMessage();
        layoutComponents();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Lays out the components for the main frame.
     */
    private void layoutComponents() {
        this.setTitle("Joseph & Armeen's Trivia Maze Game");
        myMenuBar = new JMenuBar();
        setUpFileMenu();
        setUpFileMenuHandlers();
        setUpHelpMenu();
        setUpHelpMenuHandlers();
        this.setJMenuBar(myMenuBar);
        final JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        pane.add(myView);
        pane.add(myController);
        this.add(pane);
        this.setSize(1000, 700);
        this.setVisible(true);
    }
    /**
     * Shows gameplay instructions during startup.
     */
    private void startUpMessage(){
        JLabel message = new JLabel("Game Play instructions", SwingConstants.CENTER);
        JButton button = new JButton("Okay");
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(button, BorderLayout.LINE_END);
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.add(message);
        mainPanel.add(bottomPanel, BorderLayout.PAGE_END);
        // mainPanel.setPreferredSize(new Dimension(550, 400));
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.setSize(550, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    /**
     * Creates and adds components to the file menu.
     */
    private void setUpFileMenu() {
        myFileMenu = new JMenu("File");
        myNewGameMenuItem = new JMenuItem("New Game");
        mySaveGameMenuItem = new JMenuItem("Save Game");
        myLoadGameMenuItem = new JMenuItem("Load Game");
        myExitMenuItem = new JMenuItem("Exit");
        myFileMenu.add(mySaveGameMenuItem);
        myFileMenu.add(myLoadGameMenuItem);
        myFileMenu.add(myNewGameMenuItem);
        myFileMenu.add(myExitMenuItem);
    }

    /**
     * Creates and adds components to the help menu.
     */
    private void setUpHelpMenu() {
        myHelpMenu = new JMenu("Help");
        myAboutMenuItem = new JMenuItem("About");
        myHowToPlayMenuItem = new JMenuItem("Game Play instructions");
        myHelpMenu.add(myAboutMenuItem);
        myHelpMenu.add(myHowToPlayMenuItem);
        myMenuBar.add(myFileMenu);
        myMenuBar.add(myHelpMenu);
    }

    /**
     * Sets up the handlers for the file menu.
     */
    private void setUpFileMenuHandlers() {
        myNewGameMenuItem.addActionListener(e -> myModel.setUpNewGame());
        mySaveGameMenuItem.addActionListener(e -> saveGame());
        myLoadGameMenuItem.addActionListener(e -> loadGame());
        myExitMenuItem.addActionListener(e -> this.dispose());
    }

    /**
     * Saves the gamestate to SAVE_FILE.
     */
    private void saveGame() {
        try (FileOutputStream file = new FileOutputStream(SAVE_FILE);
             ObjectOutputStream output = new ObjectOutputStream(file)) {
            output.writeObject(myModel);
        } catch (final IOException exception) {
            showError(exception);
        }
    }

    /**
     * Loads the gamestate from SAVE_FILE.
     */
    private void loadGame() {
        try (FileInputStream file = new FileInputStream(SAVE_FILE);
             ObjectInputStream input = new ObjectInputStream(file)) {
            updateModel((Model) input.readObject());
        } catch (final IOException | ClassNotFoundException exception) {
            showError(exception);
        }
    }

    /**
     * Updates the model reference for all three GUI elements.
     * @param theModel the model to update with.
     */
    private void updateModel(final Model theModel) {
        myModel = theModel;
        myView.updateModel(theModel);
        myController.updateModel(theModel);
    }

    /**
     * Shows an exception to the user using a Message Dialog.
     * @param theException the exception to be shown.
     */
    private void showError(final Exception theException) {
        JOptionPane.showMessageDialog(null, theException.getMessage(),
                theException.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Sets up the handlers for the help menu.
     */
    private void setUpHelpMenuHandlers() {
        final String about = "This is Trivia Maze, produced for TCSS360 B," +
                "made by Armeen Farange and Joseph Graves in 2021.";
        //TODO Add help string.
        final String help = "idk just click some buttons";
        myAboutMenuItem.addActionListener(e ->
                JOptionPane.showMessageDialog(null, about));
        myHowToPlayMenuItem.addActionListener(e ->
                JOptionPane.showMessageDialog(null, help));
    }
}
