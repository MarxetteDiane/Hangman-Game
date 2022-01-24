import acm.program.*;
import java.io.*;
import acm.util.*;
import java.applet.AudioClip;

public class Hangman extends ConsoleProgram implements HangmanInterface {

    @Override
    public void run() {
        AudioClip newGame = MediaTools.loadAudioClip("assets/newGame.wav");               // audio for when a game starts
        AudioClip win = MediaTools.loadAudioClip("assets/win.wav");                    // audio for when the user guesses the word
        AudioClip endGame = MediaTools.loadAudioClip("assets/endGame.wav");               // audio for when the user decides to stop playing
        int gamesCount = 0;
        int gamesWon = 0;
        boolean status = true;
        int best = 0;
        intro();

        String fileName = promptUserForFile("\nEnter the dictionary filename of your choice:\n - dict.txt" +
                "\n - large.txt\n > ", "assets/");       // asks the user to select a file
        while (status) {
            boolean stat = true;
            String secretWord = getRandomWord(fileName);                             // selects a random word from the file selected
            newGame.play();
            int guessCount = playOneGame(secretWord);                             // plays the game
            gamesCount++;

            if (guessCount > 0) {
                print("\n\nYou Win! The Secret Word was " + secretWord);
                win.play();
                gamesWon += 1;
                if (best < guessCount){
                    best = guessCount;
                }
            }
            if (guessCount == 0) {
                print("\n\nYou Lose! The Secret Word was " + secretWord);
            }
            while (stat) {                                                       // re-prompts if input is not in choices
                String play = readLine("\n\nWould you like to play again? Y/N\n").toUpperCase();    // converts input into uppercase
                if (play.equals("Y")) {
                    newGame.play();
                    status = true;
                    stat = false;
                }
                else if (play.equals("N")) {
                    endGame.play();
                    stats(gamesCount, gamesWon, best);
                    status = false;
                    stat = false;
                }
            }
        }
    }

    @Override
    // introductory message at the start of the program
    public void intro() {
        println("   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        println("             Welcome to Hangman!         ");
        println("       I will think of a random word.    ");
        println("      You'll try to guess its letters.");
        println("       Every time you guess a letter");
        println("     that isn't in my word, a new body");
        println("      part of the hanging man appears.");
        println("                  Goodluck!");
        println("   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }

    @Override
    // plays the game
    public int playOneGame(String secretWord) {
        AudioClip correct = MediaTools.loadAudioClip("assets/correct.wav");
        AudioClip invalid = MediaTools.loadAudioClip("assets/invalid.wav");
        int guessCount = 8;
        String guessedLetters = "";
        char[] filler = dashes(secretWord);                     // creates an array of dashes as hint
        displayHangman(guessCount);                                 // displays the hangman graphics

        print("\nSecret Word: " + createHint(secretWord, guessedLetters));
        print("\nYour Guesses: " + guessedLetters);
        print("\nGuesses Left: " + guessCount);

        while (guessCount > 0) {
            char letter = readGuess(guessedLetters);
            String guess = String.valueOf(letter);              // converts a char into a String

            if (secretWord.contains(letter+"")) {                       // checks whether the user's guess is in the secret word
                for (int i = 0; i < secretWord.length(); i++) {
                    if (secretWord.charAt(i) == letter) {
                        filler[i] = letter;
                    }
                }
                if (!guessedLetters.contains(guess)){           // checks whether the user's guess has not yet been guessed
                    print("\nCorrect!\n");
                    if (!secretWord.equals(String.valueOf(filler))){
                        correct.play();
                    }
                }
            }
            else if ((!secretWord.contains(letter+""))&&(!guessedLetters.contains(guess))){     // checks whether the user's guess is NOT in the secret word
                print("\nIncorrect.\n");                                                        // and has NOT been guessed yet
                guessCount--;               //number of allowed guesses decrease
                displayHangman(guessCount);
                AudioClip incorrect = MediaTools.loadAudioClip("assets/incorrect"+guessCount+".wav");
                incorrect.play();
            }

            if (guessedLetters.contains(guess)) {                               // checked whether the user's guess has already been guessed
                print("\nYou already guessed that letter.\n");
                invalid.play();
            } else guessedLetters += letter;

            String newFiller = new String(filler);                              // converts the filler Array into a String
            print("\nSecret Word: " + newFiller);
            print("\nYour Guesses: " + guessedLetters);
            print("\nGuesses Left: " + guessCount);

            if (secretWord.equals(String.valueOf(filler))){     //checks whether word is complete and returns guessCount
                return guessCount;
            }
        }
        if (guessCount == 0) {              //returns guessCount = 0 if guessCount is 0
            return guessCount;
        }
        return guessCount;
    }

    @Override
    // converts an array into string
    public String createHint(String secretWord, String guessedLetters) {
        char[] filler = dashes(secretWord);
        String newFiller = new String(filler);
        return newFiller;
    }

    @Override
    // creates an array as a hint for the secret word
    public char[] dashes(String secretWord) {
        char[] filler = new char[secretWord.length()];
        for (int i = 0; i < secretWord.length(); i++) {
            filler[i] = '-';
            if (secretWord.charAt(i) == ' ') {
                filler[i] = ' ';
            }
        }
        return filler;
    }

    @Override
    // reads the user's guess in String and returns a char variable
    public char readGuess(String guessedLetters) {
        AudioClip invalid = MediaTools.loadAudioClip("assets/invalid.wav");
        String guess = readLine("\nYour Guess: ").toUpperCase();        // reads the input and transforms into an uppercase
        while (guess.length() != 1) {                                               // checks whether the user has input a one-character guess
            print("\nType a single letter from A-Z.\n");
            invalid.play();
            guess = readLine("\nYour Guess: ").toUpperCase();
        }
        char letter = guess.charAt(0);                                              // converts the user's String input into char
        while (!Character.isLetter(letter)) {                                   // checks whether the user has input a letter
            print("\nType a single letter from A-Z.\n");
            invalid.play();
            guess = readLine("\nYour Guess: ").toUpperCase();
            letter = guess.charAt(0);
        }

        return letter;
    }

    @Override
    // calls for the canvas methods to display hangman
    public void displayHangman(int guessCount) {
        canvas.reset();
        canvas.displayHangman(guessCount);                      // displays the hangman
    }

    @Override
    // display the statistics of the user's gameplay
    public void stats(int gamesCount, int gamesWon, int best) {
        float winRate = gamesWon*100/gamesCount*100;                // computes for the winRate
        winRate = winRate/100;
        println("   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        println("             Overall Statistics:         ");
        println("              Games played: "+gamesCount);
        println("                Games won: "+gamesWon);
        println("              Win percent: "+winRate+"%");
        println("      Best game: "+best+" guess(es) remaining");
        println("             Thanks for playing");
        println("   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }

    @Override
    // returns a random word as a secret word
    public String getRandomWord(String filename) {
        String secretWord="";
        RandomGenerator random = RandomGenerator.getInstance();
        try{
            BufferedReader file = new BufferedReader(new FileReader(filename));
            String lines = file.readLine();                         // reads the first line of the file
            int words = Integer.parseInt(lines);                            // converts the String into an Integer
            print("\n");
            int wordNumber = random.nextInt(1,words);
            for(int i=0;i<=wordNumber;i++)
            {
                secretWord=file.readLine();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return secretWord;
    }

    @Override
    public void init() {
        canvas = new HangmanCanvas();
        add(canvas);
    }
    /* Solves NoClassDefFoundError */
    public static void main(String[] args) {
        new Hangman().start(args);
    }
    // private HangmanCanvas canvas;
    private HangmanCanvas canvas;
}
