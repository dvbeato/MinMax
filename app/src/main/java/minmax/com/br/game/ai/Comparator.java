package minmax.com.br.game.ai;

public class Comparator {
	
	public static final Comparator MAX = new Comparator(true);
	public static final Comparator MIN = new Comparator(false);
	
	private boolean max = false;
	private boolean min = false;
	
	public Comparator(boolean maximize) {
		if(maximize) {
			this.max = true;
		} else {
			this.min = true;
		}
	}
	
	public int initialValue() {
		if(max) {
			return -10000;
		} else {
			return 10000;
		}
	}
	
	public int compare(Integer bestScore, Integer newScore) {
		
		if(this.max) {
			return bestScore.compareTo(newScore);
		} else {
			return newScore.compareTo(bestScore);
		}
		
	}
	
	public Comparator opposite() {
		return (this.max) ? MIN : MAX; 
	}
}
