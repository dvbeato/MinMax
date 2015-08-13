package minmax.com.br.game.utils;

import java.util.ArrayList;
import java.util.List;

import minmax.com.br.game.Player;
import minmax.com.br.game.utils.GameResources.TypePiece;


/**
 * Classe responsavel por gerenciar os estado do jogo.  
 */
public class GameState {
	
	public static final int EMPTY_SPACE = 0;
	
	public int[][] map;
	
	public Player currentPlayer;
	
	public Player nextPlayer;
	
	private int rows, cols;

	private Player winner;
	
	public GameState(Player player, Player opponent, int rows, int cols){
		
		this.rows = rows;
		this.cols = cols;
		
		loadInitialState(player, opponent);
	}
	
	/**
	 * Carrega o estado do jogo no modo inicial
	 */
	public void loadInitialState(Player player, Player opponent) {
		this.currentPlayer = player;
		this.nextPlayer = opponent;
		
		this.map = new int[rows][cols];
		
		posicionarPecas();
	}

	/**
	 * Posiciona as pecas iniciais
	 */
	private void posicionarPecas() {
		
		int colunaMeio = (this.map[0].length)/2;
		
		for(int i = 0 ; i < this.map[0].length ; i++) {
			if(i == colunaMeio)
				continue;
			
			this.map[1][i] = TypePiece.PIRATA_02.getId();
			this.map[this.map.length-2][i] = TypePiece.PIRATA_01.getId();
		}
		
		this.map[this.map.length-1][colunaMeio] = TypePiece.TESOURO_01.getId();
		this.map[0][colunaMeio] = TypePiece.TESOURO_02.getId();
	}



	/**
	 * Obtem o elemento presente em determinada posicao do mapa
	 * 
	 * @param position - posicao do mapa
	 * @return id do elemento
	 */
	public int getElement(Position position) {
		return this.map[position.getRow()][position.getCol()];
	}

	public boolean isEndGame() {
		
		if(getTreasureFrom(currentPlayer) == null ||  
					getPiratesfrom(currentPlayer).isEmpty()) {
			
			this.winner = nextPlayer;
			return true;
			
		} else if ((getTreasureFrom(nextPlayer) == null)  ||
							getPiratesfrom(nextPlayer).isEmpty()) {
			
			this.winner = currentPlayer;
			return true;
		}
		
		return false;
	}
	
	/**
	 * Obtem a posicao das pecas pirata de um determinado jogador;
	 * @param player
	 */
	public List<Position> getPiratesfrom(Player player) {
		
		List<Position> positions = new ArrayList<Position>();
		
		for (int row = 0 ; row < map.length ; row++) {
			
			for(int col = 0 ;  col < map[row].length ; col++) { 
				
				int element = map[row][col];
				
				if(player.isMyPirate(element)) {
					positions.add(new Position(row, col));
				}
			}
		}
	
		return positions;
	}
	
	/**
	 * Obtem a posicao do tesouro de um determinado jogador.
	 * 
	 * @param player
	 * @return
	 */
	public Position getTreasureFrom(Player player) {
		
		int colunaMeio = (this.map[0].length)/2;
		int ultimaLinha = map.length-1;
		
		if(player.isMyTresoure(map[0][colunaMeio])) {
			
			return new Position(0,colunaMeio);
		
		} else if(player.isMyTresoure(map[ultimaLinha][colunaMeio])) {
		
			return new Position(ultimaLinha, colunaMeio);
		
		}
				
		return null;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public GameState copyState() {
		GameState copy = new GameState(this.currentPlayer, this.nextPlayer, this.rows, this.cols);
		for(int i = 0; i < this.map.length ; i++)
			copy.map[i] = this.map[i].clone();
		return copy;
	}

	public void setElement(Position position, int element) {
		int row = position.getRow();
		int col = position.getCol();
		this.map[row][col] = element;
	}

	public Player getWinner() {
		
		return this.winner;
	}
	
}
