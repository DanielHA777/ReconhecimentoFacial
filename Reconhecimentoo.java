package reconhecimento;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGRA2GRAY;  
import static org.bytedeco.javacpp.opencv_imgproc.CV_FONT_HERSHEY_PLAIN;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.putText;
import static org.bytedeco.javacpp.opencv_imgproc.rectangle;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.resize;


import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import reconhecimento.Utilitarios;

import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.RectVector;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;
import static org.bytedeco.javacpp.opencv_face.createEigenFaceRecognizer;


public class Reconhecimentoo {
    public static void main(String args[]) throws FrameGrabber.Exception {
        OpenCVFrameConverter.ToMat converteMat = new OpenCVFrameConverter.ToMat();
        OpenCVFrameGrabber camera = new OpenCVFrameGrabber(0);
        String[] pessoas = {"", "Daniel"};
        camera.start();
        
        CascadeClassifier detectorFace = new CascadeClassifier("src\\recursos\\haarcascade_frontalface_alt.xml");
        
        FaceRecognizer reconhecedor = createEigenFaceRecognizer();             // *antes: createEigenFaceRecognizer();
        reconhecedor.load("src\\recursos\\classificadorEigenFaces.yml");        // *antes: load()
        //reconhecedor.setThreshold(0);
        
        //FaceRecognizer reconhecedor = FisherFaceRecognizer.create();
        //reconhecedor.read("src\\recursos\\classificadorFisherFaces.yml");
        
        //FaceRecognizer reconhecedor = LBPHFaceRecognizer.create();
        //reconhecedor.read("src\\recursos\\classificadorLBPH.yml");
        
        
       JFrame cFrame = new JFrame("Reconhecimento" /*CanvasFrame.getDefaultGamma() / camera.getGamma()*/);
      //  Frame frameCapturado = null;
        Mat imagemColorida = imread("src\\pessoas\\tes.jpg");
        
       
        
        while ((imagemColorida ) != null) {
            Mat imagemCinza = new Mat();
            //cvtColor(imagemColorida, imagemCinza, COLOR_BGRA2GRAY);
            RectVector facesDetectadas = new RectVector();
            detectorFace.detectMultiScale(imagemCinza, facesDetectadas, 1.5, 1, 1, new Size(15,15), new Size(100,100));
            for (int i = 0; i < facesDetectadas.size(); i++) {
                Rect dadosFace = facesDetectadas.get(i);
                rectangle(imagemColorida, dadosFace, new Scalar(0,255,0,0));
                Mat faceCapturada = new Mat(imagemCinza, dadosFace);
                
                IntPointer rotulo = new IntPointer(1);
                DoublePointer confianca = new DoublePointer(1);
                
                System.out.println("w="+faceCapturada.size(0)+"  /  h="+faceCapturada.size(1));
                if ((faceCapturada.size(0) == 160) || (faceCapturada.size(1) == 160)){
                    continue;
                }  
                resize(faceCapturada, faceCapturada, new Size(160,160));
                //Size tamanho = new Size(faceCapturada); 
                reconhecedor.predict(faceCapturada, rotulo, confianca);
                int predicao = rotulo.get(0);
                String nome;
                if (predicao == -1) {
                    nome = "Desconhecido";
                } else {
                    nome = pessoas[predicao] + " - " + confianca.get(0);
                }
                
                int x = Math.max(dadosFace.tl().x() - 10, 0);
                int y = Math.max(dadosFace.tl().y() - 10, 0);
                putText(imagemColorida, nome, new Point(x, y), CV_FONT_HERSHEY_PLAIN, 1.4, new Scalar(0,255,0,0));
            }
            if (cFrame.isVisible()) {
            	//BufferedImage imagem = new BufferedImage();
            	 Utilitarios ut = new Utilitarios();
         		ut.mostraImagem(ut.convertMatToImage(imagemColorida));
         		ImageIcon icon = new ImageIcon(imagemColorida.toString());
                cFrame.setLayout(new FlowLayout());
               // cFrame.add(lbl);
                cFrame.setVisible(true);
                cFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
             //   cFrame.showImage(imagemColorida.get);
            }
        }
        cFrame.dispose();
      //  camera.stop();
    }
   
}
