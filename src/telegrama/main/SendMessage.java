package telegrama.main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class SendMessage {
	public static void sendMessage(Update update){
		JSONObject jsonObject = new JSONObject();
        jsonObject.put("chat_id", update.getMessage().getChat().getId());
        
		boolean isCommand = update.getMessage().getEntities().get(0).getType().equals("bot_command") ? true : false ;
		String text = "Comando inválido.\nLista de comandos:\n\n/cafe - Quem compra o café.\n/pao - Quem compra o pão.";
		if(isCommand){
			String command = update.getMessage().getText().split(" ")[0];
			ICommand commandAction ;
			switch (command) {
			case "/pao":
				commandAction = new PaoCommand();
				text = commandAction.doAction(update);
				break;

			default:
				break;
			}
		}
		jsonObject.put("text", text);
		CloseableHttpClient httpclient;
		httpclient = HttpClientBuilder.create().setSSLHostnameVerifier(new NoopHostnameVerifier())
				.setConnectionTimeToLive(70, TimeUnit.SECONDS).setMaxConnTotal(100).build();
		try {
			HttpPost httppost = new HttpPost(Constants.BASEURL+Constants.TOKEN+ "/sendmessage");
			httppost.addHeader("charset", StandardCharsets.UTF_8.name());
            httppost.setEntity(new StringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON));

			try (CloseableHttpResponse response = httpclient.execute(httppost)) {
				HttpEntity ht = response.getEntity();
				BufferedHttpEntity buf = new BufferedHttpEntity(ht);
				String responseContent = EntityUtils.toString(buf, StandardCharsets.UTF_8);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
