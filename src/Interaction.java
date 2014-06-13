import java.awt.event.KeyEvent;

public class Interaction {

	Run parent;
	
	public Interaction(Run parent) {
		this.parent = parent;
	}

	public void buttonUsed(String id, int copy) {
		if (id.equals("loadFile")) parent.fileSystem.load();
		if (id.equals("saveFile")) parent.fileSystem.save();
		if (id.equals("exportGraph")) parent.fileSystem.exportGraph(-1,-1);
		if (id.equals("exportMap")) parent.fileSystem.exportMap();
		if (id.equals("exportCloseGraph")) parent.fileSystem.exportCloseGraph();
		if (id.equals("newTrack")) parent.tracksManager.newTrack();
		if (id.equals("removeTrack")) parent.tracksManager.removeTrack();
		if (id.equals("duplicateTrack")) parent.tracksManager.duplicateTrack();
		if (id.equals("match")) switchMatch(copy);
		if (id.equals("sortBpm")) parent.tracksManager.sortTheList("bpm");
		if (id.equals("sortKeyFr")) parent.tracksManager.sortTheList("keyFr");
		if (id.equals("sortFrPerBpm")) parent.tracksManager.sortTheList("frPerBpm");
		if (id.equals("sortSimilar")) parent.tracksManager.sortTheList("close");
		if (id.equals("seeAll")) parent.tracksManager.makeTheList("all");
		if (id.equals("seeMatches")) parent.tracksManager.makeTheList("matches");
		if (id.equals("seeClose")) parent.tracksManager.makeTheList("close");
		if (id.equals("listUp")) parent.cellsManager.listShift(-1);
		if (id.equals("listDown")) parent.cellsManager.listShift(+1);
		if (id.equals("edit")) parent.tracksManager.edit(copy);
	}

	public void switchMatch(int copy) {
		for (int i=0;i<parent.cells.size();i++) {
			if (	parent.cells.get(i).getId()=="match" && 
					parent.cells.get(i).getCopy()==copy )
			{
				parent.tracksManager.switchMatch(copy);
			}
		}
	}

	public void keyPressed(boolean[] keys, KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode==37) parent.cellsManager.shiftSelectButton(-1);// left
		if (keyCode==39) parent.cellsManager.shiftSelectButton(1);// right
		if (keyCode==8) parent.cellsManager.deleteLastChar();
		if (keyCode!=37 && keyCode!=39 && keyCode!=8 && keyCode!=16 && keyCode!=17 && keyCode!=18) 
		{
			parent.cellsManager.write(e.getKeyChar());
		}
	}

	public void keyReleased(boolean[] keys, KeyEvent e) {
		
	}


}
