package com.filepreview.timer;


import com.filepreview.dao.FileDao;
import com.filepreview.model.FileModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClearFileTimer {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FileDao fileDao;

	/**
	 * @Description: 每小时执行一次文件清理(清理临时存储文件)
	 */
	@Scheduled(cron = "0 0 * * * ?")
	public void clear() {
		this.logger.info("ClearFileTimer@clear start...");
		List<String> keys = this.fileDao.findAllKeys();
		for (String key : keys) {
			FileModel fileModel = this.fileDao.removeFromMap(key);
			long now = System.currentTimeMillis();
			if (now - fileModel.getCreateMs() > fileModel.getLimitMs()) {
				int tmp = this.fileDao.delete(fileModel);
				if (tmp <= 0) {
					// 删除失败回滚
					this.logger.warn("ClearFileTimer@clear dir:"
							+ fileModel.getTempDir() + ",delete failed.");
					this.fileDao.rollbackFromMap(fileModel);
				}
			} else {
				// 未过期不需要回滚
				this.fileDao.rollbackFromMap(fileModel);
			}

		}
		this.logger.info("ClearFileTimer@clear end...");

	}

}
