/*
 * Mastermind
 * Author: Troylan Tempra Jr
 * Created: November 16, 2011
 * Mod: November 26, 2011
 * Description: Java rendition of the Mastermind boardgame.r
 */

public class Mastermind5_run
{
  public static void main (String[] args)
  {
    Mastermind5 nstc = new Mastermind5();
    boolean continue_program = true;
    boolean continue_mastermind = true;
    String userinmain = "";
    TheMastermindBreaker mmb = new TheMastermindBreaker();
    
    nstc.initiatedefault();//sets default guess count and deactivates cheatmode
    while(continue_program)
    {
      continue_program = nstc.runMenu();//catches a boolean thrown by this method that determines wether the user wants to quit
      continue_mastermind = continue_program;//both nested and outer loop depends on above method call and allows for instant game quitting
      while(continue_program & continue_mastermind)
      {
        userinmain = nstc.askMastermind();//asks the user for game mode
        if(userinmain.equalsIgnoreCase("codebreaker"))
        {//runs codebreaker mode if desired
          nstc.runMastermind();
          continue_mastermind = nstc.askReplay();
        }
        else
        {//runs codemaker mode if desired
          mmb.runCompCodebreaker();
          continue_mastermind = nstc.askReplay();
        }
      }
      continue_mastermind = true;
    } 
  }
}