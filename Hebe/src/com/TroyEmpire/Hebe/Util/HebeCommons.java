package com.TroyEmpire.Hebe.Util;

import java.io.IOException;

import org.jsoup.Jsoup;

import com.TroyEmpire.Hebe.Constant.Constant;

public class HebeCommons {
	public static boolean checkWhetherCouldConnectToServer() {
		try {
			if (Jsoup
					.connect(
							Constant.HEBE_SERVER_URL
									+ Constant.HEBE_SERVER_CONNECTION_CHECK_SUB_URL)
					.execute()
					.hasHeader(Constant.HEBE_SERVER_CONNECTION_STATUS))
				return true;
			else
				return false;
		} catch (IOException e) {
			return false;
		}
	}
}
