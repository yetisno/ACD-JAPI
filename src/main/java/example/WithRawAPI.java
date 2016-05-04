package example;

import org.yetiz.lib.acd.ACDSession;
import org.yetiz.lib.acd.ACDToken;
import org.yetiz.lib.acd.Configure;
import org.yetiz.lib.acd.Entity.FileInfo;
import org.yetiz.lib.acd.Entity.FolderInfo;
import org.yetiz.lib.acd.Entity.NodeInfo;
import org.yetiz.lib.acd.Entity.NodeInfoList;
import org.yetiz.lib.acd.Utils;
import org.yetiz.lib.acd.api.v1.Account;
import org.yetiz.lib.acd.api.v1.Nodes;
import org.yetiz.lib.acd.api.v1.Trash;

import java.io.File;
import java.util.Calendar;

/**
 * Created by yeti on 2015/4/13.
 */
public class WithRawAPI {

	public WithRawAPI() {
	}

	public static void main(String[] args) {
		String uploadFilePath = "/Users/yeti/Downloads/1.jpg";
		String uploadFileName = "ACD-JAPI-TEST.jpg";
		String uploadFolderName = "ACD-JAPI-TEST";
		Configure configure = getConfigure(args);
		ACDSession acdSession = getACDSession(args, configure);

		Utils.print(Account.getAccountInfo(acdSession));
		Utils.print(Account.getAccountQuota(acdSession));
		Utils.print(Account.getAccountUsage(acdSession));
		Utils.print(Account.getEndpoint(acdSession));

		FolderInfo root = Nodes.getRootFolder(acdSession);
		Utils.print(root);

		NodeInfoList rootChilds = Nodes.getChildList(acdSession, root, null);

		for (NodeInfo info : rootChilds.getList()) {
			if (info.getName().equals(uploadFolderName)) {
				Trash.moveNodeToTrash(acdSession, info);
			}

			Utils.print(info);
		}

		FolderInfo uploadFolderInfo = new FolderInfo();
		uploadFolderInfo.setName(uploadFolderName);
		uploadFolderInfo.setParents(new String[]{root.getId()});
		uploadFolderInfo = Nodes.createFolder(acdSession, uploadFolderInfo);
		Utils.print(uploadFolderInfo);

		FileInfo uploadFileInfo = new FileInfo();
		uploadFileInfo.setName(uploadFileName);
		uploadFileInfo.setParents(new String[]{uploadFolderInfo.getId()});
		uploadFileInfo = Nodes.uploadFile(acdSession, uploadFileInfo, new File(uploadFilePath));
		Utils.print(uploadFileInfo);

		uploadFileInfo = Nodes.getFileMetadata(acdSession, uploadFileInfo.getId());
		Utils.print(uploadFileInfo);

		NodeInfo node = Trash.moveNodeToTrash(acdSession, uploadFolderInfo);
		Utils.print(node);

		acdSession.destroy();
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
}
