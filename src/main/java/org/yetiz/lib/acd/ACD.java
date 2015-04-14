package org.yetiz.lib.acd;

import org.yetiz.lib.acd.exception.AuthorizationRequiredException;

/**
 * Created by yeti on 2015/4/13.
 */
public class ACD {

    private String client_id = "";
    private String client_secret = "";
    private String redirectUrl = "http://localhost";
    private boolean writable = true;
    private ACDSession currentSession = null;

    public ACD(String client_id, String client_secret) {
        this.client_id = client_id;
        this.client_secret = client_secret;
    }

    /**
     * Default: http://localhost
     *
     * @param redirectUrl
     */
    public ACD setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
        return this;
    }

    /**
     * Default: true
     *
     * @param writable
     */
    public ACD setWritable(boolean writable) {
        this.writable = writable;
        return this;
    }


    public ACDSession getACDSessionByCode(String code) throws AuthorizationRequiredException {
        if (code == null) {
            throw new AuthorizationRequiredException(client_id, redirectUrl, writable);
        }
        if (code == "") {
            throw new AuthorizationRequiredException(client_id, redirectUrl, writable);
        }
        return ACDSession.getACDSessionByCode(client_id, client_secret, code, redirectUrl);
    }

    public ACDSession getACDSessionByToken(ACDToken acdToken) {
        if (acdToken == null) {
            throw new NullPointerException("acdToken is not nullable.");
        }
        return ACDSession.getACDSessionByToken(client_id, client_secret, acdToken);
    }

    public static void main(String[] args) {
		ACD acd = new ACD("", "");
        ACDSession acdSession = acd.getACDSessionByCode(null);
        acdSession.destroy();
//        calendar.add(Calendar.SECOND, object.get("expires_in").getAsInt());
//        acdSession.acdToken = new ACDToken(
//            object.get("token_type").getAsString(),
//            calendar.getTime(),
//            object.get("refresh_token").getAsString(),
//            object.get("access_token").getAsString());
    }
}
