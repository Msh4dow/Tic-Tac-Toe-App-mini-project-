package com.fx;
//package Classes :-
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.Random;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.io.LineNumberInputStream;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    //all GUI Variables ;
    private GridPane gridPane=new GridPane();
    private BorderPane borderpane=new BorderPane();
    private Random random = new Random();//random object.
    private Label title =new Label("Tic Tac Toe Game");
    private Label turnLabel = new Label(""); // Empty label to display player's turn
    private Button restartButton=new Button("Restart Match");
    Font font = Font.font("Roboto", FontWeight.BOLD,30);
    Font turnFont = Font.font(" Open Sans", FontWeight.BOLD, 15 ); // Font for turn label



    // buttons 0....8;
    private Button[] btns =new Button[9];

    //All Logic Variables:
    boolean gameOver=false;
    int activeplayer=0;
    int gameState[]={3,3,3,3,3,3,3,3,3};
    int winingPosition[][]={
            {0,1,2},
            {3,4,5},
            {6,7,8},
            {0,3,6},
            {1,4,7},
            {2,5,8},
            {0,4,8},
            {2,4,6}
    };

    @Override
    public void start(Stage stage) throws IOException {

        this.createGUI();
        this.handleEvent(); // call event
        Scene scene = new Scene(borderpane,550,650);
        stage.setScene(scene);
        stage.show();

    }



    //this funtion is for createing gui;
    private void createGUI() {  //GUI SPACE :
        //createing title;
        title.setFont(font);
        //creating restart button
        restartButton.setFont(font);
        restartButton.setDisable(true);
        // setting title and restart to borderpane
        borderpane.setTop(title);
        borderpane.setBottom(restartButton);
        //setting borderpane componts to center
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(restartButton, Pos.CENTER);
        borderpane.setPadding(new Insets(20, 20, 20, 20));
        // Setting up the turn label
        turnLabel.setFont(turnFont); // Setting custom font
        turnLabel.getStyleClass().add("turn-label"); // Add a style class for CSS styling
        turnLabel.setTextFill(Color.RED); // Setting font color
        borderpane.setRight(turnLabel);
        //working on 9 game button 0 to 8  means--// Setting up the grid pane for buttons
        int label = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button();
                button.setId(label + "");
                button.setFont(font);
                button.setPrefWidth(150);// width of Button
                button.setPrefHeight(150);// heigth of Button
                gridPane.add(button, j, i);
                gridPane.setAlignment(Pos.CENTER);
                btns[label] = button;
                label++;
            }
        }
        borderpane.setCenter(gridPane);
    }

    //method for handling Event :-
    private void handleEvent(){
        //restart button click
        restartButton.setOnAction(new EventHandler<ActionEvent>() {          //Can change(obj pass)
            @Override
            public void handle(ActionEvent actionEvent) {
                for(int i =0; i<9;i++){
                    gameState[i] = 3;
                    //btns[i].setText("");
                    btns[i].setGraphic(null);
                    btns[i].setBackground(null);
                    btns[i].setBorder(new Border(new BorderStroke(Color.GRAY,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,new BorderWidths(1))));
                    gameOver=false;
                    restartButton.setDisable(true);
                }

            }
        });

        // Event handler for game buttons
        for(Button btn:btns){
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                    public void handle(ActionEvent actionEvent) {
                    System.out.println("Number Button Clicked");
                    Button currentBtn=(Button)actionEvent.getSource();
                    String ids=currentBtn.getId();
                    int idI=Integer.parseInt(ids); //ids=button

                    // check for game over
                    if(gameOver){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error message");
                        alert.setContentText("Game over !! Try to restart the game");
                        alert.show();
                    }else{
                        // game is not over to do chances
                        // Check for player's turn and update the turn label
                        if (activeplayer == 1) {
                            turnLabel.setText("💻Computer's Turn");
                        } else {
                            turnLabel.setText("👤User's Turn");
                        }

                        // Proceed with the game
                        if (gameState[idI]==3) {
                          //proceed
                            if(activeplayer==1){
                                //chance of 1
                                //currentBtn.setText(activeplayer+"");
                                currentBtn.setGraphic(new ImageView(new Image("file:src/main/resources/img/cross.png")));
                                gameState[idI]=activeplayer;
                                checkForWinner();
                                activeplayer=0;


                            } else {
                                // Computer's turn (chance of 0)
                                int computerMove;
                                do {
                                    computerMove = random.nextInt(9); // Generate a random move
                                } while (gameState[computerMove] != 3); // Ensure the position is not occupied

                                //chnce of 0
                                //currentBtn.setText(activeplayer+"");
                                // Make the computer's move
                                btns[computerMove].setGraphic(new ImageView(new Image("file:src/main/resources/img/zero.png")));
                                gameState[computerMove] = activeplayer;
                                checkForWinner();
                                activeplayer=1;// Switch back to player's turn
                            }
                        }else{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error message");
                            alert.setContentText("Place is already occupied");
                            alert.show();

                        }
                    }
                }
            });
        }
    }

    // This method checks for draw condition :-
    private void checkForDraw() {
        boolean isDraw = true;
        for (int i = 0; i < gameState.length; i++) {
            if (gameState[i] == 3) {
                isDraw = false;
                break;
            }
        }
        if (isDraw && !gameOver) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Draw message");
            alert.setContentText("It's a Draw!");
            alert.show();
            gameOver = true;
            restartButton.setDisable(false);
        }
    }

    //this method check for winner
    private void checkForWinner(){

        //winner
        if(!gameOver){

           for(int wp[]:winingPosition){

               if(gameState[wp[0]]==gameState[wp[1]]&&gameState[wp[1]]==gameState[wp[2]]&&gameState[wp[1]]!=3){
                 // active palyer has winnr
                   Alert alert = new Alert(Alert.AlertType.ERROR);
                   alert.setTitle("Success message");
                   alert.setContentText((activeplayer==1?"👤User " : "💻Computer  ") + " Has Won the game");
                   btns[wp[0]].setBackground(new Background(new BackgroundFill(Color.GREENYELLOW, CornerRadii.EMPTY,Insets.EMPTY)));
                   btns[wp[1]].setBackground(new Background(new BackgroundFill(Color.GREENYELLOW, CornerRadii.EMPTY,Insets.EMPTY)));
                   btns[wp[2]].setBackground(new Background(new BackgroundFill(Color.GREENYELLOW, CornerRadii.EMPTY,Insets.EMPTY)));

                   btns[wp[0]].setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(6))));
                   btns[wp[1]].setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(6))));
                   btns[wp[2]].setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(6))));


                   alert.show();
                   gameOver=true;
                   restartButton.setDisable(false);
                   break;

               }
           }
        }
        checkForDraw();

    }



    public static void main(String[] args) {
        launch();
    }

}