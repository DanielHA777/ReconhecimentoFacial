package reconhecimento;

import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_face.createEigenFaceRecognizer;
import static org.bytedeco.javacpp.opencv_face.createFisherFaceRecognizer;
import static org.bytedeco.javacpp.opencv_face.createLBPHFaceRecognizer;  
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.rectangle;
import static org.bytedeco.javacpp.opencv_imgproc.resize;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.RectVector;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import reconhecimento.ImageUtil;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import static org.bytedeco.javacpp.opencv_imgproc.resize;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Image;

public class TesteYaleFrame extends JFrame {

	private JPanel contentPane;
	private JLabel lbl1;
	private JButton btnRec;
	private byte[] img;
	private JButton btnImport;
	private JButton btnImportR;
	private JButton btnCaptura;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TesteYaleFrame frame = new TesteYaleFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TesteYaleFrame() {
		setFont(new Font("Arial Black", Font.PLAIN, 14));
		setTitle("Sistema de fotos");
		setResizable(false);
		setBounds(100, 100, 802, 513);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lbl1 = new JLabel("");
		
		
		lbl1.setBounds(5, 5, 781, 380);
		contentPane.add(lbl1);
		
		btnRec = new JButton("Ativar reconhecimento facial");
		btnRec.setFont(new Font("Arial Black", Font.PLAIN, 14));
		btnRec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int totalAcertos = 0;
				double percentualAcerto = 0;
				
				double totalConfianca = 0;

				// FaceRecognizer reconhecedor = createEigenFaceRecognizer();
				// FaceRecognizer reconhecedor = createFisherFaceRecognizer();
				FaceRecognizer reconhecedor = createLBPHFaceRecognizer();

				// reconhecedor.load("src\\recursos\\classificadorEigenfacesYale.yml");
				// reconhecedor.load("src\\recursos\\classificadorFisherfacesYale.yml");
				reconhecedor.load("src\\recursos\\classificadorLBPHYale.yml");

				File diretorio = new File("src/yalefaces/teste");
				File[] arquivos = diretorio.listFiles();

				for (File imagem : arquivos) {
					Mat foto = imread(imagem.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
					int classe = Integer.parseInt(imagem.getName().length()+"");
					if(classe <=  4) {
						String rt = imagem.getName();
					//	System.out.println(rt);
					}else {
						classe = Integer.parseInt(imagem.getName().substring(7, 9));
					}
					//resize(foto, foto, new opencv_core.Size(160, 160));

					IntPointer rotulo = new IntPointer(1);
					DoublePointer confianca = new DoublePointer(1);
					reconhecedor.predict(foto, rotulo, confianca);
					int residente = rotulo.get(0);
					System.out.println(classe + " foi reconhecido como " + residente + " - " + confianca.get(0));
				
				//	if (classe == residente) {
				//		totalAcertos++;
				//		totalConfianca += confianca.get(0);
				//	}
					//Adegina criar igual para todos residentes
					/*if(classe == classe && residente == 1) { 
						totalAcertos++;
						totalConfianca += confianca.get(0);
						//coloca a imagem reconhecida numa label e salva numa pasta
						ImageIcon imgFoto = new ImageIcon(TesteYaleFrame.class.getResource("/yalefaces/teste/"+imagem.getName()));
						lbl1.setIcon(ImageUtil.redimensiona(imgFoto, lbl1.getWidth(), lbl1.getHeight()));
						 BufferedImage image = null;
						 JOptionPane.showMessageDialog(null, classe + " foi reconhecido como Adegina" + residente + " - " + confianca.get(0), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
						try {
					           
				            URL url = new URL(TesteYaleFrame.class.getResource("/yalefaces/teste/"+ imagem.getName()), "");
				            image = ImageIO.read(url);
				             
				            ImageIO.write(image, "jpg",new File("C:/Users/recepcao/Dropbox/VILA VIDA - ADMINISTRATIVO/Bia/Fotos/Setembro/Adegina/"+imagem.getName()));
				           // ImageIO.write(image, "gif",new File("C:\\out.gif"));
				          //  ImageIO.write(image, "png",new File("C:\\out.png"));
				             
				        } catch (IOException ee) {
				            ee.printStackTrace();
				        }
									  
					        
					    }*//*else if(classe == classe && residente == 2) {
					    	totalAcertos++;
							totalConfianca += confianca.get(0);
							//coloca a imagem reconhecida numa label e salva numa pasta
							ImageIcon imgFoto = new ImageIcon(TesteYaleFrame.class.getResource("/yalefaces/teste/"+imagem.getName()));
							lbl1.setIcon(ImageUtil.redimensiona(imgFoto, lbl1.getWidth(), lbl1.getHeight()));
							 BufferedImage image = null;
							 JOptionPane.showMessageDialog(null, classe + " foi reconhecido como Antonia" + residente + " - " + confianca.get(0), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
							try {
						           
					            URL url = new URL(TesteYaleFrame.class.getResource("/yalefaces/teste/"+ imagem.getName()), "");
					            image = ImageIO.read(url);
					             
					            ImageIO.write(image, "jpg",new File("C:/Users/recepcao/Dropbox/VILA VIDA - ADMINISTRATIVO/Bia/Fotos/Setembro/Antonia/"+imagem.getName()));
					           // ImageIO.write(image, "gif",new File("C:\\out.gif"));
					          //  ImageIO.write(image, "png",new File("C:\\out.png"));
					             
					        } catch (IOException ee) {
					            ee.printStackTrace();
					        }
					    }*/if(imagem.getName().contains("01")) {				    	
					    	ImageIcon imgFoto = new ImageIcon(TesteYaleFrame.class.getResource("/yalefaces/teste/"+imagem.getName()));
							lbl1.setIcon(ImageUtil.redimensiona(imgFoto, lbl1.getWidth(), lbl1.getHeight()));
							 BufferedImage image = null;
							// JOptionPane.showMessageDialog(null, classe + " foi reconhecido como Adegina" + residente + " - " + confianca.get(0), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
							try {
						           
					            URL url = new URL(TesteYaleFrame.class.getResource("/yalefaces/teste/"+ imagem.getName()), "");
					            image = ImageIO.read(url);
					             
					            ImageIO.write(image, "jpg",new File("C:/Users/recepcao/Dropbox/VILA VIDA - ADMINISTRATIVO/Bia/Fotos/Setembro/Adegina/"+imagem.getName()));
					           // ImageIO.write(image, "gif",new File("C:\\out.gif"));
					          //  ImageIO.write(image, "png",new File("C:\\out.png"));
					             
					        } catch (IOException ee) {
					            ee.printStackTrace();
					        }
					    }if(imagem.getName().contains("02")) {
					    	ImageIcon imgFoto = new ImageIcon(TesteYaleFrame.class.getResource("/yalefaces/teste/"+imagem.getName()));
							lbl1.setIcon(ImageUtil.redimensiona(imgFoto, lbl1.getWidth(), lbl1.getHeight()));
							 BufferedImage image = null;
						// JOptionPane.showMessageDialog(null, classe + " foi reconhecido como Jandyra" + residente + " - " + confianca.get(0), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
							try {
						           
					            URL url = new URL(TesteYaleFrame.class.getResource("/yalefaces/teste/"+ imagem.getName()), "");
					            image = ImageIO.read(url);
					             
					            ImageIO.write(image, "jpg",new File("C:/Users/recepcao/Dropbox/VILA VIDA - ADMINISTRATIVO/Bia/Fotos/Setembro/Jandyra/"+imagem.getName()));
					           // ImageIO.write(image, "gif",new File("C:\\out.gif"));
					          //  ImageIO.write(image, "png",new File("C:\\out.png"));
					             
					        } catch (IOException ee) {
					            ee.printStackTrace();
					        }
					    }if(imagem.getName().contains("03")) {
					    	ImageIcon imgFoto = new ImageIcon(TesteYaleFrame.class.getResource("/yalefaces/teste/"+imagem.getName()));
							lbl1.setIcon(ImageUtil.redimensiona(imgFoto, lbl1.getWidth(), lbl1.getHeight()));
							 BufferedImage image = null;
							// JOptionPane.showMessageDialog(null, classe + " foi reconhecido como Maria Celia" + residente + " - " + confianca.get(0), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
							try {
						           
					            URL url = new URL(TesteYaleFrame.class.getResource("/yalefaces/teste/"+ imagem.getName()), "");
					            image = ImageIO.read(url);
					             
					            ImageIO.write(image, "jpg",new File("C:/Users/recepcao/Dropbox/VILA VIDA - ADMINISTRATIVO/Bia/Fotos/Setembro/MariaCelia/"+imagem.getName()));
					           // ImageIO.write(image, "gif",new File("C:\\out.gif"));
					          //  ImageIO.write(image, "png",new File("C:\\out.png"));
					             
					        } catch (IOException ee) {
					            ee.printStackTrace();
					        }
					    }

					percentualAcerto = (totalAcertos / 30.0) * 100;
					totalConfianca = totalConfianca / totalAcertos;
				System.out.println("Percentual de acerto: " + percentualAcerto);
				//	System.out.println("Total confiança: " + totalConfianca);
				//	JOptionPane.showMessageDialog(null,"Percentual de acerto: " + percentualAcerto + " Total confianÃ§a:" + totalConfianca , "Dados", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnRec.setBounds(403, 396, 267, 23);
		contentPane.add(btnRec);
		
		btnImport = new JButton("Importar fotos para reconhecer");
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(System.getProperty("user.home")));
				javax.swing.filechooser.FileFilter filtroImg = new FileNameExtensionFilter("src/yalefaces/teste",
						ImageIO.getWriterFileSuffixes());
				fc.setFileFilter(filtroImg);
				//String rt = fc.MULTI_SELECTION_ENABLED_CHANGED_PROPERTY;
				int retorno = fc.showOpenDialog(null);
				if (retorno ==JFileChooser.APPROVE_OPTION) {
					
					File arqSelec = fc.getSelectedFile();
					try {
						BufferedImage foto = ImageIO.read(arqSelec); 
						ImageIcon imgFoto = new ImageIcon(foto);
						lbl1.setIcon(ImageUtil.redimensiona(imgFoto, lbl1.getWidth(), lbl1.getHeight()));
						ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 	
						if(arqSelec != null) {
							ImageIO.write(foto, "jpg", outStream); 
							//ImageIO.write(foto, "jpg", new File("src/yalefaces.teste")); 
							img = outStream.toByteArray();
							byte[] signature = img;

		                    // passando caminho para salvar
		                    try {
		                        //destina-se a gravar fluxos de bytes brutos, como dados de imagem.
		                        FileOutputStream fos = new FileOutputStream("src/yalefaces/teste/"+ arqSelec.getName());
		                        //Grava b.lengthbytes da matriz de bytes especificada para este fluxo de saída de arquivo.
		                        fos.write(signature);
		                        FileDescriptor fd = fos.getFD(); //Retorna o descritor de arquivo associado a este fluxo.
		                        fos.flush();
		                        fd.sync();
		                        fos.close();
						}finally {
							
						}}
					} catch (Exception e2) {
						e2.printStackTrace();
						JOptionPane.showMessageDialog(null, e2.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnImport.setFont(new Font("Arial Black", Font.PLAIN, 13));
		btnImport.setBounds(126, 426, 267, 23);
		contentPane.add(btnImport);
		
		btnImportR = new JButton("Importar fotos para treinar IA");
		btnImportR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(System.getProperty("user.home")));
				javax.swing.filechooser.FileFilter filtroImg = new FileNameExtensionFilter("src/yalefaces/treinamento",
						ImageIO.getWriterFileSuffixes());
				fc.setFileFilter(filtroImg);
				//String rt = fc.MULTI_SELECTION_ENABLED_CHANGED_PROPERTY;
				int retorno = fc.showOpenDialog(null);
				if (retorno ==JFileChooser.APPROVE_OPTION) {
					
					File arqSelec = fc.getSelectedFile();
					try {
						BufferedImage foto = ImageIO.read(arqSelec); 
						ImageIcon imgFoto = new ImageIcon(foto);
						lbl1.setIcon(ImageUtil.redimensiona(imgFoto, lbl1.getWidth(), lbl1.getHeight()));
						ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 	
						if(arqSelec != null) {
							ImageIO.write(foto, "jpg", outStream); 
							//ImageIO.write(foto, "jpg", new File("src/yalefaces.teste")); 
							img = outStream.toByteArray();
							byte[] signature = img;

		                    // passando caminho para salvar
		                    try {
		                        //destina-se a gravar fluxos de bytes brutos, como dados de imagem.
		                        FileOutputStream fos = new FileOutputStream("src/yalefaces/treinamento/"+ arqSelec.getName());
		                        //Grava b.lengthbytes da matriz de bytes especificada para este fluxo de saída de arquivo.
		                        fos.write(signature);
		                        FileDescriptor fd = fos.getFD(); //Retorna o descritor de arquivo associado a este fluxo.
		                        fos.flush();
		                        fd.sync();
		                        fos.close();
							
		                        java.awt.Toolkit.getDefaultToolkit().beep();
		    					int botao = JOptionPane.showConfirmDialog(null,
		    							"Deseja treinar a ia para reconhecer as novas fotos?", "CONFIRMAR",
		    							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		    					if (botao == 0) {
		    						 File diretorioo = new File("src\\yalefaces\\treinamento");
		    					        File[] arquivoss = diretorioo.listFiles();
		    					        MatVector fotos = new MatVector(arquivoss.length);
		    					        Mat rotulos = new Mat(arquivoss.length, 1, CV_32SC1);
		    					        IntBuffer rotulosBuffer = rotulos.createBuffer();
		    					        int contador = 0;
		    					      
		    					        for (File imagem : arquivoss) {
		    					            Mat fotoo = imread(imagem.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
		    					            int classee = Integer.parseInt(imagem.getName().substring(7,9));
		    					          //  resize(foto, foto, new Size(160, 160));
		    					            fotos.put(contador, fotoo);
		    					            rotulosBuffer.put(contador, classee);
		    					            contador++;
		    					        }
		    					        
		    					        FaceRecognizer eigenface = createEigenFaceRecognizer(30, 0);
		    					        FaceRecognizer fisherface = createFisherFaceRecognizer(30, 0);        
		    					        FaceRecognizer lbph = createLBPHFaceRecognizer(12, 10, 15, 15, 0);

		    					        eigenface.train(fotos, rotulos);
		    					        eigenface.save("src\\recursos\\classificadorEigenfacesYale.yml");
		    					        
		    					        fisherface.train(fotos, rotulos);
		    					        fisherface.save("src\\recursos\\classificadorFisherfacesYale.yml");
		    					        
		    					        lbph.train(fotos, rotulos);
		    					        lbph.save("src\\recursos\\classificadorLBPHYale.yml");
		    					    }
		    					}
		                       
						finally {
							
						}}
					} catch (Exception e2) {
						e2.printStackTrace();
						JOptionPane.showMessageDialog(null, e2.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
					}
				}
				
			}
		});
		btnImportR.setFont(new Font("Arial Black", Font.PLAIN, 14));
		btnImportR.setBounds(126, 396, 267, 23);
		contentPane.add(btnImportR);
		
		btnCaptura = new JButton("Capturar via webcam");
		btnCaptura.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KeyEvent tecla = null;
				OpenCVFrameConverter.ToMat converteMat = new OpenCVFrameConverter.ToMat();
				OpenCVFrameGrabber camera = new OpenCVFrameGrabber(0);
				try {
					camera.start();
				} catch (org.bytedeco.javacv.FrameGrabber.Exception e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}

				CascadeClassifier detectorFaces = new CascadeClassifier("src\\recursos\\haarcascade_frontalface_alt.xml");
				
				CanvasFrame cFrame = new CanvasFrame("Preview", CanvasFrame.getDefaultGamma() / camera.getGamma());
				Frame frameCapturafdo = null;
				Mat imagemColorida = new Mat();
				int numeroAmostras = 25;
				int amostra =1;
				System.out.println("Digite seu id: ");
				Scanner cadastro = new Scanner(System.in);
				int idPessoal = cadastro.nextInt();
				
				try {
					while ((frameCapturafdo = camera.grab()) != null) {
						imagemColorida = converteMat.convert(frameCapturafdo);
						Mat imagemCinza = new Mat();
						cvtColor(imagemColorida, imagemCinza,  COLOR_BGR2GRAY);
						RectVector facesDetectadas = new RectVector();
						detectorFaces.detectMultiScale(imagemCinza, facesDetectadas, 1.1, 1,0,new Size(150,150), new Size(500,500));
					
						if(tecla == null) {
							try {
								tecla = cFrame.waitKey(5);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
						for(int i=0; i <facesDetectadas.size();i++) {
						Rect dadosFace = facesDetectadas.get(0);
						rectangle(imagemColorida, dadosFace, new Scalar(0,0,255,0));
					    Mat faceCapturada = new Mat(imagemCinza, dadosFace);
					   // resize(faceCapturada, faceCapturada, new Size(160,160));
					   
					    if(tecla == null) {
							try {
								tecla = cFrame.waitKey(5);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
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
							try {
								tecla = cFrame.waitKey(20);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
						if (cFrame.isVisible()) {
							cFrame.showImage(frameCapturafdo);
						}
						if(amostra > numeroAmostras) {
							break;
						}
					}
				} catch (org.bytedeco.javacv.FrameGrabber.Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				cFrame.dispose();
				try {
					camera.stop();
				} catch (org.bytedeco.javacv.FrameGrabber.Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		btnCaptura.setFont(new Font("Arial Black", Font.PLAIN, 14));
		btnCaptura.setBounds(403, 426, 267, 23);
		contentPane.add(btnCaptura);
	}
}

