package org.yetiz.lib.acd;

import org.yetiz.lib.acd.Entity.FileInfo;
import org.yetiz.lib.acd.Entity.FolderInfo;
import org.yetiz.lib.acd.Entity.Property;
import org.yetiz.lib.acd.api.v1.Nodes;

import java.io.File;
import java.util.Calendar;

/**
 * Created by yeti on 2015/4/13.
 */
public class MainClass {

	private ACDSession currentSession = null;

	public MainClass() {
	}

	public static void main(String[] args) {
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

		MainClass acd = new MainClass();
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

		//		AccountInfo accountInfo = Account.getAccountInfo(acdSession);
		//		AccountQuota accountQuota = Account.getAccountQuota(acdSession);
		//		AccountUsage accountUsage = Account.getAccountUsage(acdSession);
		//		FileInfo fileInfo = Nodes.getFileMetadata(acdSession, "wflhKX14RYGJu-K21JLrNA");
		//		fileInfo.setName("IMG_0770.m4v");
		//		fileInfo = Nodes.updateFileMetadata(acdSession, fileInfo);
		FolderInfo folderInfo = Nodes.getFolderMetadata(acdSession, "1nqEb8n4RuCHu8hr2M1GQA");
		folderInfo.setName("Documents");
		folderInfo = Nodes.updateFolderMetadata(acdSession, folderInfo);
		System.out.println("File counts: " + Nodes.getFileInfoLists(acdSession, null).getCount());
		folderInfo = new FolderInfo("Lbn5nWhfRqq4ecjizmuIhQ");
		FileInfo fileInfo = new FileInfo("vLLrmvMeRHGru-Xhe9EjxA");
		Nodes.getChildList(acdSession, folderInfo, null);
		Property property = new Property("Key", "VAL");
		Nodes.addProperty(acdSession, fileInfo, property);
		Nodes.getProperties(acdSession, fileInfo, acdSession.getConfigure().getOwner());
		Nodes.getProperty(acdSession, fileInfo, acdSession.getConfigure().getOwner(), property.getKey());
		System.out.println(Nodes.getProperties(acdSession, fileInfo, acdSession.getConfigure().getOwner())
			.get().size());
		Nodes.deleteProperty(acdSession, fileInfo, property.getKey());
		System.out.println(Nodes.getProperties(acdSession, fileInfo, acdSession.getConfigure().getOwner())
			.get().size());

		//		FileInfo upload = new FileInfo();
		//		upload.setName("Test1.jpg");
		//		upload.setParents(null);
		//		upload.setLabels(null);
		//		upload = Nodes.uploadFile(acdSession, upload, new File("/home/yeti/Downloads/1.jpg"));
		//		upload = Nodes.overwriteFile(acdSession, upload, new File("/Users/yeti/Desktop/i5e62nT.jpg"));
		//		InputStream inputStream = Nodes.downloadFile(acdSession, upload);
		//		try {
		//			FileOutputStream fileOutputStream = new FileOutputStream(new File("/Users/yeti/Desktop/i5e62nT2
		// .jpg"));
		//			int read = 0;
		//			byte[] buffer = new byte[256];
		//			while (true) {
		//				read = inputStream.read(buffer);
		//				if (read == -1) {
		//					break;
		//				}
		//				fileOutputStream.write(buffer);
		//			}
		//			inputStream.close();
		//			fileOutputStream.close();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
		acdSession.destroy();
	}
}
