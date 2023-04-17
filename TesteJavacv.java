package reconhecimento;

import org.bytedeco.javacpp.opencv_face.FaceRecognizer;  
import static org.bytedeco.javacpp.opencv_face.createEigenFaceRecognizer;

public class TesteJavacv {
	public static void main(String[] args) {
		FaceRecognizer r = createEigenFaceRecognizer();
	}
}
