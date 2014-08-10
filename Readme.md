Mastermind
=========
  - Author: Troylan Tempra Jr.
  - Created: November 16, 2011
  - Modified: November 26, 2011
  - *This document was edited for markdown on Aug 10, 2014
  - Description: A programmed version of the mastermind board game created in Java.


A detailed description of the game can be found [here](http://en.wikipedia.org/wiki/Mastermind):

Instructions
-----

Run the program (Mastermind5_run)

You will be immediately directed to the main menu. It should display your 
record, the available commands, and the status of cheatmode.

From the main menu you may enter the following commands:

		- Enter: play -> Select mode
		- q: quit
		- h: help
		- o: options
From the options menu, you may enter the following commands:
 
		- r: reset record
		- g: change guess count
		- c: toggle cheat mode
		- q: return to main menu

Rules
----
The objective of the game as the codebreaker is to guess correctly the code 
	which the codemaker creates, both in colour and position.
	The codebreaker must do this within an alotted number of guesses.
	The code consists of four of a selection of six colours. 
	Colours may be repeated in the same code.
	
	Eg: [Red][Red][Yel][Ora]
	
The available colours are as follows: 

	- Yellow
    - Orange 
    - Red
    - Green 
    - Blue 
    - Purple
	
After every guess the codemaker gives feedback to the codebreaker in the 
	form of keys.
	
	Eg:  [X][O][O][] 
	
Each X key means a colour in the guess is in both the right colour 
	and position. An O key means that a colour in the guess is correct but 
	is placed in the wrong position.
	
The codemaker can put these keys at any order and the codebreaker must 
	remember that X keys take precedence over O keys.
	
You can reset your record and edit the maximum number of 
	guesses per round in the options menu.
	
*Changing the number of guesses available per round will change the 
	size of the board.
	
							**Disclaimer**
	Since this program is completely text based, the colours can be 
			thought of as simple numbers or words as well.

The rules for placement of black and white keys can be vague. Technically
speaking, as when talking to a computer, the total number of keys for a guess
should be the sum of the minimums of the number of times each colour appears 
in both the guess and the code.

When there are 6 colours:

let n represent the number of times the colour appears on the guess
let j represents the number of times the colour appears on the guess

	total keys = min(n1, j1) + min(n2, j2) + min(n3, j3) ... + min(n6, j6)

Notes
---

November 26, 2011

	- Codemaker mode added.
	- Mode can be selected after pressing enter at the main menu.
November 24, 2011

	- Program is fully operational
	Main Menu Commands:
		- Enter: play
		- q: quit
		- h: help
		- o: options
	Options Menu Commands:
		r: reset record
		g: change guess count
		c: toggle cheat mode
		q: return to main menu
