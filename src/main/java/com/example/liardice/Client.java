package com.example.liardice;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client{

    public static void main(String[] args) {
        playGame game = new playGame(args);
    }

}

class playGame extends Application{
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private ArrayList<Dice> hand = new ArrayList<>();
    private int handSize;
    private int playerNum;
    private int roundNum;
    private int currentFace;
    private int currentNum;
    private Scanner input;

    public playGame(String[] args) {
        try {
            launch(args);

            input = new Scanner(System.in);

            socket = new Socket("localhost", 8000);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            playerNum = in.readInt();
            System.out.println("Welcome Player: " + playerNum);

            while (true) {
                fillHand();

                boolean newRound = true;

                do {
                    currentFace = in.readInt();
                    currentNum = in.readInt();

                    System.out.println("Current face: " + currentFace + " current num: " + currentNum);


                    boolean isTurn = in.readBoolean();
                    if (isTurn == true) {
                        playTurn();
                    }
                    newRound = in.readBoolean();
                } while (newRound);

            }
        } catch (IOException e) {

        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane pane = new BorderPane();

        Scene scene = new Scene(pane,500,500);
        stage.setScene(scene);
        stage.show();
    }


    public void playTurn() {
        try {
            System.out.println("Please enter in -1 to accuse 0 to guess face 1 to guess num");
            int guess = input.nextInt();
            out.writeInt(guess);
            if (guess == 0) {
                System.out.println("Please enter a face ");
                int faceGuess = input.nextInt();
                while (faceGuess <= currentFace) {
                    System.out.println("Please enter a face ");
                    faceGuess = input.nextInt();
                }
                out.writeInt(faceGuess);
                System.out.println("Guessed " + faceGuess);
            } else if (guess == 1) {
                System.out.println("Please enter a number ");
                int numGuess = input.nextInt();
                while (numGuess <= currentNum) {
                    System.out.println("Please enter a number ");
                    numGuess = input.nextInt();
                }
                out.writeInt(numGuess);
            }

        } catch (IOException e) {

        }
    }

    public void fillHand() {
        try {
            handSize = in.readInt();
            for (int i = 0; i < handSize; i++) {
                hand.add(new Dice(in.readInt()));
                System.out.println(hand.get(i));
            }
        } catch (IOException e) {

        }

        System.out.println("Current hand size: " + handSize);
    }
}


