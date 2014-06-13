import java.util.Vector;

public class CellsManager {

	Run parent;
	
	private int listOffset=0;
	private int currentSelected=0;
	
	public int getListOffset() {
		return listOffset;
	}

	public void setListOffset(int listOffset) {
		this.listOffset = listOffset;
	}

	CellsManager(Run parent) {
		this.parent = parent;
	}

	public Vector<Cell> cellsPanel() {
		float[] pX = new float[13];
		float[] pY = new float[8 + parent.window.nbSongsInList];
		float[] xCoeff = {0.2f,0.7f,0.5f,2.5f,2.5f,0.2f,0.7f,0.7f,0.7f,0.7f,0.7f,2.0f,0.7f,0.2f};
		pX[0]=(float)parent.window.width*xCoeff[0]/(float)pX.length;
		for (int i = 1; i < pX.length; i++)
			pX[i] = pX[i-1] + (float)parent.window.width*xCoeff[i]/(float)pX.length;
		for (int i = 0; i < pY.length; i++)
			pY[i] = (i + 1) * (float)parent.window.height / (pY.length + 1);

		Vector<Cell> cells = new Vector<Cell>();
		cells.add(new Button("loadFile", 0, pX, pY, 0, 0, "load file", parent.window));
		cells.add(new Button("saveFile", 0, pX, pY, 1, 0, "save file", parent.window));
		cells.add(new Button("exportGraph", 0, pX, pY, 2, 0, "export graph", parent.window));
		cells.add(new Button("exportMap", 0, pX, pY, 3, 0, "export map", parent.window));
		cells.add(new TextArea("refFr", 0, pX, pY, 8, 0, "440", parent.window));
		cells.add(new Button("exportCloseGraph", 0, pX, pY, 2, 1, "export close bpm graph", parent.window));		
		cells.add(new Button("newTrack", 0, pX, pY, 0, 2, "new track", parent.window));
		cells.add(new Cell("authorLabel", 0, pX, pY, 2, 2, "author", parent.window));
		cells.add(new Cell("titleLabel", 0, pX, pY, 3, 2, "title", parent.window));
		cells.add(new Cell("partLabel", 0, pX, pY, 4, 2, "part", parent.window));
		cells.add(new Cell("bpmLabel", 0, pX, pY, 5, 2, "bpm", parent.window));
		cells.add(new Cell("keyFrLabel", 0, pX, pY, 6, 2, "key fr", parent.window));
		cells.add(new Cell("frPerBpmLabel", 0, pX, pY, 7, 2, "hz per bpm", parent.window));
		cells.add(new Cell("tempoForRefLabel", 0, pX, pY, 8, 2, "tempo for ref", parent.window));
		cells.add(new Cell("refFrLabel", 0, pX, pY, 9, 2, "ref fr used", parent.window));
		cells.add(new Cell("keyNotesLabel", 0, pX, pY, 10, 2, "key notes", parent.window));
		cells.add(new Button("removeTrack", 0, pX, pY, 0, 3, "remove track", parent.window));
		cells.add(new TextArea("currentAuthor", 0, pX, pY, 2, 3, "", parent.window));
		cells.add(new TextArea("currentTitle", 0, pX, pY, 3, 3, "", parent.window));
		cells.add(new TextArea("currentPart", 0, pX, pY, 4, 3, "", parent.window));
		cells.add(new TextArea("currentBpm", 0, pX, pY, 5, 3, "", parent.window));
		cells.add(new Cell("currentKeyFr", 0, pX, pY, 6, 3, "", parent.window));
		cells.add(new Cell("currentFrPerBpm", 0, pX, pY, 7, 3, "", parent.window));
		cells.add(new TextArea("currentTempoForRef", 0, pX, pY, 8, 3, "", parent.window));
		cells.add(new Cell("currentRefFr", 0, pX, pY, 9, 3, "", parent.window));
		cells.add(new TextArea("currentKeyNotes", 0, pX, pY, 10, 3, "", parent.window));
		cells.add(new Button("duplicateTrack", 0, pX, pY, 0, 4, "duplicate track", parent.window));
		cells.add(new TextArea("searchAuthor", 0, pX, pY, 2, 5, "", parent.window));
		cells.add(new TextArea("searchTitle", 0, pX, pY, 3, 5, "", parent.window));
		cells.add(new Button("sortBpm", 0, pX, pY, 5, 5, "Sort by bpm", parent.window));
		cells.add(new Button("sortKeyFr", 0, pX, pY, 6, 5, "Sort by key", parent.window));
		cells.add(new Button("sortFrPerBpm", 0, pX, pY, 7, 5, "Sort key/bpm", parent.window));
		cells.add(new Button("sortSimilar", 0, pX, pY, 8, 5, "Most similar", parent.window));
		cells.add(new Button("listUp", 0, pX, pY, 11, 6, "up", parent.window));
		cells.add(new Button("listDown", 0, pX, pY, 11, 7, "down", parent.window));
		cells.add(new CheckBox("seeAll", 0, pX, pY, 11, 8, "see all", parent.window));
		cells.add(new CheckBox("seeMatches", 0, pX, pY, 11, 9, "see matches", parent.window));
		cells.add(new CheckBox("seeClose", 0, pX, pY, 11, 10, "see close", parent.window));
		cells.add(new TextArea("frBpmThreshold", 0, pX, pY, 11, 11, "0.02", parent.window));
		cells.add(new TextArea("bpmThreshold", 0, pX, pY, 11, 12, "50", parent.window));
		
		for (int i=0;i<parent.window.nbSongsInList;i++) {
			cells.add(new CheckBox("edit", i, pX, pY, 0, 6+i, "edit", parent.window));
			cells.add(new CheckBox("match", i, pX, pY, 1, 6+i, "match", parent.window));
			cells.add(new Cell("listAuthor", i, pX, pY, 2, 6+i, "", parent.window));
			cells.add(new Cell("listTitle", i, pX, pY, 3, 6+i, "", parent.window));
			cells.add(new Cell("listPart", i, pX, pY, 4, 6+i, "", parent.window));
			cells.add(new Cell("listBpm", i, pX, pY, 5, 6+i, "", parent.window));
			cells.add(new Cell("listKeyFr", i, pX, pY, 6, 6+i, "", parent.window));
			cells.add(new Cell("listFrPerBpm", i, pX, pY, 7, 6+i, "", parent.window));
			cells.add(new Cell("listTempoForRef", i, pX, pY, 8, 6+i, "", parent.window));
			cells.add(new Cell("listRefFr", i, pX, pY, 9, 6+i, "", parent.window));
			cells.add(new Cell("listKeyNotes", i, pX, pY, 10, 6+i, "", parent.window));
		}			
		
		return cells;
	}

	public void updateList() {
		for (int i=0;i<parent.window.nbSongsInList;i++) {
			if (parent.tracksManager.tracksList.size()>i+listOffset) 
			{
				Track thisTrack = parent.tracksManager.tracksList.get(i+listOffset);
				
				if (thisTrack.getId()==parent.tracksManager.currentTrack) getSpecificCell("edit", i).setValue("");
				else getSpecificCell("edit", i).setValue("edit");
				getSpecificCell("edit", i).setActive(thisTrack.getId()==parent.tracksManager.currentTrack);
				getSpecificCell("match", i).setActive(thisTrack.matches[parent.tracksManager.currentTrack().getId()]!=0);
				if (thisTrack.matches[parent.tracksManager.currentTrack().getId()]==2)
						getSpecificCell("match", i).setValue("SUPER");
				else	getSpecificCell("match", i).setValue("match");
				getSpecificCell("listAuthor", i).setValue(thisTrack.author);
				getSpecificCell("listTitle", i).setValue(thisTrack.title);
				getSpecificCell("listPart", i).setValue(String.valueOf(thisTrack.part));
				getSpecificCell("listBpm", i).setValue(String.valueOf(thisTrack.bpm));
				getSpecificCell("listKeyFr", i).setValue(String.valueOf(thisTrack.keyFr));
				getSpecificCell("listFrPerBpm", i).setValue(String.valueOf(thisTrack.frPerBpm));
				getSpecificCell("listTempoForRef", i).setValue(String.valueOf(thisTrack.tempoForRef));
				getSpecificCell("listRefFr", i).setValue(String.valueOf(thisTrack.refFr));
				getSpecificCell("listKeyNotes", i).setValue(thisTrack.keyNotes);
			}
			else
			{
				getSpecificCell("edit", i).setValue("");
				getSpecificCell("edit", i).setActive(false);
				getSpecificCell("match", i).setActive(false);
				getSpecificCell("listAuthor", i).setValue("");
				getSpecificCell("listTitle", i).setValue("");
				getSpecificCell("listPart", i).setValue("");
				getSpecificCell("listBpm", i).setValue("");
				getSpecificCell("listKeyFr", i).setValue("");
				getSpecificCell("listFrPerBpm", i).setValue("");
				getSpecificCell("listTempoForRef", i).setValue("");
				getSpecificCell("listRefFr", i).setValue("");
				getSpecificCell("listKeyNotes", i).setValue("");				
			}
		}
	}

	public void listShift(int i) {
		listOffset += i;
		listOffset = Math.max(listOffset, 0);
		updateList();
	}

	public void selectButton(int s) {
		finalizeSelection(currentSelected);
		currentSelected=s;
		updateSelected();
	}
	
	public void updateSelected() {
		for (int i=0 ; i<parent.cells.size() ; i++) { 
			if (i==currentSelected) parent.cells.get(i).setSelected(true);
			else parent.cells.get(i).setSelected(false);
		}
	}

	public void shiftSelectButton(int i) {
		finalizeSelection(currentSelected);
		currentSelected=(currentSelected+i+parent.cells.size())%parent.cells.size();
		updateSelected();
	}
	
	public void finalizeSelection(int selected) {
		Cell thisCell = parent.cells.get(selected);
		if (thisCell.getId().equals("refFr") && !thisCell.getValue().equals("") )
			parent.tracksManager.setRefFr(Float.parseFloat(thisCell.getValue()));
		if (thisCell.getId().equals("currentAuthor")) 
			parent.tracksManager.currentTrack().setAuthor(thisCell.getValue());
		if (thisCell.getId().equals("currentTitle"))
			parent.tracksManager.currentTrack().setTitle(thisCell.getValue());
		if (thisCell.getId().equals("currentPart") && !thisCell.getValue().equals("") ) 
			parent.tracksManager.currentTrack().setPart(Integer.parseInt(thisCell.getValue()));
		if (thisCell.getId().equals("currentBpm") && !thisCell.getValue().equals("") )
		{
			parent.tracksManager.computeEditBpm(Float.parseFloat(thisCell.getValue()));
			for (int i=0 ; i<parent.cells.size() ; i++) 
			{
				fillSpecificCell("currentKeyFr",String.valueOf(parent.tracksManager.currentTrack().getKeyFr()));
			}
		}
		if (thisCell.getId().equals("currentTempoForRef") && !thisCell.getValue().equals("") )
		{
			parent.tracksManager.computeEditHzPerBpm(Float.parseFloat(thisCell.getValue()));
			for (int i=0 ; i<parent.cells.size() ; i++) 
			{
				fillSpecificCell("currentRefFr",String.valueOf(parent.tracksManager.currentTrack().getRefFr()));
				fillSpecificCell("currentKeyFr",String.valueOf(parent.tracksManager.currentTrack().getKeyFr()));
				fillSpecificCell("currentFrPerBpm",String.valueOf(parent.tracksManager.currentTrack().getFrPerBpm()));
			}
		}
		if (thisCell.getId().equals("currentRefFr") && thisCell.getValue()!="" )
			parent.tracksManager.currentTrack().setRefFr(Float.parseFloat(thisCell.getValue()));
		if (thisCell.getId().equals("currentKeyNotes")) 
			parent.tracksManager.currentTrack().setKeyNotes(thisCell.getValue());
	}

	public void write(char c) {
		parent.cells.get(currentSelected).write(c);
		if (parent.cells.get(currentSelected).getId().equals("searchAuthor") ||
			parent.cells.get(currentSelected).getId().equals("searchTitle")) {
			parent.tracksManager.makeTheList(null);
		}
		if (parent.cells.get(currentSelected).getId().equals("frBpmThreshold") ||
			parent.cells.get(currentSelected).getId().equals("bpmThreshold"))
		{
			parent.tracksManager.makeTheList(null);
		}	
		finalizeSelection(currentSelected);
		updateList();
	}

	public void deleteLastChar() {
		parent.cells.get(currentSelected).backspace();
		if (parent.cells.get(currentSelected).getId().equals("searchAuthor") ||
				parent.cells.get(currentSelected).getId().equals("searchTitle")) {
				parent.tracksManager.makeTheList(null);
		}
		finalizeSelection(currentSelected);
		updateList();
	}

	public void updateEditTrack() {
			fillSpecificCell("currentAuthor",String.valueOf(parent.tracksManager.currentTrack().getAuthor()));
			fillSpecificCell("currentTitle",String.valueOf(parent.tracksManager.currentTrack().getTitle()));
			fillSpecificCell("currentPart",String.valueOf(parent.tracksManager.currentTrack().getPart()));
			fillSpecificCell("currentBpm",String.valueOf(parent.tracksManager.currentTrack().getBpm()));
			fillSpecificCell("currentKeyFr",String.valueOf(parent.tracksManager.currentTrack().getKeyFr()));
			fillSpecificCell("currentFrPerBpm",String.valueOf(parent.tracksManager.currentTrack().getFrPerBpm()));
			fillSpecificCell("currentTempoForRef",String.valueOf(parent.tracksManager.currentTrack().getTempoForRef()));
			fillSpecificCell("currentRefFr",String.valueOf(parent.tracksManager.currentTrack().getRefFr()));
			fillSpecificCell("currentKeyNotes",String.valueOf(parent.tracksManager.currentTrack().getKeyNotes()));
	}
	
	public void fillSpecificCell(String id, String value) {
		for (int i=0 ; i<parent.cells.size() ; i++) 
		{
			if (parent.cells.get(i).getId().equals(id))	parent.cells.get(i).setValue(value);			
		}
	}

	public String valueOfCell(String id, int copy) {
		// returns the value of a given cell
		for (int i=0 ; i<parent.cells.size() ; i++) 
		{
			if (parent.cells.get(i).getId().equals(id) && parent.cells.get(i).getCopy() == copy) 
				return parent.cells.get(i).getValue();			
		}		
		return null;
	}

	public Cell getSpecificCell(String id, int copy) {
		for (int i=0 ; i<parent.cells.size() ; i++) 
		{
			if (parent.cells.get(i).getId().equals(id) && parent.cells.get(i).getCopy() == copy) 
				return parent.cells.get(i);			
		}		
		return null;
	}

}
