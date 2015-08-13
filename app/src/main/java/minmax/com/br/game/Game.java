package minmax.com.br.game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import minmax.com.br.RenderView;
import minmax.com.br.game.ai.MinMax;
import minmax.com.br.game.utils.GameMove;
import minmax.com.br.game.utils.GameResources;
import minmax.com.br.game.utils.GameResources.Sprite;
import minmax.com.br.game.utils.GameResources.TypePiece;
import minmax.com.br.game.utils.GameState;
import minmax.com.br.game.utils.Position;
import minmax.com.br.interfaces.GraphicalElement;


public class Game {
	public static final int ROWS = 7;
	public static final int COLS = 5;
	public static final String GAME_MODE = "gameMode";
	public static final String DIFICULT = "dificult";
	
	private final GameResources gameResources;
	
	private RenderView renderView;
	
	private TouchHandler touchHandler;
	
	private Position posicaoSelecionada;
	
	private GameState gameState;
	
	private BoardGame boardGame;
	
	private MinMax minMaxIA;
	
	private Player player01;
	private Player player02;
	
	public Game(RenderView renderView, boolean playerVsPC, Integer dificuldade) {
		
		this.renderView = renderView;
		
		this.touchHandler = new TouchHandler();
		this.renderView.setOnTouchListener(touchHandler);
		
		this.gameResources = new GameResources(this.renderView.getResources());

		this.boardGame = new BoardGame(renderView.calculateTileSize(ROWS, COLS));
		
		player01 = new Player("Azuis", gameResources.getPiece(TypePiece.PIRATA_01.getId()), gameResources.getPiece(TypePiece.TESOURO_01), false);
		player02 = new Player("Pretos",gameResources.getPiece(TypePiece.PIRATA_02.getId()), gameResources.getPiece(TypePiece.TESOURO_02), playerVsPC);
		
		this.gameState = new GameState(player01, player02, ROWS, COLS);
		
		if(dificuldade != null && dificuldade > 0) {
			Log.d("d", "dificuldade:"+dificuldade);
			this.minMaxIA = new MinMax(dificuldade);
		}
		
		this.renderView.addElement(boardGame);
	}
	
	public GameMove createNextMove(Position nextPosition) {
		
		if(!validarSelecao(nextPosition)) {
			this.posicaoSelecionada = null;
			return null;
		}
		
		if(this.posicaoSelecionada != null) {
			
			GameMove gameMove = new GameMove(posicaoSelecionada, nextPosition, gameState.currentPlayer);
			
			this.posicaoSelecionada = null;
			
			return gameMove;
			
		} else {
			this.posicaoSelecionada = nextPosition;
		}
		
		return null;
	}

	/**
	 * Método para validar seleção.
	 * 
	 * @param nextPosition
	 * @return true caso seja uma seleção valida
	 */
	private boolean validarSelecao(Position nextPosition) {
		
		int elemento = gameState.getElement(nextPosition);
		
		if(this.posicaoSelecionada == null && !gameState.getCurrentPlayer().isMyPirate(elemento)){
			return false;
		} 
		
		return true;
	}
	
	/**
	 * Método para mover um elemento para a proxima posição.
	 * Caso seja uma posição invalida, ou um movimento invalido,
	 * a ação é ignorada.
	 * 
	 * @param nextPosition - posição em que ele vai mover
	 */
	private void executarMovimento(GameMove gameMove) {
		
		if(gameMove.execute(gameState)) {

			if(gameState.isEndGame()) {
				
				encerrarPartida();
								
			} else {
			
				this.changeTurn();
			}
		}
	}
	
	/**
	 * Método para trocar o jogador do turno
	 */
	private void changeTurn() {
		Player playerAux = gameState.currentPlayer;
		gameState.currentPlayer = gameState.nextPlayer;
		gameState.nextPlayer = playerAux;
		
		if(gameState.currentPlayer.isComputer()) {
			processIA(gameState.currentPlayer, gameState.nextPlayer);
		}
	}

	/**
	 * Método para executar algoritimos de inteligencia artificial
	 */
	private void processIA(Player player, Player opponent) {
		
		
		GameMove bestMove = this.minMaxIA.bestMove(gameState, player, opponent);
		
		executarMovimento(bestMove);
		
	}
	
	public void encerrarPartida() {
		confirmarNovaPartida(this.renderView.getContext());
	}
	
	/**
	 * Exibe um Alerta para o jogador escolher se dejesa jogar novamente.
	 * 
	 * @param context
	 */
	private void confirmarNovaPartida(Context context) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        
        dialog.setTitle("Fim de Jogo");
        
        Player winner = gameState.getWinner();
        
        String message = "Os piratas "+winner.getName()+" venceram!!! \nDeseja Jogar Novamente?";
        
        dialog.setMessage(message);
        
        dialog.setCancelable(false);
        
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
            
            	gameState.loadInitialState(player01, player02);
            }
        });
        
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                renderView.finish();
            }
        });
        
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        
        dialog.show();
    }
	
	public void onPlayerTouch(float x, float y) {
		Position posicaoSelecionada = boardGame.selecionarPosicao(x, y);
		
		GameMove nextMove = createNextMove(posicaoSelecionada);
		
		if( nextMove != null) {
			executarMovimento(nextMove);
		}
	}
	
	public Position getPosicaoSelecionada() {
		return posicaoSelecionada;
	}
	
	/**
	 * Classe responsavel por monitorar os eventos de TouchScreen do usuario
	 */
	private class TouchHandler implements OnTouchListener {
		
		Vibrator vibrator = (Vibrator) renderView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
		
		/**
		 * Metodo executado toda vez que o usuario tocar na tela
		 * 
		 * (non-Javadoc)
		 * @see OnTouchListener#onTouch(View, MotionEvent)
		 */
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			vibrator.vibrate(10);
			
			onPlayerTouch(event.getX(), event.getY());
			
			return false;
		}
	}
	
	/**
	 * Classe responsável pelo tabuleiro do jogo
	 *
	 */
	private class BoardGame implements GraphicalElement {

		private float tileSize;
		
		public BoardGame(float tileSize) {
			
			this.tileSize = tileSize;
		}
		
		/**
		 * Método que seleciona um elemento do tabuleiro
		 * dependendo das cordenadas de X e Y da tela.
		 * Executado quando o usuario toca na tela.
		 * 
		 * @param x - posição x com relação a tela
		 * @param y - posição y com relação a tela
		 */
		public Position selecionarPosicao(float x, float y) {
			
			int row = (int) (y/this.tileSize);
			int col = (int) (x/this.tileSize);
			
			return new Position(row, col);
		}
		
		@Override
		public void render(Canvas canvas) {
			
			RectF rect;
			
			for (int row = 0 ; row < ROWS ; row++){
				
				for(int col = 0 ;  col < COLS ; col++) { 
					
					float left = col*this.tileSize;
					float top  = row*this.tileSize;
					
					rect = new RectF(left, top, 
							 left+this.tileSize, 
							 top+this.tileSize);
					
					Bitmap grama = gameResources.getSprite(Sprite.GRAMA);
					
					Position position = new Position(row, col);
					
					int elementId = gameState.getElement(position);
					
					if(gameState.getCurrentPlayer().isMyPirate(elementId)) {
						grama =	gameResources.getSprite(Sprite.GRAMA_ESCURA);
					}
					
					if(getPosicaoSelecionada() != null) {
						if (row == getPosicaoSelecionada().getRow() && col == getPosicaoSelecionada().getCol()) {
							grama = gameResources.getSprite(Sprite.GRAMA_SELECIONADA);
						}
					}
					canvas.drawBitmap(grama, null, rect, null);
					
					if(gameResources.getPiece(elementId) != null) {
						Piece piece = gameResources.getPiece(elementId);
						piece.render(canvas, rect);
					}
				}
			}
		}

		@Override
		public void render(Canvas canvas, RectF rect) {
			canvas.drawBitmap(gameResources.getSprite(Sprite.GRAMA), null, rect, null);
		}
	}
}
