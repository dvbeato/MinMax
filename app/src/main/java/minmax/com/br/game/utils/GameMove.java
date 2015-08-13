package minmax.com.br.game.utils;

import java.util.ArrayList;
import java.util.List;

import minmax.com.br.game.Player;
import minmax.com.br.game.utils.GameResources.TypePiece;

/**
 * Classe responsavel por gerenciar os movimentos das peças do jogo
 */
public class GameMove {
	public static final int MAX_STEP = 1;
	
	private Position initPosition;
	private Position goalPosition;
	private Player player;
	private int undoInit;
	private int undoGoal;
	
	public GameMove(Position initPosition, Position goalPosition, Player player) {
		this.initPosition = initPosition;
		this.goalPosition = goalPosition;
		this.player = player;
	}

	/**
	 * Executa o movimento no estado do jogo
	 * 
	 * @param gameState - estado do jogo
	 * 
	 * @return true se o movimento for executado
	 */
	public boolean execute(GameState gameState) {
		
		if (!validarMovimento(gameState, this)) {
			return false;
		}
		
		int element = gameState.getElement(initPosition);	
		
		changeElement(this.initPosition, element, this.goalPosition, TypePiece.EMPTY.getId(), gameState);
		
		return true;
	}
	
	public void undo(GameState gameState) {
		
		changeElement(this.goalPosition, this.undoInit, this.initPosition, this.undoGoal, gameState);
	}
	
	private void changeElement(Position init, int element1, Position goal, int element2, GameState gameState) {
		
		int row = init.getRow();
		int col = init.getCol();
		
		int nextRow = goal.getRow();
		int nextCol = goal.getCol();
		
		this.undoInit = gameState.getElement(init);
		this.undoGoal = gameState.getElement(goal);
		
		gameState.map[nextRow][nextCol] = element1;
		gameState.map[row][col] = element2;
	}
	
	/**
	 * Valida se o movimento a ser realizado é valido
	 * 
	 * @param nextPosition
	 * @return
	 */
	public static boolean validarMovimento(GameState gameState, GameMove gameMove) {
		
		int nextElement = gameState.getElement(gameMove.getGoalPosition());
		
		if(nextElement == GameState.EMPTY_SPACE) {
			return  isVerticalMove(gameMove) ^ isHorizontalMove(gameMove);
		
		} else if (!gameMove.getPlayer().isMyPirate(nextElement) && 
				!gameMove.getPlayer().isMyTresoure(nextElement)) {
			
			return isDiagonalMove(gameMove);
		} 
		
		return false;
	}
	
	/**
	 * Verifica se é um movimento vertical
	 * 
	 * @param move
	 * @return
	 */
	private static boolean isVerticalMove(GameMove move) {
		int row = move.getInitPosition().getRow();
		int col = move.getInitPosition().getCol();
		
		int nextRow = move.getGoalPosition().getRow();
		int nextCol = move.getGoalPosition().getCol();
		
		return (Math.abs(nextRow - row) == GameMove.MAX_STEP && 
				  Math.abs(nextCol - col) == 0);
	}
	
	/**
	 * Verifica se é um movimento horizontal
	 * 
	 * @param move
	 * @return
	 */
	private static boolean isHorizontalMove(GameMove move) {
		int row = move.getInitPosition().getRow();
		int col = move.getInitPosition().getCol();
		
		int nextRow = move.getGoalPosition().getRow();
		int nextCol = move.getGoalPosition().getCol();
		
		return (Math.abs(nextRow - row) == 0 && 
				  Math.abs(nextCol - col) == GameMove.MAX_STEP);
	}
	
	/**
	 * Verifica se é um movimento diagonal
	 * 
	 * @param move
	 * @return
	 */
	private static boolean isDiagonalMove(GameMove move) {
		int row = move.getInitPosition().getRow();
		int col = move.getInitPosition().getCol();
		
		int nextRow = move.getGoalPosition().getRow();
		int nextCol = move.getGoalPosition().getCol();
		
		return (Math.abs(nextRow - row) == GameMove.MAX_STEP && 
				  Math.abs(nextCol - col) == GameMove.MAX_STEP);
	}
	
	/**
	 * Obtém todos os movimentos validos de acordo com o estado atual do jogo
	 */
	public static List<GameMove> getValidMoves(GameState gameState, Player player) {
		
		List<GameMove> validMoves = new ArrayList<GameMove>();
		
		List<Position> listInitPositions = gameState.getPiratesfrom(player);
		
		for(Position initPosition : listInitPositions) {
			
			List<Position> positionsAround = GameMove.getAround(initPosition, gameState.getRows(), gameState.getCols());
			
			for(Position goalPosition : positionsAround) {
				
				GameMove gameMove = new GameMove(initPosition, goalPosition, player);
				
				if(GameMove.validarMovimento(gameState, gameMove)) {
					validMoves.add(gameMove);
				}
			}
		}
		
		return validMoves;
	}
	
	
	public static List<Position> getAround(Position initPosition, int rowsLimit, int colsLimit) {
		
		int initRow = initPosition.getRow();
		int initCol = initPosition.getCol();
		
		List<Position> aroundPositions = new ArrayList<Position>();
		
		boolean canMoveLeft  = initCol-GameMove.MAX_STEP >= 0;
		boolean canMoveRight = initCol+GameMove.MAX_STEP <= colsLimit-1;
		boolean canMoveUp    = initRow-GameMove.MAX_STEP >= 0;
		boolean canMoveDown  = initRow+GameMove.MAX_STEP <= rowsLimit-1;
		
		/*
		 * 0X0
		 * 010
		 * 000
		 */
		if(canMoveUp){
			
			aroundPositions.add(new Position(initRow-GameMove.MAX_STEP, initCol));
			
			/*
			 * X00
			 * 010
			 * 000
			 */
			if(canMoveLeft) {
				aroundPositions.add(new Position(initRow-GameMove.MAX_STEP, initCol-GameMove.MAX_STEP));
			}
			
			/*
			 * 00X
			 * 010
			 * 000
			 */
			if(canMoveRight) {
				aroundPositions.add(new Position(initRow-GameMove.MAX_STEP, initCol+GameMove.MAX_STEP));
			}
		}
		
		/*
		 * 000
		 * 010
		 * 0X0
		 */
		if(canMoveDown){
			aroundPositions.add(new Position(initRow+GameMove.MAX_STEP, initCol));
			
			/*
			 * 000
			 * 010
			 * x00
			 */
			if(canMoveLeft) {
				aroundPositions.add(new Position(initRow+GameMove.MAX_STEP, initCol-GameMove.MAX_STEP));
			}
			
			/*
			 * 000
			 * 010
			 * 00x
			 */
			if(canMoveRight) {
				aroundPositions.add(new Position(initRow+GameMove.MAX_STEP, initCol+GameMove.MAX_STEP));
			}
		}
		
		/*
		 * 000
		 * 01X
		 * 000
		 */
		if(canMoveRight){
			aroundPositions.add(new Position(initRow, initCol+GameMove.MAX_STEP));
		}
		
		/*
		 * 000
		 * X10
		 * 000
		 */
		if(canMoveLeft){
			aroundPositions.add(new Position(initRow, initCol-GameMove.MAX_STEP));
		}

		return aroundPositions;
	}
	
	/**
	 * Obtém a avaliação de uma jogada realizada por um player.
	 * 
	 * @param gameState
	 * @param player
	 * @return
	 */
	public static Integer moveEvaluation(GameState gameState, GameMove move, Player player) {
		return null;
	}
	
	public Position getInitPosition() {
		return initPosition;
	}

	public void setInitPosition(Position initPosition) {
		this.initPosition = initPosition;
	}

	public Position getGoalPosition() {
		return goalPosition;
	}

	public void setGoalPosition(Position goalPosition) {
		this.goalPosition = goalPosition;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
