package pacman.entries.ghosts;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getActions() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.ghosts.mypackage).
 */
public class MyGhosts extends Controller<EnumMap<GHOST, MOVE>>
{
    private EnumMap<GHOST, MOVE> myMoves = new EnumMap<GHOST, MOVE>(GHOST.class);

    private Random rnd = new Random();

    public static final int PACMAN_DISTANCE_TO_POWER = 30;
    public static final int GHOST_DISTANCE_TO_POWER = 10;
    public static final int CROWDED_DISTANCE = 140;

    /**
     * Retrieves the move.
     *
     * @param game    A copy of the current game
     * @param timeDue The time the next move is due
     * @return All moves of ghosts.
     */
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue)
    {
        myMoves.clear();

        for (GHOST ghost : GHOST.values())                //for each ghost
            if (game.doesGhostRequireAction(ghost))        //if it requires an action
            {

                if (isCrowded(game))
                    myMoves.put(ghost, MOVE.NEUTRAL);
                else
                {
                    MOVE[] possibilities = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghost), game.getGhostLastMoveMade(ghost));
                    myMoves.put(ghost, possibilities[rnd.nextInt(possibilities.length)]);
                }

            }


        return myMoves;
    }

    /**
     * Return true if Pacman is close to the power.
     *
     * @param game A copy of the current game
     * @return if Pacman is close enough to the power
     */
    private boolean pacmanCloseToPower(Game game)
    {

        int[] powerPills = game.getPowerPillIndices();

        for (int i = 0; i < powerPills.length; i++)
            if (game.isPowerPillStillAvailable(i) &&
                    game.getShortestPathDistance(powerPills[i], game.getPacmanCurrentNodeIndex()) < PACMAN_DISTANCE_TO_POWER)
                return true;

        return false;
    }

    /**
     * Return true if Pacman is close to the power.
     *
     * @param ghost The ghost
     * @param game  A copy of the current game
     * @return if Pacman is close enough to the power
     */
    private boolean ghostsCloseToPower(GHOST ghost, Game game)
    {

        int[] powerPills = game.getPowerPillIndices();

        for (int i = 0; i < powerPills.length; i++)
            if (game.isPowerPillStillAvailable(i) &&
                    game.getShortestPathDistance(powerPills[i], game.getGhostCurrentNodeIndex(ghost)) < GHOST_DISTANCE_TO_POWER)
                return true;

        return false;
    }


    /**
     * Checks if is crowded.
     *
     * @param game A copy of the current game
     * @return true, if is crowded
     */
    private boolean isCrowded(Game game)
    {
        GHOST[] ghosts = GHOST.values();
        float distance = 0;

        for (int i = 0; i < ghosts.length - 1; i++)
            for (int j = i + 1; j < ghosts.length; j++)
                distance += game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghosts[i]), game.getGhostCurrentNodeIndex(ghosts[j]));

        return distance < CROWDED_DISTANCE ? true : false;
    }
}