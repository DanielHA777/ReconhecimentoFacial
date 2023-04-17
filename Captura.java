package reconhecimento;

import java.awt.Rectangle;  
import java.awt.event.KeyEvent; 

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.RectVector;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacpp.opencv_core.Scalar;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGR2GRAY;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import java.util.Scanner;
import static org.bytedeco.javacpp.opencv_imgproc.rectangle;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.resize;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;


public class Captura {
	public static void main(String[] args) throws Exception, InterruptedException {
		KeyEvent tecla = null;
		OpenCVFrameConverter.ToMat converteMat = new OpenCVFrameConverter.ToMat();
		OpenCVFrameGrabber camera = new OpenCVFrameGrabber(0);
		camera.start();

		CascadeClassifier detectorFaces = new CascadeClassifier("src\\recursos\\haarcascade_frontalface_alt.xml");
		
		CanvasFrame cFrame = new CanvasFrame("Preview", CanvasFrame.getDefaultGamma() / camera.getGamma());
		Frame frameCapturafdo = null;
		Mat imagemColorida = new Mat();
		int numeroAmostras = 25;
		int amostra =1;
		System.out.println("Digite seu id: ");
		Scanner cadastro = new Scanner(System.in);
		int idPessoal = cadastro.nextInt();
		
		while ((frameCapturafdo = camera.grab()) != null) {
			imagemColorida = converteMat.convert(frameCapturafdo);
			Mat imagemCinza = new Mat();
			cvtColor(imagemColorida, imagemCinza,  COLOR_BGR2GRAY);
			RectVector facesDetectadas = new RectVector();
			detectorFaces.detectMultiScale(imagemCinza, facesDetectadas, 1.1, 1,0,new Size(150,150), new Size(500,500));
		
			if(tecla == null) {
				tecla = cFrame.waitKey(5);
			}
			
			for(int i=0; i <facesDetectadas.size();i++) {
			Rect dadosFace = facesDetectadas.get(0);
			rectangle(imagemColorida, dadosFace, new Scalar(0,0,255,0));
		    Mat faceCapturada = new Mat(imagemCinza, dadosFace);
		    resize(faceCapturada, faceCapturada, new Size(160,160));
		   
		    if(tecla == null) {
				tecla = cFrame.waitKey(5);
			}
		    
		    if(tecla != null) {
		    	if(tecla.getKeyChar() == 'q') {
		    		if(amostra <= numeroAmostras) {
		    			imwrite("src\\fotos\\pessoas."+ idPessoal + "."+ amostra+ ".jpg", faceCapturada);
		    		System.out.println("foto "+ amostra + " capturada\n");
		    		amostra++;
		    		}
		    	}
		    	tecla = null;
		    }
		}
			
			if(tecla == null) {
				tecla = cFrame.waitKey(20);
			}
			
			if (cFrame.isVisible()) {
				cFrame.showImage(frameCapturafdo);
			}
			if(amostra > numeroAmostras) {
				break;
			}
		}
		cFrame.dispose();
		camera.stop();
	}
}
