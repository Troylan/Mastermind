/*
 * Mastermind
 * Author: Troylan Tempra Jr
 * Created: November 16, 2011
 * Mod: November 26, 2011
 * Description: Java rendition of the Mastermind boardgame.
 */

import java.io.*;

public class TheMastermindBreaker
{
  InputStreamReader isr = new InputStreamReader (System.in);
  BufferedReader br = new BufferedReader (isr);
  String userin = "";
  boolean verpos[][][][] = new boolean[6][6][6][6];//lists the available possibilities, true means the combination is eligible
  boolean pseuverpos[][][][] = new boolean[6][6][6][6];//same as above but like a copy used for looking for minimax score
  int black_keys[] = new int[12];//key arrays holding information provided by the user
  int white_keys[] = new int[12];
  int pos[] = new int[12];//holds the number of possibilites at each guess
  int guessnum = 0;
  int[][] keys = new int[12][4];//same as in codebreaker mode
  int[][] board = new int[12][4];//same as in codebreaker mode
  boolean error = false;//holds wether or not an impossible scoreset has been given
  boolean failure = false;//
  int controlval_1=0;//used to count the number of white keys both for pseu and verpos
  int maxguesscount = 6;//cuz we don't need any more.
  int fgc1 = 0;//final guess colours (as numbers)
  int fgc2 = 0;
  int fgc3 = 0;
  int fgc4 = 0;
  
  public void getInput()
  {//Basic get input method: try catch readLine. Input recieved is put into userin.
    try
    {
      userin = br.readLine();
    }
    catch (IOException ugh)
    {
      ugh.printStackTrace ();
    }
  }
  
  public void printColourB(int cn)
  {//same as in codebreaker but the emtpy slot is now at 7 and the colours, 0-5
    if (cn == 0)
    {
      System.out.print("[Yel]");
    }
    else if (cn == 1)
    {
      System.out.print("[Ora]");
    }
    else if (cn == 2)
    {
      System.out.print("[Red]");
    }
    else if (cn == 3)
    {
      System.out.print("[Blu]");
    }
    else if (cn == 4)
    {
      System.out.print("[Gre]");
    }
    else if (cn == 5)
    {
      System.out.print("[Pur]");
    }
    else if (cn == 7)
    {
      System.out.print("[   ]");
    }
  }
  
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
//[=][=]  CodeBreaker Logic  [=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
  
  public void initializeVer()
  {//all array of verpos is set to true, meaning all combinations are made eligible
    for (int a = 0; a < 6; a++)
    {
      for (int b = 0; b < 6; b++)
      {
        for (int c = 0; c < 6; c++)
        {
          for (int d = 0; d < 6; d++)
          {
            verpos[a][b][c][d] = true;
          }
        }
      }
    }
    for (int y = 0; y<12 ; y++)
    {//all possibilities are set to 0 and will be counted later
      pos[y]=0;
    }
  }
  
  public void countVer(int y)
  {//counts possibilities eligible, 1296 before first guess.
    for (int a = 0; a < 6; a++)
    {
      for (int b = 0; b < 6; b++)
      {
        for (int c = 0; c < 6; c++)
        {
          for (int d = 0; d < 6; d++)
          {
            if(verpos[a][b][c][d])
            {
              pos[y]++;
            }
          }
        }
      }
    }
    System.out.println("Current possibilites remaining: "+pos[y]);
    if(pos[y] < 1)
    { error = true; }
  }
  
  public void findanswer()
  {//method used when there is only one possibility remaining, it goes through all the possibilities and searches
    // for the only one that is available
    for (int a = 0; a < 6; a++)
    {
      for (int b = 0; b < 6; b++)
      {
        for (int c = 0; c < 6; c++)
        {
          for (int d = 0; d < 6; d++)
          {
            if(verpos[a][b][c][d])
            {//once the only possible combination has been found, the fgc variables take the value of the combination
              //the combination is then printed out through other methods
              fgc1 = a;
              fgc2 = b;
              fgc3 = c;
              fgc4 = d;
            }
          }
        }
      }
    }
  }
  
  public void printguess(int gc1, int gc2, int gc3, int gc4)
  {//print four colours via a method above
    printColourB(gc1);
    printColourB(gc2);
    printColourB(gc3);
    printColourB(gc4);
    System.out.println("");
  }
  
  public void askFeedback(int y)
  {// asks for number of black and white keys and puts them into their respective arrays
    System.out.println("How many have the right colour and are in the correct position?");
    getInput();
    black_keys[y] = Integer.parseInt(userin);
    System.out.println("How many white keys would you score?");
    getInput();
    white_keys[y] = Integer.parseInt(userin);
  }
  
  public void eliminateVer(int gc1, int gc2, int gc3, int gc4, int gbK, int gwK)
    //checks the elegibility of each possibility as the code
  {//gc = guess colour
   //gbK = guess' black keys when checked against secret code (provided by the user)
   //gwK = guess' white keys when checked against secret code (provided by the user)
   //vc = possibility colour
   //vbK = possibilities' black keys when checked against the current guess being used to eliminate possibilities
   //vwK = possibilities' white keys when checked against the current guess being used to eliminate possibilities
    for (int vc1 = 0; vc1 < 6; vc1++)
    {
      for (int vc2 = 0; vc2 < 6; vc2++)
      {
        for (int vc3 = 0; vc3 < 6; vc3++)
        {
          for (int vc4 = 0; vc4 < 6; vc4++)
          {
            //the number of keys for each possibility is determined via the 2 methods below
            int vbK = checkElegibilityB(vc1,vc2,vc3,vc4,gc1,gc2,gc3,gc4);
            int vwK = checkElegibilityW(vc1,vc2,vc3,vc4,gc1,gc2,gc3,gc4);
            //if the possibility being tested has the same number of white keys and black keys
            if (vwK == gwK & vbK == gbK)
            {
              //that combination of colours remains eligible
            }
            else 
            {
              //if not, that combination of colours is no longer eligible
              verpos[vc1][vc2][vc3][vc4] = false;
            }
          }
        }
      }
    }
    //this is because of the fact that scoring is symetric
    //this means that the score when checking a guess against the secret code is the same as if the code and 
    //guess where to have reversed roles
    //meaning the secret code has to be one of the combinations that when checked against a the current guess,
    //gives the same score as what the user would give the current guess against the secret code.
  }
  
  public int checkElegibilityB(int vc1, int vc2, int vc3, int vc4, int gc1, int gc2, int gc3, int gc4)
  {//counting black keys and returning them
    //if the numbers which represent colours used to access the array are equal a black key is added, naturally
    int bKeys = 0;
    if (vc1 == gc1)
      bKeys++;
    if (vc2 == gc2)
      bKeys++;
    if (vc3 == gc3)
      bKeys++;
    if (vc4 == gc4)
      bKeys++;
    return bKeys;
  }
  
  public int checkElegibilityW(int vc1, int vc2, int vc3, int vc4, int gc1, int gc2, int gc3, int gc4)
  {
    int tKeys = 0;//total number of keys
    int wKeys = 0;//number of white keys of the possibility being tested
    int bKeys = checkElegibilityB(vc1,vc2,vc3,vc4,gc1,gc2,gc3,gc4);//number of black keys of the possibility being tested,
    //acquired from above method
    int[][] cInstances = new int[6][3];
    //array used the same way as in human code breaker mode, but the the colours starting at 0 instead of 1
    
    for(int y = 0; y <6 ;y++)
    {//used to go through every colour
      //if a colour is found to exist in the guess the number of instances in the code is increased by one (counting)
      //if a colour is found to exist in the code the number of instances in the code is increased by one (counting)
      
        if (vc1 == y)
          cInstances[y][0]++;

        if (gc1 == y)
          cInstances[y][1]++;
        
      //check every position just as with black
        
        if (vc2 == y)
          cInstances[y][0]++;

        if (gc2 == y)
          cInstances[y][1]++;
        
        if (vc3 == y)
          cInstances[y][0]++;

        if (gc3 == y)
          cInstances[y][1]++;
        
        if (vc4 == y)
          cInstances[y][0]++;

        if (gc4 == y)
          cInstances[y][1]++;
        
    //record the minimum number of instances
        if (cInstances[y][0]<=cInstances[y][1])
        {cInstances[y][2] = cInstances[y][0];}
        else 
        {cInstances[y][2] = cInstances[y][1];}
    }
      
      tKeys = cInstances[0][2] + cInstances[1][2] + cInstances[2][2] + cInstances[3][2] + cInstances[4][2] + cInstances[5][2];
      wKeys = tKeys-bKeys;
      return wKeys;
  }
  
  public void eliminatePseuVer(int gc1, int gc2, int gc3, int gc4, int gbK, int gwK)
  {
    for (int vc1 = 0; vc1 < 6; vc1++)
    {
      for (int vc2 = 0; vc2 < 6; vc2++)
      {
        for (int vc3 = 0; vc3 < 6; vc3++)
        {
          for (int vc4 = 0; vc4 < 6; vc4++)
          {
            int vbK = checkElegibilityB(vc1,vc2,vc3,vc4,gc1,gc2,gc3,gc4);
            int vwK = checkElegibilityW(vc1,vc2,vc3,vc4,gc1,gc2,gc3,gc4);
            if (vwK == gwK & vbK == gbK)
            {
            }
            else 
            {
              pseuverpos[vc1][vc2][vc3][vc4] = false;
            }
          }
        }
      }
    }
  }
  
  public int detminimax(int vc1, int vc2, int vc3, int vc4)
  { //a situation is an elegible set of key pegs
    //a score is the number of possibilities that would remain in a situation
    //the minimax score is the highest of the situation scores, i.e. worst case (situation)scenario for the combination
    //the combination with the lowest minimax score is the one that we need. This method does not find it but it helps us do so
    //this method merely finds and throws the minimax score for  a possibility it catches. This will be made to catch every
    //possibility so that it will throw back the minimax score of every possibility.
    //those minimax scores are compared in another method (the method that calls it)
    int minimax = 0;//the minimax score of the possibility being tested
    int[] score = new int[14];//the score for each situation,

    //The process for determining a minimax score is as follows:
    for(int sit = 1; sit < 14 ; sit++)
    {
//Step 1: [=][=][=]find score for each situation.
      //1a:============Set elegible possibilities to equal current=================================
      for (int a = 0; a < 6; a++)
      {
        for (int b = 0; b < 6; b++)
        {
          for (int c = 0; c < 6; c++)
          {
            for (int d = 0; d < 6; d++)
            {
              pseuverpos[a][b][c][d] = verpos[a][b][c][d];
            }
          }
        }
      }
      //1b:===================================================
      //Eliminate the possibilities that do not give the same score as the situation when tested against the possibility being tested for minimax score
      //The possibilities that have already been eliminated by previous guesses have already been eliminated by the 4 for loops above
      
      //There are only 14 viable situations. When there are 4 black and white keys max,
      //There are 25 possible combinations of key pegs. 10 of which have total keys greater than 4 (3 black, 2 white; 4 black, 2 white etc)
      //They are not viable as there are only 4 key slots available. The other non viable situation is (3 black 1 white)
      //It should be easy to figure out why
      
      //In each situation the eliminatePseuVer() is passed the 4 colours of the possibility being tested and the
      // black and white key combination of that situation 
      //ie: situation 1 holds none of both keys(0,0), situation 8 holds 1 black and 3 white keys (1,3)
      if (sit == 1)
        eliminatePseuVer(vc1, vc2, vc3, vc4, 0, 0);
      else if (sit == 2)
        eliminatePseuVer(vc1, vc2, vc3, vc4, 0, 1);
      else if (sit == 3)
        eliminatePseuVer(vc1, vc2, vc3, vc4, 0, 2);
      else if (sit == 4)
        eliminatePseuVer(vc1, vc2, vc3, vc4, 0, 4);
      else if (sit == 5)
        eliminatePseuVer(vc1, vc2, vc3, vc4, 1, 0);
      else if (sit == 6)
        eliminatePseuVer(vc1, vc2, vc3, vc4, 1, 1);
      else if (sit == 7)
        eliminatePseuVer(vc1, vc2, vc3, vc4, 1, 2);
      else if (sit == 8)
        eliminatePseuVer(vc1, vc2, vc3, vc4, 1, 3);
      else if (sit == 9)
        eliminatePseuVer(vc1, vc2, vc3, vc4, 2, 0);
      else if (sit == 10)
        eliminatePseuVer(vc1, vc2, vc3, vc4, 2, 1);
      else if (sit == 11)
        eliminatePseuVer(vc1, vc2, vc3, vc4, 2, 2);
      else if (sit == 12)
        eliminatePseuVer(vc1, vc2, vc3, vc4, 3, 0);
      else if (sit == 13)
        eliminatePseuVer(vc1, vc2, vc3, vc4, 4, 0);
      
      //for reach situation, a score is recorded
      for (int a = 0; a < 6; a++)
      {
        for (int b = 0; b < 6; b++)
        {
          for (int c = 0; c < 6; c++)
          {
            for (int d = 0; d < 6; d++)
            {
              if(pseuverpos[a][b][c][d])
                score[sit]++;
            }
          }
        }
      }
      //if that recorded score is greater than the current minimax score, that becomes the minimax
      //The minimax score is recorded this way
      if (0 < score[sit] & score[sit]>minimax)
        minimax = score[sit];
    }
    return minimax;
  }
  
  public void detGuess()
  {//determinds the best guess available by comparing minimax scores for ALL combinations.
    int vc1 = 0;//colours of the possibility being tested as numbers
    int vc2 = 0;
    int vc3 = 0;
    int vc4 = 0;
    int vminimax = 0;//minimax score of possibility being tested
    int gc1 = 0;//colours of the possibility with currently the lowest minimax score
    int gc2 = 0;
    int gc3 = 0;
    int gc4 = 0;
    int gminimax = 1296;//curret lowest minimax score
    
    vc1=0;
    while (vc1 < 6)
    {
      vc2=0;
      while (vc2 < 6)
      {
        vc3=0;
        while (vc3 < 6)
        {
          vc4=0;
          while (vc4 < 6)
          {
            vminimax = detminimax(vc1, vc2, vc3, vc4);//calls method above with the number/colour data of the combination
            //repeated by the loops for all combinations
            //System.out.println(" minimax: of " + vc1 + " " + vc2 + " "+ vc3 + " "+ vc4 + " is "+ vminimax);
            if(0 < vminimax & vminimax<gminimax)
            {//takes the lowest minimax number and the combination that spawned it and puts them into the following variables
              //System.out.println("New minimax: " + vminimax);
              gminimax = vminimax;
              gc1 = vc1;
              gc2 = vc2;
              gc3 = vc3;
              gc4 = vc4;
            }
            else
            {
              
            }
            vc4++;
          }
          vc3++;
        }
        vc2++;
      }
      vc1++;
      if(vc1==1)
      {System.out.println("...17%");}
      else if(vc1==2)
      {System.out.println("...33%");}
      else if(vc1==3)
      {System.out.println("...50%");}
      else if(vc1==4)
      {System.out.println("...66%");}
      else if(vc1==5)
      {System.out.println("...83%");}
      
    }
    //that which has the best(lowest) score is then used as the next guess
    fgc1 = gc1;
    fgc2 = gc2;
    fgc3 = gc3;
    fgc4 = gc4;
  }
  
  public void runCompCodebreaker()
  { //reset vars for new round
    failure = false;
    initializeVer();
    fgc1 = 0;//values for first guess
    fgc2 = 0;//always yel yel ora ora
    fgc3 = 1;
    fgc4 = 1;
    guessnum = 0;
    countVer(guessnum);
    pos[0]=0;
    clearBoard();
    board[guessnum][0]=fgc1;
    board[guessnum][1]=fgc2;
    board[guessnum][2]=fgc3;
    board[guessnum][3]=fgc4;
    System.out.println("You would dare challenge me?");
    System.out.println("Think about your pitiful code and write it down.");
    System.out.println("Remember, the only available colours are: Yellow, Orange, Red, Green, Blue, and Purple.");
    System.out.println("And when you're ready, press enter");
    getInput();
    
    do
    {
        System.out.println("My Guess is: ");
        printguess(fgc1, fgc2, fgc3, fgc4);
        askFeedback(guessnum);//ask for keys
        if(black_keys[guessnum]==4)
        {}
        else
        {//eliminate possibilities depending on keys (situation)
          eliminateVer(fgc1,fgc2,fgc3,fgc4,black_keys[guessnum], white_keys[guessnum]);
          countVer(guessnum);//count remaining possibilities
          if (pos [guessnum] == 1)
          {//if there is only one possibility left, print it out
            System.out.println("There is only one possible code.");
            findanswer();
          }
          else if (pos[guessnum] == 0)
          {//if there is no possibile combination, there is an error in scoring
            System.out.println("Muahahahahaha! There are no possibilities remaining! \n There is an error in your feedback, I win.");
            failure = true;
          }
          else
          {//if there i s more than one possibility left, determine the next guess which would
            //have the best worst-case-scenario
            //ie: best minimax score
            //ie: the highest number of possibilities eliminated when it recieves 
            // the key combination that eliminates the least amount
            // of possibilities for that combination
            System.out.println("Processing, this may take a while...");
            detGuess();
          }
        }
        if (failure!=true)
        {//set board and variables for printing (very little to do with algorithm)
          shuffleKeys(guessnum);
          guessnum++;
          board[guessnum][0]=fgc1;
          board[guessnum][1]=fgc2;
          board[guessnum][2]=fgc3;
          board[guessnum][3]=fgc4;
          printBoard();
          System.out.println("[=][=][=][=][=][=][=][=][=][=][=][=]");
          System.out.println("[=][=][=][=][=][=][=][=][=][=][=][=]");
        } else {guessnum++;}
    }while (guessnum<12 & black_keys[guessnum-1]!=4 & failure!=true);
    
    if(failure == false)
    {
      System.out.println("Well, it seems that I have won in " + guessnum + " guess/es.");
    }
  }
  
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
//[=][=]  Same as Human CodeBreakerMode  [=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
//[=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=][=]
  
  
  public void shuffleKeys(int ypos)
  {
    int controlval_1=0;
    for (int tempval_9 = 0 ; tempval_9 < black_keys[ypos]; tempval_9++)
    {
      keys[ypos][tempval_9] = 2;
    }
    controlval_1 = black_keys[ypos];
    while (controlval_1 < (black_keys[ypos]+white_keys[ypos]))
    {
      keys[ypos][controlval_1] = 1;
      controlval_1++;
    }
  }
  public void printKey(int kn)
  {
    if (kn == 0)
    {
      System.out.print("[ ]");
    }
    else if (kn == 1)
    {
      System.out.print("[O]");
    }
    else if (kn == 2)
    {
      System.out.print("[X]");
    }
    else
    {
      System.out.print("**An error has occured, this should never print.**");
    }
  }
  
  public void printBoard()
  {
    for (int y = 0; y < maxguesscount; y++)
    {
      for (int x = 0; x < 4; x++)
      {
        printColourB(board [y][x]);
      }
      System.out.print(" || ");
      for (int x = 0; x < 4; x++)
      {
        printKey(keys [y][x]);
      }
      System.out.println(pos[y] + "");
    }
  }
  public void clearBoard()
  {
    for (int y = 0; y < 12; y++)
    {
      for (int x = 0; x < 4; x++)
      {
        board [y][x] = 7;
        keys [y][x] = 0;
      }
    } 
  }
}