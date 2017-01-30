# mastermind
Game of Master Mind as a command line application.

Can operate in one of two modes:
* automatic mode - The secret solution is passed on the command line, and the algorithm tries to guess it, adding judgements itself
* interactive mode - the algorithm proposes solutions, the user judges them interactively.

Implemented as a Java 8 streams coding exercise.
In each step, the algorithm selects the next guess with the highest entropy.
