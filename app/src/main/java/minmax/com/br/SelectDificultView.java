package minmax.com.br;

import minmax.com.br.game.Game;
import minmax.com.br.minmax.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class SelectDificultView implements View.OnClickListener {
	
	
	enum Dificuldade {
		FACIL(1, "Facil"), MEDIO(2, "Medio"), DIFICIL(3, "Dificil");
		
		private int nivel;
		private String nome;
		
		private Dificuldade(int nivel, String nome) {
			this.nivel = nivel;
			this.nome = nome;
		}
		
		public int getNivel() {
			return this.nivel;
		}
		
		public String getNome() {
			return this.nome;
		}
	}
	
	private Integer dificuldadeSelecionada = Dificuldade.MEDIO.getNivel();
	
	private LayoutInflater inflater;
	private Activity activity;
	
	private Dialog dialog;
	
	public SelectDificultView(Activity activity) {
		this.activity = activity;
		this.inflater = (LayoutInflater) this.activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		this.dialog = new Dialog(activity);
	}
	
	@Override
	public void onClick(View arg0) {
		
		View dificuldade = inflater.inflate(R.layout.dialog_difficulty, null);
		
		Button btnEasy = (Button) dificuldade.findViewById(R.id.btnEasy);
		Button btnMedium = (Button) dificuldade.findViewById(R.id.btnMedium);
		Button btnHard = (Button) dificuldade.findViewById(R.id.btnHard);
		
		dialog.setTitle(R.string.dificult);
		dialog.setContentView(dificuldade);
		
		btnEasy.setOnClickListener(new OpenDialog(Dificuldade.FACIL));
		
		btnMedium.setOnClickListener(new OpenDialog(Dificuldade.MEDIO));
		
		btnHard.setOnClickListener(new OpenDialog(Dificuldade.DIFICIL));
		
		dialog.show();
	}

	public Integer getDificuldadeSelecionada() {
		return dificuldadeSelecionada;
	}

	public void setDificuldadeSelecionada(Integer dificuldadeSelecionada) {
		this.dificuldadeSelecionada = dificuldadeSelecionada;
	}
	
	/**
	 * Inner Classe para abrir o dialog ao selecionar a dificuldade
	 */
	private class OpenDialog implements View.OnClickListener {
		private int nivelDificuldade;
		
		public OpenDialog(Dificuldade dificuldade) {
			this.nivelDificuldade = dificuldade.getNivel();
		}
		
		@Override
		public void onClick(View view) {
			dialog.dismiss();
			Intent intent = new Intent(activity, GameActivity.class);
			intent.putExtra(Game.DIFICULT, nivelDificuldade);
			intent.putExtra(Game.GAME_MODE, true);
			activity.startActivity(intent);
		}
		
	}
	
}
