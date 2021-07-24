package com.esmc.client.qrcode;

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Exchanger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class QrCapture extends JFrame implements Closeable {

    private static final long serialVersionUID = 1L;

    private Webcam webcam = null;
    private BufferedImage image = null;
    private Result result = null;
    private Exchanger<String> exchanger = new Exchanger<>();
    List<Webcam> ListWebcams;
    
   
    public QrCapture() {

        super();

        setLayout(new FlowLayout());
        setTitle("Capture");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ListWebcams = Webcam.getWebcams();
		int size = ListWebcams.size();
		if(size>0) {
			Object[] objectWebCam = ListWebcams.toArray();
			String[] TabWebcam = new String[objectWebCam.length];
			
			for (int i = 0; i < objectWebCam.length; i++) {
			TabWebcam[i] = (String) objectWebCam[i].toString() ;
			}
		
			if(TabWebcam.length>0) {
				String n = (String)JOptionPane.showInputDialog(null, "Sélectionner la caméra", 
                "choisir caméra", JOptionPane.QUESTION_MESSAGE, null, TabWebcam, TabWebcam[0]);
       	       
				int indextrouve=0;
				
				if(Objects.nonNull(n)) {
		    	  
		    	  for (int i = 0; i < TabWebcam.length; i++) {
		    		  if(n.equals(TabWebcam[i])){
		    			  indextrouve = i;
		    		  }
		  	    } 
		    	     
		    	  if(indextrouve>=0) {
		    		 webcam = Webcam.getWebcams().get(indextrouve);
		    	  }
				} 
			}
		 }
        webcam = Webcam.getWebcams().get(0);
        webcam.setViewSize(WebcamResolution.QVGA.getSize());
        webcam.open();

        add(new WebcamPanel(webcam));

        pack();
        setVisible(true);
       
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        final Thread daemon = new Thread(new Runnable() {

            @Override
            public void run() {
                while (isVisible()) {
                    read();
                }
            }
        });
        daemon.setDaemon(true);
        daemon.start();
    }

    private static BinaryBitmap toBinaryBitmap(BufferedImage image) {
        return new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
    }

    private void read() {

        if (!webcam.isOpen()) {
            return;
        }
        if ((image = webcam.getImage()) == null) {
            return;
        }

        try {
            result = new MultiFormatReader().decode(toBinaryBitmap(image));
        } catch (NotFoundException e) {
            return; // fall thru, it means there is no QR code in image
        }

        if (result != null) {
            try {
                exchanger.exchange(result.getText());
            } catch (InterruptedException e) {
                return;
            } finally {
            	webcam.close();
            	dispose();
            }
        }
    }

    public String getResult() throws InterruptedException {
        return exchanger.exchange(null);
    }

    @Override
    public void close() {
        webcam.close();
    }
}
