
package com.simplechatclient.android;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class OnetAuth {
    private static final String AJAX_API = "http://czat.onet.pl/include/ajaxapi.xml.php3";

    private static final String TAG = "SCC";

    private boolean authorizing = false;

    private boolean registeredNick;

    private boolean override = false;

    private String nick;

    private String password;

    private String version;

    HttpClient httpclient;

    public void authorize(String n, String p) {
        if (authorizing) {
            Log.e(TAG, "Already authorizing");
            return;
        }

        nick = n;
        password = p;

        registeredNick = true;
        override = true;

        authorizing = true;

        createHttpClient();

        downloadChat();

        destroyHttpClient();
    }

    private void createHttpClient() {
        httpclient = new DefaultHttpClient();
    }

    private void destroyHttpClient() {
        httpclient = null;
    }

    private void downloadChat() {
        String url = "http://czat.onet.pl/chat.html";
        String content = "ch=&n=&p=&category=0";

        HttpDownload(url, content);

        downloadDeploy();
    }

    private void downloadDeploy() {
        String url = "http://czat.onet.pl/_s/deployOnetCzat.js";

        String web = HttpDownload(url, null);
        version = getVersion(web);
        version = String.format("1.1(%s - R)", version);

        downloadKropka();
    }

    private void downloadKropka() {
        String url = "http://kropka.onet.pl/_s/kropka/1?DV=czat%2Fchat&SC=1&IP=&DG=id%3Dno-gemius&RI=&C1=&CL=std161&CS=1280x800x24&CW=1280x243&DU=http://czat.onet.pl/chat.html&DR=http://czat.onet.pl/";
        HttpDownload(url, null);

        downloadKropkaFull();
    }

    private void downloadKropkaFull() {
        String url = "http://kropka.onet.pl/_s/kropka/1?DV=czat/applet/FULL";
        HttpDownload(url, null);

        downloadSk();
    }

    private void downloadSk() {
        String url = "http://czat.onet.pl/sk.gif";
        HttpDownload(url, null);

        if (registeredNick)
            downloadSecureKropka();
        else {
            // showCaptchaDialog();
            String code = "";
            downloadCheckCode(code);
        }
    }

    private void downloadCheckCode(String code) {
        String url = AJAX_API;
        String content = String.format(
                "api_function=checkCode&params=a:1:{s:4:\"code\";s:%d:\"%s\";}", code.length(),
                code);

        HttpDownload(url, content);

        downloadUo();
    }

    private void downloadSecureKropka() {
        String url = "http://kropka.onet.pl/_s/kropka/1?DV=secure&SC=1&CL=std161&CS=1280x800x24&CW=1280x243&DU=http://secure.onet.pl/&DR=";
        HttpDownload(url, null);

        downloadSecureLogin();
    }

    private void downloadSecureLogin() {
        String url = "https://secure.onet.pl/mlogin.html";
        String content = String.format("r=&url=&login=%s&haslo=%s&app_id=20&ssl=1&ok=1", nick,
                password);

        HttpDownload(url, content);

        if (override)
            downloadOverride();
        else
            downloadUo();
    }

    private void downloadOverride() {
        String url = AJAX_API;
        String content = String.format(
                "api_function=userOverride&params=a:1:{s:4:\"nick\";s:%d:\"%s\";}", nick.length(),
                nick);

        HttpDownload(url, content);

        downloadUo();
    }

    private void downloadUo() {
        String url = AJAX_API;

        int isRegistered = (registeredNick == true ? 0 : 1);
        String content = String
                .format("api_function=getUoKey&params=a:3:{s:4:\"nick\";s:%d:\"%s\";s:8:\"tempNick\";i:%d;s:7:\"version\";s:%d:\"%s\";}",
                        nick.length(), nick, isRegistered, version.length(), version);

        String data = HttpDownload(url, content);

        parseUoResponse(data);
        authorizing = false;
    }

    private String getVersion(String data) {
        if (data != null) {
            if (data.indexOf("OnetCzatLoader") != -1) {
                String strFind1 = "signed-OnetCzatLoader-";
                String strFind2 = ".jar";
                int pos1 = data.indexOf(strFind1) + strFind1.length();
                int pos2 = data.indexOf(strFind2, pos1);
                String ver = data.substring(pos1, pos2);

                if ((ver != null) && (ver.length() > 0) && (ver.length() < 20))
                    return ver;
            }
        }

        return "20120711-1544b";
    }

    private void parseUoResponse(String data) {
        // TODO
    }

    private String HttpDownload(String url, String content) {
        try {

            HttpResponse httpResponse;

            if (content == null) {
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpclient.execute(httpGet);
            } else {
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new StringEntity(content));
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
                httpResponse = httpclient.execute(httpPost);
            }

            int status = httpResponse.getStatusLine().getStatusCode();
            if (status == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                return EntityUtils.toString(httpEntity);
            } else {
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Unable to retrieve web page: " + url);
            e.getStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Unable to retrieve web page: " + url);
            e.getStackTrace();
            return null;
        } catch (IOException e) {
            Log.e(TAG, "Unable to retrieve web page: " + url);
            e.getStackTrace();
            return null;
        }
    }
}
