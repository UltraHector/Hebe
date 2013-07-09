package com.TroyEmpire.Hebe.Entities;

import lombok.Data;

@Data
public class Campus {
	private long id;
	private String name;
	private int restaurantDbVersion;
	private int mapDbVersion;
}
