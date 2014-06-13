import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Interface {

	Element[] trackNodes;

	public static void main(String[] args) {
		Interface inter = new Interface();
		inter.run();
		System.exit(0);
	}

	void run() {

		// load the nml
		Frame frame = new Frame();
		FileDialog fileDialog = new FileDialog(frame, "open nml file",
				FileDialog.LOAD);
		fileDialog.setVisible(true);
		String filePath = fileDialog.getDirectory();
		String fileName = fileDialog.getFile();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document library = builder.parse(filePath + fileName);
			Element root = library.getDocumentElement();
			Element[] collection = getElementsByTagName(root, "COLLECTION");

			trackNodes = getElementsByTagName(collection[0], "ENTRY");

		} catch (Exception e) {
			System.out.println(e);
		}

		// inspects the folder
		fileDialog = new FileDialog(frame, "open files directory",
				FileDialog.LOAD);
		fileDialog.setVisible(true);
		File selectedFolder = new File(fileDialog.getDirectory());

		// write result in
		fileDialog = new FileDialog(frame, "save text result", FileDialog.SAVE);
		fileDialog.setVisible(true);
		try {
			PrintWriter out = new PrintWriter(fileDialog.getDirectory()
					+ fileDialog.getFile());

			File[] files = selectedFolder.listFiles();
			for (int i = 0; i < files.length; i++) {
				String oneFileName = files[i].toString();
				int afterDir = oneFileName.length() - 1;
				while (afterDir >= 0 && oneFileName.charAt(afterDir) != '\\')
					afterDir--;
				oneFileName = oneFileName.substring(afterDir + 1);
				long modifiedTime = files[i].lastModified();
				if (!isFileReferenced(oneFileName)) {
					out.println("NO  : " + modifiedTime + " " + oneFileName);
				} else {
					out.println("YES : " + modifiedTime + " " + oneFileName);
				}
			}

			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	boolean isFileReferenced(String fileName) {

		for (int i = 0; i < trackNodes.length; i++) {
			Element[] location = getElementsByTagName(trackNodes[i], "LOCATION");
			String url = location[0].getAttribute("FILE");
			if (fileName.equals(url))
				return true;
		}

		return false;

	}

	Interface() {
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

}
