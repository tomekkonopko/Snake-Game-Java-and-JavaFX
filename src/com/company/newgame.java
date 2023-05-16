package com.company;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;


public class newgame
{

    static ArrayList<Integer> wymiary=new ArrayList<Integer>();
    static int wys=40,szer=40;

    static Stage window=new Stage();

    static Slider sliderwys=new Slider();
    static Slider sliderszer=new Slider();


    static  Label infowys=new Label("30");
    static  Label infoszer=new Label("30");
    static  Label mapa=new Label("Tak bedzie wygladac twoja plansza");

    static Button submit=new Button("Submit");


    final static ChoiceBox<String> choiceBox=new ChoiceBox<>();
    static boolean flagachoicebox=true;
    static boolean flaga=true;


    public static void newgame() //ArrayList<Integer>
    {
        if (flagachoicebox)
        {
            setChoiceBox();
        }

        VBox layout= new VBox();
        Scene scene=new Scene(layout,300,300);

        choiceBox.setValue("Normal");

        Canvas c = new Canvas(100,100);
        GraphicsContext gc = c.getGraphicsContext2D();

        final long startNanoTime = System.nanoTime();

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                gc.setFill(Color.BLACK);
                gc.fillRect(0,0,82,82);
                gc.setFill(Color.WHITE);
                gc.fillRect(1,1,80,80);

                gc.setFill(Color.GREEN);
                gc.fillRect(1, 1, 2*sliderszer.getValue(),2*sliderwys.getValue());

            }
        }.start();

        wymiary.add(0,wys);
        wymiary.add(1,szer);
        setWindow();

        setSlider();
        setLabels();

        window.setOnCloseRequest(e->{
            flaga=false;
        });

        submit.setOnAction(e -> {

            flaga=true;
            window.close();
        });



        scene.getStylesheets().addAll(newgame.class.getResource("style.css").toExternalForm()); //stackoverflow dodanie CSS

        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(10);
        layout.getChildren().addAll(mapa,c,sliderwys,infowys,sliderszer,infoszer,choiceBox,submit);
        layout.setId("pane");
        window.setScene(scene);
        window.showAndWait();

    }
    public static int getSzer()
    {
        return szer;
    }
    public static int getWys()
    {
        return wys;
    }

    public static void setSlider()
    {
        sliderwys.setId("jfx-slider");
        sliderwys.setMin(20);
        sliderwys.setMax(40);
        sliderwys.setValue(30);
        sliderwys.setBlockIncrement(1);

        sliderszer.setId("jfx-slider");
        sliderszer.setMin(20);
        sliderszer.setMax(40);
        sliderszer.setValue(30);
        sliderszer.setBlockIncrement(1);


        sliderwys.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                wys=t1.intValue();
                infowys.setText("Ilosc wierszy: "+ Integer.toString(wys));
                wymiary.add(0,wys);
                flaga=true;
            }
        });

        sliderszer.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {

                szer=t1.intValue();
                infoszer.setText("Ilosc kolumn: "+ Integer.toString(szer));
                wymiary.add(1,szer);
                flaga=true;
            }
        });


    }
    public static void setWindow()
    {
        window.setMinWidth(300);
        window.setMinHeight(300);
        window.setMaxHeight(400);
        window.setMaxWidth(400);
        window.setTitle("Rozmiar planszy");
        window.getIcons().add(new Image(SnakeGame.class.getResourceAsStream("snake-icon.png")));

    }

    public static void setLabels()
    {
        mapa.setId("info");

        infowys.setId("info");
        infowys.setText("Ilosc kolumn: "+ 30);

        infoszer.setId("info");
        infoszer.setText("Ilosc kolumn: "+ 30);
    }

    public static boolean getflaga()
    {
        return flaga;
    }

    public static void tick(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.fillRect(1, 1, 2*getSzer(),2*getWys());
    }
    public static String getlevel()
    {
        String level=choiceBox.getValue();

        return level;
    }
    public static void setChoiceBox()
    {
        choiceBox.getItems().addAll("Easy","Normal","Hard");
        flagachoicebox=false;
    }


    }


