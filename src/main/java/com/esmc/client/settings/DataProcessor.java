package com.esmc.client.settings;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.esmc.client.utils.Utils;
import com.neurotec.biometrics.NBiometricStatus;
import com.neurotec.biometrics.NFAttributes;
import com.neurotec.biometrics.NFRecord;
import com.neurotec.biometrics.NFinger;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.NTemplate;
import com.neurotec.images.NImage;
import com.neurotec.io.NBuffer;
import com.neurotec.io.NFile;

import com.esmc.client.utils.Utilities;

public final class DataProcessor {

	// ==============================================
	// Private static fields
	// ==============================================

	private static DataProcessor instance;

	private static final String ROOT_ELEMENT = "EnrollmentResult";
	private static final String INFORMATION_ELEMENT = "Information";
	private static final String INFO_FIELD_ELEMENT = "Info";
	private static final String DATA_ELEMENT = "Data";
	private static final String TEMPLATE_ATTRIBUTE = "Template";
	private static final String DATA_FIELD_ELEMENT = "DataField";
	private static final String POSITION_ATTRIBUTE = "Position";
	private static final String IMPRESSION_ATTRIBUTE = "Impression";
	private static final String CREATE_STRING_ATTRIBUTE = "CreateString";
	private static final String FILE_ATTRIBUTE = "File";

	private static final String TEMPLATE_NAME = "Template.data";

	// ==============================================
	// Public static methods
	// ==============================================

	public static DataProcessor getInstance() {
		synchronized (DataProcessor.class) {
			if (instance == null) {
				instance = new DataProcessor();
			}
			return instance;
		}
	}

	// ==============================================
	// Private fields
	// ==============================================

	private final EnrollmentDataModel dataModel = EnrollmentDataModel.getInstance();
	private final EnrollmentSettings settings = EnrollmentSettings.getInstance();

	// ==============================================
	// Private method
	// ==============================================

	private void writeDataField(Document doc, Element dataElement, String dir, NFinger finger) throws IOException {
		Element dataFieldElement = doc.createElement(DATA_FIELD_ELEMENT);
		dataElement.appendChild(dataFieldElement);

		Attr positionAttr = doc.createAttribute(POSITION_ATTRIBUTE);
		positionAttr.setValue(finger.getPosition().toString());
		dataFieldElement.setAttributeNode(positionAttr);
		Attr impressionAttr = doc.createAttribute(IMPRESSION_ATTRIBUTE);
		impressionAttr.setValue(finger.getImpressionType().toString());
		dataFieldElement.setAttributeNode(impressionAttr);

		String name = Utilities.convertNFPositionNameToCamelCase(finger.getPosition());
		String type = finger.getImpressionType().isRolled() ? "Rolled" : "";
		name = name.concat(type);
		name = name.concat(".png");
		String imagePath = dir + Utils.FILE_SEPARATOR + name;
		finger.getImage().save(imagePath);

		Attr fileAttr = doc.createAttribute(FILE_ATTRIBUTE);
		fileAttr.setValue(name);
		dataFieldElement.setAttributeNode(fileAttr);

		for (NFAttributes item : finger.getObjects()) {
			if (item != null && item.getChild() instanceof NFinger) {
				writeDataField(doc, dataFieldElement, dir, (NFinger) item.getChild());
			}
		}

	}

	// ==============================================
	// Public methods
	// ==============================================

	public void save(String dir, String dirName) throws IOException, ParserConfigurationException, TransformerException {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement(ROOT_ELEMENT);
		doc.appendChild(rootElement);

		Element information = doc.createElement(INFORMATION_ELEMENT);
		rootElement.appendChild(information);

		for (InfoField inf : dataModel.getInfo()) {
			if (inf.getKey().equals("Template") || inf.getKey().equals("HashName")) {
				continue;
			}
                       Element infoField = doc.createElement(INFO_FIELD_ELEMENT);
                       information.appendChild(infoField);

			Attr infAttr = doc.createAttribute(CREATE_STRING_ATTRIBUTE);
			infAttr.setValue(inf.toString());
			infoField.setAttributeNode(infAttr);

			if (inf.getValue() != null) {
				if (inf.getValue() instanceof NImage) {
					String name = inf.getKey() + ".png";
					Attr fileAttr = doc.createAttribute(FILE_ATTRIBUTE);
					fileAttr.setValue(name);
					infoField.setAttributeNode(fileAttr);
					((NImage) inf.getValue()).save(dir + Utils.FILE_SEPARATOR + name);
				} else {
					infoField.appendChild(doc.createTextNode(inf.getValue().toString()));
				}
			}
		}

		if (dataModel.getSubject() != null) {
			Element data = doc.createElement(DATA_ELEMENT);
			rootElement.appendChild(data);
			NBuffer template = null;
			try {
				template = dataModel.getSubject().getTemplateBuffer();
				NFile.writeAllBytes((dir + Utils.FILE_SEPARATOR + TEMPLATE_NAME), template);
				Attr templateAttr = doc.createAttribute(TEMPLATE_ATTRIBUTE);
				templateAttr.setValue(TEMPLATE_NAME);
				data.setAttributeNode(templateAttr);

				for (NFinger finger : dataModel.getSubject().getFingers()) {
					if (finger.getStatus() == NBiometricStatus.OK && finger.getParentObject() == null) {
						writeDataField(doc, data, dir, finger);
					}
				}

			} finally {
				if (template != null) {
					template.dispose();
				}
			}
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(dir + Utils.FILE_SEPARATOR + dirName + ".xml"));

		transformer.transform(source, result);
	}

	public void saveTemplate(Frame owner) {
			
		    NTemplate template = dataModel.getSubject().getTemplate();
			String absoluteUSbPath = getAbsoluteUSBPath();
		   
			if(Objects.nonNull(absoluteUSbPath)){
		   
				String savePath = absoluteUSbPath+"esmctemp.data";
           
				try {
					NBuffer buffer = template.save();
					NFile.writeAllBytes(savePath, buffer);
				//Utilities.showWarning(owner, "Fichier bien sauvegardé");
                //code du bouton annuler
                                
				} catch (IOException e) {
				e.printStackTrace();
				}
				//dataModel.clearModel();
          
			}else{
				Utilities.showWarning(owner, "Veuillez vérifier le branchement de la carte");
            }
        }
	
	public Boolean countFingers(Frame owner) {
		if (dataModel.getSubject() == null) {
			Utilities.showWarning(owner, "Rien à sauvegarder");
			return false;
		}else{
			NTemplate template = dataModel.getSubject().getTemplate();
			if(template.getFingers()!=null){
			
			List<NFRecord> records = template.getFingers().getRecords();
			
			
			if(records.size()<10){
			Utilities.showWarning(owner, "Empreintes incomplètes. Veuillez scanner tous les dix doigts!!");	
			return false;
			}else{
		    return true;
			}
			}else{
				Utilities.showWarning(owner, "Pas d'empreintes prises!!");	
				return false;
            }
		}
	}
       
        public void saveSerializableTemplate(Frame owner){
            if (dataModel.getSubject() == null) {
			Utilities.showWarning(owner, "Rien à sauvegarder");
		} else {
                
            }
        }

	public void saveImages(Frame owner) {
		if (dataModel.getSubject() != null) {
			NSubject subject = dataModel.getSubject();
			List<NFinger> fingers = new ArrayList<NFinger>();
			for (NFinger finger : subject.getFingers()) {
				if (finger.getStatus() == NBiometricStatus.OK) {
					fingers.add(finger);
				}
			}

			if (fingers.size() > 0) {
				JFileChooser folderBrowserDialog = new JFileChooser();
				folderBrowserDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (settings.getLastDirectory() != null) {
					folderBrowserDialog.setCurrentDirectory(new File(settings.getLastDirectory()));
				}
				if (folderBrowserDialog.showSaveDialog(owner) != JFileChooser.APPROVE_OPTION) {
					return;
				}
				try {
					String dir = folderBrowserDialog.getSelectedFile().getPath();
					settings.setLastDirectory(dir);
					for (NFinger item : fingers) {
						boolean isRolled = item.getImpressionType().isRolled();
						String name = String.format("%s%s.png", Utilities.convertNFPositionNameToCamelCase(item.getPosition()), isRolled ? "Rolled" : "");
						item.getImage().save(dir + Utils.FILE_SEPARATOR + name);
					}
				} catch (Exception ex) {
					Utilities.showError(owner, ex);
				}
			} else {
				Utilities.showWarning(owner, "Rien à sauvegarder");
			}
		} else {
			Utilities.showWarning(owner, "Rien à sauvegarder");
		}
	}

	public void saveAll(Frame owner) {
		JFileChooser folderBrowserDialog = new JFileChooser();
		folderBrowserDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (settings.getLastDirectory() != null) {
			folderBrowserDialog.setCurrentDirectory(new File(settings.getLastDirectory()));
		}
		if (folderBrowserDialog.showSaveDialog(owner) == JFileChooser.APPROVE_OPTION) {
			File selectedFolder = folderBrowserDialog.getSelectedFile();
			settings.setLastDirectory(selectedFolder.getPath());
			try {
				save(selectedFolder.getPath(), selectedFolder.getName());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			}
		}
            
           
	}
        
        
 //retrouver le lecteur USB qui contiendra les fichiers
private static String getUSBPathName(){
            FileSystemView fsv = FileSystemView.getFileSystemView();
            File[] paths;
            paths = File.listRoots();
      
            for (File path : paths) {
                if(fsv.getSystemDisplayName(path).contains("USB")||fsv.getSystemDisplayName(path).contains("Disque amovible")){
                return path.getAbsolutePath();
                
            }

          }
            return null;
   }

private static String getAbsoluteUSBPath(){
	FileSystemView fsv = FileSystemView.getFileSystemView();
    File[] paths;
    paths = File.listRoots();
	int index = paths.length;
    
    if(index>0) {
	
	String[] ListPaths = new String [index];

	for (int i = 0; i < index; i++) {
		ListPaths[i]= fsv.getSystemDisplayName(paths[i]);
	}
	String n = (String)JOptionPane.showInputDialog(null, "Sélectionner le lecteur de la carte biométrique", 
            "choix du disque", JOptionPane.QUESTION_MESSAGE, null, ListPaths, ListPaths[0]);
    
	int indextrouve=0;
	
	if(Objects.nonNull(n)) {
	  
	  for (int i = 0; i < index; i++) {
		  if(n.equals(fsv.getSystemDisplayName(paths[i]))){
			  indextrouve = i;
		  }
	  } 
	     
	  	if(indextrouve>=0) {
		  return paths[indextrouve].getAbsolutePath();
		 
	  }
	}  
    
    
    }
    
	return null;
}



public String decodeBase64(byte[] b){
Base64 codec = new Base64();
String encoded = codec.encodeBase64String(b);
System.out.println("encoded= "+encoded);   // Outputs "SGVsbG8="

String decoded = new String(codec.decodeBase64(encoded));
return decoded;
}
/*
//Converting a bytes array to string of hex character
    public String byteArrayToHexString(byte[] b) {
        int len = b.length;
        String data = new String();
        for (int i = 0; i < len; i++) {
            data += Integer.toHexString((b[i] >> 4) & 0xf);
            data += Integer.toHexString(b[i] & 0xf);
        }
        return data.toUpperCase();
    }

public String decodeBase64(byte[] b){
Base64 codec = new Base64();
String encoded = codec.encodeBase64String(b);
System.out.println("encoded= "+encoded);   // Outputs "SGVsbG8="

String decoded = new String(codec.decodeBase64(encoded));
return decoded;
}

*/

/*

    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    //Converting a bytes array to string of hex character
    public String byteArrayToHexString(byte[] b) {
        int len = b.length;
        String data = new String();
        for (int i = 0; i < len; i++) {
            data += Integer.toHexString((b[i] >> 4) & 0xf);
            data += Integer.toHexString(b[i] & 0xf);
        }
        return data.toUpperCase();
    }*/
}
