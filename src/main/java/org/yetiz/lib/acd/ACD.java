package org.yetiz.lib.acd;

import org.yetiz.lib.acd.Entity.AccountInfo;
import org.yetiz.lib.acd.Entity.AccountQuota;
import org.yetiz.lib.acd.Entity.AccountUsage;
import org.yetiz.lib.acd.api.Account;
import org.yetiz.lib.acd.exception.AuthorizationRequiredException;

import java.io.File;
import java.util.Calendar;

/**
 * Created by yeti on 2015/4/13.
 */
public class ACD {

	private ACDSession currentSession = null;

	public ACD() {
	}

	public ACDSession getACDSessionByCode(Configure configure, String code) throws AuthorizationRequiredException {
		if (code == null) {
			throw new AuthorizationRequiredException(configure.getClientId(), configure.getRedirectUri(),
				configure.isWritable());
		}
		if (code.equals("")) {
			throw new AuthorizationRequiredException(configure.getClientId(), configure.getRedirectUri(),
				configure.isWritable());
		}
		return ACDSession.getACDSessionByCode(configure, code);
	}

	public ACDSession getACDSessionByToken(Configure configure, ACDToken acdToken) {
		if (acdToken == null) {
			throw new NullPointerException("acdToken is not nullable.");
		}
		return ACDSession.getACDSessionByToken(configure, acdToken);
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
				configure.update();
			}
		}

		ACD acd = new ACD();
		ACDSession acdSession = null;
		if (configure.getAccessToken().equals("")) {
			if (args.length == 2)
				acdSession = acd.getACDSessionByCode(configure, args[1]);
			else {
				acd.getACDSessionByCode(configure, null);
			}
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, 3600);
			ACDToken acdToken = new ACDToken(configure.getTokenType(), calendar.getTime(),
				configure.getRefreshToken(), configure.getAccessToken());
			acdSession = acd.getACDSessionByToken(configure, acdToken);
		}

		AccountInfo accountInfo = Account.getAccountInfo(acdSession);
		AccountQuota accountQuota = Account.getAccountQuota(acdSession);
		AccountUsage accountUsage = Account.getAccountUsage(acdSession);

		acdSession.destroy();
	}
}
