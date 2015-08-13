package minmax.com.br;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import minmax.com.br.minmax.R;

public class MainActivity extends Activity {
	
	private SelectDificultView selectDificult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.selectDificult = new SelectDificultView(this);
		
		Button btnDificuldade = (Button) findViewById(R.id.pvc);
		btnDificuldade.setOnClickListener(this.selectDificult);
	}
	
	public void startGame(View view) {
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}
	
	public void direcionarSobre(View view) {
		Intent intent = new Intent(this, SobreActivity.class);
		startActivity(intent);
	}
	
}
