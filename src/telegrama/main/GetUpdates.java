package telegrama.main;

import java.io.InvalidObjectException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetUpdates {
	private final ConcurrentLinkedDeque<Update> receivedUpdates = new ConcurrentLinkedDeque<>();
	private volatile CloseableHttpClient httpclient;
	private int lastReceivedUpdate;

	public GetUpdates() {
		lastReceivedUpdate = 0;
		httpclient = HttpClientBuilder.create().setSSLHostnameVerifier(new NoopHostnameVerifier())
				.setConnectionTimeToLive(70, TimeUnit.SECONDS).setMaxConnTotal(100).build();
		ReaderThread reader = new ReaderThread();
		reader.start();
		HandlerThread handler = new HandlerThread();
		handler.start();

	}

	private class ReaderThread extends Thread {
		@Override
		public void run() {
			setPriority(Thread.MIN_PRIORITY);
			while (!Thread.interrupted()) {
				try {
					String url = Constants.BASEURL + Constants.TOKEN + "/getupdates";
					// http client
					HttpPost httpPost = new HttpPost(url);
					httpPost.addHeader("charset", StandardCharsets.UTF_8.name());

					try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
						HttpEntity ht = response.getEntity();
						BufferedHttpEntity buf = new BufferedHttpEntity(ht);
						String responseContent = EntityUtils.toString(buf, StandardCharsets.UTF_8);
						JSONObject jsonObject = new JSONObject(responseContent);
						if (!jsonObject.getBoolean(Constants.RESPONSEFIELDOK)) {
							System.err.println("Não foi possível receber atualização!");
						}
						JSONArray jsonArray = jsonObject.getJSONArray(Constants.RESPONSEFIELDRESULT);
						if (jsonArray.length() != 0) {
							for (int i = 0; i < jsonArray.length(); i++) {
								Update update = new Update(jsonArray.getJSONObject(i));
								if (update.getUpdateId() > lastReceivedUpdate) {
									lastReceivedUpdate = update.getUpdateId();
									receivedUpdates.addFirst(update);
								}
							}
							synchronized (receivedUpdates) {
								receivedUpdates.notifyAll();
							}
						} else {
							try {
								synchronized (this) {
									this.wait(500);
								}
							} catch (InterruptedException e) {
							}
						}
					} catch (InvalidObjectException | JSONException e) {
					}
				} catch (Exception global) {
					try {
						synchronized (this) {
							this.wait(500);
						}
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}

	private class HandlerThread extends Thread {
		@Override
		public void run() {
			setPriority(Thread.MIN_PRIORITY);
			while (!Thread.interrupted()) {
				try {
					Update update = receivedUpdates.pollLast();
					if (update == null) {
						synchronized (receivedUpdates) {
							try {
								receivedUpdates.wait();
							} catch (InterruptedException e) {
								continue;
							}
							update = receivedUpdates.pollLast();
							if (update == null) {
								continue;
							}
							SendMessage.sendMessage(update);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
