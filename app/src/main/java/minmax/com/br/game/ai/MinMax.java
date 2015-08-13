package minmax.com.br.game.ai;

import java.util.List;

import minmax.com.br.game.Player;
import minmax.com.br.game.utils.GameMove;
import minmax.com.br.game.utils.GameState;


public class MinMax {

	private int level;
	private Player origPlayer;
	private Player origOpponent;
	private GameState gameState;

	public MinMax(int level) {
		this.level = level;
	}
	
	/**
	 * Método para obter o melhor movimento pelo algoritimo de MinMax
	 *
	 * @param gameState estado do jogo
	 * @param player jogador
	 * @param opponent oponente
	 * @return a melhor jogada
	 */
	public GameMove bestMove(GameState gameState, Player player, Player opponent) {
		this.origPlayer = player;
		this.origOpponent = opponent;

		this.gameState = gameState.copyState();

		MoveEvaluation bestMoves = minmax(level, Comparator.MAX, player, opponent);
		int randomMove = (int)(Math.random()*bestMoves.getMoves().size()-1); 
		
		return bestMoves.getMoves().get(randomMove);
	}

	/**
	 * Algoritimo de MinMax
	 * 
	 * @param level
	 * @param player
	 * @param opponent
	 * @return
	 */
	private MoveEvaluation minmax(int level, Comparator comparator, Player player, Player opponent) {
	
		if(level == 0 || gameState.isEndGame()) {

			return new MoveEvaluation(origPlayer.eval(gameState) - origOpponent.eval(gameState));
		}
		
		MoveEvaluation best = new MoveEvaluation(comparator.initialValue());
		
		List<GameMove> validMoves = GameMove.getValidMoves(gameState, player);
		
		for(GameMove move : validMoves) {
			
			move.execute(gameState);
			
			MoveEvaluation me = minmax(level-1, comparator.opposite(), opponent, player);
			
			move.undo(gameState);
			
			if(comparator.compare(best.getScore(), me.getScore()) < 0) {
				best = new MoveEvaluation(move, me.getScore());
				
			} else if (comparator.compare(best.getScore(), me.getScore()) == 0) {
				best.addGameMove(move);
			}
		}
		
		return best;
	}
	
}
