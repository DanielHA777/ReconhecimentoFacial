package reconhecimento;

import java.awt.FlowLayout; 
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.bytedeco.javacpp.opencv_core.Mat;



public class Utilitarios {
	public BufferedImage convertMatToImage(Mat mat) { 
        int type = BufferedImage.TYPE_BYTE_GRAY; // tipo da imagem
        if (mat.channels() > 1) { // se os canais forem maior q 1
            type = BufferedImage.TYPE_3BYTE_BGR; // passa pra rgb azul,verde, vermelho
        }
// buffersize armazena o tamanho da imagem 
        int bufferSize = mat.channels() * mat.cols() * mat.rows(); // multiplica o numero de canais pelas colunas e linhas
        byte[] bytes = new byte[bufferSize]; // cria array de bytes passando a imagem
        mat.getByteBuffer(bytes.length); // pega a imagem
        BufferedImage imagem = new BufferedImage(mat.cols(), mat.rows(), type); // passa o numero de colunas e linhas e o tipo
        byte[] targetPixels = ((DataBufferByte) imagem.getRaster().getDataBuffer()).getData(); //pga informações e dados da imagem
        System.arraycopy(bytes, 0, targetPixels, 0, bytes.length); // copia o array de bytes, targetpixels cria nova imagem
        return imagem; // retorna a imagem
    }
    
    public void mostraImagem(BufferedImage imagem) {
        ImageIcon icon = new ImageIcon(imagem);
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(imagem.getWidth() + 50, imagem.getHeight() + 50);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
