package org.yetiz.lib.acd.sample;

import org.yetiz.lib.acd.ACD;
import org.yetiz.lib.acd.ACDSession;
import org.yetiz.lib.acd.ACDToken;
import org.yetiz.lib.acd.Configure;
import org.yetiz.lib.acd.Entity.*;
import org.yetiz.lib.acd.api.v1.Nodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yeti on 2015/4/13.
 */
public class MainSampleClass {

	private ACDSession currentSession = null;

	public MainSampleClass() {

	}

	public static Configure getConfigure(String[] args) {
		Configure configure = null;
		if (args.length >= 1) {
			File file = new File(System.getProperty("user.dir") + File.separator + args[0]);
			if (file.exists()) {
				configure = Configure.load(file);
			}
			file = new File(args[0]);
			if (file.exists()) {
				configure = Configure.load(file);
			}
		}
		if (configure == null) {
			configure = new Configure();
			if (new File(configure.getPath()).exists()) {
				configure = Configure.load(new File(configure.getPath()));
			} else {
				configure.save();
			}
		}
		return configure;
	}

	public static ACDSession getACDSession(String[] args, Configure configure) {
		ACDSession acdSession = null;
		if (configure.getAccessToken().equals("")) {
			if (args.length == 2)
				acdSession = ACDSession.getACDSessionByCode(configure, args[1]);
			else {
				ACDSession.getACDSessionByCode(configure, null);
			}
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, 3600);
			ACDToken acdToken = new ACDToken(configure.getTokenType(), calendar.getTime(),
				configure.getRefreshToken(), configure.getAccessToken());
			acdSession = ACDSession.getACDSessionByToken(configure, acdToken);
		}
		return acdSession;
	}

	public static void download(InputStream inputStream) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(new File("/Users/yeti/Downloads/i5e62nT2.jpg"));
			int read = 0;
			byte[] buffer = new byte[256];
			while (true) {
				read = inputStream.read(buffer);
				if (read == -1) {
					break;
				}
				fileOutputStream.write(buffer);
			}
			inputStream.close();
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String testFilePath = "/Users/yeti/Downloads/1.jpg";
		String testReplaceFilePath = "/Users/yeti/Downloads/2.jpg";
		Configure configure = getConfigure(args);
		ACDSession acdSession = getACDSession(args, configure);
		ACD acd = new ACD(acdSession);
		AccountInfo info = acd.getUserInfo();
		AccountUsage usage = acd.getUserUsage();
		AccountQuota quota = acd.getUserQuota();
		FolderInfo rootFolder = Nodes.getRootFolder(acdSession);
		NodeInfoList infoList = Nodes.getChildList(acdSession, rootFolder, null);
		List<NodeInfo> nodeInfos = infoList.getList();
		for (int i = 0; i < nodeInfos.size(); i++) {
			if (nodeInfos.get(i).getName().equals("Test")){
				acd.removeFolder(nodeInfos.get(i).getId());
			}
			if (nodeInfos.get(i).getName().equals("Test2")){
				acd.removeFolder(nodeInfos.get(i).getId());
			}
		}

		FolderInfo folderInfo = acd.createFolder(null, "Test");
		folderInfo = acd.renameFolder(folderInfo.getId(), "Test2");
		folderInfo = acd.removeFolder(folderInfo.getId());
		folderInfo = acd.restoreFolder(folderInfo.getId());
		FileInfo fileInfo = acd.uploadFile(folderInfo.getId(), "Test.jpg", new File(testFilePath));
		fileInfo = acd.updateFile(fileInfo.getId(), new File(testReplaceFilePath));
		fileInfo = acd.renameFile(fileInfo.getId(), "Test2.jpg");
		fileInfo = acd.removeFile(fileInfo.getId());
		fileInfo = acd.restoreFile(fileInfo.getId());
		acd.addProperty(fileInfo.getId(), "KEY", "VAL");
		System.out.println(acd.getProperties(fileInfo.getId()).get().size());
		acd.removeProperty(fileInfo.getId(), "KEY");
		System.out.println(acd.getProperties(fileInfo.getId()).get().size());
		System.out.println(acd.getList(folderInfo.getId()).size());
		download(acd.getFile(fileInfo.getId()));
		acd.removeFolder(folderInfo.getId());
		acd.destroy();
	}
}
