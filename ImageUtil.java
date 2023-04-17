package reconhecimento;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ImageUtil {
	public static ImageIcon redimensiona(ImageIcon iconOriginal, int largura, int altura) {
		// obter a image do imageicon
		Image imgOriginal = iconOriginal.getImage();
		// criar a imagem redimensionada a partir da img original
		Image redimensionada = imgOriginal.getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
		//retorna um imageicon
		return new ImageIcon(redimensionada);
	}
}
