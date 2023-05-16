package com.company;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.scene.layout.VBox;


public class Main extends Application {

    //ZMIENNE
    Stage window;
    Button buttonnew, buttonhigh, buttonexit, buttonmusicon;
    VBox layout;
    Label eska, welcome;
    Scene scene;
    Image imgnew, imghigh, imgexit, imgon,imgoff;
    MediaPlayer mp;
    ArrayList<Integer> wymiary = new ArrayList<>();
    //ZMIENNE

    @Override
    public void start(Stage stage) throws Exception
    {
        window=stage;
        setWindow();
        setLayout();
        setScene();
        setLabels();
        setButtons();


        String path = Main.class.getResource("music.mp3").toString(); //sciezka do muzyki
        Media media = new Media(path);
        mp = new MediaPlayer(media);
        //mp.play();

        System.out.println("Muzyka start...");


        layout.getChildren().addAll(welcome,eska,buttonnew,buttonhigh,buttonexit,buttonmusicon);
        window.setScene(scene);
        window.show();
    }



    public static void main(String[] args) {
        launch(args);
    }

    private void setWindow()
    {
        window.setTitle("Snake");
        window.setMinWidth(450);
        window.setMinHeight(450);
        window.setMaxHeight(600);
        window.setMaxWidth(600);
        window.getIcons().add(new Image(getClass().getResourceAsStream("snake-icon.png")));
    }
    private void setButtons()
    {
        buttonnew=new Button("New Game");
        buttonnew.setId("buttonnew");
        buttonnew.setStyle("-fx-font-size: 2em; ");
        buttonnew.setOnAction(e ->
        {
            newgame.newgame();
            if (newgame.flaga)
            SnakeGame.start();
        });

        buttonhigh=new Button("Highscore");
        buttonhigh.setId("buttonhigh");
        buttonhigh.setStyle("-fx-font-size: 2em; ");
        buttonhigh.setOnAction(e ->
        {
            try {
                Highscore.display();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });

        buttonexit=new Button("Exit");
        buttonexit.setId("buttonexit");
        buttonexit.setStyle("-fx-font-size: 2em; ");
        buttonexit.setOnAction(e -> window.close());

        imgon=new Image(getClass().getResource("speakeron.png").toExternalForm(),25,25,false,false);
        imgoff=new Image(getClass().getResource("speakeroff.png").toExternalForm(),25,25,false,false);

        buttonmusicon=new Button();
        buttonmusicon.setId("buttonmusicon");

        final ImageView toggleon = new ImageView(imgon);
        final ImageView toggleoff = new ImageView(imgoff);

        buttonmusicon.setGraphic(toggleon);

        buttonmusicon.setOnAction(e -> {
            if(buttonmusicon.getGraphic().equals(toggleon))
            {
                buttonmusicon.setGraphic(toggleoff);
                mp.stop();
                System.out.println("Muzyka off");
            }
            else {
                buttonmusicon.setGraphic(toggleon);
                mp.play();
                System.out.println("Muzyka on...");
            }
        });



        imgnew=new Image(getClass().getResource("newgame-icon.png").toExternalForm(),30,30,true,true);
        buttonnew.setGraphic(new ImageView(imgnew));

        imghigh=new Image(getClass().getResource("highscore.png").toExternalForm(),30,30,true,true);
        buttonhigh.setGraphic(new ImageView(imghigh));

        imgexit=new Image(getClass().getResource("exit-icon.png").toExternalForm(),30,30,true,true);
        buttonexit.setGraphic(new ImageView(imgexit));
    }
    private void setLabels()
    {
        eska=new Label("Tomasz Konopko, s18902");
        eska.setId("eska");
        welcome=new Label("Snake");
        welcome.setId("welcome");
    }
    private void setLayout()
    {
        layout=new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setId("pane");
    }
    private void setScene()
    {
        scene=new Scene(layout,500,500);
        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm()); //stackoverflow
    }






}
