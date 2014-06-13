public class Track {
	
	protected int id;
	protected String author;
	protected String title;
	protected int part;
	protected float bpm;
	protected float keyFr;
	protected float frPerBpm;
	protected float tempoForRef;
	protected float refFr;
	protected String keyNotes;
	protected int[] matches;
		
	public Track(int id, String author, String title, int part, float bpm,
			float keyFr, float frPerBpm, float tempoForRef, float refFr,
			String keyNotes, int[] matches) {
		super();
		this.id = id;
		this.author = author;
		this.title = title;
		this.part = part;
		this.bpm = bpm;
		this.keyFr = keyFr;
		this.frPerBpm = frPerBpm;
		this.tempoForRef = tempoForRef;
		this.refFr = refFr;
		this.keyNotes = keyNotes;
		this.matches = matches;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPart() {
		return part;
	}

	public void setPart(int part) {
		this.part = part;
	}

	public float getBpm() {
		return bpm;
	}

	public void setBpm(float bpm) {
		this.bpm = bpm;
	}

	public float getKeyFr() {
		return keyFr;
	}

	public void setKeyFr(float keyFr) {
		this.keyFr = keyFr;
	}

	public float getFrPerBpm() {
		return frPerBpm;
	}

	public void setFrPerBpm(float frPerBpm) {
		this.frPerBpm = frPerBpm;
	}

	public float getTempoForRef() {
		return tempoForRef;
	}

	public void setTempoForRef(float tempoForRef) {
		this.tempoForRef = tempoForRef;
	}

	public float getRefFr() {
		return refFr;
	}

	public void setRefFr(float refFr) {
		this.refFr = refFr;
	}

	public int[] getMatches() {
		return matches;
	}

	public void setMatches(int[] matches) {
		this.matches = matches;
	}

	public String getKeyNotes() {
		return keyNotes;
	}

	public void setKeyNotes(String keyNotes) {
		this.keyNotes = keyNotes;
	}

	public void setMatch(int index, int state) {
		this.matches[index] = state;	
	}

	public float closenessTo(Track compare) {
		float biggest = Math.max(frPerBpm,compare.getFrPerBpm());
		float smallest = Math.min(frPerBpm,compare.getFrPerBpm());
		float result = Math.min(biggest/smallest,smallest/(biggest/2)) - 1;
		return result;
	}

	public int getNbOfMatches() {
		int result = 0;
		for (int i=0;i<matches.length;i++) {
			if (matches[i]!=0) result++;
		}
		return result;
	}

}
