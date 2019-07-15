package metrotransit.http;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import metrotransit.constant.MetroTransitConstant;



public class APIConnectionProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger("Core_APIConnectionProvider");

	public HttpClient proxySetup(boolean isProxyEnable, String proxyHost, String proxyPort) throws IOException {

		HttpClient client;

		if (MetroTransitConstant.ISSSLENABLED) {
			LOGGER.info("HTTPS RestClient creating API connection");
			client = createHttpsClient(isProxyEnable, proxyHost, proxyPort);
		} else {
			LOGGER.info("Http RestClient creating API connection");
			client = createHttpClient(isProxyEnable, proxyHost, proxyPort);
		}
		return client;

	}

	public HttpResponse get(String url, boolean isProxyEnable, String proxyHost, String proxyPort) throws IOException {
		HttpClient client = proxySetup(isProxyEnable, proxyHost, proxyPort);
		HttpGet getRequest = new HttpGet(url);
		return client.execute(getRequest);
	}

	 
	
	 
	
	private HttpClient createHttpsClient(boolean isProxyEanbled, String proxyHost, String proxyPort)
			throws IOException {
		SSLContext sslContext = null;
		try {
			sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					return true;
				}
			}).build();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
				NoopHostnameVerifier.INSTANCE);

		if (isProxyEanbled && proxyHost != null && proxyPort != null) {
			LOGGER.info("creating https client with proxy support..");
			HttpHost proxy = new HttpHost(proxyHost, Integer.parseInt(proxyPort));
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
			return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).setRoutePlanner(routePlanner)
					.build();

		}
		LOGGER.info("creating https client with system properties ..");
		return HttpClientBuilder.create().setSSLSocketFactory(sslConnectionSocketFactory).useSystemProperties().build();
	}

	public HttpClient createHttpClient(boolean isProxyEnable, String proxyHost, String proxyPort) throws IOException {

		if (isProxyEnable && proxyHost != null && proxyPort != null) {
			HttpHost proxy = new HttpHost(proxyHost, Integer.parseInt(proxyPort));
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
			return HttpClients.custom().setRoutePlanner(routePlanner).build();
		}
		return HttpClientBuilder.create().useSystemProperties().build();
	}	 
	
	 
	
	public HttpResponse postWithParameter(String url, Map<String, String> parameters, boolean isProxyEnable, String proxyHost, String proxyPort) throws IOException {

		HttpClient client = proxySetup(isProxyEnable, proxyHost, proxyPort); 
		LOGGER.info("POST Request reaching resource....");
		HttpPost postRequest = new HttpPost(url);
		List<BasicNameValuePair> postParams = new ArrayList<>();

		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			postParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		postRequest.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));

		return client.execute(postRequest);

	}
	
	
	public HttpResponse putWithParameter(String url, Map<String, String> parameters, boolean isProxyEnable, String proxyHost, String proxyPort) throws IOException {

		HttpClient client = proxySetup(isProxyEnable, proxyHost, proxyPort); 
		LOGGER.info("PUT Request reaching resource....");
		HttpPut putRequest = new HttpPut(url);
		List<BasicNameValuePair> putParams = new ArrayList<>();

		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			putParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		putRequest.setEntity(new UrlEncodedFormEntity(putParams, "UTF-8"));

		return client.execute(putRequest);

	}
}
