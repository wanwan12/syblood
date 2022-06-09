package com.syblood.app.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.syblood.app.api.ApiRequestCallBack;
import com.syblood.app.api.ApiRequset;
import com.syblood.app.api.ApiResponseObject;
import com.syblood.app.application.AppContext;
import com.syblood.app.constants.Constants;
import com.syblood.app.models.VersionInfo;
import com.syblood.app.nohttp.CallServer;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;


import java.io.File;
import java.util.List;

/**
 * APP更新对象
 * 
 * @author xw
 * 
 */
public class AppUpdater
{
	private Context context;
	private UpdateInfo upInfo = null;
	private OnNewVersionListener onNewVersionListener = null;
	private OnProgressListener onProgressListener = null;
	private OnCompleteListener onCompleteListener = null;
	private OnNoNewVersionListener onNoNewVersionListener=null;
	
	public OnNewVersionListener getOnNewVersionListener()
	{
		return onNewVersionListener;
	}

	/**
	 * 下载请求
	 */
	private DownloadRequest downloadRequest;

	private int mFileSize;//下载文件总大小

	public void setOnNewVersionListener(OnNewVersionListener onNewVersionListener)
	{
		this.onNewVersionListener = onNewVersionListener;
	}

	public OnProgressListener getOnProgressListener()
	{
		return onProgressListener;
	}

	public void setOnProgressListener(OnProgressListener onProgressListener)
	{
		this.onProgressListener = onProgressListener;
	}

	public OnCompleteListener getOnCompleteListener()
	{
		return onCompleteListener;
	}

	public void setOnCompleteListener(OnCompleteListener onCompleteListener)
	{
		this.onCompleteListener = onCompleteListener;
	}

	public OnNoNewVersionListener getOnNoNewVersionListener()
	{
		return onNoNewVersionListener;
	}

	public void setOnNoNewVersionListener(OnNoNewVersionListener onNoNewVersionListener)
	{
		this.onNoNewVersionListener = onNoNewVersionListener;
	}

	public AppUpdater(Context _cnt)
	{
		context = _cnt;
	}

	/**
	 * 检查app更新
	 */
	public void checkForUpdate() {

		/**
		 * 当前版本号
		 */
		String currentVersion = "";
		try
		{
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			currentVersion = info.versionName;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			currentVersion = "";
		}
		final String version = currentVersion;

		ApiRequset.versionInfo(context, Constants.APP_CODE, Constants.APP_NAME, version, new ApiRequestCallBack() {

			@Override
			public void onPreStart() {
				super.onPreStart();
			}

			@Override
			public void onSuccess(ApiResponseObject t) {
				super.onSuccess(t);

				List<VersionInfo> list = t.JsonToList("versionInfo", VersionInfo[].class);
				VersionInfo info = null;
				if (list != null && list.size() != 0) {
					info = list.get(0);
				}
				else
				{
					if (onNoNewVersionListener != null) {
						onNoNewVersionListener.onNoNewVersion();
					}
					return;
				}

				if (info != null) {
					upInfo = new UpdateInfo();
					upInfo.setAppUrl(info.getAppUrl());
					upInfo.setUseFlag(info.getUseFlag());
					upInfo.setVersion(info.getVersion());
					upInfo.setVersionDate(info.getVersionDate());
					upInfo.setVersionMemo(info.getVersionMemo());
					// 触发有新版本事件
					if (onNewVersionListener != null) {
						onNewVersionListener.onNewVersion(upInfo);
					}
				} else {
					if (onNoNewVersionListener != null) {
						onNoNewVersionListener.onNoNewVersion();
					}
				}
			}

			@Override
			public void onFailure(String errorCode, String strMsg) {
				super.onFailure(errorCode, strMsg);
				AppContext.showToastShort(strMsg);
			}

			@Override
			public void onFinish() {
				super.onFinish();
			}
		});

	}

	/**
	 * 
	 * @param appName
	 *            保存的app名称
	 * @return
	 * @throws Exception
	 */
	private File getFile(String appName) throws Exception
	{		
		File file = new File(context.getExternalFilesDir(""), appName);
		if (file.exists())
		{
			file.delete();
			file.createNewFile();
		}
		else
		{
			file.createNewFile();
		}
		return file;
	}

	public void downloadApp(final UpdateInfo upInfo){

		new Thread()
		{
			public void run()
			{
				// url 下载地址
				// fileFloader 保存的文件夹
				// fileName 文件名
				// isRange 是否断点续传下载
				// isDeleteOld 如果发现文件已经存在是否删除后重新下载
				try {
					String folder=context.getExternalFilesDir("").getAbsolutePath();
					//String folder= Environment.getExternalStorageDirectory().toString();
					downloadRequest= NoHttp.createDownloadRequest(upInfo.getAppUrl(), folder, upInfo.getAppName(), true, false);

					// what 区分下载
					// downloadRequest 下载请求对象
					// downloadListener 下载监听
					CallServer.getDownloadInstance().add(0, downloadRequest, new DownloadListener() {
						@Override
						public void onDownloadError(int what, Exception exception) {

						}

						@Override
						public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
							 mFileSize=(int)allCount;

						}

						@Override
						public void onProgress(int what, int progress, long fileCount, long speed)
						{
							LogUtil.e("123", "onProgress");
							if (onProgressListener != null && downloadRequest != null)
							{
								onProgressListener.onProgress(mFileSize, (int)(((progress*(long)mFileSize))/100), downloadRequest);
							}
						}

						@Override
						public void onFinish(int what, String filePath) {
							LogUtil.e("123", "onFinish");
							if (onCompleteListener != null)
							{
								onCompleteListener.onComplete(upInfo);
							}
						}

						@Override
						public void onCancel(int what) {

						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	public class UpdateInfo
	{
		private String Version = "";
		private String VersionDate = "";
		private String VersionMemo = "";
		private String AppUrl = "";
		private String UseFlag = "";

		public String getVersion()
		{
			return Version;
		}

		public void setVersion(String version)
		{
			Version = version;
		}

		public String getVersionDate()
		{
			return VersionDate;
		}

		public void setVersionDate(String versionDate)
		{
			VersionDate = versionDate;
		}

		public String getVersionMemo()
		{
			return VersionMemo;
		}

		public void setVersionMemo(String versionMemo)
		{
			VersionMemo = versionMemo;
		}

		public String getAppUrl()
		{
			return AppUrl;
		}

		public void setAppUrl(String appUrl)
		{
			AppUrl = appUrl;
		}

		public String getUseFlag()
		{
			return UseFlag;
		}

		public void setUseFlag(String useFlag)
		{
			UseFlag = useFlag;
		}

		public String getAppName()
		{
			String[] strs = this.AppUrl.split("/");
			if (strs.length != 0)
			{
				return strs[strs.length - 1];
			}
			else
			{
				return "";
			}
		}
	}

	public interface OnNoNewVersionListener
	{
		public void onNoNewVersion();
	}
	
	/**
	 * 有新版本事件侦听对象
	 * 
	 * @author EricCheng
	 * 
	 */
	public interface OnNewVersionListener
	{
		public void onNewVersion(UpdateInfo info);
	}

	/**
	 * 下载进度事件侦听对象
	 * 
	 * @author EricCheng
	 * 
	 */
	public interface OnProgressListener
	{
		/**
		 * 文件下载进度
		 * 
		 * @param fileSize
		 *            文件总大小
		 * @param downloadSize
		 *            已下载大小
		 */
		public void onProgress(int fileSize, int downloadSize, DownloadRequest downloadRequest);
	}

	/**
	 * 下载完成事件侦听对象
	 * 
	 * @author EricCheng
	 * 
	 */
	public interface OnCompleteListener
	{
		public void onComplete(UpdateInfo info);
	}




}
