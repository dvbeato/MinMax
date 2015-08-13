package minmax.com.br.game.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Map;

import minmax.com.br.game.Piece;
import minmax.com.br.minmax.R;

/**
 * Classe responsavel por manter em memoria os recursos de imagem e som do jogo.
 */
public class GameResources {

	/**
	 * Enum dos sprites utilizados no jogo
	 */
	public enum Sprite {
		GRAMA, TESOURO, PIRATA01,PIRATA02, GRAMA_ESCURA, GRAMA_SELECIONADA;
	}
	
	/**
	 * Enum dos tipos de pecas utilizados no jogo e suas caracteristicas 
	 */
	public enum TypePiece {
		PIRATA_01(5,1), PIRATA_02(6, 1), TESOURO_01(9, 10), TESOURO_02(8, 10), EMPTY(0,0);
		
		private int id;
		private int score;
		
		private TypePiece(int id, int score) {
			this.id = id;
			this.score = score;
		}
		
		public int getId() {
			return this.id;
		}
		
		public int getScore() {
			return this.score;
		}
	}
	
	private Map<Sprite, Bitmap> sprites;
	private Map<TypePiece, Piece> pieces;
	
	private Resources resources;
	
	public GameResources(Resources resources) {
		this.resources = resources;
		
		this.sprites   = new HashMap<Sprite, Bitmap>();
		this.pieces    = new HashMap<TypePiece, Piece>();
		
		loadImages();
		loadPieces();
	}
	
	/**
	 * Metodo responsavel por carregar as pecas do jogo em memoria
	 */
	private void loadPieces() {
		this.pieces.put(TypePiece.PIRATA_01,  new Piece(this.getSprite(Sprite.PIRATA01), TypePiece.PIRATA_01 ));
		this.pieces.put(TypePiece.PIRATA_02,  new Piece(this.getSprite(Sprite.PIRATA02), TypePiece.PIRATA_02 ));
		this.pieces.put(TypePiece.TESOURO_01, new Piece(this.getSprite(Sprite.TESOURO) , TypePiece.TESOURO_01));
		this.pieces.put(TypePiece.TESOURO_02, new Piece(this.getSprite(Sprite.TESOURO) , TypePiece.TESOURO_02));
	}

	/**
	 * Metodo responsavel por carregar as imagens na memória
	 */
	private void loadImages() {
		sprites.put(Sprite.GRAMA, BitmapFactory.decodeResource(this.resources, R.drawable.grama_));
		sprites.put(Sprite.TESOURO, BitmapFactory.decodeResource(this.resources, R.drawable.tesouro_));
		sprites.put(Sprite.PIRATA01, BitmapFactory.decodeResource(this.resources, R.drawable.pirata_blue));
		sprites.put(Sprite.PIRATA02, BitmapFactory.decodeResource(this.resources, R.drawable.pirata_black));
		sprites.put(Sprite.GRAMA_ESCURA, BitmapFactory.decodeResource(this.resources, R.drawable.grama_escura));
		sprites.put(Sprite.GRAMA_SELECIONADA, BitmapFactory.decodeResource(this.resources, R.drawable.grama_selecionada));
	}
	
	/**
	 * Obtem a imagem de um sprite
	 * 
	 * @param sprite
	 * @return
	 */
	public Bitmap getSprite(Sprite sprite) {
		return this.sprites.get(sprite);
	}
	
	/**
	 * Obtem uma peca do jogo pelo tipo de peca
	 * 
	 * @param typePiece - tipo de peca
	 * @return
	 */
	public Piece getPiece(TypePiece typePiece) {
		return this.pieces.get(typePiece);
	}
	
	/**
	 * Obtem uma peca do jogo pelo id do tipo da peca
	 * 
	 * @param typePieceId - id do tipo da peca
	 * @return
	 */
	public Piece getPiece(int typePieceId) {
		
		for(TypePiece tp : TypePiece.values()) {
			if(tp.getId() == typePieceId) {
				return this.pieces.get(tp);
			}
		}
		
		return null;
	}
}
