package com.company;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;
import javafx.scene.control.cell.PropertyValueFactory;


public class Highscore
{
    static Stage window=new Stage();
    static Button exit=new Button("Exit");
    private static TableView<Inputhighscore> table = new TableView();
    static ObservableList<Inputhighscore> data= FXCollections.observableArrayList();
    static TableColumn nicknamecol=new TableColumn("Nickname");
    static TableColumn idcol=new TableColumn("Miejsce");
    static TableColumn scorecol=new TableColumn("Score");
    static ArrayList<Inputhighscore> sublist=new ArrayList<>();
    static TreeMap<Integer,String> mapasortujaca=new TreeMap<>(Collections.reverseOrder());


    public static void display() throws FileNotFoundException {

        setWindow();
        setButtons();
        //ZMIENNE
        String nickname;
        String score;
        File file = new File("highscore.txt");
        Scanner odczyt=new Scanner(file);
        //ZMIENNE

        int pomocnicza=1;

            //ZCZYTYWANIE WARTOSCI DO TREEMAPY
            while(odczyt.hasNextLine())
            {
                nickname=odczyt.nextLine();
                score= odczyt.nextLine();
                mapasortujaca.put(Integer.parseInt(score),nickname); // KLUCZ - SCORE VALUE-NICKNAME
            }
            odczyt.close();

            //SORTOWANIE TREEMAPY I ZAPIS DO PLIKU W POPRAWNEJ KOLEJNOSCI
            Set set = mapasortujaca.entrySet();
            Iterator i = set.iterator();
            PrintWriter zapis = new PrintWriter("highscore.txt");
            while (i.hasNext())
            {
            Map.Entry me = (Map.Entry)i.next();
            zapis.println(me.getValue());
            zapis.println(me.getKey());

            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
            }
            zapis.close();


            // ZCZYTYWANIE WARTOSCI DO SUBLISTY-ARRAYLISTY z typem klasay inputhighscore(potrzebna do wrzucenia tego do klasy TABLEVIEW)
            Scanner in=new Scanner(file);
            while (in.hasNextLine())
            {
                nickname=in.nextLine();
                score= in.nextLine();
                sublist.add(new Inputhighscore(Integer.toString(pomocnicza),nickname,Integer.valueOf(score)));
                pomocnicza++;
            }
             pomocnicza=1;

            data.clear(); //czyszczenie ObservableList zeby nie doublowaly sie wartosci w TABLEVIEW
            data.addAll(sublist); //dodanie scorow ktore sa POSORTOWANE do ObservableList
            sublist.clear(); //czyszczenie ARRAYLISTY zeby nie doublowaly sie wartosci w TABLEVIEW


        final Label highscore=new Label("Highscores");
        highscore.setFont(new Font("Arial",20));

        table.setEditable(false);

        idcol.setCellValueFactory(new PropertyValueFactory<Inputhighscore,Integer>("id"));
        idcol.setSortable(false);
        nicknamecol.setCellValueFactory(new PropertyValueFactory<Inputhighscore,String>("nickname"));
        nicknamecol.setSortable(false);
        scorecol.setCellValueFactory(new PropertyValueFactory<Inputhighscore,Integer>("score"));
        scorecol.setSortable(true);

        table.setItems(data);//dodanie ObservableList do tableview gdzie wartosci sa posorotwane
        table.getColumns().clear(); //wyczyszczenie kolumn zeby nie byly zdoublowane

        table.getColumns().addAll(idcol,nicknamecol,scorecol);


        VBox layout=new VBox(10);

        layout.getChildren().addAll(table); //dodanie "dzieci" do layout vbox //usuaanelm button exit
        layout.setAlignment(Pos.CENTER); //ustawienie wszystkiego jako wycentrowane
        Scene scene=new Scene(layout);
        scene.getStylesheets().addAll(newgame.class.getResource("style.css").toExternalForm()); //stackoverflow dodanie css
        in.close();
        window.setScene(scene);
        window.show();

    }
    public static void setWindow()
    {
        //window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Highscore");
        window.getIcons().add(new Image(Highscore.class.getResourceAsStream("highscore.png")));
        window.setMinWidth(300);
        window.setMinHeight(300);
        window.setMaxHeight(400);
        window.setMaxWidth(400);
        window.setWidth(350);
        window.setHeight(350);
    }
    public static void setButtons()
    {
        exit.setOnAction(e -> window.close()); //uzycie lambdy
        exit.setPrefWidth(70);
        exit.setPrefHeight(40);
    }
}
