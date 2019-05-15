import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {

	public static void main(String[] args) {
		JFileChooser filechooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg",
				"png");
		filechooser.setFileFilter(filter);
		int result = filechooser.showDialog(null, "Open");
		if (result == JFileChooser.APPROVE_OPTION) {
			File openFile = filechooser.getSelectedFile();
			try {
				new DisplayASCII(new ASCIIImage(ImageIO.read(openFile)));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
