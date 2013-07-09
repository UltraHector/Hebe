package com.TroyEmpire.Hebe.Entities;

import java.util.Date;

import com.TroyEmpire.Hebe.Constant.NewsType;

import lombok.Data;

@Data
public class NewsOnServer {
	private int id;
	private Date publishDate;
	private String title;
	private String content;
	private NewsType newsType;
}
