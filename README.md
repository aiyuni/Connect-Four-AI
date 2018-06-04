# Connect Four AI
An AI that plays a 9x8 Connect Four game using Alpha-Beta pruning with Minimax. The AI is unbeatable at it's default depth. The grid size
is set to 9x8 as that is the size that I most prefer to play Connect Four at (it allows for more combinations and strategies
than a typical 7x7 grid).  The player can choose to go first or second. 

## Algorithm
The AI uses the Minimax algorithm to decide on its moves.  By also using Alpha-Beta pruning, search time for each tree branch 
has been greatly reduced, especially at higher depths.  The evaluation function at non-terminal board states will seek the most connections 
along a row, column, or diagonal.  To ensure the most optimal moves, the evaluation scores have also been adjusted according to the 
current depth of the evaluation.  Nevertheless, if more than one move has the same evaluation score, the AI will randomly pick one of those moves to
ensure a different game each time.

## How to play
Either clone or download the repo. Then, navigate to the *ConnectFourAI* folder.

Using command prompt, type `javac Main.java` to compile the program, and then type `java ConnectFourAI.Main` to run the program.  

To change difficulty, simply adjust the DEPTH_LIMIT constant in the `Game.java` file.


