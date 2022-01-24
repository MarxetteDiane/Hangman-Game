interface HangmanInterface {
    void intro();
    int playOneGame(String secretWord);
    void displayHangman(int guessCount);
    String createHint(String secretWord, String guessedLetters);
    char readGuess(String guessedLetters);
    String getRandomWord(String filename);
    void stats(int gamesCount, int gamesWon, int best);
    char[] dashes(String secretWord);

}