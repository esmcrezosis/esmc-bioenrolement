/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package com.esmc.client.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A subject represents a Person for whom one or more biometric samples are being collected,
 * and for whom we want to associate all of these biometrics together with a single subjectId identifier
 */
public class BiometricSubject implements Serializable {
	private static final long serialVersionUID =1L;

    private String codeMembre;
    private List<String> stringTemplates;

    public BiometricSubject() { }

    
    public String getCodeMembre() {
		return codeMembre;
	}


	public void setCodeMembre(String codeMembre) {
		this.codeMembre = codeMembre;
	}


	public List<String> getStringTemplates() {
		if (stringTemplates == null) {
        	stringTemplates = new ArrayList<>();
        }
		return stringTemplates;
	}


	public void setStringTemplates(List<String> stringTemplates) {
		this.stringTemplates = stringTemplates;
	}


    public void addStringTemplate(String stringTemplate) {
        getStringTemplates().add(stringTemplate);
    }
}
