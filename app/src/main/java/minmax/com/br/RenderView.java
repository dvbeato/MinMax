package minmax.com.br;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import minmax.com.br.interfaces.GraphicalElement;

/**
 * Classe responsavel pela renderizacao dos elementos do jogo
 *
 */
public class RenderView extends SurfaceView implements SurfaceHolder.Callback{
		
		private RenderThread thread;
		private int screenWidth;
		private int screenHeight;
		private List<GraphicalElement> elements = new ArrayList<GraphicalElement>();
		
		/**
		 * Construtor padrão
		 * 
		 * @param context
		 * @param attrs
		 */
		public RenderView(Context context, AttributeSet attrs) {
			super(context, attrs);
			
			SurfaceHolder surfaceHolder = getHolder();
			surfaceHolder.addCallback(this);
			
			this.getScreenSize();
			
			thread = new RenderThread(context, surfaceHolder);

		}
		
		/**
		 * Metodo responsavel por adicionar novos elementos para
		 * serem renderizados no jogo.
		 * 
		 * @param elemento
		 */
		public void addElement(GraphicalElement elemento) {
			this.elements.add(elemento);
		}
		
		
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			this.getScreenSize();
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			thread.start();
		}
		
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			thread.setRunning(false);
			
			boolean retry = true;
			
			while(retry)

			{
				try {
					thread.join(); 
					retry = false;
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		public int getScreenWidth() {
			return this.screenWidth;
		}
		
		public int getScreenHeight() {
			return this.screenHeight;
		}
		
		public void finish() {
			((Activity)getContext()).finish();
		}
		
		/**
		 * Método para obter o tamanho da tela
		 * OBS: por causa das diferenças nas versões da API no Android
		 * é necessario checar qual a versão do device em que está sendo executada e usar a API correta.
		 */
		@SuppressLint("NewApi")
		private void getScreenSize() {

			Point size = new Point();
			
			WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
			
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
				wm.getDefaultDisplay().getSize(size);
			    this.screenWidth = size.x;
			    this.screenHeight = size.y; 
			}else{
			    Display d = wm.getDefaultDisplay(); 
			    this.screenWidth = d.getWidth(); 
			    this.screenHeight = d.getHeight(); 
			}
		}
		
		/**
		 * Metodo para calcular o tamanho dos quadrados do mapa
		 * de acordo com a resolucao do aparelho
		 * 
		 * @param width - largura da tela
		 * @param height - altura da tela
		 * @param COLS - quantidade de colunas
		 * @param ROWS - quantidade de linhas
		 * @return
		 */
		public float calculateTileSize(int rows, int cols) {
			int width = this.getScreenWidth(); 
			int height = this.getScreenHeight();
			
			float ratioScreen = getRatio(width, height);
			
			float ratioBoard = getRatio(rows, cols);
			
			int menorTamanho = (width < height) ? width : height;
			
			int maxQtd = (rows > cols) ? rows : cols;
			
			float menorRatio = (ratioScreen < ratioBoard) ? ratioScreen : ratioBoard;
			
			float tileSize = (menorTamanho*(menorRatio)) / maxQtd;

			return tileSize;
		}
		
		/**
		 * Método para obter a proporção entre x e y 
		 * 
		 * @param x
		 * @param y
		 * @return
		 */
		private float getRatio(float x, float y) {
			
			float ratio;

			if(x>y)
				ratio = x/y;
			else
				ratio = y/x;
			
			return ratio;
		}
		
		
		
		/**
		 * Thread para gerenciar o ciclo das renderizacoes do jogo
		 */
		class RenderThread extends Thread {
			
			private SurfaceHolder surfaceHolder;
			
			private Context context;

			private boolean running;
			
			
			public RenderThread(Context context, SurfaceHolder surfaceHolder) {
				
				this.surfaceHolder = surfaceHolder;
				this.context = context;
				setRunning(true);
			}


			public void setRunning(boolean running) {
				this.running = running;
			}


			/**
			 * Metodo executado toda vez que um frame for renderizado
			 * 
			 * @param canvas
			 */
			protected void render(Canvas canvas) {
				canvas.drawColor(Color.BLACK);
				canvas.save();
				
				for (GraphicalElement element : elements) {
					element.render(canvas);
				}

				canvas.restore();
			}
			
			
			@Override
			public void run() {
				
				while(running) {
					
					Canvas canvas = null;
					try {
						canvas = surfaceHolder.lockCanvas();
						if(canvas != null) {
							synchronized (surfaceHolder) {
								render(canvas);
							}
						}
					} finally {
						if(canvas != null)
							surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
			}
		}
}
