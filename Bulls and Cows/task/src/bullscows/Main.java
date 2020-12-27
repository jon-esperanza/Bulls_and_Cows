package bullscows;

import java.util.Random;
import java.util.Scanner;

public class Main {


    // main method
    public static void main(String[] args) {
        GameLog gl = new GameLog();
        initiateGame(gl);
        if(gl.code != null) {
            turnMenu(gl);
        }
     }

    // GameLog Class
    public static class GameLog {
        char[] code; // stores code in char[] form
        String secret; // stores secret in String
        boolean isCorrect = false; // stores boolean for comparing
        GameLog() {}
        GameLog(char[] code, String secret) {
            this.code = code;
            this.secret = secret;
        }

        //method used to generate a random code
        public void generateCode(int length, int end) {
            Random rand = new Random();
            String randomCode = ""; //main string
            for(int i = 0; i < length; i++) {
                int r = rand.nextInt(end);
                String random = "" + getCharFromSymbols(r); // grab random char from pool
                while(randomCode.contains(random)) { // while main string contains char keep updating random char
                    r = rand.nextInt(end);
                    random = "" + getCharFromSymbols(r);
                }
                randomCode += random; // once it finds a unique char add it to main string
            }
            System.out.println(randomCode); // print out rand code
            this.setSecret(randomCode); // set string code to secret
            char[] code = randomCode.toCharArray();
            this.setCode(code); // set rand code array to code
        }
        // method compares code to an answer
        public void compareTo(char[] answer) {
            if(this.getBulls(answer) == secret.length()) { this.isCorrect = true; }else{ this.isCorrect = false;} // if bulls = length of code
        }

        //GETTERS
        // method finds bulls in an answer
        private int getBulls(char[] answer) {
            int bull = 0;
            for(int i = 0; i < answer.length; i++) { // find numbers in the same place
                if(this.code[i] == answer[i]) {
                    bull++;
                }
            }
            return bull;
        }
        //method finds cows in an answer
        private int getCows(char[] answer) {
            int cows = 0;
            for(int i = 0; i < answer.length; i++) { // find numbers anywhere in the code
                if(secret.contains(Character.toString(answer[i]))) {
                    cows++;
                }
            }
            return cows - this.getBulls(answer); // subtract any numbers already found
        }
        // method returns String secret;
        public String getCode() { return " The secret code is " + secret; }
        // method gets String form of result from comparing code to answer
        public String getResult(char[] ans) {
            String result = "Grade:";
            if(this.getBulls(ans) == 0 && this.getCows(ans) == 0) {
                result += " None.";
                return result;
            }
            if(this.getBulls(ans) > 0) {
                result += " " + this.getBulls(ans);
                result += this.getBulls(ans) == 1 ? " bull" : " bulls";
            }
            if(this.getCows(ans) > 0) {
                result += " " + this.getCows(ans);
                result += this.getCows(ans) == 1 ? " cow." : " cows.";
            }else {
                result += ".";
            }
            return result;
        }

        //SETTERS
        // method sets char[] code for gamelog object
        public void setCode(char[] code) { this.code = code; }
        // method sets String secret for gamelog object
        public void setSecret(String s) { this.secret = s; }


    }

    // method initiates game
        // checks all preconditions and sets up gameLog
     public static void initiateGame(GameLog gl) {
        // scan length
         String sLength = getStringInput("Input the length of the secret code: ");
         // string preconditions for length
         if(isNumber(sLength) == false) {
             System.out.println("Error: Your input isn't a valid number");
             return;
         }
         int length = Integer.parseInt(sLength); // convert length to int
         // integer preconditions for length
         if(length == 0) {
             System.out.println("Error: Please enter a length greater than zero.");
             return;
         }
         // scan end
         String sEnd = getStringInput("Input the number of possible symbols in the code: ");
         // string preconditions for end
         if(isNumber(sEnd) == false) {
             System.out.println("Error: Your input isn't a valid number");
             return;
         }
         int end = Integer.parseInt(sEnd); // convert end to int
         // integer preconditions for end
         if (end < length) {
             System.out.println("Error: cannot generate code of length " + length + " with " + end + " unique symbols.");
             return;
         }
         if(length > 36 || end > 36) {
             System.out.println("Error: Can only generate length of numbers less than 36\n" +
                     "Your length: " + length +"\nYour Possible Symbols: " + end);
             return;
         }

         // once conditions met
         gl.generateCode(length, end); // get random code
         String censored = "";
         for (int i = 0; i < length; i++) { // create censored version of code
             censored += "*";
         }
         String alpEnd = end > 10 ? ", a-" + getCharFromSymbols(end - 1) + ")." : ""; // checks alpha range
         String numEnd = end > 10 ? "(0-9" : "(0-" + getCharFromSymbols(end - 1) + ")."; // checks num range
         System.out.println("The secret is prepared: " + censored + " " + numEnd + alpEnd);
    }

    //method grabs a symbol from available pool
     public static char getCharFromSymbols(int index) {
        String alpha = "0123456789abcdefghijklmnopqrstuvwxyz";
        return alpha.charAt(index);
     }
     //method checks if String is numeric
     public static boolean isNumber(String s) {
        for(int i = 0; i < s.length(); i ++) {
            if(!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
     }
     // method runs the loop that asks for turn answer
     public static void turnMenu(GameLog gl) {
        int turnCount = 1;
        while(!gl.isCorrect) {
            char[] ans = getStringInput("Turn " + turnCount + ":").toCharArray();
            String grade = "Grade: ";
            System.out.println(gl.getResult(ans));
            gl.compareTo(ans);
            if(gl.isCorrect) {
                System.out.println("Congratulations! The secret code is " + gl.secret);
            }
            turnCount++;
        }

    }
    // method scans for input using a prompt
    public static int getIntegerInput(String prompt) {
        Scanner scan = new Scanner(System.in);
        System.out.println("\n" + prompt);
        return scan.nextInt();
    }
    // method scans for input using a prompt
    public static String getStringInput(String prompt) {
        Scanner scan = new Scanner(System.in);
        System.out.println("\n" + prompt);
        return scan.nextLine();
    }
}
