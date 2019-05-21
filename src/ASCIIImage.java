import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

public class ASCIIImage implements ImageObserver {

	BufferedImage img;
	ArrayList<String> conversion;
	private int width = 1500;
	private int height = 900;

	public ASCIIImage(BufferedImage img) {
		this.img = img;
		this.conversion = new ArrayList<>();
		this.convert();
	}
	
	public ASCIIImage(BufferedImage img, int width, int height) {
		this.img = img;
		this.width = width;
		this.height = height;
		this.conversion = new ArrayList<>();
		this.convert();
	}

	public int getWidth() {
		return this.conversion.get(0).length();
	}

	public int getHeight() {
		return this.conversion.size();
	}

	private void convert() {
		this.resizeIfNecessary();
		System.out.println(this.img.getWidth());
		System.out.println(this.img.getHeight());
		String resultStr = "";
		for (int pixelRow = 0; pixelRow < this.img.getHeight(this); pixelRow += 1) {
			for (int pixelCol = 0; pixelCol < this.img.getWidth(this); pixelCol += 1) {
				Color pixelColor = this.getColorAt(pixelCol, pixelRow);
				double brightness = getBrightness(pixelColor);
				resultStr += getEnergyChar(brightness);
			}
			this.conversion.add(resultStr);
			resultStr = "";
		}
	}

	private void resizeIfNecessary() {
		BufferedImage newImg = new BufferedImage(this.img.getWidth(), this.img.getHeight(),
				this.img.getType());
		if (newImg.getWidth() > this.width || newImg.getHeight() > this.height) {
			if (newImg.getWidth() > this.width) {
				newImg = new BufferedImage(this.width,
						(int) (newImg.getHeight() / (newImg.getWidth() / (double)this.width)),
						this.img.getType());
			}
			if (newImg.getHeight() > this.height) {
				newImg = new BufferedImage((int) (newImg.getWidth() / (newImg.getHeight() / this.height)),
						this.height, this.img.getType());
			}
			Graphics2D g2 = (Graphics2D) newImg.getGraphics();
			g2.drawImage(this.img, 0, 0, newImg.getWidth(), newImg.getHeight(), null);
			this.img = newImg;
		}

	}

	private String getEnergyChar(double brightness) {
		// map : "NMGHO$C7?>;!:-. "
		double energy = 255 - brightness;
		if (energy > 240) {
			return "N";
		} else if (energy > 224) {
			return "M";
		} else if (energy > 208) {
			return "Q";
		} else if (energy > 192) {
			return "H";
		} else if (energy > 176) {
			return "O";
		} else if (energy > 160) {
			return "$";
		} else if (energy > 144) {
			return "C";
		} else if (energy > 128) {
			return "7";
		} else if (energy > 112) {
			return "?";
		} else if (energy > 96) {
			return ">";
		} else if (energy > 80) {
			return ";";
		} else if (energy > 64) {
			return "!";
		} else if (energy > 48) {
			return ":";
		} else if (energy > 32) {
			return "-";
		} else if (energy > 16) {
			return ".";
		} else {
			return " ";
		}
	}

	private static double getBrightness(Color pixelColor) {
		return ((pixelColor.getRed() + pixelColor.getBlue() + pixelColor.getGreen()) / 3.0);
	}

	private Color getColorAt(int x, int y) {
		return new Color(this.img.getRGB(x, y));
	}

	public List<String> getASCII() {
		return this.conversion;
	}

	public void paint(Graphics2D g2) {
		int yLoc = 0;
		g2.setBackground(Color.WHITE);
		g2.clearRect(0, 0, this.getWidth() * this.getFontSize(),
				this.getHeight() * this.getFontSize());
		g2.setColor(Color.BLACK);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_NORMALIZE);
		g2.scale(1.66, 1); // approximate height to width ratio
		g2.setFont(new Font("monospaced", Font.PLAIN, this.getFontSize()));
		for (String str : this.getASCII()) {
			g2.drawString(str, 0, yLoc);
			yLoc += this.getFontSize();
		}
	}

	public int getFontSize() {
		return 12;
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		return false;
	}

}
