Overview

Write a Java client program for a network application to learn about network programming with sockets.

Fifteen

In the game of Fifteen, two players take turns picking a number from 1 to 9. Each number can only be picked once. Each player's score is the sum of the numbers the player has picked. The game ends when one player's score is exactly 15; that player wins. Otherwise, the game ends in a draw when all numbers have been picked.

For example: 
Alice picks 4; her score is 4. 
Bob picks 1; his score is 1. 
Alice picks 5; her score is 9. 
Bob picks 2; his score is 3. 
Alice picks 6; her score is 15. 
Alice wins!

Network Game

For this project you will implement the client program for a Fifteen client-server network application. (You will not implement the server program.) The network application uses TCP sockets for communication, with the protocol described below. The network application is a multi-session multi-client application; the server supports any number of simultaneous sessions; each session consists of two players (clients) playing against each other. The server does not participate in the game; the server is just a glorified game board.

The client program is run with this command:

    $ java Fifteen <playername> <host> <port>
When a client starts up, it sets up a socket connection to the server at the given host and port. If the server has no sessions, or if every session has two players, the server creates a new session and adds the client to that session; the client then waits to start playing until another client joins the session. Otherwise, the server has a session with one waiting client, and the server adds the client to that session; the two clients then start playing each other.

The server keeps track of whose turn it is; only that player is allowed to pick a number. The server also keeps track of each player's score and detects when one player wins or the game is a draw.

Client GUI

The client's graphical user interface looks like this:



The window's title is "Fifteen -- <name>" where <name> is replaced with the player's name. At the left are nine buttons for picking each of the numbers 1 through 9. At the top right are three text boxes. The first text box displays the player's own name and score. The second text box displays the opponent's name and score. The third text box displays who won the game, or whether the game was a draw. At the bottom right is a button for starting a new game between the same two players.

Here is the partial code for the client GUI: FifteenView.java

When you write the client program, you must add any necessary code to the UI class. When you submit your project, you must include the modified UI class as part of your submission.

Here is a game session between Alice and Bob, showing what appears on each player's GUI. The server is running on localhost port 5678. Alice types this command:

    $ java Fifteen Alice localhost 5678
Alice's GUI window appears. All buttons are disabled. The first text box says just "Alice". The second text box says "Waiting for partner". The third text box is blank. (If she changes her mind, at this point Alice can close the window, and the game session terminates.)


Bob types this command:

    $ java Fifteen Bob localhost 5678
Bob's GUI window appears. Both windows show Alice's and Bob's initial scores of 0. Note that the player's own name and score appear in the first text box, and the opponent's name and score appear in the second text box. In Alice's window, the number buttons are enabled, signifying that it's her turn. In Bob's window, the number buttons are disabled, signifying that it's not his turn. (The first player in the game session always takes the first turn.) In both windows, the New Game button is enabled. If either player clicks this button at any point, a new game starts, and both windows go back to this initial state.
    

Alice clicks the 4 button. Note that in both windows, the 4 button becomes blank and is disabled, preventing either player from picking 4 again. Alice's new score shows in both windows. In Bob's window, the number buttons are enabled (except the 4 button), signifying that it's his turn. In Alice's window, the number buttons are disabled, signifying that it's not her turn.

    

Bob clicks the 9 button.

    

To prevent Bob from winning on the next turn, Alice clicks the 6 button.

    

Bob isn't paying attention and clicks the 1 button.

    

Alice clicks the 5 button and wins the game. In both windows, the third text field displays "Alice wins!"; the number buttons are disabled; and the New Game button is enabled.

    

If Bob had won, the third text field would have displayed "Bob wins!". If the game had been a draw, the third text field would have displayed "Draw!".

If either player clicks the New Game button (at any point), a new game starts, and both windows go back to the initial state shown above.

If either player closes the window (at any point), the game session terminates, the other player's window closes as well, and both client programs terminate.

Protocol

The client and server exchange messages encoded using a textual protocol over the socket connection. Each message ends with a newline.

Client-to-server messages:

join <n> 
Sent when the client starts. <n> is replaced with the player's name.
digit <d> 
Sent when the player clicks a number button. <d> is replaced with the number (1 to 9).
newgame 
Sent when the player clicks the New Game button.
quit 
Sent when the player closes the window.
Server-to-client messages:

id <i> 
Sent when the client joins the game session. <i> is replaced with the player's ID. The first player to join gets ID = 1. The second player to join gets ID = 2.
name <i> <n> 
Sent to report one of the player's names. <i> is replaced with the ID of the player whose name is being reported. <n> is replaced with that player's name.
digits <aaaaaaaaa> 
Sent to report which numbers are available to be picked. <aaaaaaaaa> is replaced with a nine-character string of 0s and 1s corresponding to the numbers 1 through 9. Each character is 0 if the corresponding number is not available or is 1 if the corresponding number is available. For example, if numbers 1, 5, and 8 are available, the string would be 100010010.
score <i> <s> 
Sent to report one of the player's scores. <i> is replaced with the ID of the player whose score is being reported. <s> is replaced with that player's score.
turn <i> 
Sent to report which player's turn it is. <i> is replaced with the ID of the player whose turn it is. If the game is over (win or draw), <i> is 0.
win <i> 
Sent to report which player won the game. <i> is replaced with the ID of the player who won the game. If the game was a draw, <i> is 0.
quit 
Sent to report that the game session has terminated.
Professor's Server

To help you develop your client program, I have written a server program that implements the server side of the above protocol. The server program prints on the console the source or destination and the contents of every message the server receives or sends.

Here is a Java archive with the server: server.jar

You must use JDK 1.7 to run my server. To run my server on the CS Department computers, download the server JAR file into your account, then type these commands:

    $ exec bash
    $ export PATH=/usr/local/dcs/versions/jdk1.7.0_11/bin:$PATH
    $ export CLASSPATH=.:server.jar
    $ java FifteenServer <host> <port>
The first command puts you in the bash shell. The second command sets up the PATH variable to include the JDK 1.7 directory. The third command sets the Java class path to include the current directory plus the server JAR file. The fourth command runs my server program.
You are only allowed to run my server. You are not allowed to examine, list, dump, disassemble, reverse-engineer, or do anything else with my server.

Software Requirements

The client program must be run by typing this command line:
java Fifteen <playername> <host> <port>
<playername> is the name of the player. The player name must not include any whitespace.
<host> is the host name or IP address of the server.
<port> is the port number of the server.

Note: This means that the client program's class must be named Fifteen, and this class must not be in a package.
If the command line does not have the required number of arguments, or if any argument is invalid, the client program must print a usage message on the standard error and must terminate. The wording of the usage message is up to you.
If the client program cannot connect to the server at the given host and port, or if any other error condition occurs, the client program must print an error message on the standard error and must terminate. The wording of the error message is up to you.
The client program must display and operate the graphical user interface specified above under "Client GUI."
The client program must communicate with the server using the protocol specified above under "Protocol."
The client must not print anything on the standard output.
Note: If your client program does not conform exactly to Requirements 4, 5, and 6, you will lose credit on your project. See the Grading Criteria below.

Note: If you are not sure what the client is supposed to do in any particular situation, please come see me. I will let you run my implementation of the client program along with my server program. (I will not give you the source code or class files for my client program.)

Software Design Criteria

The client program must follow the network programming patterns studied in class as appropriate (MVC, Observer, Network Proxy, Model Clone).
The client program must be designed using object oriented design principles as appropriate.
The client program must make use of reusable software components as appropriate.
Each class or interface must include a Javadoc comment describing the overall class or interface.
Each method within each class or interface must include a Javadoc comment describing the overall method, the arguments if any, the return value if any, and the exceptions thrown if any.
Note: See my Java source files which we studied in class for the style of Javadoc comments I'm looking for.

Note: If your program's design does not conform to Software Design Criteria 1 through 5, you will lose credit on your project. See the Grading Criteria below.

Submission Requirements

Your project submission will consist of a Java archive (JAR) file containing the Java source file for every class and interface in your client program (including the modified UI class). Put all the source files into a JAR file named "<username>.jar", replacing <username> with the user name from your Computer Science Department account. The command is:

jar cvf <username>.jar *.java

Send your JAR file to me by email at ark­@­cs.rit.edu. Include your full name and your computer account name in the email message, and include the JAR file as an attachment.

When I get your email message, I will extract the contents of your JAR file into a directory. I will set my Java class path to include the directory where I extracted your files. I will compile all the Java source files in your program using the JDK 1.7 compiler. I will then send you a reply message acknowledging I received your project and stating whether I was able to compile all the source files. If you have not received a reply within one business day (i.e., not counting weekends), please contact me. Your project is not successfully submitted until I have sent you an acknowledgment stating I was able to compile all the source files.

The submission deadline is Wednesday, April 16, 2014 at 11:59pm. The date/time at which your email message arrives in my inbox (not the time when I actually read the message, which will be some time later than when it arrives in my inbox) will determine whether your project meets the deadline.

You may submit your project multiple times up until the deadline. I will keep and grade only the most recent successful submission. There is no penalty for multiple submissions.

If you submit your project before the deadline, but I do not accept it (e.g. I can't compile all the source files), and you cannot or do not submit your project again before the deadline, the project will be late (see below). I strongly advise you to submit the project several days before the deadline, so there will be time to deal with any problems that may arise in the submission process.

Grading Criteria

I will grade your project by:

(10 points) Evaluating the design of your program, as documented in the Javadoc and as implemented in the source code.
All of the Software Design Criteria are fully met: 10 points.
Some of the Software Design Criteria are not fully met: 0 points.
(30 points) Running your project. I will have a test script consisting of 30 actions. An "action" consists of starting a client program, clicking a button, or clicking a close box. I will perform the actions one by one. If any action does not produce the required result, I will stop the test script. The number of points is equal to the number of actions completed.
(40 points) Total.
When I run your program, the Java class path will point to the directory with your compiled class files. I will use JDK 1.7 to run my server program and your client program.

I will grade the test script based solely on whether your client program produces the correct results as specified in the above Software Requirements. Any deviation from the requirements will stop the test script. This includes errors in the formatting of the displayed messages, such as extra or missing spaces; misspelled words; incorrect capitalization; incorrect, extra, or missing punctuation; and extraneous output not called for in the requirements. The requirements state exactly what the output is supposed to be, and there is no excuse for outputting anything different. If any requirement is unclear, please ask for clarification.

After grading your project I will put your grade and any comments I have in your encrypted grade file. For further information, see the Course Grading and Policies and the Encrypted Grades.

Late Projects

If I have not received a successful submission of your project by the deadline, your project will be late and will receive a grade of zero. You may request an extension for the project. There is no penalty for an extension. See the Course Policies for my policy on extensions.

Plagiarism

Programming Project 3 must be entirely your own individual work. I will not tolerate plagiarism. If in my judgment the project is not entirely your own work, you will automatically receive, as a minimum, a grade of zero for the assignment. See the Course Policies for my policy on plagiarism.

Resubmission

If you so choose, you may submit a revised version of your project after you have received the grade for the original version. However, if the original project was not successfully submitted by the (possibly extended) deadline or was not entirely your own work (i.e., plagiarized), you are not allowed to submit a revised version. Submit the revised version via email in the same way as the original version. I will accept a resubmission up until 11:59pm Thursday 01-May-2014. You may resubmit your project multiple times up until the deadline; I will keep and grade only the most recent successful resubmission; there is no penalty for multiple resubmissions. I will grade the revised version using the same criteria as the original version, then I will subtract 4 points (10% of the maximum possible points) as a resubmission penalty. The revised grade will replace the original grade, even if the revised grade is less than the original grade.
