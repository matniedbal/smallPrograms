package saper.jFrame;

import saper.exceptions.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JFrameController {

    private JLabel firstWindowMessage = new JLabel();
    private JTextField chooseXSizeField;
    private JTextField chooseYSizeField;
    private JTextField chooseNumberOfBombs;
    private JButton acceptButton;
    private JButton topScoreButton;
    private String name;
    private JTextField writeYourName;

    public JFrameController() {

    }




    public void initialize() {
        firstWindowMessage.setBounds(10, 5, 400, 30);
        firstWindowMessage.setText("Input parameters of your game:");
        firstWindow();
    }

    private void firstWindow() {
        JFrame firstWindow = new JFrame();
        firstWindow.setTitle("Super Retro Saper v.1.0");
        firstWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        firstWindow.setSize(540, 250);
        firstWindow.setResizable(false);
        firstWindow.add(acceptButton());
        firstWindow.add(chooseXSizeField());
        firstWindow.add(chooseYSizeField());
        firstWindow.add(chooseNumberOfBombs());
        firstWindow.add(chooseXSizeFieldJLabel());
        firstWindow.add(chooseYSizeFieldJLabel());
        firstWindow.add(chooseNumberOfBombsJLabel());
        firstWindow.add(firstWindowMessage);
        firstWindow.add(firstWindowImageLabel());
        firstWindow.add(topScoreButton());
        firstWindow.add(writeYourName());
        firstWindow.add(writeYourNameJLabel());
        firstWindow.setLayout(null);
        firstWindow.setVisible(true);
    }

    private Component chooseXSizeFieldJLabel() {
        JLabel chooseXSizeFieldJLabel = new JLabel();
        chooseXSizeFieldJLabel.setBounds(10, 25, 120, 30);
        chooseXSizeFieldJLabel.setText("Columns no.(max 70):");
        return chooseXSizeFieldJLabel;
    }

   private Component chooseYSizeFieldJLabel() {
       JLabel chooseYSizeFieldJLabel = new JLabel();
       chooseYSizeFieldJLabel.setBounds(150, 25, 120, 30);
       chooseYSizeFieldJLabel.setText("Rows no.(max 35):");
        return chooseYSizeFieldJLabel;
    }

   private Component chooseNumberOfBombsJLabel() {
       JLabel chooseNumberOfBombsJLabel = new JLabel();
       chooseNumberOfBombsJLabel.setBounds(290, 25, 120, 30);
       chooseNumberOfBombsJLabel.setText("Number of bombs:");
        return chooseNumberOfBombsJLabel;
    }

    private JTextField chooseXSizeField() {
        chooseXSizeField = new JTextField();
        addOnEnterKeyListener(chooseXSizeField);
        chooseXSizeField.setBounds(10, 50, 120, 40);
        return chooseXSizeField;
    }

    private JTextField chooseYSizeField() {
        chooseYSizeField = new JTextField();
        addOnEnterKeyListener(chooseYSizeField);
        chooseYSizeField.setBounds(150, 50, 120, 40);
        return chooseYSizeField;
    }

    private JTextField chooseNumberOfBombs() {
        chooseNumberOfBombs = new JTextField();
        addOnEnterKeyListener(chooseNumberOfBombs);
        chooseNumberOfBombs.setBounds(290, 50, 120, 40);
        return chooseNumberOfBombs;
    }


    private Component writeYourNameJLabel() {
        JLabel writeYourNameJLabel = new JLabel();
        writeYourNameJLabel.setBounds(10, 135, 400, 40);
        writeYourNameJLabel.setText("You can write your name:");
        return writeYourNameJLabel;
    }
    private JTextField writeYourName() {
        writeYourName = new JTextField();
        addOnEnterKeyListener(writeYourName);
        writeYourName.setBounds(10, 165, 400, 40);
        return writeYourName;
    }

    private JButton acceptButton(){
        acceptButton = new JButton(new ImageIcon("src\\main\\resources\\saper\\buttonAccept.png"));
        acceptButton.setBounds(10, 100, 400, 40);
        acceptButton.setText("ACCEPT GAME");
        acceptButton.setHorizontalTextPosition(JLabel.CENTER);
        acceptButton.setVerticalTextPosition(JLabel.CENTER);
        acceptButton.addActionListener(actionEvent -> {
            try {

                if((Integer.parseInt(chooseXSizeField.getText())*Integer.parseInt(chooseYSizeField.getText())) <= Integer.parseInt(chooseNumberOfBombs.getText())){
                    System.out.println("fields:"+Integer.parseInt(chooseXSizeField.getText())*Integer.parseInt(chooseYSizeField.getText()));
                    System.out.println("bombs:"+Integer.parseInt(chooseNumberOfBombs.getText()));
                    throw new TooMuchBombsException();
                }
                if(Integer.parseInt(chooseXSizeField.getText()) > 70 || Integer.parseInt(chooseYSizeField.getText()) > 35){
                    throw new TooBigFieldException();
                }

                JMatrix matrix = new JMatrix(Integer.parseInt(chooseXSizeField.getText()), Integer.parseInt(chooseYSizeField.getText()), Integer.parseInt(chooseNumberOfBombs.getText()));
                name = writeYourName.getText();
                matrix.getMatrix().setPlayerRandomName(name);




            } catch (TooMuchBombsException ex2){
                firstWindowMessage.setText("More bombs than fields, try again:");

        }  catch (TooBigFieldException ex2){
                firstWindowMessage.setText("Too large field, try to fit in 70 columns and 35 rows:");

        } catch (Exception ex) {
                firstWindowMessage.setText("Input CORRECT parameters of your game:");
            }
        });
        return acceptButton;
    }

private JButton topScoreButton(){
    topScoreButton = new JButton(new ImageIcon("src\\main\\resources\\saper\\buttonAccept.png"));
    topScoreButton.setBounds(420, 165, 100, 40);
    topScoreButton.setText("TOP 100");
    topScoreButton.setHorizontalTextPosition(JLabel.CENTER);
    topScoreButton.setVerticalTextPosition(JLabel.CENTER);
    topScoreButton.addActionListener(actionEvent -> {
        JTopScoreFrame top100 = new JTopScoreFrame();

    });
        return topScoreButton;
    }

    private void addOnEnterKeyListener(JTextField textField){
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 10) {
                    try {

                        if((Integer.parseInt(chooseXSizeField.getText())*Integer.parseInt(chooseYSizeField.getText())) <= Integer.parseInt(chooseNumberOfBombs.getText())){
                            System.out.println("fields:"+Integer.parseInt(chooseXSizeField.getText())*Integer.parseInt(chooseYSizeField.getText()));
                            System.out.println("bombs:"+Integer.parseInt(chooseNumberOfBombs.getText()));
                            throw new TooMuchBombsException();
                        }
                        if(Integer.parseInt(chooseXSizeField.getText()) > 70 || Integer.parseInt(chooseYSizeField.getText()) > 35){
                            throw new TooBigFieldException();
                        }

                        JMatrix matrix = new JMatrix(Integer.parseInt(chooseXSizeField.getText()), Integer.parseInt(chooseYSizeField.getText()), Integer.parseInt(chooseNumberOfBombs.getText()));
                        name = writeYourName.getText();
                        matrix.getMatrix().setPlayerRandomName(name);



                    } catch (TooMuchBombsException ex2){
                        firstWindowMessage.setText("More bombs than fields, try again:");
                    }  catch (TooBigFieldException ex2){
                        firstWindowMessage.setText("Too large field, try to fit in 70 columns and 35 rows:");

                    } catch (Exception ex) {
                        firstWindowMessage.setText("Input CORRECT parameters of your game:");
                    }
                }
            }
            @Override
            public void keyPressed(KeyEvent e) { }
            @Override
            public void keyReleased(KeyEvent e) { }  });
    }

    private JLabel firstWindowImageLabel() {
        JLabel imageLabel = new JLabel(new ImageIcon("src\\main\\resources\\saper\\mine_field_first.png"));
        imageLabel.setBounds(420, 10, 100, 150);
        return imageLabel;
    }

}
