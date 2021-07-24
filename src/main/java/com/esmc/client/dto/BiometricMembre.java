package com.esmc.client.dto;

import java.io.Serializable;

public class BiometricMembre implements Serializable{
	private static final long serialVersionUID =1L;

	private String codeMembre;
    private String nomMembre;
    private String prenomMembre;
    
	public BiometricMembre() {
		
	}

	public String getCodeMembre() {
		return codeMembre;
	}

	public void setCodeMembre(String codeMembre) {
		this.codeMembre = codeMembre;
	}

	public String getNomMembre() {
		return nomMembre;
	}

	public void setNomMembre(String nomMembre) {
		this.nomMembre = nomMembre;
	}

	public String getPrenomMembre() {
		return prenomMembre;
	}

	public void setPrenomMembre(String prenomMembre) {
		this.prenomMembre = prenomMembre;
	}

	
	
	
}
