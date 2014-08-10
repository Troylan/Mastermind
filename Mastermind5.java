/*
 * Mastermind
 * Author: Troylan Tempra Jr
 * Created: November 16, 2011
 * Mod: November 26, 2011
 * Description: Java rendition of the Mastermind boardgame.
 */

import java.io.*;

public class Mastermind5
{
  /* Yellow = 1;
   * Orange = 2;
   * Red = 3;
   * Blue = 4;
   * Green = 5;
   * Purple = 6;
   */ 
  InputStreamReader isr = new InputStreamReader (System.in);
  BufferedReader br = new BufferedReader (isr);
  int[][] board = new int[12][4];//Array used to store the board, this is used to help print colours and make a visual aid for the user. 
  //It stores the colours as represented by integers. See line 13 to 18
  
  int[][] instances = new int[7][3];/*
   This array is core to the algorithm used to find the number of white keys.
   The first dimension is the colour, and the positions of the second dimension represent the guess, the code and the least of the two respectively.
   It is used to store the number of instances a colour appears in the code and guess and the least of both.
   Eg:    If the guess is 3321 or [Red][Red][Ora][Yel]   
          And the code is 1131 or [Yel][Yel][Red][Yel]
   
         The array will store the following integers as Data:
         
         Colour         Instances in Guess         Instances in Code           Minimum Instances
         
         Yellow                [1]                       [3]                          [1]
         Orange                [1]                       [0]                          [0]                 
         Red                   [2]                       [1]                          [1]  
         Blue                  [0]                       [0]                          [0]
         Green                 [0]                       [0]                          [0]  
         Purple                [0]                       [0]                          [0]
   
   This array is used in a method which adds the whole third column(Minimum Instances) in order to arrive at the total number of key pegs.
  */
  
  int[][] keys = new int[12][4];//Array used to store the feedback in a similar fashion and purpose as the board[][].
  //It stores integers where the presence black keys are represented by the number 2, white keys by 1, and empty keyholes for 0,
  int black_keys = 0;// temporary variable that is cleared each time the program shuffles the keys to be printed out. 
  //This stores the number of black keys at the point when the program analyzes a guess
  int white_keys = 0;//similar to black_keys
  int key_total = 0;//sum of white and black keys, this is found BEFORE the white keys
  int controlval_1 = 0;//used in the code which controls the number of printed white keys. 
  //Essentially starts as a copy of black_keys in that codeblock and increases at each iteration of that codeblock
  //This is used to limit the number of white keys printed such that the total number of keys printed is exact
  
  int[] code = new int[4]; //stores the code which is randomly generated
  String userin = ""; //stores user input everytime that getInput() is ran
  int userval_1 = 0;//use any point that the user's input which is a string is needed to be changed into a number
  int maxguesscount = 0; //stores the number of guesses, and the length of the board, this can be edited in the options menu
  int printedguesscount = 0; // stores the printed number of guesses, shows at how many guesses the user already is
  int printedval = 0; //used when asking the colour for the users, guess. 
  //Stores whatever number the colour of the user's guess is being asked. (First/Second/Third/Fourth colour of the guess)
  int total_guesses = 0; //total number of guesses the user has made playing this game
  int totalCB_rounds = 0; //total rounds the user has played this game as codebreaker
  int totalCB_wins = 0; //total times the user has won as codebreaker
  
  boolean cheat_mode = true;//toggles a few lines of java code that reveals the generated secret code in codebreaker mode
  //following booleans control the flow of loops
  boolean menu_loop = true;
  boolean options_menuloop = true;
  boolean tbr = true;//general boolean used widly. It controls 3 loops, one of which is in the main
  //It helps in asking again in case of invalid input and replays/quit.
  
  boolean lose = false;//states wether the computer is victorious against the user or not
  
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]  
//[=][=][=][=] WIDE USE METHODS [=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
  public void initiatedefault()
  {//Creates default options. Can be edited by the user in the options menu
    maxguesscount = 12;
    cheat_mode = false;
  } 
  
  public void getInput()
  {//Basic get input method with only try catch and readline. Input recieved is shoved into userin.
    try
    {
      userin = br.readLine();
    }
    catch (IOException ugh)
    {
      ugh.printStackTrace ();
    }
  }
  
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
//[=][=][=][=]OPTIONS MENU METHOD [=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]  
  
  public void optionsMenu()
  {
    options_menuloop = true;
    while(options_menuloop)
    {//print options menu commands
      //incidentally, all commands in the options menu are accessable by the left hand
      System.out.println("OPTIONS MENU: \n Select an option by entering a character. \n");
      System.out.println("r:Reset Record");
      System.out.println("g:Modify Maximum Guess Count");
      System.out.println("c:Toggle Cheat Mode");
      System.out.println("q:Return to Main Menu");
      getInput();
      if (userin.equalsIgnoreCase("r"))
      {
        //resets all scoring records to 0
        int total_guesses = 0;
        int totalCB_rounds = 0;
        int totalCB_wins = 0;
        System.out.println("Records have been reset. Press enter to continue.");
        getInput();//'Pauses' the program until the user is ready to continue.
      }
      else if(userin.equalsIgnoreCase("g"))
      {
        System.out.println("Enter the number of guesses per round you would like to have: ");
        getInput();//ask user for new guess limit
        maxguesscount = Integer.parseInt(userin);//take user input ant parses it into a new number of guesses.
        System.out.println("The number of guesses per round has been changed to: " + maxguesscount + ". Press enter to continue");//User notification
        getInput();//'Pauses' the program until the user is ready to continue.
      }
      else if(userin.equalsIgnoreCase("c"))
      {
        cheat_mode = (cheat_mode) ? false:true;
        if(cheat_mode)//prints notification on status of cheat mode
        {
          System.out.println("Cheat mode is activated");
        }
        else 
        {
          System.out.println("Cheat mode is deactivated\n");
        }
      }
      else if(userin.equalsIgnoreCase("q"))
      {
        options_menuloop = false;//ends options loop and but does not end menu loop, therefore returning to main menu.
      }
      else
      {//returns to ask for input again in case of invalid input
        System.out.println("**Invalid Input**");
      }
    }
  }
  
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]  
//[=][=][=][=]MAIN MENU METHODS [=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]  
  
  public void printMenu()
  {//Print main menu commands
    System.out.println("Welcome to Mastermind.");
    System.out.println("Record:\n Your Total Guesses: " + total_guesses + "\n Total Codebreaker Rounds Played: " + totalCB_rounds + "\n Codebreaker Rounds Won: " + totalCB_wins);
    System.out.println("Press Enter to begin playing.");    
    System.out.println("Enter 'o' to access the options menu.");
    System.out.println("Enter 'h' to access help.");
    System.out.println("Enter 'q' to quit.");
    if(cheat_mode)//Menu notification of cheatmode status
        {
          System.out.println("Cheat mode is activated");
        }
        else 
        {
          System.out.println("Cheat mode is deactivated\n");
        }
  }
  
  public void printHelp()
  {//Online Help text
    System.out.println("This program is a programmed version of the board game Mastermind.");
    System.out.println("The objective of the game as the codebreaker is to guess correctly the code which the codemaker creates, both in colour and position.");
    System.out.println("The codebreaker must do this within an alotted number of guesses.");
    System.out.println("The code consists of four of a selection of six colours. Colours may be repeated in the same code.");
    System.out.println("Eg:\n [Red][Red][Yel][Ora]");
    System.out.println("The available colours are as follows: ");
    System.out.println("Yellow, Orange, Red, Green, Blue, Purple.");
    System.out.println("After every guess the codemaker gives feedback to the codebreaker in the form of keys.");
    System.out.println("Eg: \n [X][O][O][]");
    System.out.println("Each X key means a colour in the guess is in both the right colour and position.");
    System.out.println("An O key means that a colour in the guess is correct but is placed in the wrong position.");
    System.out.println("The codemaker can put these keys at any order and the codebreaker must remember that X keys take precedence over O keys.");
    System.out.println("You can reset your record and edit the maximum number of guesses per round in the options menu.");
    System.out.println("*Changing the number of guesses available per round will change the size of the board.");
    System.out.println("**Disclaimer**");
    System.out.println("Since this program is completely text based, the colours can be thought of as simple numbers or words as well.");
    System.out.println("\n Press enter to continue.");
    getInput();//'Pause'
    System.out.println("");
  }
  
  public boolean runMenu()
  {//Method that answers to Main.
   //tbr is returned at the end to confirm wether the rest of the program will run or not (quit).
    while(menu_loop)
    {
      printMenu();//shows commands
      getInput();//asks input
      //test input
      //setting menu_loop to false continues with the rest of the program
      if(userin.equalsIgnoreCase("q"))
      {
        menu_loop = false;
        tbr = false;//setting tbr to false along with menu_loop will quit the program (see main line 21 & 22).
      }
      else if (userin.equalsIgnoreCase("h"))
      {
        printHelp();//shows help text
        tbr = true;
      }
      else if (userin.equalsIgnoreCase("o"))
      {
        optionsMenu();//enters options menu method
        tbr = true;
      }
      else if (userin.equals(""))
      {//continues with the rest of main
        menu_loop = false;
        tbr = true;
      }
      else{//returns to ask for input again in case of invalid input
        System.out.println("**Invalid Input**");
      }
    }
    menu_loop = true;
    return tbr;
  }

  public String askMastermind()
  {//Method that answers back to main. Main then selects codebreaker/codemaker mode. Needed since some flow control is in main
    //and codemaker mode is in another class.
    System.out.println("Would you like to play as codemaker or codebreaker? ");
    getInput();
    return userin;
  }
  
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]  
//[=][=][=][=] PLAYING AS CODEBREAKER METHODS  [=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
//[=][=][=][=]            PRINTING             [=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]  
  
 public void printColourB(int cn)
  {//prints a piece of the board every time it is called depending on the number thrown to it.
   //it is thrown a number value between 0 and 6
   //0 would represent an empty slot in the board and 1-6 for the colours
    if (cn == 0)
    {
      System.out.print("[   ]");
    }
    else if (cn == 1)
    {
      System.out.print("[Yel]");
    }
    else if (cn == 2)
    {
      System.out.print("[Ora]");
    }
    else if (cn == 3)
    {
      System.out.print("[Red]");
    }
    else if (cn == 4)
    {
      System.out.print("[Blu]");
    }
    else if (cn == 5)
    {
      System.out.print("[Gre]");
    }
    else if (cn == 6)
    {
      System.out.print("[Pur]");
    }
  }
 
  public void printKey(int kn)
  {
    if (kn == 0)
    {//prints an empty key slot
      System.out.print("[ ]");
    }
    else if (kn == 1)
    {//pritns a slot occupied by a white key
      System.out.print("[O]");
    }
    else if (kn == 2)
    {//prints a slot occupied by a black key
      System.out.print("[X]");
    }
    else
    {
      System.out.print("**An error has occured, this should never print.**");
    }
  }
  
  public void printBoard()
  {//calls the methods above using the integers stored in the board array and the keys array
   //the above methods causes it to print the board and keys as readable to the user and not as numbers
    for (int y = 0; y < maxguesscount; y++)
    {//y represents the y-coordinate/row in the array and x represents the x-coordinate/column
      for (int x = 0; x < 4; x++)
      {
        printColourB(board [y][x]);
      }
      System.out.print(" || ");
      for (int x = 0; x < 4; x++)
      {
        printKey(keys [y][x]);
      }
      System.out.println("");//return character after every row
    }
  }

  public void printcode()
  {//cheatmode and game over codeblock
   //calls a method printColourB() the same way printBoard() does but uses the integer stored in code[] instead
   //the secret code is printed out
    for (int x = 0; x < 4; x++)
    {
      printColourB(code[x]);
    }
  }
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]  
//[=][=][=][=] PLAYING AS CODEBREAKER METHODS  [=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
//[=][=][=][=]           GAME LOGIC            [=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]  
 
  public void clearBoard()
  {//sets all values in the board[][] and keys[][] to 0 or as 'unoccupied' slots
   //arrays are accessed in a similar way as printBoard() but with both arrays in the same loop
   //as it does not matter which array is reset first
    for (int y = 0; y < 12; y++)
    {
      for (int x = 0; x < 4; x++)
      {
        board [y][x] = 0;
        keys [y][x] = 0;
      }
    } 
  }
  
  public void generateCode()
  {//Places a random integer from 1-6, which represent their respective colours, into the code[]
    for (int x = 0; x < 4; x++)
    {
      code[x] = (int) (Math.random() * 6 + 1);
    }
  }
  
  public void convertColour()
  {//Asks for user input (colour) and converts it to a number held by userval_1.
    //(putting this value into an array happens after this method is called in another method)
    while (tbr)
    {
      getInput();
      if (userin.equalsIgnoreCase("Yellow") | userin.equalsIgnoreCase("Yel")){
        tbr = false;
        userval_1 = 1;
      }
      else if (userin.equalsIgnoreCase("Orange") | userin.equalsIgnoreCase("Ora")){
        tbr = false;
        userval_1 = 2;
      }
      else if (userin.equalsIgnoreCase("Red")){
        tbr = false;
        userval_1 = 3;
      }
      else if (userin.equalsIgnoreCase("Blue")| userin.equalsIgnoreCase("Blu")){
        tbr = false;
        userval_1 = 4;
      }
      else if (userin.equalsIgnoreCase("Green")| userin.equalsIgnoreCase("Gre")){
        tbr = false;
        userval_1 = 5;
      }
      else if (userin.equalsIgnoreCase("Purple")| userin.equalsIgnoreCase("Pur")){
        tbr = false;
        userval_1 = 6;
      }
      else {
        System.out.println("Invalid Input");  
      }//failsafe
    }
    tbr = true;
  }
  
  public void checkBlackkeys(int ypos)
  {//counts the number of black keys needed
   for (int x = 0; x < 4; x++)
    {//checks for matching between each of the 4 positions in the code and current guess
      if (board[ypos][x] == code[x])
      {//for every correct colour and position match a black key is required
        black_keys++;
      }else{}
    }
  }
 
  public void checkWhitekeys(int ypos)
  { //counts the number of white keys needed
  //Checking white keys
 //[=]==================================================================================================[=]
    //Preparation: clearing the instances' array!
    for(int y = 1; y <7 ;y++)
    {
      for(int x = 0; x<3; x++)
      {
        instances[y][x]=0;
      }
    }

    //count instances
    for(int y = 1; y <7 ;y++)
    {//used to go through every colour
      for (int x = 0; x<4; x++)
      {//used to go through every spot in the guess/code
    //Step 1: Count the number of times each colour appears in the guess
        if (board[ypos][x] == y)
        {//if a colour is found to exist in the guess the number of instances in the code is increased by one (counting)
          instances[y][0]++;
        }else{//if not, nothing is added to the number of instances
        }
    //Step 2: Count the number of times each colour appears on the code
        if (code[x]==y)
        {//if a colour is found to exist in the code the number of instances in the code is increased by one (counting)
          instances[y][1]++;
        }else{//if not, nothing is added to the number of instances
        }
    //Step 3: Record the lesser value for each colour between the occurance
        //the minimum between the number of instances between each colour then is recorded.
        //they are recorded on the third position of the second dimension of the instances[][]
        if (instances[y][0]<=instances[y][1])
        {//if the instance count in the board is less or both instance counts are equal, it is recorded.
          instances[y][2] = instances[y][0];
        }
        else 
        {//if the instance count in the code is not the minimum, then the code must have the minimum instance count, it is then recorded.
          instances[y][2] = instances[y][1];
        }
      }
      //THE TOTAL NUMBER OF KEYS IS EQUAL TO THE SUM OF THE MINIMUM OF THESE INSTANCES
    //Step 4: Add all the minimum values from step 3 to arrive at the total number of keys
      key_total = instances[1][2] + instances[2][2] + instances[3][2] + instances[4][2] + instances[5][2] + instances[6][2];
      //It therefore follows that the difference between the total number of keys and the number of black keys is the number of white keys
    //Step 5: Subtract the number of black keys from the total from step 4 to arrive at the number of white keys. 
      white_keys = key_total-black_keys;
    }
  }
  
  public void shuffleKeys(int ypos)
  {//accesses the keys[][] and places the correct amount of black and white keys, putting black keys in first.
    for (int tempval_9 = 0 ; tempval_9 < black_keys; tempval_9++)
    {//for every black key required, a 2 is inserted into the array
      keys[ypos][tempval_9] = 2;
    }
    controlval_1 = black_keys;//contolval_1 is used such white keys are placed at the position after the black keys
    //preventing the white keys from overwriting the black keys
    //since arrays start at 0, the line is controlval_1 = black_keys; not controlval_1 = black_keys+1;
    while (controlval_1 < key_total)
    {//for every white key required, a 1 is inserted into the array
      keys[ypos][controlval_1] = 1;
      controlval_1++;
    }
  }
  
  public void checkCode(int ypos)
  {//does the checking and key shuffling in order
    checkBlackkeys(ypos);
    checkWhitekeys(ypos);
    shuffleKeys(ypos);
  }
  
  public void runMastermind()
  {//main game flow
    lose = true;//allows activation of the codeblock if user fails to guess code
    totalCB_rounds++;//adds to number of rounds played
    clearBoard();//sets whole board[][] and keys[][] to 0/unoccupied
    generateCode();//creates a random code
    System.out.println("A code has been generated.");//notify user
    System.out.println("");
    for (int y = 0; y < maxguesscount; y++)
    {//y still represents the number of guess that is being processed but starts at 0 for array use.
      //used similar to the y coordinate in math
      printedguesscount = y+1;
      for (int x = 0; x < 4; x++)
      {//x represents the horizontal position in the guess
        printedval = x + 1;
        System.out.println("Enter colour " + printedval + " of your guess");
        convertColour();//asks for input as colour and that colour(or String) is converted to an integer and stored in userval_1
        board[y][x] = userval_1;//that integer is put into the board[number of guess][horizontal position(0, 1, 2 or 3)]
        //with this, that position in the board is ready for printing through the printColourB()
        //process is repeated for all 4 colours of the guess
      }
      total_guesses++;//count total number of guesses
      checkCode(y);//checks the current guess counts black and white and sets up the keys[][] for subsequent printing
      printBoard();//prints the board[][] and keys[][] via printColourB() and printKey()
      if(cheat_mode)
      {//prints code if cheat_mode is active
        System.out.println("Cheat Mode Help: ");
        printcode();
      } else{}
      System.out.println("");
      //shows stats for CURRENT guess
      System.out.println("right position and colour: " + black_keys);
      System.out.println("right colour wrong position: " + white_keys);
      System.out.println("Guess " + (printedguesscount) + " of " +  maxguesscount);
      if (black_keys == 4)
      {//checks if user is successful
        System.out.println("Congratulations. You have entered the correct colour sequence.");
        lose = false;//disables a codeblock below in case of victory
        y = maxguesscount;//ends the game is user wins
        totalCB_wins++;//adds to win record
      }
      else{}
      //key count is all reset for the next guess
      black_keys = 0;
      white_keys = 0;
      key_total = 0;
    }
    if (lose)
    {//codeblock upon loss
      System.out.println("Game Over.");
      System.out.println("The code was:");
      printcode();
    }else{}
  }
  
  public boolean askReplay()
  {//codeblock that activates and asks for replay regardless of user loss or success
    tbr = true;
    System.out.println("\nPress enter to play again or q to return to main menu.");
    getInput();
    if(userin.equals("q"))
    {//if user wants to play again, a nested loop in main continues
      tbr = false;//if the user desires to quit, this ends the nested loop but still allows the outside loop
    }
    return tbr;
  }
}