package com.TroyEmpire.Hebe.Entities;



import java.io.Serializable;
import java.util.Date;

import com.TroyEmpire.Hebe.Constant.NewsType;

import lombok.Data;


@Data
public class News implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private long newsIdOnServer;
	private Date publishDate;
	private String title;
	private String content;
	private NewsType newsType;
}
