import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DisplayASCII extends JPanel {

	ASCIIImage img;
	Camera camera = new Camera(0, 0, 1 / 12.0);

	public DisplayASCII(ASCIIImage asciiImage) {
		this.img = asciiImage;
		this.initComponents();
	}

	private void initComponents() {
		JFrame frame = new JFrame("ASCII Converter");
		frame.setResizable(false);
		frame.setPreferredSize(new Dimension(
				900 * this.img.getWidth() / Math.max(this.img.getWidth(), this.img.getHeight()),
				900 * this.img.getHeight() / Math.max(this.img.getWidth(), this.img.getHeight())));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		///////////////

		this.setOpaque(true);
		this.setBackground(Color.WHITE);

		this.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				Camera camera = DisplayASCII.this.camera;
				double scale = (float) camera.getScale();

				if (e.getPreciseWheelRotation() > 0.1) { // zoom out (shrinks)
					if (scale > 1 / 50.0) {
						camera.scaleAboutPoint(0.95, e.getX(), e.getY());
					}

				} else if (e.getPreciseWheelRotation() < -0.1) { // zoom in (grows)
					if (scale < 1.26) {
						camera.scaleAboutPoint(1 / 0.95, e.getX(), e.getY());
					}
				}
				System.out.println(camera.getScale());
				DisplayASCII.this.repaint();
			}
		});

		this.addMouseMotionListener(new MouseMotionListener() {

			int lastX = 0;
			int lastY = 0;

			// resets last position of mouseX and mouseY to 0
			// if not dragged
			public void mouseMoved(MouseEvent e) {

				lastX = 0;
				lastY = 0;
			}

			// responsible for panning camera
			public void mouseDragged(MouseEvent e) {
				Camera camera = DisplayASCII.this.camera;
				if (lastX == 0) {
					lastX = e.getX();
					lastY = e.getY();
				}

				// pans camera
				camera.translate(e.getX() - lastX, e.getY() - lastY);

				// updates values of mouse's last position
				lastX = e.getX();
				lastY = e.getY();

				// repaints the view to display the changes
				DisplayASCII.this.repaint();
			}
		});

		this.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			// adds shortcuts for recentering and reseting camera
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (e.getClickCount() == 2) {
						DisplayASCII.this.recenterCamera();
					} else if (e.getClickCount() >= 3) {
						DisplayASCII.this.resetCamera();
					}
				}
				DisplayASCII.this.repaint();
			}
		});

		JPopupMenu rightClick = new JPopupMenu();
		JMenuItem copy = new JMenuItem("Copy ASCII");
		copy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String result = "";
				for (String str : DisplayASCII.this.img.getASCII()) {
					result += str + "\n";
				}
				StringSelection stringSelection = new StringSelection(result);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			}
		});
		rightClick.add(copy);
		JMenuItem save = new JMenuItem("Save image...");
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				BufferedImage awtImage = new BufferedImage(img.getWidth() * img.getFontSize(),
						img.getHeight() * img.getFontSize(), BufferedImage.TYPE_BYTE_GRAY);
				img.paint((Graphics2D) awtImage.getGraphics());

				JFileChooser filechooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg",
						"png");
				filechooser.setFileFilter(filter);
				int result = filechooser.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File saveFile = filechooser.getSelectedFile();
					try {
						ImageIO.write(awtImage, "png", saveFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		rightClick.add(save);
		rightClick.setInheritsPopupMenu(true);
		this.setComponentPopupMenu(rightClick);

		//////////////
		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.transform(this.camera.getTransform());
		g2.scale(1, -1);
		this.img.paint(g2);
	}

	public void recenterCamera() {
		this.camera = new Camera(
				(this.getWidth()
						- (this.img.getWidth() * this.img.getFontSize() * this.camera.getScale()))
						/ 2,
				(this.getHeight()
						- (this.img.getHeight() * this.img.getFontSize() * this.camera.getScale()))
						/ 2,
				this.camera.getScale());
	}

	public void resetCamera() {
		this.camera = new Camera(0, 0, 1 / 12.0);
	}

}
