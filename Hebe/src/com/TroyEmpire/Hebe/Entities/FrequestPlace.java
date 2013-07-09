package com.TroyEmpire.Hebe.Entities;

import lombok.Data;

@Data
public class FrequestPlace {
	/**
	 * 在数据库的id
	 * */
	private int id;
	/**
	 * 对应的cellID
	 * */
	private int cellId;

	/**
	 * 点击量
	 * 
	 */
	private int hitCount;
}
