package com.company;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;

public class name
{
    static int wynik;
    static String wyniktosend;

    static Stage window=new Stage();
    static Alert errorAlert=new Alert(Alert.AlertType.ERROR);
    static Label setname=new Label("Enter your nickname");
    static TextField nameinput=new TextField();
    static Button submit=new Button("Submit");
   // static BufferedWriter writer;



//    static {
//        try {
//
//            //writer = new BufferedWriter(new FileWriter("highscore.txt",true));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

   public static void score(int time, int score,int szer, int wys) throws IOException
   {
       //system liczenia punktow = wys x szer * punkty dzielone przez czas
       VBox layout=new VBox(10);
       Scene scene=new Scene(layout,200,200);

       wynik= (wys*szer)/time*score; //liczenie wyniku
       wyniktosend= String.valueOf(wynik); //branie inta jako Stringa, zeby zapisac do pliku

       setWindow();  //ustawienie okna
       setLabel(); //ustawienie labelow
       setButtons(); //ustawienie buttonow -> setonAction


       scene.getStylesheets().addAll(name.class.getResource("style.css").toExternalForm()); //stackoverflow dodanie CSS
       layout.setId("layoutname");
       layout.setAlignment(Pos.CENTER);
       layout.getChildren().addAll(setname,nameinput,submit);
       window.setScene(scene);
       window.show();

   }
    public static void setWindow()
    {
        window.setTitle("Save your score");
        window.setMinWidth(300);
        window.setMinHeight(200);
        window.setMaxHeight(300);
        window.setMaxWidth(400);
        window.setHeight(200);
        window.setWidth(300);
        window.getIcons().add(new Image(SnakeGame.class.getResourceAsStream("snake-icon.png")));

    }

    public static void setLabel()
    {
        setname.setId("setname");
    }

    public static void setButtons()
    {
        submit.setOnAction(e ->
        {
            if (nameinput.getText().isEmpty())
            {
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("Your nickname must have some characters");
                errorAlert.showAndWait();
            }
            else
            {
                try {

                    FileWriter writer=new FileWriter("C:\\Users\\Tomek\\Desktop\\Snake-PROJEKTjavafxpozajeciach\\highscore.txt",true);
                    BufferedWriter bw=new BufferedWriter(writer);



                    System.out.println(nameinput.getText());
                    System.out.println(wyniktosend);


                    bw.write(nameinput.getText());
                    bw.newLine();
                    bw.write(wyniktosend);
                    bw.newLine();
                    bw.close();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }


                window.close();
            }
        });
    }


}
