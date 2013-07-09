package com.TroyEmpire.Hebe.IServices;

import java.util.List;

import com.TroyEmpire.Hebe.Constant.NewsType;
import com.TroyEmpire.Hebe.Entities.News;

public interface IInformationPlatformService {
	/**
	 * check and download more news from server
	 */
	public void updateNews(NewsType type);

	/**
	 * @param startId
	 *            , the startId from which a number of limit news will be
	 *            fetched if startId == null, the latest news in the storage
	 *            will be returned
	 * @param the
	 *            number of news will be returned
	 */
	public List<News> getNewsFromStorage(NewsType type, Long startId, int limit);

	public News getNewsByid(long id);
}
