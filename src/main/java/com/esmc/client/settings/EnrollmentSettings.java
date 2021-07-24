package com.esmc.client.settings;

import java.io.File;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.esmc.client.utils.Utils;

public final class EnrollmentSettings implements Cloneable {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static EnrollmentSettings instance;
	private static EnrollmentSettings defaultInstance;
	private static final String PROJECT_NAME = "EnrollmentMcnp";
	private static final String PROJECT_DATA_FOLDER = Utils.getHomeDirectory() + Utils.FILE_SEPARATOR + ".neurotec" + Utils.FILE_SEPARATOR + PROJECT_NAME;
	private static final String SETTINGS_FILE_PATH = getSettingsFolder() + Utils.FILE_SEPARATOR + "user.xml";

	// ===========================================================
	// Private static methods
	// ===========================================================

	private static String getSettingsFolder() {
		File settingsFolder = new File(PROJECT_DATA_FOLDER);
		if (!settingsFolder.exists()) {
			settingsFolder.mkdirs();
		}
		return settingsFolder.getAbsolutePath();
	}

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static synchronized EnrollmentSettings getDefaultInstance() {
		synchronized (EnrollmentSettings.class) {
			if (defaultInstance == null) {
				defaultInstance = new EnrollmentSettings();
				defaultInstance.loadDefault();
			}
			return defaultInstance;
		}
	}

	public static synchronized EnrollmentSettings getInstance() {
		synchronized (EnrollmentSettings.class) {
			if (instance == null) {
				instance = new EnrollmentSettings();
				instance.load();
			}
			return instance;
		}
	}

	// ===========================================================
	// Private fields
	// ===========================================================

	// ===========================================================
	// Device settings
	// ===========================================================

	@Element(required = false)
	private String selectedFScannerId;
	@Element
	private boolean scanSlaps;
	@Element
	private boolean scanRolled;
	@Element
	private boolean scanPlain;

	// ===========================================================
	// Extraction settings
	// ===========================================================

	@Element
	private String clientProperties;

	// ===========================================================
	// Enrollment settings
	// ===========================================================

	@Element
	private String thumbnailField;

	// ===========================================================
	// Connection settings
	// ===========================================================

	// ===========================================================
	// Other settings
	// ===========================================================

	@Element
	private String information;
	@Element
	private boolean showOriginal;
	@Element
	private String lastDirectory;

	// ===========================================================
	// Private constructor
	// ===========================================================

	private EnrollmentSettings() {

	}

	// ===========================================================
	// Private methods
	// ===========================================================

	private void load() {
		try {
			File file = new File(SETTINGS_FILE_PATH);
			if (file.exists()) {
				Serializer serializer = new Persister();
				instance = serializer.read(EnrollmentSettings.class, file);
			} else {
				instance = (EnrollmentSettings) getDefaultInstance().clone();
			}
		} catch (Exception e) {
			try {
				instance = (EnrollmentSettings) getDefaultInstance().clone();
			} catch (CloneNotSupportedException e1) {
				throw new AssertionError("Cannot happen");
			}
		}
	}

	private void loadDefault() {
		setLastDirectory(System.getProperty("user.home"));
		setSelectedFScannerId("");
		setScanSlaps(true);
		setScanRolled(true);
		setScanPlain(true);
		setClientProperties("");
		setShowOriginal(true);
		setThumbnailField("Thumbnail");
		setInformation("Key = \'Name\' ;Key = \'Thumbnail\', IsThumbnail = \'True\', Editable = \'False\' ;Key = \'Middle Name\';Key = \'Last Name\' ;Key = \'National Id\'; Key = \'Nationality\'");
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	public void save() {
		Serializer serializer = new Persister();
		File file = new File(SETTINGS_FILE_PATH);
		try {
			serializer.write(this, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getLastDirectory() {
		return lastDirectory;
	}

	public void setLastDirectory(String lastDirectory) {
		this.lastDirectory = lastDirectory;
	}

	public String getSelectedFScannerId() {
		return selectedFScannerId;
	}

	public void setSelectedFScannerId(String selectedFScannerId) {
		this.selectedFScannerId = selectedFScannerId;
	}

	public boolean isScanSlaps() {
		return scanSlaps;
	}

	public void setScanSlaps(boolean scanSlaps) {
		this.scanSlaps = scanSlaps;
	}

	public boolean isScanRolled() {
		return scanRolled;
	}

	public void setScanRolled(boolean scanRolled) {
		this.scanRolled = scanRolled;
	}

	public boolean isScanPlain() {
		return scanPlain;
	}

	public void setScanPlain(boolean scanPlain) {
		this.scanPlain = scanPlain;
	}

	public boolean isShowOriginal() {
		return showOriginal;
	}

	public void setShowOriginal(boolean showOriginal) {
		this.showOriginal = showOriginal;
	}

	public String getClientProperties() {
		return clientProperties;
	}

	public void setClientProperties(String clientProperties) {
		this.clientProperties = clientProperties;
	}

	public String getThumbnailField() {
		return thumbnailField;
	}

	public void setThumbnailField(String thumbnailField) {
		this.thumbnailField = thumbnailField;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

}
