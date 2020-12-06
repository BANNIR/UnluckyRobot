/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unluckyrobot;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author shahe
 */
public class UnluckyRobot {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //variable declaration
        int totalScore = 300;
        int x = 0;
        int y = 0;
        char dir = ' ';
        int itrCount = 0;
        int reward = 0;

        //loop
        do {
            //movement
            displayInfo(totalScore, x, y, itrCount);
            dir = inputDirection();
            x = movementX(x, dir);
            y = movementY(y, dir);
            itrCount++;
            //movement penalty
            switch (dir) {
                case 'u':
                    totalScore -= 10; //-10 is the penalty for going up
                    break;
                default:
                    totalScore -= 50; // -50 is the penalty for going down left and right
            }
            //exceeding penalty
            if (doesExceed(x, y, dir)) {
                totalScore -= 2000; //-2000 is the penalty of going out of the grid
                System.out.println("Exceed boundary, -2000 damage applied");
                if (x < 0) {
                    x = 0;
                } else if (x > 4) {
                    x = 4;
                } else if (y < 0) {
                    y = 0;
                }
                y = 4;
            }
            //rewards
            reward = reward();
            reward = punishOrMercy(dir, reward);
            //Score
            totalScore = totalScore + reward;
            System.out.println("");
        } while (!isGameOver(x, y, totalScore, itrCount));
        //evaluation for when you reach end points
        evaluation(totalScore);
    }
    
    /**
     * prints all the info
     * @param totalScore the score of the player
     * @param x the x position of the robot
     * @param y the y position of the robot
     * @param itrCount the amount of iterations
     */
    public static void displayInfo(int totalScore, int x, int y, int itrCount) {
        System.out.printf("For point(X = %d, Y = %d) at iteration: %d the total score "
                + "is %d\n", x, y, itrCount, totalScore);
    }
    
    /**
     * the direction the robot will move
     * @return the direction the user input
     */
    public static char inputDirection() {
        Scanner console = new Scanner(System.in);
        String sDir = "udlrUDLR";
        String c = "";

        do {
            System.out.print("Please input a valid direction: ");
            c = console.next();
        } while (!sDir.contains(c));

        return c.charAt(0);
    }
    
    /**
     * checks to see where to move the robot on the x axis
     * @param x the x position the robot is currently at
     * @param dir the direction the user input
     * @return the new x after moving the robot
     */
    public static int movementX(int x, char dir) {
        dir = Character.toLowerCase(dir);
        switch (dir) {
            case 'u':
                return x + 1;
            case 'd':
                return x - 1;
            default:
                return x;
        }
    }
    
    /**
     * checks to see where to move the robot on the y axis
     * @param y the y position the robot is currently at
     * @param dir the direction the user input
     * @return the new y after moving the robot
     */
    public static int movementY(int y, char dir) {
        dir = Character.toLowerCase(dir);
        switch (dir) {
            case 'l':
                return y - 1;
            case 'r':
                return y + 1;
            default:
                return y;
        }
    }
    
    /**
     * checks if the robot exceed the grid
     * @param x the x position of the robot
     * @param y the y position of the robot
     * @param dir the direction the user input
     * @return true or false statement if the robot exceeded the grid
     */
    public static boolean doesExceed(int x, int y, char dir) {
        return x < 0 || x > 4 || y < 0 || y > 4;
    }
    
    /**
     * gives the player a "reward"
     * @return a value depending on what number you get
     */
    public static int reward() {
        Random rand = new Random();
        int dice = rand.nextInt(6) + 1;
        switch (dice) {
            case 1:
                System.out.println("Dice: 1, reward: -100");
                return -100;
            case 2:
                System.out.println("Dice: 2, reward: -200");
                return -200;
            case 3:
                System.out.println("Dice: 3, reward: -300");
                return -300;
            case 4:
                System.out.println("Dice: 4, reward: 300");
                return 300;
            case 5:
                System.out.println("Dice: 5, reward: 400");
                return 400;
            default:
                System.out.println("Dice: 6, reward: 600");
                return 600;
        }
    }
    
    /**
     * either gives or removes negative score
     * @param dir the direction the robot went
     * @param reward the reward they got this round
     * @return either 0 if they're  lucky or the reward they got
     */
    public static int punishOrMercy(char dir, int reward) {
        Random rand = new Random();
        int coin;
        if (reward < 0 && dir == 'u') {
            coin = rand.nextInt(2);
            if (coin == 0) {
                System.out.println("Coin: tail | Mercy the negative reward is removed.");
                return 0;
            } else {
                System.out.println("Coin: head | No mercy, the negative rewarded is applied.");
                return reward;
            }
        }
        return reward;
    }
    
    /**
     * turns a string given to title case
     * @param str the string the user input
     * @return the string in title case
     */
    public static String toTitleCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1, str.indexOf(" "))
                .toLowerCase() + " " + str.substring(str.indexOf(" ") + 1, str.indexOf(" ") + 2).
                        toUpperCase() + str.substring(str.indexOf(" ")
                        + 2, str.length()).toLowerCase();
    }
    
    /**
     * Checks to see if the user won at the end of the game
     * @param totalScore the score the player accumulated
     */
    public static void evaluation(int totalScore) {
        Scanner console = new Scanner(System.in);
        System.out.print("Please enter your name (only two words): ");
        String str = console.nextLine();

        if (totalScore >= 2000)
            System.out.printf("Victory, %s, your score is %d\n", toTitleCase(str), totalScore);
        else {
            System.out.printf("Mission failed, %s, your score is %d\n",
                    toTitleCase(str), totalScore);
        }
    }
    
    /**
     * Checks to see if the game is over
     * @param x the x position of the robot
     * @param y the y position of the robot
     * @param totalScore the score of the player
     * @param itrCount the amount of iterations the player did
     * @return true or false statement if the game ended or not
     */
    public static boolean isGameOver(int x, int y, int totalScore, int itrCount) {
        return totalScore >= 2000 || totalScore <= -1000
                || itrCount >= 20 || x == 4 && y == 0 || x == 4 && y == 4;
    }
}