package minmax.com.br;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import minmax.com.br.SelectDificultView.Dificuldade;
import minmax.com.br.game.Game;
import minmax.com.br.minmax.R;

public class GameActivity extends Activity {
		
		RenderView renderView;
		Game game;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			Intent intent = getIntent();
			Integer dificuldade = intent.getIntExtra(Game.DIFICULT, Dificuldade.MEDIO.getNivel());
			Boolean playerVsPc = intent.getBooleanExtra(Game.GAME_MODE, false);
			
			setContentView(R.layout.activity_game);
			
			renderView = (RenderView) findViewById(R.id.renderView);
			
			this.game = new Game(renderView, playerVsPc, dificuldade);

		}
		
		
}
