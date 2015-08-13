package minmax.com.br.game.ai;

import java.util.ArrayList;
import java.util.List;

import minmax.com.br.game.utils.GameMove;

public class MoveEvaluation {
	
	public static final int INIT_SCORE = 0;
	
	private List<GameMove> moves;
	private int score;
	
	public MoveEvaluation(int score) {
		this.score = score;
	}
	
	public MoveEvaluation(List<GameMove> moves, int score) {
		this.moves = moves;
		this.score = score;
	}

    public MoveEvaluation(GameMove move, int score) {
        this.moves = new ArrayList<GameMove>();
        this.moves.add(move);
        this.score = score;
    }

    public void addGameMove(GameMove move) {
        if(this.moves == null) {
            this.moves = new ArrayList<GameMove>();
        }

        this.moves.add(move);
    }

    public List<GameMove> getMoves() {
        return moves;
    }

    public void setMoves(List<GameMove> moves) {
        this.moves = moves;
    }

    public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
}
