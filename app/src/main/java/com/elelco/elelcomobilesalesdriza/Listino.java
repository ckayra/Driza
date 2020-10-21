package com.elelco.elelcomobilesalesdriza;

public class Listino{
	String codProdotto;
	String codCliente;
	float pzoLordo;
	float sconto1;
	float pzoNetto;
	String codGiro;
//	public Listino(float pzoLordo, float sc1, float sc2, float sc3, float pzoNetto){
//		this.pzoLordo=pzoLordo;
//		this.sconto1=sc1;
//		this.sconto2=sc2;
//		this.sconto3=sc3;
//		this.pzoNetto=pzoNetto;
//	}
	
	public Listino(String codProdotto, String codCliente,float pzoLordo, float sc1,  float pzoNetto, String codGiro){
		this.codCliente=codCliente;
		this.codProdotto=codProdotto;
		this.pzoLordo=pzoLordo;
		this.sconto1=sc1;
		this.pzoNetto=pzoNetto;
		this.codGiro=codGiro;
	}
	
	public float getPrezzoLordo(){
		return this.pzoLordo;
	}
	public float getSconto1(){
		return this.sconto1;
	}
	public float getPrezzoNetto(){
		return this.pzoNetto;
	}
}
