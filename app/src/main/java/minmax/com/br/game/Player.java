package minmax.com.br.game;

import minmax.com.br.game.utils.GameState;


public class Player {
	
	private Piece pirata;
	private Piece tesouro;
	private String name;
	private int score;
	
	private boolean computer;
	
	public Player(String name, Piece pirata, Piece tesouro, boolean computer) {
		this.name = name;
		this.pirata = pirata;
		this.tesouro = tesouro;
		this.score = 4;
		this.computer = computer;
	}
	
	public void updateScore(int score) {
		this.score = score;
	}
	
	public int getScore() {
		return this.score;
	}
	
	/**
	 * Checa se o elemento é um pirata aliado
	 * 
	 * @param elemento
	 * @return
	 */
	public boolean isMyPirate(int elemento) {
		return pirata.getTypePiece().getId() == elemento;
	}
	
	/**
	 * Checa se o elemento é um tesouro aliado
	 * 
	 * @param elemento 
	 * @return
	 */
	public boolean isMyTresoure(int elemento) {
		return tesouro.getTypePiece().getId() == elemento;
	}

	public boolean isComputer() {
		return computer;
	}

	public int eval(GameState gameState) {
		
		int score = gameState.getPiratesfrom(this).size();
		
		if(gameState.getTreasureFrom(this) != null) {
			score += 1000;
		}
		
		return score;
	}

	public String getName() {
		return name;
	}
}
