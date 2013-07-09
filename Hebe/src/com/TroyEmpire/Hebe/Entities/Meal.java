package com.TroyEmpire.Hebe.Entities;

import lombok.Data;

@Data
public class Meal {
	private long id;
	private long restaurantId;
	private String name;
	private float price;
	private String description;
}
