package metrotransit.http;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import metrotransit.constant.MetroTransitConstant;

@Service
public class APIConnectionProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger("Core_APIConnectionProvider");

	/**
	 * @param isProxyEnable
	 * @param proxyHost
	 * @param proxyPort
	 * @return
	 * @throws IOException
	 * 
	 * This method to setup proxy if any as per parameters.
	 */
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

	/**
	 * @param url
	 * @param isProxyEnable
	 * @param proxyHost
	 * @param proxyPort
	 * @return
	 * @throws IOException
	 * 
	 * This method to call GET api as per input parameters.
	 */
	public HttpResponse get(String url, boolean isProxyEnable, String proxyHost, String proxyPort) throws IOException {
		HttpClient client = proxySetup(isProxyEnable, proxyHost, proxyPort);
		HttpGet getRequest = new HttpGet(url);
		return client.execute(getRequest);
	}

	/**
	 * @param isProxyEanbled
	 * @param proxyHost
	 * @param proxyPort
	 * @return
	 * @throws IOException
	 * 
	 * This method create HTTPS client as per input parameters.
	 */
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

	/**
	 * @param isProxyEnable
	 * @param proxyHost
	 * @param proxyPort
	 * @return
	 * @throws IOException
	 * 
	 * This method to create HTTP client as per input parameters.
	 */
	public HttpClient createHttpClient(boolean isProxyEnable, String proxyHost, String proxyPort) throws IOException {

		if (isProxyEnable && proxyHost != null && proxyPort != null) {
			HttpHost proxy = new HttpHost(proxyHost, Integer.parseInt(proxyPort));
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
			return HttpClients.custom().setRoutePlanner(routePlanner).build();
		}
		return HttpClientBuilder.create().useSystemProperties().build();
	}

}
