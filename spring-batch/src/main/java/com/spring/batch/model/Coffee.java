package com.spring.batch.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Coffee {
	private String brand;
	private String origin;
	private String characteristics;

	public Coffee(String brand, String origin, String characteristics) {
		this.brand = brand;
		this.origin = origin;
		this.characteristics = characteristics;
	}

	public Coffee() {
		super();
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(String characteristics) {
		this.characteristics = characteristics;
	}

	@Override
	public String toString() {
		String json=null;
		try {
			json = new ObjectMapper().writeValueAsString(new Coffee(brand, origin, characteristics));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}

}