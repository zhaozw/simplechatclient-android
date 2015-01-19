/*
 * Simple Chat Client
 *
 *   Copyright (C) 2014 Piotr Łuczko <piotr.luczko@gmail.com>
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

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.core.Messages;
import com.core.Network;
import com.core.Settings;
import com.models.Channels;

public class OnetAuth {
    private static final String AJAX_API = "http://czat.onet.pl/include/ajaxapi.xml.php3";
    private static final String TAG = "OnetAuth";

    private boolean registeredNick;
    private boolean override = false;
    private String nick;
    private String password;
    private String version;
    private HttpClient httpclient;

    private static OnetAuth instance = new OnetAuth();
    public static synchronized OnetAuth getInstance() { return instance; }
    
    private AuthHandler authHandler;
    private String current_category;
    private String current_result;
    
    public OnetAuth()
    {
        httpclient = new DefaultHttpClient();
        authHandler = new AuthHandler();
    }

    public void authorize() {
        if (Settings.getInstance().get("authorizing") == "true") {
            Log.w(TAG, "Already authorizing");
            return;
        }
        
        nick = Settings.getInstance().get("nick");
        password = Settings.getInstance().get("password");

        if (!nick.contains("~"))
        	registeredNick = true;
        
        override = true;
        Settings.getInstance().set("authorizing", "true");
        current_category = null;
        current_result = null;

        downloadChat();
    }

    private class AuthHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            //Bundle bundle = msg.getData();
            
            //String current_category = msg.getData().getString("current_category");
            //String current_result = msg.getData().getString("current_result");
            
            authkernel();
        }
        
        public void authkernel()
        {
        	if (current_category == null)
        		downloadChat();
        	else
        	{
            	if (current_category.equals("chat"))
                    downloadDeploy();
                else if (current_category.equals("deploy")) {
                    parseDeploy(current_result);
                    downloadKropka1();
                }
                else if (current_category.equals("kropka_1"))
                    downloadKropka1Full();
                else if (current_category.equals("kropka_1_full"))
                    downloadKropka5Full();
                else if (current_category.equals("kropka_5_full"))
                    downloadSk();
                else if (current_category.equals("sk")) {
                    if (registeredNick)
                        downloadSecureLogin();
                    else {
                        // showCaptchaDialog();
                    	// TODO crash null pointer exception
                        String code = "empty";
                        downloadCheckCode(code);
                    }
                }
                else if (current_category.equals("secure_login")) {
                    if (override)
                        downloadOverride();
                    else
                        downloadUo();
                }
                else if (current_category.equals("override"))
                    downloadUo();
                else if (current_category.equals("check_code"))
                    downloadUo();
                else if (current_category.equals("uo")) {
                    parseUo(current_result);
                    Settings.getInstance().set("authorizing", "false");
                }
                else {
                    Log.e(TAG, "Undefined category: "+current_category);
                    Settings.getInstance().set("authorizing", "false");
                }
        	}
        }
    }

    private void downloadChat() {
        String url = "http://czat.onet.pl/chat.html";
        String content = "ch=&n=&p=&category=0";
        String category = "chat";

        new HttpDownload(url, content, category).start();
    }

    private void downloadDeploy() {
        String url = "http://czat.onet.pl/_s/deployOnetCzat.js";
        String content = null;
        String category = "deploy";

        new HttpDownload(url, content, category).start();
    }

    private void parseDeploy(String data)
    {
        version = parseVersion(data);
        version = String.format("1.1(%s - R)", version);        
    }
    
    private void downloadKropka1() {
        String url = "http://kropka.onet.pl/_s/kropka/1?DV=czat/applet";
        String content = null;
        String category = "kropka_1";
        
        new HttpDownload(url, content, category).start();
    }

    private void downloadKropka1Full() {
        String url = "http://kropka.onet.pl/_s/kropka/1?DV=czat/applet/FULL";
        String content = null;
        String category = "kropka_1_full";
        
        new HttpDownload(url, content, category).start();
    }

    private void downloadKropka5Full() {
        String url = "http://kropka.onet.pl/_s/kropka/5?DV=czat/applet/FULL";
        String content = null;
        String category = "kropka_5_full";
        
        new HttpDownload(url, content, category).start();
    }

    private void downloadSk() {
        String url = "http://czat.onet.pl/sk.gif";
        String content = null;
        String category = "sk";
        
        new HttpDownload(url, content, category).start();
    }

    @SuppressLint("DefaultLocale")
	private void downloadCheckCode(String code) {
        String url = AJAX_API;
        String content = String.format("api_function=checkCode&params=a:1:{s:4:\"code\";s:%d:\"%s\";}", code.length(), code);
        String category = "check_code";

        new HttpDownload(url, content, category).start();
    }

    private void downloadSecureLogin() {
        String url = "https://secure.onet.pl/mlogin.html";
        String content = String.format("r=&url=&login=%s&haslo=%s&app_id=20&ssl=1&ok=1", nick, password);
        String category = "secure_login";

        new HttpDownload(url, content, category).start();
    }

    @SuppressLint("DefaultLocale")
	private void downloadOverride() {
        String url = AJAX_API;
        String content = String.format("api_function=userOverride&params=a:1:{s:4:\"nick\";s:%d:\"%s\";}", nick.length(), nick);
        String category = "override";

        new HttpDownload(url, content, category).start();
    }

    @SuppressLint("DefaultLocale")
	private void downloadUo() {
        int isRegistered = (registeredNick ? 0 : 1);

        String url = AJAX_API;
        String content = String.format("api_function=getUoKey&params=a:3:{s:4:\"nick\";s:%d:\"%s\";s:8:\"tempNick\";i:%d;s:7:\"version\";s:%d:\"%s\";}", nick.length(), nick, isRegistered, version.length(), version);
        String category = "uo";

        new HttpDownload(url, content, category).start();
    }

    private String parseVersion(String data) {
        if (data != null) {
            if (data.contains("OnetCzatLoader")) {
                String strFind1 = "signed-OnetCzatLoader-";
                String strFind2 = ".jar";
                int pos1 = data.indexOf(strFind1) + strFind1.length();
                int pos2 = data.indexOf(strFind2, pos1);
                String ver = data.substring(pos1, pos2);

                if ((ver.length() > 0) && (ver.length() < 20))
                    return ver;
            }
        }

        return "20120711-1544b";
    }

    // <?xml version="1.0" encoding="ISO-8859-2"?><root><uoKey>LY9j2sXwio0G_yo3PdpukDL8iZJGHXKs</uoKey><zuoUsername>~Succubi_test</zuoUsername><error err_code="TRUE" err_text="wartoĹ›Ä‡ prawdziwa" ></error></root>
    // <?xml version="1.0" encoding="ISO-8859-2"?><root><error err_code="-2" err_text="U.ytkownik nie zalogowany" ></error></root>
    private void parseUo(String data) {
        try {
        	Log.i(TAG, "pareUO: "+data);

        	if (data == null)
        	{
                Messages.getInstance().showMessage(Channels.STATUS, "Błąd autoryzacji [Brak odpowiedzi od serwera]");
        		return;
        	}
        	
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(data)));
            document.getDocumentElement().normalize();

            Node error = document.getElementsByTagName("error").item(0);
            String err_code = error.getAttributes().getNamedItem("err_code").getNodeValue();
            String err_text = error.getAttributes().getNamedItem("err_text").getNodeValue();

            if (err_code.equalsIgnoreCase("true")) {
                String UOKey = document.getElementsByTagName("uoKey").item(0).getTextContent();
                String nick = document.getElementsByTagName("zuoUsername").item(0).getTextContent();

                if (Network.getInstance().isConnected())
                {
                    Settings.getInstance().set("nick", nick);
                    Settings.getInstance().set("uo_key", UOKey);
                    
                    Network.getInstance().send(String.format("AUTHKEY"));
                }
            } else {
                Log.e(TAG, String.format("Authentication error [%s]", err_text));
                Messages.getInstance().showMessage(Channels.STATUS, String.format("Błąd autoryzacji [%s]", err_text));
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

    public class HttpDownload extends Thread {
    	private String url;
    	private String content;
    	private String category;
    	
    	public HttpDownload(String _url, String _content, String _category) {
	       url = _url;
	       content = _content;
	       category = _category;
	    }

	    public void run() {
	    	Log.i(TAG, "HttpDownload url: "+url);
	    	Log.i(TAG, "HttpDownload content: "+content);
	    	
	    	current_category = category;
	        current_result = null;

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
	                current_result = EntityUtils.toString(httpEntity);
	            }
	        } catch (ClientProtocolException e) {
	            Log.e(TAG, "Unable to retrieve web page (ClientProtocolException:"+e.getMessage()+"): " + url);
	            e.getStackTrace();
	        } catch (UnsupportedEncodingException e) {
	            Log.e(TAG, "Unable to retrieve web page (UnsupportedEncodingException:"+e.getMessage()+"): " + url);
	            e.getStackTrace();
	        } catch (IllegalArgumentException e) {
	            Log.e(TAG, "Unable to retrieve web page (IllegalArgumentException:"+e.getMessage()+"): " + url);
	            e.getStackTrace();
	        } catch (IOException e) {
	            Log.e(TAG, "Unable to retrieve web page (IOException:"+e.getMessage()+"): " + url);
	            e.getStackTrace();
	        } finally {
	        	Message msg =  new Message();
                Bundle bundle = new Bundle();

                bundle.putString("current_category", current_category);
                bundle.putString("current_result", current_result);
                msg.setData(bundle);

                authHandler.sendMessage(msg);
	        }
	    }
    }
}
