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

package com.simplechatclient.android;

import java.io.IOException;
import java.io.InputStream;
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
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.core.Config;

public class ProfileRegisterFragment extends Fragment implements View.OnClickListener {

	private Context context;
	private View view;
	private HttpClient httpclient;
	private static final String AJAX_API = "http://czat.onet.pl/include/ajaxapi.xml.php3";
    private static final String TAG = "ProfileRegisterFragment";
	
	public static ProfileRegisterFragment newInstance() {
		ProfileRegisterFragment fragment = new ProfileRegisterFragment();
		return fragment;
	}

	public ProfileRegisterFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.profile_register_fragment, container, false);
		context = container.getContext();
		return view;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.id.buttonRegister:
				buttonRegister();
				break;
		}
	}

	@SuppressLint("DefaultLocale") private void buttonRegister()
	{		
		EditText editTextRegisterNick = (EditText)view.findViewById(R.id.editTextRegisterNick);
		EditText editTextRegisterPassword = (EditText)view.findViewById(R.id.editTextRegisterPassword);
		EditText editTextRegisterCode = (EditText)view.findViewById(R.id.editTextRegisterCode);
		
		String nick = editTextRegisterNick.getText().toString();
		String password = editTextRegisterPassword.getText().toString();
		String code = editTextRegisterCode.getText().toString();

		String[] params = {nick, password, code};		
		new RegisterUserTask().execute(params);
	}
	
	@SuppressLint("DefaultLocale") private class RegisterUserTask extends AsyncTask<String, Void, String> {
		private String nick;
		private String password;
		private String code;
	    //private ProgressDialog dialogProgress;

	    protected String doInBackground(String... urls) {
			try
			{
				//dialogProgress = ProgressDialog.show(context, getResources().getString(R.string.profile_register_progress_title), getResources().getString(R.string.profile_register_progress_text), true);
				
		    	nick = urls[0];
		    	password = urls[1];
		    	code = urls[2];

				String content = String.format("api_function=registerNick&params=a:3:{s:4:\"nick\";s:%d:\"%s\";s:4:\"pass\";s:%d:\"%s\";s:4:\"code\";s:%d:\"%s\";}", nick.length(), nick, password.length(), password, code.length(), code);
			
				HttpResponse httpResponse;
				
				HttpPost httpPost = new HttpPost(AJAX_API);
	            httpPost.setEntity(new StringEntity(content));
	            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
	            httpResponse = httpclient.execute(httpPost);
	            
				int statusKropka = httpResponse.getStatusLine().getStatusCode();
				if (statusKropka == 200) {
					HttpEntity httpEntity = httpResponse.getEntity();
                    return EntityUtils.toString(httpEntity);
				}
			} catch (ClientProtocolException e) {
		        Log.e(TAG, "Unable to retrieve web page (ClientProtocolException:"+e.getMessage()+"): " + AJAX_API);
		        e.getStackTrace();
		    } catch (UnsupportedEncodingException e) {
		        Log.e(TAG, "Unable to retrieve web page (UnsupportedEncodingException:"+e.getMessage()+"): " + AJAX_API);
		        e.getStackTrace();
		    } catch (IllegalArgumentException e) {
		        Log.e(TAG, "Unable to retrieve web page (IllegalArgumentException:"+e.getMessage()+"): " + AJAX_API);
		        e.getStackTrace();
		    } catch (IOException e) {
		        Log.e(TAG, "Unable to retrieve web page (IOException:"+e.getMessage()+"): " + AJAX_API);
		        e.getStackTrace();
		    }
						
			return null;
	    }
	    
		 // <?xml version="1.0" encoding="ISO-8859-2"?><root><status>-1</status><error err_code="0"  err_text="OK" ></error></root>
		 // <?xml version="1.0" encoding="ISO-8859-2"?><root><status>1</status><error err_code="0"  err_text="OK" ></error></root>
	    protected void onPostExecute(String data) {
	    	try {
	    		int status = 0;
	    		
	    		if (data != null)
	    		{
		            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		            Document document = builder.parse(new InputSource(new StringReader(data)));
		            document.getDocumentElement().normalize();
	
		            status = Integer.parseInt(document.getElementsByTagName("status").item(0).getTextContent());
	    		}
	            
	            //if (dialogProgress != null && dialogProgress.isShowing())
	            	//dialogProgress.dismiss();
	            
	            if (status == 1)
	            {
	            	Config current_config = new Config(context);
	            	current_config.addProfile(nick, password);
	            	
	            	Toast toast = Toast.makeText(context, getResources().getString(R.string.profile_register_successfull), Toast.LENGTH_SHORT);
	        		toast.show();
	        		
	        		Intent profileListIntent = new Intent(context, MainActivity.class);
	            	profileListIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            	profileListIntent.putExtra("tab", "1"); // profile list
	                startActivity(profileListIntent);
	            }
	            else
	            {
	            	String error_text = null;

	            	switch(status)
			        {
	            		case 0:
			            	error_text = getResources().getString(R.string.profile_register_error_0);
			                break;	            			
			            case -1:
			            	error_text = getResources().getString(R.string.profile_register_error_m1);
			                break;
			            case -2:
			            	error_text = getResources().getString(R.string.profile_register_error_m2);
			                break;
			            case -3:
			            	error_text = getResources().getString(R.string.profile_register_error_m3);
			                break;
			            case -4:
			            	error_text = getResources().getString(R.string.profile_register_error_m4);
			                break;
			            case -99:
			            	error_text = getResources().getString(R.string.profile_register_error_m99);
			                break;
			            case -101:
			            	error_text = getResources().getString(R.string.profile_register_error_m101);
			                break;
			            case -102:
			            	error_text = getResources().getString(R.string.profile_register_error_m102);
			                break;
			            case -104:
			            	error_text = getResources().getString(R.string.profile_register_error_m104);
			                break;
			            default:
			            	error_text = getResources().getString(R.string.profile_register_error)+String.valueOf(status);
			                break;
			        }
	            	
	            	Toast toast = Toast.makeText(context, error_text, Toast.LENGTH_SHORT);
	        		toast.show();
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
	}
	    
	@Override
	public void onStart() {
		super.onStart();

		Button buttonRegister = (Button)view.findViewById(R.id.buttonRegister);
		buttonRegister.setOnClickListener(this);
		
		new DownloadImageTask().execute("http://czat.onet.pl/myimg.gif");
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    protected Bitmap doInBackground(String... urls) {
	    	String url = urls[0];
			try
			{
				httpclient = new DefaultHttpClient();
				
				HttpResponse httpResponseKropka;
				HttpGet httpGetKropka = new HttpGet("http://kropka.onet.pl/_s/kropka/1?DV=czat%2Findex");
				httpResponseKropka = httpclient.execute(httpGetKropka);
	        
				int statusKropka = httpResponseKropka.getStatusLine().getStatusCode();
				if (statusKropka == 200) {
					HttpResponse httpResponseGif;
					HttpGet httpGetGif = new HttpGet(url);
					httpResponseGif = httpclient.execute(httpGetGif);
		        
					int statusGif = httpResponseGif.getStatusLine().getStatusCode();
					if (statusGif == 200) {
						HttpEntity entity = httpResponseGif.getEntity();
						BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
				        InputStream input = b_entity.getContent();

				        return BitmapFactory.decodeStream(input);				    
					}
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
		    }
			
			return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
	    }
	    
	    protected void onPostExecute(Bitmap bitmap) {
	    	if ((bitmap.getWidth() == 1) && (bitmap.getHeight() == 1))
	    	{
	    		new DownloadImageTask().execute("http://czat.onet.pl/myimg.gif");
	    		return; // empty image
	    	}
	    	
	    	ProgressBar progressBarRegisterCaptcha = (ProgressBar)view.findViewById(R.id.progressBarRegisterCaptcha);
	    	progressBarRegisterCaptcha.setVisibility(View.GONE);
	    	ImageView captcha = (ImageView)view.findViewById(R.id.imageViewRegisterCaptcha);
	        captcha.setVisibility(View.VISIBLE);
	        captcha.setImageBitmap(bitmap);
	    }
	}
}