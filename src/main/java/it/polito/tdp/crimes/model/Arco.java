package it.polito.tdp.crimes.model;

public class Arco implements Comparable<Arco>{
	private String s1;
	private String s2;
	private Integer peso;

	public Arco(String s1, String s2, Integer peso) {
		this.s1 = s1;
		this.s2 =s2;
		this.peso = peso;
	}

	public String getS1() {
		return s1;
	}

	public String getS2() {
		return s2;
	}

	public Integer getPeso() {
		return peso;
	}

	@Override
	public String toString() {
		return  s1 + " ----- " + s2 + " " + peso;
	}

	@Override
	public int compareTo(Arco o) {
		return -this.peso.compareTo(o.getPeso());
	}
	
	

}
