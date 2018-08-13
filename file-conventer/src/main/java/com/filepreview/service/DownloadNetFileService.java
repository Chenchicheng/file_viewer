package com.filepreview.service;


import com.filepreview.model.FileModel;

public interface DownloadNetFileService {

	/**
	 * @Description: 下载文件,状态改变,下载文件路径,会回写到fileModel中
	 * @param fileModel
	 */
	public void download(FileModel fileModel);

}
