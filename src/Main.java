import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {

	public static void main(String[] args) {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png");
		fileChooser.setFileFilter(filter);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileHidingEnabled(true);
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File openFile = fileChooser.getSelectedFile();
			try {
				new DisplayASCII(new ASCIIImage(ImageIO.read(openFile)));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
