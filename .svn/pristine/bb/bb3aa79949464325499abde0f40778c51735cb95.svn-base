package com.jjshome.service.impl;


import com.jjshome.dao.FileDao;
import com.jjshome.model.FileModel;
import com.jjshome.service.DownloadNetFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

@Component
public class DownloadNetFileServiceImpl implements DownloadNetFileService {

	@Autowired
	private FileDao fileDao;

	@Override
	public void download(FileModel fileModel) {
		if (fileModel.getState() != FileModel.STATE_WXZ) {
			throw new RuntimeException("the file state:" + fileModel.getState()
					+ " is not 1");
		}
		try {
			URL url = new URL(fileModel.getOriginal());
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			// 文件多媒体类型MIME
			fileModel.setOriginalMIMEType(connection.getContentType());
			// 源文件的路径
			String orignal = fileModel.getOriginal();
			// 第一个问号
			int firstQM = orignal.indexOf("?");
			if (firstQM != -1) {
				orignal = orignal.substring(0, firstQM);
			}
			int lastSlash = orignal.lastIndexOf("/");
			// 源文件的名字
			fileModel.setOriginalFile(orignal.substring(lastSlash + 1));
			InputStream in = connection.getInputStream();
			try {
				// 文件存储到本地临时目录
				this.fileDao.saveFile(in, fileModel);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				in.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

}
