package com.TroyEmpire.Hebe.Entities;

import java.io.Serializable;

import lombok.Data;


@Data
public class Building implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private double latitude;
	private double longitude;
	private String description;
	//图书馆对应路上的点,reference to Class PathDot
	private long pathDotId;
	
	//the scale of the building
	private double miniLatitude;
	private double maxLatitude;
	private double miniLongitude;
	private double maxLongitude;
}
