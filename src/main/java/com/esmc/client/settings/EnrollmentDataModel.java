package com.esmc.client.settings;

import java.util.ArrayList;
import java.util.List;

import com.neurotec.biometrics.NFace;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.devices.NDeviceManager;
import com.neurotec.lang.NAbstractDisposable;

public final class EnrollmentDataModel extends NAbstractDisposable {

	// ==============================================
	// Private static fields
	// ==============================================

	private static EnrollmentDataModel instance;

	// ==============================================
	// Public static methods
	// ==============================================

	public static synchronized EnrollmentDataModel getInstance() {
		synchronized (EnrollmentDataModel.class) {
			if (instance == null) {
				instance = new EnrollmentDataModel();
			}
			return instance;
		}
	}

	// ==============================================
	// Private fields
	// ==============================================

	private final List<InfoField> info = new ArrayList<InfoField>();
	private NSubject subject;
	private NBiometricClient client;
	private NFace thumbFace;

	// ==============================================
	// Private constructor
	// ==============================================

	public NFace getThumbFace() {
		return thumbFace;
	}

	public void setThumbFace(NFace thumbFace) {
		this.thumbFace = thumbFace;
	}

	private EnrollmentDataModel() {
		loadInfoFields();
	}

	// ==============================================
	// Private methods
	// ==============================================

	private void loadInfoFields() {
		String[] split = EnrollmentSettings.getInstance().getInformation().split(";");
		for (String item : split) {
			InfoField inf = new InfoField(item);
			info.add(inf);
		}
	}

	// ==============================================
	// Protected methods
	// ==============================================

	@Override
	protected void dispose(boolean b) {
		dispose();
	}

	// ==============================================
	// Public methods
	// ==============================================

	public List<InfoField> getInfo() {
		return info;
	}

	public Object getInfo(String key) {
		for (InfoField infoField : info) {
			if (infoField.getKey().equals(key)) {
				return infoField.getValue();
			}
		}
		return null;
	}

	@Override
	public void dispose() {
		subject.dispose();
		subject = null;

		client.dispose();
		client = null;
	}

	public void clearModel() {
		info.clear();
		loadInfoFields();
		thumbFace = null;
		if (subject != null) {
			if (subject.getFingers() != null) {
				subject.getFingers().removeAll(subject.getFingers());
			}
			subject.clear();
		}
	}

	public NBiometricClient getBiometricClient() {
		return client;
	}

	public void setBiometricClient(NBiometricClient client) {
		this.client = client;
	}

	public NDeviceManager getDeviceManager() {
		if (client != null) {
			return client.getDeviceManager();
		}
		return null;
	}

	public NSubject getSubject() {
		return subject;
	}

	public void setSubject(NSubject subject) {
		this.subject = subject;
	}

}
