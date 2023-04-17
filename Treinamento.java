package reconhecimento;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Size;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import static org.bytedeco.javacpp.opencv_imgproc.resize;
import static org.bytedeco.javacpp.opencv_face.createEigenFaceRecognizer;

public class Treinamento {
	public static void main(String[] args) {

		File diretorio = new File("src\\fotos");
		FilenameFilter filtroImagem = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String nome) {
				return nome.endsWith(".jpg") || nome.endsWith(".gif") || nome.endsWith(".png");
			}
		};

		File[] arquivos = diretorio.listFiles(filtroImagem);
		MatVector fotos = new MatVector(arquivos.length);
		Mat rotulos = new Mat(arquivos.length, 1, CV_32SC1);
		IntBuffer rotulosBuffer = rotulos.createBuffer();
		int contador = 0;
		for (File imagem : arquivos) {
			Mat foto = imread(imagem.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
			//int classe = Integer.parseInt(imagem.getName().split("\\.")[1]);
			resize(foto, foto, new Size(160, 160));
			fotos.put(contador, foto);
			rotulosBuffer.put(contador /*classe*/);
			contador++;
		}
		FaceRecognizer eigenFaces = createEigenFaceRecognizer(10,0);
		FaceRecognizer fisherFaces = createEigenFaceRecognizer();
		FaceRecognizer lbph = createEigenFaceRecognizer();

		eigenFaces.train(fotos, rotulos);
		eigenFaces.save("src\\recursos\\classificadorEigenFaces.yml");
		fisherFaces.train(fotos, rotulos);
		fisherFaces.save("src\\recursos\\classificadorFisherFaces.yml");
		lbph.train(fotos, rotulos);
		lbph.save("src\\recursos\\classificadorLBPH.yml");
	}
}
