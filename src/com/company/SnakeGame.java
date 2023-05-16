package com.company;


import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class SnakeGame
{
    //LEGENDA PO KODZIE
    /*
#shortcut - shortcut do przerwania gry
#tlo -rysowanie tla
#glowa - dodanie glowy weza na srodku mapy
#glowadraw - rysowanie glowy weza
#score - rysowaanie score
#time -rysowanie czasu
#fruit - rysowanie owocka
#bonusfruit - generowanie bonusowego owocka 1:10
#body - rysowanie weza
#threadtime - watek z czasem
#threadhitwallbody - watek ktory sprawdza czy uderzyl w sciane lub samego siebie



     */

    static int speed = 10,width,height,foodX=0,foodY=0,cornersize=25,score=0,scoreprim=0,time=0,randomfood;
    static List<Corner> snake = new ArrayList<>();
    static Dir direction = Dir.left;
    static boolean gameOver = false;
    static boolean gameOverprim = false;
    static Random rand = new Random();
    static boolean start=true;
    static boolean threadthread=true;
    static boolean hitwallthread=true;


    public enum Dir {
        left, right, up, down
    }


    public static void start()
    {
        width=newgame.getSzer(); // USTALENIE SZEROKOSCI OKIENKA Z GRA
        height=newgame.getWys(); //USTALENIE WYSOKOSCI OKIENKA Z GRA
        if (newgame.getlevel()=="Easy")
        {
            speed=8;
        }
        if (newgame.getlevel()=="Normal")
        {
            speed=13;
        }
        if (newgame.getlevel()=="Hard")
        {
            speed=18;
        }


        Thread thread=new Thread(()-> // WATEK Z CZASEM #threadtime
        {
            System.out.println("start");
            while (threadthread)
            {
                System.out.println(time);
                time++;
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.getMessage();
                }
            }
        });

        //WATEK KTORY SPRAWDZA CZY GLOWA UDERZYLA W SCIAANE LUB SAMEGO SIEBIE #threadhitwallbody
        Thread hitwall=new Thread(()->
        {
            while (true)
            {
                // Koczenie gry w przypadku gdy waz uderzy w samego siebie
                //iteruje po calej "dlugosci weza", jezeli "glowa weza" uderzy w ktorys kawalek ciala weza to gracz przegrywa
                //czyli jesli punkt x(0),y(0)-> glowa uderzy w 1,2,3,4,5...kawalek ciala to gameover=true;
                for (int i = 1; i < snake.size(); i++) {
                    if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                        gameOver = true;
                    }
                }

                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
                if (snake.get(0).y > height) {
                    gameOver = true;
                }
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
                if (snake.get(0).x > width) {
                    gameOver = true;
                }
                try {
                    thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start(); //start liczcenia czasu

        Stage stage=new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
            
            newFruit();
            VBox layout = new VBox();
            Canvas c = new Canvas(width * cornersize, height * cornersize);
            GraphicsContext gc = c.getGraphicsContext2D();
            layout.getChildren().add(c);


                new AnimationTimer() { //DO ROZKIMNY
                    long lastUpdate = 0;

                    public void handle(long now) {

                        if (now - lastUpdate > 1000_000_000 / speed) {
                            lastUpdate = now;
                            tick(gc);
                        }

                        try {
                            if (gameOver) {
                                this.stop();
                                stage.close();
                                thread.stop();
                                hitwall.stop();
                                snake.clear();
                                name.score(time, score, width, height);
                                resetGame();
                            }
                            if (gameOverprim) {
                                this.stop();
                                stage.close();
                                threadthread=false;
                                hitwallthread=false;
                                snake.clear();
                                resetGame();
                            }
                        } catch (IOException ex) {
                            ex.getMessage();
                        }
                    }

                }.start();

            stage.setOnCloseRequest(e-> {
                gameOverprim=true;
                stage.close();
            });
            Scene scene = new Scene(layout, width * cornersize, height * cornersize);

            // control
            scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
                if (key.getCode() == KeyCode.UP && direction != Dir.down) {
                    direction = Dir.up;
                }
                if (key.getCode() == KeyCode.LEFT && direction != Dir.right) {
                    direction = Dir.left;
                }
                if (key.getCode() == KeyCode.DOWN && direction != Dir.up) {
                    direction = Dir.down;
                }
                if (key.getCode() == KeyCode.RIGHT && direction != Dir.left) {
                    direction = Dir.right;
                }
            });

            //SHORTCUT DO PRZERWANIA GRY #shortcut
            KeyCombination comb=new KeyCodeCombination(KeyCode.W,KeyCombination.SHIFT_DOWN);
            scene.setOnKeyPressed(event -> {
                if (comb.match(event))
                {
                    gameOver=true;
                    System.out.println("KOMBINACJA ZADZIALALA");
                }
            });


            // Dodanie weza(glowy) na samym srodku mapy #glowa
            snake.add(new Corner(width / 2, height / 2));


            hitwall.start();

            stage.getIcons().add(new Image(SnakeGame.class.getResourceAsStream("snake-icon.png")));
            stage.setScene(scene);
            stage.setTitle("Snake");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // tick ( DO ROZKMINY)
    public static void tick(GraphicsContext gc) {

            //ruch cialaa snake, bez tej funckji cialo snake nie bedziaalo sie pojawialo
        //przejscie kazdego kawalka ciala o jeden do przodu " "
            for (int i = snake.size() - 1; i >= 1; i--)
            {
                snake.get(i).x = snake.get(i - 1).x;
                snake.get(i).y = snake.get(i - 1).y;
            }

            switch (direction) {
                case up:
                    snake.get(0).y--;
                    break;
                case down:
                    snake.get(0).y++;
                    break;
                case left:
                    snake.get(0).x--;
                    break;
                case right:
                    snake.get(0).x++;
                    break;

            }

            // Jedzenie owockow 3 przypadki
            //1 przypadek, ->** jesli glowa weza (x,y) bedzie rowna x,y owocka** <- ,a dlugosc weza bedzie wieksza od 1 to zostanie dodany nowy kawalek ciala weza i wygeneruje sie nowy owoc na mapie
            if (foodX == snake.get(0).x && foodY == snake.get(0).y && randomfood >= 1) // 1)
            {
                snake.add(new Corner(-1, -1));
                System.out.println();
                newFruit();
                score++;
            }
            // 2) jesli **...** i bedzie wygenerowany bonus owoc oraz dlugosc weza bedzie wieksza od 2(czyli minimum waz bedzie miec dwa kawaalki ciala) to
            //zostanie odjety ostatni kawalek ciala weza, wygeneruje sie nowy owoc oraz gracz zdobedzie punkt
            else if (foodX == snake.get(0).x && foodY == snake.get(0).y && randomfood == 0 && snake.size() > 2) {
                snake.remove(snake.size() - 1);
                newFruit();
                score++;
            }
            // 3) jesli **...** i bedzie wygenerowany bonusowy owoc oraz dlugosc weza bedzie mniejsza badz rowna 2(czyli waz bedzie miec jeden kawalek ciala lub mniej(sama glowa))
            // to gracz zdobedzie punkt, zostanie wygenerowany nowy owoc, ale nie zostanie dodany, ani odjety zaden kawalek jego ciala
            else if (foodX == snake.get(0).x && foodY == snake.get(0).y && randomfood == 0 && snake.size() <= 2) {
                newFruit();
                score++;
            }



            // Rysowanie tła #tlo
            //gc.setFill(Color.BLACK);
            //gc.fillRect(0, 0, width * cornersize, height * cornersize);
            Image backgroundimage = new Image("com/company/imagebackground.png");
            gc.drawImage(backgroundimage, 0, 0, width * cornersize, height * cornersize);

            // Pokazywanie i rysowanie aktualnego wyniku gracza(zjedzone owocki) #score
            scoreprim=(height*width)/time*score;
            gc.setFill(Color.WHITE);
            gc.setFont(new Font("", 30));
            gc.fillText("Score: " + scoreprim, 10, 30);

            //Pokazywanie i rysowanie aktualnego czasu ktory gracz spedzil w grze w sec #time
            gc.setFill(Color.WHITE);
            gc.setFont(new Font("", 30));
            gc.fillText("Time(sec): " + time, 150, 30);

            // Fruit-> rysowanie obrazka jablka lub anansa zalezne od tego jaki typ owocaa sie wygeneruje normalny czy bonusowy #fruit
            if (randomfood >= 1) {
                Image image = new Image("com/company/fruit.png");
                gc.drawImage(image, foodX * cornersize, foodY * cornersize);
            } else if (randomfood == 0) {
                Image imagebonus = new Image("com/company/pineapple.png");
                gc.drawImage(imagebonus, foodX * cornersize, foodY * cornersize);
            }


            // SNAKE-> rysowanie snake na planszy #body
        int i=1;
            for (Corner c : snake) //foreach
            {
                //rysowanie ciala snake'a
                gc.setFill(Color.GREEN);
                if (c.x == snake.get(0).x && c.y == snake.get(0).y) //GLOWA
                {
                    if (direction == Dir.left) {
                        Image imageheadsnakeleft = new Image("com/company/headleft.png");
                        gc.drawImage(imageheadsnakeleft, snake.get(0).x * cornersize, snake.get(0).y * cornersize);
                    }
                    if (direction == Dir.right) {
                        Image imageheadsnakeright = new Image("com/company/headright.png");
                        gc.drawImage(imageheadsnakeright, snake.get(0).x * cornersize, snake.get(0).y * cornersize);
                    }
                    if (direction == Dir.up) {
                        Image imageheadsnakeup = new Image("com/company/headup.png");
                        gc.drawImage(imageheadsnakeup, snake.get(0).x * cornersize, snake.get(0).y * cornersize);
                    }
                    if (direction == Dir.down) {
                        Image imageheadsnakedown = new Image("com/company/headdown.png");
                        gc.drawImage(imageheadsnakedown, snake.get(0).x * cornersize, snake.get(0).y * cornersize);
                    }
                }


                else if (c.x == snake.get(snake.size() - 1).x && c.y == snake.get(snake.size() - 1).y) //TAIL
                {


                    if (snake.get(i).x - snake.get(i-1).x == 0 && snake.get(i).y - snake.get(i-1).y > 0)
                    {
                        Image imagetail = new Image("com/company/tailup.png");
                        gc.drawImage(imagetail, c.x * cornersize, c.y * cornersize);

                    }
                    if (snake.get(i).x - snake.get(i-1).x == 0 && snake.get(i).y - snake.get(i-1).y < 0)
                    {
                        Image imagetail = new Image("com/company/taildown.png");
                        gc.drawImage(imagetail, c.x * cornersize, c.y * cornersize);

                    }
                    if (snake.get(i).y - snake.get(i-1).y == 0 && snake.get(i).x - snake.get(i-1).x > 0)
                    {
                        Image imagetail = new Image("com/company/tailright.png");
                        gc.drawImage(imagetail, c.x * cornersize, c.y * cornersize);

                    }
                    if (snake.get(i).y - snake.get(i-1).y == 0 && snake.get(i).x - snake.get(i-1).x < 0)
                    {
                        Image imagetail = new Image("com/company/tailleft.png");
                        gc.drawImage(imagetail, c.x * cornersize, c.y * cornersize);

                    }

                } else //BODY
                    {
                            Boolean t=true;
                        if (snake.size()>=3 && i>=1 && i<=snake.size()-2)
                        {
                            //PRAWO GORA
                            if (    snake.get(i).x - snake.get(i - 1).x == 0
                                    && snake.get(i).y - snake.get(i - 1).y > 0
                                    && snake.get(i).x - snake.get(i + 1).x > 0
                                    && snake.get(i).y - snake.get(i + 1).y == 0)

                            {
                                Image elka1 = new Image("com/company/elka1.png");
                                gc.drawImage(elka1, c.x * cornersize, c.y * cornersize);
                                t=false;
                            }
                            //LEWO W GORE
                            if (    snake.get(i).x - snake.get(i - 1).x == 0
                                    && snake.get(i).y - snake.get(i - 1).y > 0
                                    && snake.get(i).x - snake.get(i + 1).x < 0
                                    && snake.get(i).y - snake.get(i + 1).y == 0)

                            {
                                Image elka4 = new Image("com/company/elka4.png");
                                gc.drawImage(elka4, c.x * cornersize, c.y * cornersize);
                                t=false;

                            }
                            // PRAWO DOL
                            if (    snake.get(i).x - snake.get(i - 1).x > 0
                                    && snake.get(i).y - snake.get(i - 1).y == 0
                                    && snake.get(i).x - snake.get(i + 1).x == 0
                                    && snake.get(i).y - snake.get(i + 1).y < 0)
                            {
                                Image elka2 = new Image("com/company/elka2.png");
                                gc.drawImage(elka2, c.x * cornersize, c.y * cornersize);
                                t=false;

                            }
                            // GORA PRAWO
                            if (    snake.get(i).x - snake.get(i - 1).x < 0
                                    && snake.get(i).y - snake.get(i - 1).y == 0
                                    && snake.get(i).x - snake.get(i + 1).x == 0
                                    && snake.get(i).y - snake.get(i + 1).y < 0)
                            {
                                Image elka3 = new Image("com/company/elka3.png");
                                gc.drawImage(elka3, c.x * cornersize, c.y * cornersize);
                                t=false;

                            }
                            // LEWO DOL
                            if (    snake.get(i).x - snake.get(i - 1).x == 0
                                    && snake.get(i).y - snake.get(i - 1).y < 0
                                    && snake.get(i).x - snake.get(i + 1).x < 0
                                    && snake.get(i).y - snake.get(i + 1).y == 0)
                            {
                                Image elka3 = new Image("com/company/elka3.png");
                                gc.drawImage(elka3, c.x * cornersize, c.y * cornersize);
                                t=false;

                            }
                            //GORA LEWO
                            if (    snake.get(i).x - snake.get(i - 1).x == 0
                                    && snake.get(i).y - snake.get(i - 1).y < 0
                                    && snake.get(i).x - snake.get(i + 1).x > 0
                                    && snake.get(i).y - snake.get(i + 1).y == 0)
                            {
                                Image elka3 = new Image("com/company/elka2.png");
                                gc.drawImage(elka3, c.x * cornersize, c.y * cornersize);
                                t=false;

                            }
                            // DOL LEWO
                            if (    snake.get(i).x - snake.get(i - 1).x > 0
                                    && snake.get(i).y - snake.get(i - 1).y == 0
                                    && snake.get(i).x - snake.get(i + 1).x == 0
                                    && snake.get(i).y - snake.get(i + 1).y > 0)
                            {
                                Image elka3 = new Image("com/company/elka1.png");
                                gc.drawImage(elka3, c.x * cornersize, c.y * cornersize);
                                t=false;

                            }
                            // DOL PRAWO
                            if (    snake.get(i).x - snake.get(i - 1).x < 0
                                    && snake.get(i).y - snake.get(i - 1).y == 0
                                    && snake.get(i).x - snake.get(i + 1).x == 0
                                    && snake.get(i).y - snake.get(i + 1).y > 0)
                            {
                                Image elka3 = new Image("com/company/elka4.png");
                                gc.drawImage(elka3, c.x * cornersize, c.y * cornersize);
                                t=false;

                            }


                        }


                    //roznica kolumn pierwszego i nastepnego musi byc rowna zero, a roznica wierszy, musi byc rozna od zera
                        if (snake.size()-1==i)
                        {

                        }
                        else if (t)
                        {
                                if ((snake.get(i).x - snake.get(i - 1).x) == 0 && snake.get(i).y - snake.get(i - 1).y != 0)
                                {

                                    Image imagebodypion = new Image("com/company/bodyup.png");
                                    gc.drawImage(imagebodypion, c.x * cornersize, c.y * cornersize);

                                }
                                else
                                {
                                    Image imageheadsnakedown = new Image("com/company/bodynormal.png");
                                    gc.drawImage(imageheadsnakedown, c.x * cornersize, c.y * cornersize);

                                }

                        }
                        //x kolumna, y wiersz
                        i++;
//                    gc.setFill(Color.BLACK);
//                    gc.fillOval(c.x * cornersize, c.y * cornersize, cornersize - 1, cornersize - 1);
//                    gc.setFill(Color.GREEN);
//                    gc.fillOval(c.x * cornersize, c.y * cornersize, cornersize - 3, cornersize - 3);

                    }



                }
            }


    // food DO ROZKIMNY
    public static void newFruit() {

        foodX = rand.nextInt(width);
        foodY = rand.nextInt(height);
        randomfood=rand.nextInt(10); //#bonusfruit

        System.out.println("Randomowa liczba jakie jablko: "+randomfood);

    }
    public static void resetGame()
    {
        gameOver = false;
        speed = 10;
        foodX = 0;
        foodY = 0;
        cornersize = 25;
        score = 0;
        time = 0;
        width=0;
        height=0;
    }





}
