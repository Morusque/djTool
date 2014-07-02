import java.util.Vector;

public class TracksManager {

	Run parent;

	int currentTrack = 0;
	Vector<Track> tracksList = new Vector<Track>();

	String selectionType = "all";

	float refFr = 440;
	float closeFrPerBpmThreshold = 0.05f;
	float closeBpmThreshold = 0.05f;

	public float getCloseFrPerBpmThreshold() {
		return closeFrPerBpmThreshold;
	}

	public void setCloseFrPerBpmThreshold(float closeFrPerBpmThreshold) {
		this.closeFrPerBpmThreshold = closeFrPerBpmThreshold;
	}

	public float getRefFr() {
		return refFr;
	}

	public void setRefFr(float refFr) {
		this.refFr = refFr;
	}

	public TracksManager(Run parent) {
		this.parent = parent;
	}

	public void newTrack() {
		int theId = lookForBlankId();
		int newMatchesSize = Math.max(theId + 1, highestId() + 1);
		int[] matches = new int[newMatchesSize];
		for (int i = 0; i < matches.length; i++)
			matches[i] = 0;
		Track newTrack = new Track(theId, "", "", 1, 0, 0, 0, 0, 0, "", matches);
		for (int i = 0; i < parent.tracks.size(); i++) {
			int[] newMatches = new int[newMatchesSize];
			for (int j = 0; j < newMatches.length; j++)
				newMatches[j] = 0;
			for (int j = 0; j < parent.tracks.get(i).getMatches().length; j++)
				newMatches[j] = parent.tracks.get(i).getMatches()[j];
			parent.tracks.get(i).setMatches(newMatches);
		}
		parent.tracks.add(newTrack);
		currentTrack = theId;
		makeTheList(null);
		parent.cellsManager.updateEditTrack();
		parent.cellsManager.updateList();
	}

	public int highestId() {
		int highest = 0;
		for (int i = 0; i < parent.tracks.size(); i++)
			if (parent.tracks.get(i).getId() > highest)
				highest = parent.tracks.get(i).getId();
		return highest;
	}

	private int lookForBlankId() {
		int id = 0;
		for (int i = 0; i < parent.tracks.size(); i++) {
			if (id == parent.tracks.get(i).getId()) {
				id++;
				i = -1;
			}
		}
		return id;
	}

	public void makeTheList(String mode) {
		closeFrPerBpmThreshold = Float.parseFloat(parent.cellsManager.valueOfCell("frBpmThreshold", 0));
		closeBpmThreshold = Float.parseFloat(parent.cellsManager.valueOfCell("bpmThreshold", 0));
		if (mode != null)
			selectionType = mode;
		if (mode != null)
			parent.cellsManager.setListOffset(0);
		parent.cellsManager.getSpecificCell("seeAll", 0).setActive(false);
		parent.cellsManager.getSpecificCell("seeMatches", 0).setActive(false);
		parent.cellsManager.getSpecificCell("seeClose", 0).setActive(false);
		if (selectionType == "all")
			parent.cellsManager.getSpecificCell("seeAll", 0).setActive(true);
		if (selectionType == "matches")
			parent.cellsManager.getSpecificCell("seeMatches", 0).setActive(true);
		if (selectionType == "close")
			parent.cellsManager.getSpecificCell("seeClose", 0).setActive(true);
		tracksList = new Vector<Track>();
		for (int i = 0; i < parent.tracks.size(); i++) {
			if (fitsSearches(parent.tracks.get(i).getAuthor(), parent.cellsManager.valueOfCell("searchAuthor", 0))
					&& fitsSearches(parent.tracks.get(i).getTitle(), parent.cellsManager.valueOfCell("searchTitle", 0))) {
				if ((selectionType == "all") || (selectionType == "matches" && parent.tracks.get(i).getMatches()[currentTrack().getId()] != 0)
						|| (selectionType == "close" && parent.tracks.get(i).closenessTo(currentTrack()) < closeFrPerBpmThreshold)
						&& (Math.abs(parent.tracks.get(i).getBpm() - currentTrack().getBpm()) < closeBpmThreshold))
					tracksList.add(parent.tracks.get(i));
			}
		}
		if (selectionType == "close")
			sortTheList("bpm");
		parent.cellsManager.updateList();
	}

	private boolean fitsSearches(String haystack, String needle) {
		if (needle.equals(""))
			return true;
		for (int i = 0; i <= haystack.length() - needle.length(); i++) {
			if (haystack.substring(i, i + needle.length()).toLowerCase().equals(needle.toLowerCase()))
				return true;
		}
		return false;
	}

	public void removeTrack() {
		if (parent.tracks.size() > 1) {
			for (int i = 0; i < parent.tracks.size(); i++) {
				if (parent.tracks.get(i).getId() == currentTrack().getId()) {
					for (int j = 0; j < parent.tracks.size(); j++) {
						parent.tracks.get(j).setMatch(currentTrack().getId(), 0);
					}
					parent.tracks.remove(i);
					currentTrack = parent.tracks.size() - 1;
					makeTheList(null);
					parent.cellsManager.updateEditTrack();
					parent.cellsManager.updateList();
					return;
				}
			}
		}
	}

	public Track currentTrack() {
		for (int i = 0; i < parent.tracks.size(); i++) {
			if (parent.tracks.get(i).getId() == currentTrack)
				return parent.tracks.get(i);
		}
		return null;
	}

	public Track getTrackById(int id) {
		for (int i = 0; i < parent.tracks.size(); i++) {
			if (parent.tracks.get(i).getId() == id)
				return parent.tracks.get(i);
		}
		return null;
	}

	public void computeEditHzPerBpm(Float value) {
		currentTrack().setRefFr(refFr);
		currentTrack().setTempoForRef(value);
		float frPerBpm = 0;
		if (currentTrack().getTempoForRef() > 0)
			frPerBpm = currentTrack().getRefFr() / currentTrack().getTempoForRef();
		if (frPerBpm > 0) {
			while (frPerBpm >= 2)
				frPerBpm /= 2;
			while (frPerBpm < 1)
				frPerBpm *= 2;
		}
		if (currentTrack().getTempoForRef() > 0)
			currentTrack().setKeyFr(currentTrack().getRefFr() * currentTrack().getBpm() / currentTrack().getTempoForRef());
		currentTrack().setFrPerBpm(frPerBpm);
	}

	public void computeEditBpm(float value) {
		currentTrack().setBpm(value);
		if (currentTrack().getTempoForRef() > 0)
			currentTrack().setKeyFr(currentTrack().getRefFr() * currentTrack().getBpm() / currentTrack().getTempoForRef());
	}

	public void computeEditKeyFr(float value) {
		currentTrack().setKeyFr(value);
		currentTrack().setRefFr(refFr);		
		float frPerBpm = 0;
		currentTrack().setTempoForRef(currentTrack().getBpm()*currentTrack().getRefFr()/currentTrack().getKeyFr());
		if (currentTrack().getTempoForRef() > 0)
			frPerBpm = currentTrack().getRefFr() / currentTrack().getTempoForRef();
		if (frPerBpm > 0) {
			while (frPerBpm >= 2)
				frPerBpm /= 2;
			while (frPerBpm < 1)
				frPerBpm *= 2;
		}		
		currentTrack().setFrPerBpm(frPerBpm);
	}

	public void edit(int copy) {
		copy += parent.cellsManager.getListOffset();
		if (copy < tracksList.size()) {
			int cellId = tracksList.get(copy).getId();
			currentTrack = cellId;
			parent.cellsManager.updateEditTrack();
			parent.cellsManager.updateList();
		}
	}

	public void switchMatch(int copy) {
		copy += parent.cellsManager.getListOffset();
		if (copy < tracksList.size()) {
			int newState = (tracksList.get(copy).getMatches()[currentTrack] + 1) % 3;
			tracksList.get(copy).setMatch(currentTrack().getId(), newState);
			currentTrack().setMatch(tracksList.get(copy).getId(), newState);
			parent.cellsManager.updateList();
		}
	}

	public void sortTheList(String mode) {
		Vector<Track> sortedList = new Vector<Track>();
		int smallest = -1;
		float smallestValue = 0;
		while (tracksList.size() > 0) {
			for (int i = 0; i < tracksList.size(); i++) {
				float relevantValue = 0;
				if (mode.equals("bpm"))
					relevantValue = tracksList.get(i).getBpm();
				if (mode.equals("keyFr"))
					relevantValue = tracksList.get(i).getKeyFr();
				if (mode.equals("frPerBpm"))
					relevantValue = tracksList.get(i).getFrPerBpm();
				if (mode.equals("close"))
					relevantValue = tracksList.get(i).closenessTo(currentTrack());
				if (smallestValue >= relevantValue || smallest == -1) {
					smallestValue = relevantValue;
					smallest = i;
				}
			}
			sortedList.add(tracksList.remove(smallest));
			smallest = -1;
		}
		for (int i = 0; i < sortedList.size(); i++)
			tracksList.add(sortedList.get(i));
		parent.cellsManager.updateList();
	}

	public void keepOnlyDuplexes() {
		for (int i = 0; i < parent.tracks.size(); i++) {
			for (int j = 0; j < parent.tracks.get(i).matches.length; j++) {
				if (parent.tracks.get(i).matches[j] != 0) {
					Track target = getTrackById(j);
					if (target == null) {
						parent.tracks.get(i).setMatch(j, 0);
					} else if (target.matches[parent.tracks.get(i).getId()] == 0) {
						parent.tracks.get(i).setMatch(j, 0);
					}
				}
			}
		}
	}

	public void duplicateTrack() {
		int theId = lookForBlankId();
		int newMatchesSize = Math.max(theId + 1, highestId() + 1);
		int[] matches = new int[newMatchesSize];
		for (int i = 0; i < matches.length; i++) {
			if (i == matches.length - 1)
				matches[i] = 0;
			else
				matches[i] = currentTrack().getMatches()[i];
		}
		Track newTrack = new Track(theId, currentTrack().getAuthor(), currentTrack().getTitle(), currentTrack().getPart() + 1, currentTrack()
				.getBpm(), currentTrack().getKeyFr(), currentTrack().getFrPerBpm(), currentTrack().getTempoForRef(), currentTrack().getRefFr(),
				currentTrack().getKeyNotes(), matches);
		for (int i = 0; i < parent.tracks.size(); i++) {
			int[] newMatches = new int[newMatchesSize];
			for (int j = 0; j < newMatches.length; j++)
				newMatches[j] = 0;
			for (int j = 0; j < parent.tracks.get(i).getMatches().length; j++)
				newMatches[j] = parent.tracks.get(i).getMatches()[j];
			newMatches[theId] = matches[i];
			parent.tracks.get(i).setMatches(newMatches);
		}
		parent.tracks.add(newTrack);
		currentTrack = theId;
		makeTheList(null);
		parent.cellsManager.updateEditTrack();
		parent.cellsManager.updateList();
	}
}
