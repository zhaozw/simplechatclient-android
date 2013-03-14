/*
 * Simple Chat Client
 *
 *   Copyright (C) 2013 Piotr Łuczko <piotr.luczko@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.onet;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.os.AsyncTask;
import android.util.Log;

public class OnetAuth {
    private static final String AJAX_API = "http://czat.onet.pl/include/ajaxapi.xml.php3";
    private static final String TAG = "ONETAUTH";

    private boolean authorizing = false;
    private boolean registeredNick;
    private boolean override = false;
    private String nick;
    private String password;
    private String version;

    HttpClient httpclient;

    public void authorize(String n, String p) {
        if (authorizing) {
            Log.w(TAG, "Already authorizing");
            return;
        }

        nick = n;
        password = p;

        registeredNick = true;
        override = true;

        authorizing = true;
        httpclient = new DefaultHttpClient();

        downloadChat();
    }

    private void downloadChat() {
        String url = "http://czat.onet.pl/chat.html";
        String content = "ch=&n=&p=&category=0";
        String category = "chat";

        new HttpDownload().execute(url, content, category);
    }

    private void downloadDeploy() {
        String url = "http://czat.onet.pl/_s/deployOnetCzat.js";
        String content = null;
        String category = "deploy";

        new HttpDownload().execute(url, content, category);
    }

    private void parseDeploy(String data)
    {
        version = parseVersion(data);
        version = String.format("1.1(%s - R)", version);    	
    }
    
    private void downloadKropka() {
        String url = "http://kropka.onet.pl/_s/kropka/1?DV=czat%2Fchat&SC=1&IP=&DG=id%3Dno-gemius&RI=&C1=&CL=std161&CS=1280x800x24&CW=1280x243&DU=http://czat.onet.pl/chat.html&DR=http://czat.onet.pl/";
        String content = null;
        String category = "kropka";
        
        new HttpDownload().execute(url, content, category);
    }

    private void downloadKropkaFull() {
        String url = "http://kropka.onet.pl/_s/kropka/1?DV=czat/applet/FULL";
        String content = null;
        String category = "kropka_full";
        
        new HttpDownload().execute(url, content, category);
    }

    private void downloadSk() {
        String url = "http://czat.onet.pl/sk.gif";
        String content = null;
        String category = "sk";
        
        new HttpDownload().execute(url, content, category);
    }

    private void downloadCheckCode(String code) {
        String url = AJAX_API;
        String content = String.format("api_function=checkCode&params=a:1:{s:4:\"code\";s:%d:\"%s\";}", code.length(), code);
        String category = "check_code";

        new HttpDownload().execute(url, content, category);
    }

    private void downloadSecureKropka() {
        String url = "http://kropka.onet.pl/_s/kropka/1?DV=secure&SC=1&CL=std161&CS=1280x800x24&CW=1280x243&DU=http://secure.onet.pl/&DR=";
        String content = null;
        String category = "secure_kropka";
        
        new HttpDownload().execute(url, content, category);
    }

    private void downloadSecureLogin() {
        String url = "https://secure.onet.pl/mlogin.html";
        String content = String.format("r=&url=&login=%s&haslo=%s&app_id=20&ssl=1&ok=1", nick, password);
        String category = "secure_login";

        new HttpDownload().execute(url, content, category);
    }

    private void downloadOverride() {
        String url = AJAX_API;
        String content = String.format("api_function=userOverride&params=a:1:{s:4:\"nick\";s:%d:\"%s\";}", nick.length(), nick);
        String category = "override";

        new HttpDownload().execute(url, content, category);
    }

    private void downloadUo() {
        int isRegistered = (registeredNick == true ? 0 : 1);

        String url = AJAX_API;
        String content = String.format("api_function=getUoKey&params=a:3:{s:4:\"nick\";s:%d:\"%s\";s:8:\"tempNick\";i:%d;s:7:\"version\";s:%d:\"%s\";}", nick.length(), nick, isRegistered, version.length(), version);
        String category = "uo";

        new HttpDownload().execute(url, content, category);
    }

    private String parseVersion(String data) {
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

    // <?xml version="1.0" encoding="ISO-8859-2"?><root><uoKey>LY9j2sXwio0G_yo3PdpukDL8iZJGHXKs</uoKey><zuoUsername>~Succubi_test</zuoUsername><error err_code="TRUE" err_text="wartość prawdziwa" ></error></root>
    // <?xml version="1.0" encoding="ISO-8859-2"?><root><error err_code="-2" err_text="U.ytkownik nie zalogowany" ></error></root>
    private void parseUo(String data) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(data)));
            document.getDocumentElement().normalize();

            Node error = document.getElementsByTagName("error").item(0);
            String err_code = error.getAttributes().getNamedItem("err_code").getNodeValue();
            String err_text = error.getAttributes().getNamedItem("err_text").getNodeValue();

            if (err_code.equalsIgnoreCase("true")) {
                String UOKey = document.getElementsByTagName("uoKey").item(0).getTextContent();
                String nick = document.getElementsByTagName("zuoUsername").item(0).getTextContent();

                // TODO
                Log.i(TAG, "UO nick:"+nick);
                Log.i(TAG, "UO key:"+UOKey);
            } else {
                Log.e(TAG, String.format("Authentication error [%s]", err_text));
                return;
            }
        } catch (ParserConfigurationException e) {
            Log.e(TAG, "Parse XML configuration exception:"+ e.getMessage());
            e.printStackTrace();
        } catch (SAXException e) {
            Log.e(TAG, "Wrong XML file structure:"+ e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "Cannot parse XML:"+ e.getMessage());
            e.printStackTrace();
        }
    }

    private class HttpDownload extends AsyncTask<String, Void, String> {
    	private String category;
    	
		@Override
	    protected String doInBackground(String... params) {
        	String url = params[0];
        	String content = params[1];
        	category = params[2];

            HttpResponse httpResponse;

            try {
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
            } catch (ClientProtocolException e) {
	            Log.e(TAG, "Unable to retrieve web page (ClientProtocolException:"+e.getMessage()+"): " + url);
	            e.getStackTrace();
	            return null;            	
	        } catch (UnsupportedEncodingException e) {
	            Log.e(TAG, "Unable to retrieve web page (UnsupportedEncodingException:"+e.getMessage()+"): " + url);
	            e.getStackTrace();
	            return null;
	        } catch (IllegalArgumentException e) {
	            Log.e(TAG, "Unable to retrieve web page (IllegalArgumentException:"+e.getMessage()+"): " + url);
	            e.getStackTrace();
	            return null;
	        } catch (IOException e) {
	            Log.e(TAG, "Unable to retrieve web page (IOException:"+e.getMessage()+"): " + url);
	            e.getStackTrace();
	            return null;
	        }
		}
		
		@Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);

	        if (category.equals("chat"))
	        	downloadDeploy();
	        else if (category.equals("deploy")) {
            	parseDeploy(result);
            	downloadKropka();
	        }
	        else if (category.equals("kropka"))
	        	downloadKropkaFull();
	        else if (category.equals("kropka_full"))
	        	downloadSk();
	        else if (category.equals("sk")) {
	            if (registeredNick)
	                downloadSecureKropka();
	            else {
	                // showCaptchaDialog();
	                String code = null;
	                downloadCheckCode(code);
	            }
	        }
	        else if (category.equals("secure_kropka"))
	        	downloadSecureLogin();
	        else if (category.equals("secure_login")) {
	            if (override)
	                downloadOverride();
	            else
	                downloadUo();	        	
	        }
	        else if (category.equals("override"))
	        	downloadUo();
	        else if (category.equals("check_code"))
	        	downloadUo();
	        else if (category.equals("uo")) {
	            parseUo(result);
	            authorizing = false;
	        }
	        else {
	        	Log.e(TAG, "Undefined category: "+category);
	        	authorizing = false;
	        }
	    }
		
		@Override
	    protected void onCancelled() {
	        super.onCancelled();
	        
	        authorizing = false;
	    }
    }
}
