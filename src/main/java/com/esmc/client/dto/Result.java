package com.esmc.client.dto;

public class Result {

     	private Integer resultat;
		private String message;

		public Result() {
		}

		public Result(Integer resultat, String message) {
			this.resultat = resultat;
			this.message = message;
		}

		public Integer getResultat() {
			return resultat;
		}

		public void setResultat(Integer resultat) {
			this.resultat = resultat;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}



