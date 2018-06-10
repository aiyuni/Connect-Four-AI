# Connect Four AI
An AI that plays a 9x8 Connect Four game using Alpha-Beta pruning with Minimax.  At its default depth, the AI looks up to 6 moves ahead.
The grid size is set to 9x8 as that is the size that I most prefer to play Connect Four at (it allows for more combinations and strategies
than a typical 7x6 grid).  The player can choose to go first or second. 

## Algorithm
The AI uses the Minimax algorithm to decide on its moves.  By also using Alpha-Beta pruning, search time for each tree branch 
has been greatly reduced, especially at higher depths.  The evaluation function at non-terminal board states will seek the most connections 
along a row, column, or diagonal. It also actively seeks out 'traps' (i.e a possible winning connection with a gap missing in the middle). To ensure the most optimal moves, the evaluation scores have also been adjusted according to the 
current depth of the evaluation.  Nevertheless, if more than one move has the same evaluation score, the AI will randomly pick one of those moves to
ensure a different game each time.

By default, the AI looks up to 6 moves ahead, which is strong enough to beat most players. 
This depth can be increased to make the AI extremely hard to beat.

## How to play
Either clone or download the repo. Then, navigate to the *ConnectFourAI* folder.

Using command prompt, type `javac Main.java` to compile the program, and then type `java ConnectFourAI.Main` to run the program.  

To change difficulty, simply adjust the DEPTH_LIMIT constant in the `Game.java` file.

