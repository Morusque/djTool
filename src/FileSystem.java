import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

public class FileSystem {

	Run parent;

	String filePath = "";
	String fileName = "";

	FileSystem(Run parent) {
		this.parent = parent;
	}

	public void load() {
		Frame frame = new Frame();
		FileDialog fileDialog = new FileDialog(frame, "open", FileDialog.LOAD);
		fileDialog.setVisible(true);
		filePath = fileDialog.getDirectory();
		fileName = fileDialog.getFile();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		Vector<Track> tracks = new Vector<Track>();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document library = builder.parse(filePath + fileName);
			Element root = library.getDocumentElement();
			Element[] trackNodes = getElementsByTagName(root, "track");
			int highestId = 0;
			for (int i = 0; i < trackNodes.length; i++) {
				int thisId = Integer.parseInt(trackNodes[i].getAttribute("id"));
				if (thisId > highestId)
					highestId = thisId;
			}
			for (int i = 0; i < trackNodes.length; i++) {
				int id = Integer.parseInt(trackNodes[i].getAttribute("id"));
				String author = trackNodes[i].getAttribute("author");
				String title = trackNodes[i].getAttribute("title");
				int part = Integer.parseInt(trackNodes[i].getAttribute("part"));
				float bpm = Float.parseFloat(trackNodes[i].getAttribute("bpm"));
				float keyFr = Float.parseFloat(trackNodes[i].getAttribute("keyFr"));
				float frPerBpm = Float.parseFloat(trackNodes[i].getAttribute("frPerBpm"));
				float tempoForRef = Float.parseFloat(trackNodes[i].getAttribute("tempoForRef"));
				float refFr = Float.parseFloat(trackNodes[i].getAttribute("refFr"));
				String keyNotes = trackNodes[i].getAttribute("keyNotes");
				int[] matches = new int[highestId + 1];
				Element allMatches = getElementsByTagName(trackNodes[i], "matches")[0];
				Element[] matchesNodes = getElementsByTagName(allMatches, "match");
				for (int j = 0; j < trackNodes.length; j++)
					matches[j] = 0;
				for (int j = 0; j < matchesNodes.length; j++)
					matches[Integer.parseInt(matchesNodes[j].getAttribute("id"))] = Integer.parseInt(matchesNodes[j].getAttribute("type"));
				tracks.add(new Track(id, author, title, part, bpm, keyFr, frPerBpm, tempoForRef, refFr, keyNotes, matches));
			}

		} catch (Exception e) {
			System.out.println("exception : " + e.getMessage());
		}
		parent.tracks = tracks;
		parent.tracksManager.keepOnlyDuplexes();
		parent.tracksManager.currentTrack = tracks.get((int) Math.floor(Math.random() * tracks.size())).getId();
		parent.tracksManager.makeTheList("all");
		parent.tracksManager.sortTheList("close");
		parent.cellsManager.updateEditTrack();
		parent.cellsManager.updateList();
	}

	public void save() {
		if (filePath.equals("") || fileName.equals("")) {
			Frame frame = new Frame();
			FileDialog fileDialog = new FileDialog(frame, "save", FileDialog.LOAD);
			fileDialog.setVisible(true);
			filePath = fileDialog.getDirectory();
			fileName = fileDialog.getFile();

			if (!fileName.substring(fileName.length() - 4).toLowerCase().equals(".xml"))
				fileName += ".xml";
		}
		File file = new File(filePath + fileName);
		Calendar now = Calendar.getInstance();
		String theDate = String.valueOf(now.get(Calendar.YEAR) + " " + now.get(Calendar.MONTH) + " " + now.get(Calendar.DAY_OF_MONTH) + " ");
		File backupFile = new File(filePath + "backups/" + theDate + fileName);
		saveXml(file);
		saveXml(backupFile);
	}

	public void saveXml(File file) {
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fileStream = new FileOutputStream(file);
			String empty = "<library></library>";
			for (int i = 0; i < empty.length(); i++) {
				fileStream.write(empty.charAt(i));
			}
			fileStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document library = builder.parse(file);
			Element root = library.getDocumentElement();
			for (int i = 0; i < parent.tracks.size(); i++) {
				Element thisNode = library.createElement("track");
				Track thisTrack = parent.tracks.get(i);

				thisNode.setAttribute("id", String.valueOf(thisTrack.getId()));
				thisNode.setAttribute("author", String.valueOf(thisTrack.getAuthor()));
				thisNode.setAttribute("title", String.valueOf(thisTrack.getTitle()));
				thisNode.setAttribute("part", String.valueOf(thisTrack.getPart()));
				thisNode.setAttribute("bpm", String.valueOf(thisTrack.getBpm()));
				thisNode.setAttribute("keyFr", String.valueOf(thisTrack.getKeyFr()));
				thisNode.setAttribute("frPerBpm", String.valueOf(thisTrack.getFrPerBpm()));
				thisNode.setAttribute("tempoForRef", String.valueOf(thisTrack.getTempoForRef()));
				thisNode.setAttribute("refFr", String.valueOf(thisTrack.getRefFr()));
				thisNode.setAttribute("keyNotes", String.valueOf(thisTrack.getKeyNotes()));

				Element allMatches = library.createElement("matches");
				for (int j = 0; j < thisTrack.matches.length; j++) {
					if (thisTrack.matches[j] != 0) {
						Element matchNode = library.createElement("match");
						matchNode.setAttribute("id", String.valueOf(j));
						matchNode.setAttribute("type", String.valueOf(thisTrack.matches[j]));
						allMatches.appendChild(matchNode);
					}
				}
				thisNode.appendChild(allMatches);
				root.appendChild(thisNode);
			}

			Source source = new DOMSource(library);
			TransformerFactory fabrique = TransformerFactory.newInstance();
			Transformer transformer = fabrique.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			javax.xml.transform.Result resultat = new StreamResult(file);
			transformer.transform(source, resultat);

		} catch (Exception e) {
			System.out.println("exception : " + e.getMessage());
		}
	}

	void exportGraph(float bpmRef, int marginBpm) {
		Frame frame = new Frame();
		FileDialog fileDialog = new FileDialog(frame, "save", FileDialog.LOAD);
		fileDialog.setVisible(true);
		String filePath = fileDialog.getDirectory() + fileDialog.getFile();
		if (!filePath.substring(filePath.length() - 4).toLowerCase().equals(".gvt"))
			filePath += ".gvt";
		File file = new File(filePath);
		FileOutputStream fileStream;
		try {
			boolean[] added = new boolean[parent.tracksManager.highestId() + 1];
			for (int i = 0; i < added.length; i++) {
				added[i] = false;
			}
			fileStream = new FileOutputStream(file);
			Vector<String> lines = new Vector<String>();
			lines.add("graph G {");
			lines.add("overlap = false;");
			lines.add("graph [charset=latin1];");
			parent.tracksManager.makeTheList("all");
			parent.tracksManager.sortTheList("frPerBpm");
			for (int i = 0; i < parent.tracks.size(); i++) {
				Vector<Track> trackParts = new Vector<Track>();
				for (int j = 0; j < parent.tracksManager.tracksList.size(); j++) {
					if (!added[parent.tracksManager.tracksList.get(j).getId()]
							&& parent.tracksManager.tracksList.get(i).getAuthor().equals(parent.tracksManager.tracksList.get(j).getAuthor())
							&& parent.tracksManager.tracksList.get(i).getTitle().equals(parent.tracksManager.tracksList.get(j).getTitle())
							&& parent.tracksManager.tracksList.get(i).getNbOfMatches() > 0) {
						if (bpmRef == -1 || (Math.abs(parent.tracksManager.tracksList.get(j).getBpm() - bpmRef) <= marginBpm)) {
							trackParts.add(parent.tracksManager.tracksList.get(j));
							added[parent.tracksManager.tracksList.get(j).getId()] = true;
						}
					}
				}
				String thisLine = "";
				if (trackParts.size() == 0) {
					// do nothing
				} else if (trackParts.size() < 2) {
					thisLine += "T" + trackParts.get(0).getId() + " [shape=box, label=\"" + trackParts.get(0).getAuthor() + "\\n"
							+ trackParts.get(0).getTitle() + "\\n" + trackParts.get(0).getBpm() + "\"";
					thisLine += "];";
					lines.add(thisLine);
				} else {
					thisLine += "subgraph clusterMT" + trackParts.get(0).getId() + " {";
					thisLine += "label = \"" + trackParts.get(0).getAuthor() + "\\n" + trackParts.get(0).getTitle() + "\\n"
							+ trackParts.get(0).getBpm() + "\";";
					for (int j = 0; j < trackParts.size(); j++) {
						thisLine += "T" + trackParts.get(j).getId() + " [label=\"" + trackParts.get(j).getPart() + "\"];";
					}
					thisLine += "}";
					lines.add(thisLine);
				}
			}
			for (int i = 0; i < parent.tracks.size(); i++) {
				for (int m = 0; m < parent.tracks.get(i).getMatches().length; m++) {
					if (parent.tracks.get(i).getMatches()[m] != 0) {
						if (m > parent.tracks.get(i).getId()) {
							if (bpmRef == -1
									|| (Math.abs(parent.tracks.get(i).getBpm() - bpmRef) <= marginBpm && Math.abs(parent.tracksManager
											.getTrackById(m).getBpm() - bpmRef) <= marginBpm)) {
								String thisLine = "T" + parent.tracks.get(i).getId() + " -- T" + m;
								if (parent.tracks.get(i).getMatches()[m] == 2) thisLine +=	" [color=\"#FF0000\"]";
								thisLine +=";";
								lines.add(thisLine);
							}
						}
					}
				}
			}
			lines.add("}");
			for (int li = 0; li < lines.size(); li++) {
				for (int ch = 0; ch < lines.get(li).length(); ch++) {
					fileStream.write(lines.get(li).charAt(ch));
				}
				fileStream.write('\n');
			}
			fileStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void exportMap() {
		Frame frame = new Frame();
		FileDialog fileDialog = new FileDialog(frame, "save", FileDialog.LOAD);
		fileDialog.setVisible(true);
		String filePath = fileDialog.getDirectory() + fileDialog.getFile();
		if (!filePath.substring(filePath.length() - 5).toLowerCase().equals(".html"))
			filePath += ".html";
		File file = new File(filePath);
		FileOutputStream fileStream;
		try {
			boolean[] added = new boolean[parent.tracks.size()];
			for (int i = 0; i < added.length; i++) {
				added[i] = false;
			}
			fileStream = new FileOutputStream(file);
			Vector<String> lines = new Vector<String>();
			lines.add("<html>");
			for (int i = 0; i < parent.tracks.size(); i++) {
				Vector<Track> trackParts = new Vector<Track>();
				for (int j = 0; j < parent.tracks.size(); j++) {
					if (!added[parent.tracks.get(j).getId()] && parent.tracks.get(i).getAuthor().equals(parent.tracks.get(j).getAuthor())
							&& parent.tracks.get(i).getTitle().equals(parent.tracks.get(j).getTitle())) {
						trackParts.add(parent.tracks.get(j));
						added[parent.tracks.get(j).getId()] = true;
					}
				}
				String thisLine = "";
				if (trackParts.size() == 0) {
					// do nothing
				} else if (trackParts.size() < 2) {
					thisLine += "<div style=\"" + "top: " + (2 - trackParts.get(0).getFrPerBpm()) * 1000 + ";" + "left: "
							+ trackParts.get(0).getBpm() * 100 + ";" + "position: absolute;" + "z-index: 1;" + "visibility: show;\">"
							+ trackParts.get(0).getAuthor() + "<br/>" + trackParts.get(0).getTitle() + "</div>";
					lines.add(thisLine);
				} else {
					thisLine += "<div style=\"" + "top: " + (2 - trackParts.get(0).getFrPerBpm()) * 1000 + ";" + "left: "
							+ trackParts.get(0).getBpm() * 100 + ";" + "position: absolute;" + "z-index: 1;" + "visibility: show;\">"
							+ trackParts.get(0).getAuthor() + "<br/>" + trackParts.get(0).getTitle() + "<br/>" + trackParts.get(0).getPart()
							+ "</div>";
					lines.add(thisLine);
				}
			}
			lines.add("</html>");
			for (int li = 0; li < lines.size(); li++) {
				for (int ch = 0; ch < lines.get(li).length(); ch++) {
					fileStream.write(lines.get(li).charAt(ch));
				}
				fileStream.write('\n');
			}
			fileStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	Element[] getElementsByTagName(Element baseElement, String name) {
		Vector<Element> elements = new Vector<Element>();
		NodeList list = baseElement.getElementsByTagName(name);
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				elements.add((Element) node);
			}
		}
		Element[] result = new Element[elements.size()];
		for (int i = 0; i < elements.size(); i++) {
			result[i] = elements.get(i);
		}
		return result;
	}

	public void exportCloseGraph() {
		exportGraph(parent.tracksManager.currentTrack().getBpm(), Integer.parseInt(parent.cellsManager.valueOfCell("bpmThreshold", 0)));
	}

}
