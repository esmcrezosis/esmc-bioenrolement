/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esmc.client.dto;

import java.io.Serializable;

/**
 *
 * @author Mawuli
 */
public class Membre implements Serializable{
    private static final long serialVersionUID = 1L;
    private String codeMembre;
    private String nomMembre;
    private String prenomMembre;
    private String profession;
    private String portable;
    private String email;
    private String quartier;
    private String ville;
    private byte[] empreinte1;
    private byte[] empreinte2;
    private byte[] empreinte3;
    private byte[] empreinte4;
    private byte[] empreinte5;
    private byte[] empreinte6;
    private byte[] empreinte7;
    private byte[] empreinte8; 
    private byte[] empreinte9;
    private byte[] empreinte10;
    private byte[] empreinte11;
    private byte[] empreinte12;
    private byte[] photompp;
    private String autoEnroler;
  

    public Membre() {
    }

    public Membre(String codeMembre, String autoEnroler) {
        this.codeMembre = codeMembre;
        this.autoEnroler = autoEnroler;
      
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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPortable() {
        return portable;
    }

    public void setPortable(String portable) {
        this.portable = portable;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public byte[] getEmpreinte1() {
        return empreinte1;
    }

    public void setEmpreinte1(byte[] empreinte1) {
        this.empreinte1 = empreinte1;
    }

    public byte[] getEmpreinte2() {
        return empreinte2;
    }

    public void setEmpreinte2(byte[] empreinte2) {
        this.empreinte2 = empreinte2;
    }

    public byte[] getEmpreinte3() {
        return empreinte3;
    }

    public void setEmpreinte3(byte[] empreinte3) {
        this.empreinte3 = empreinte3;
    }

    public byte[] getEmpreinte4() {
        return empreinte4;
    }

    public void setEmpreinte4(byte[] empreinte4) {
        this.empreinte4 = empreinte4;
    }

    public byte[] getEmpreinte5() {
        return empreinte5;
    }

    public void setEmpreinte5(byte[] empreinte5) {
        this.empreinte5 = empreinte5;
    }

    public byte[] getEmpreinte6() {
        return empreinte6;
    }

    public void setEmpreinte6(byte[] empreinte6) {
        this.empreinte6 = empreinte6;
    }

    public byte[] getEmpreinte7() {
        return empreinte7;
    }

    public void setEmpreinte7(byte[] empreinte7) {
        this.empreinte7 = empreinte7;
    }

    public byte[] getEmpreinte8() {
        return empreinte8;
    }

    public void setEmpreinte8(byte[] empreinte8) {
        this.empreinte8 = empreinte8;
    }

    public byte[] getEmpreinte9() {
        return empreinte9;
    }

    public void setEmpreinte9(byte[] empreinte9) {
        this.empreinte9 = empreinte9;
    }

    public byte[] getEmpreinte10() {
        return empreinte10;
    }

    public void setEmpreinte10(byte[] empreinte10) {
        this.empreinte10 = empreinte10;
    }

    public byte[] getEmpreinte11() {
        return empreinte11;
    }

    public void setEmpreinte11(byte[] empreinte11) {
        this.empreinte11 = empreinte11;
    }

    public byte[] getEmpreinte12() {
        return empreinte12;
    }

    public void setEmpreinte12(byte[] empreinte12) {
        this.empreinte12 = empreinte12;
    }

    public byte[] getPhotompp() {
        return photompp;
    }

    public void setPhotompp(byte[] photompp) {
        this.photompp = photompp;
    }

    public String getAutoEnroler() {
        return autoEnroler;
    }

    public void setAutoEnroler(String autoEnroler) {
        this.autoEnroler = autoEnroler;
    }

   }
