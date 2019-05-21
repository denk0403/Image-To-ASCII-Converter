import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {

	public static void main(String[] args) {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png",
				"jpeg");
		fileChooser.setFileFilter(filter);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileHidingEnabled(true);
		JPanel resolution = new JPanel();
		resolution.add(new JLabel("Resolution"));
		JTextField width = new JTextField("1500");
		JTextField height = new JTextField("900");
		resolution.add(width);
		resolution.add(new JLabel("x"));
		resolution.add(height);
		fileChooser.add(resolution);
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File openFile = fileChooser.getSelectedFile();
			try {
				new DisplayASCII(new ASCIIImage(ImageIO.read(openFile),
						Integer.parseInt(width.getText()), Integer.parseInt(height.getText())));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
