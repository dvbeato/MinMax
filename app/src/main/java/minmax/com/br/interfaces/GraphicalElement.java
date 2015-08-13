package minmax.com.br.interfaces;

import android.graphics.Canvas;
import android.graphics.RectF;

public interface GraphicalElement {

	/**
	 * Metodo executado toda vez que o 
	 * elemento precisa ser renderizado na tela
	 * 
	 * @param canvas
	 */
	public void render(Canvas canvas);
	
	/**
	 * Metodo executado toda vez que o 
	 * elemento precisa ser renderizado na tela
	 * 
	 * @param canvas
	 * @param rect
	 */
	public void render(Canvas canvas, RectF rect);
}
