package minmax.com.br.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import minmax.com.br.game.utils.GameResources.TypePiece;
import minmax.com.br.interfaces.GraphicalElement;

/**
 * Classe responsavel pelo elemento do jogo 'Pirata'
 */
public class Piece implements GraphicalElement {
	
	private TypePiece typePiece;
	
	//imagem que vai ser renderizada
	private Bitmap image;
	
	//cordenada x da posicao em que ele sera renderizado na tela
	private float x = 0.0f;
	
	//cordenada y da posicao em que ele sera renderizado na tela
	private float y = 0.0f;
		
	/**
	 * Construtor
	 * 
	 * @param image - imagem que vai ser renderizada
	 * @param gamePiece 
	 */
	public Piece(Bitmap image, TypePiece typePiece) {
		this.image = image;
		this.typePiece = typePiece;
	}
	
	/** 
	 * @see minmax.com.br.game.ActorElement#render(Canvas)
	 */
	@Override
	public void render(Canvas canvas) {
		canvas.drawBitmap(this.image, x, y, null);
	}
	
	@Override
	public void render(Canvas canvas, RectF rect) {
		canvas.drawBitmap(this.image, null, 
				rect, null);
	}

	public TypePiece getTypePiece() {
		return typePiece;
	}

	public void setTypePiece(TypePiece typePiece) {
		this.typePiece = typePiece;
	}
}
