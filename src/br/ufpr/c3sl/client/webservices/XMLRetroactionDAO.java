package br.ufpr.c3sl.client.webservices;

import java.net.HttpURLConnection;

import javax.swing.JOptionPane;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import br.ufpr.c3sl.connection.HTTPClientFactory;
import br.ufpr.c3sl.model.Retroaction;
import br.ufpr.c3sl.session.Session;

public class XMLRetroactionDAO {

	public static Retroaction save(Retroaction retroaction){
		Retroaction r = null;
		try {
		    String _url = HOST.URL + "/"
		    				+ Session.getCurrentUser().getId()
		    				+ "/mistakes/"
		    				+ retroaction.getMistake().getId()
		    				+ "/retroactions.xml";
			
			HttpURLConnection conn = HTTPClientFactory.getURLConnection(_url);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/xml");
			
			
			Serializer serializer = new Persister();
			serializer.write(retroaction,  conn.getOutputStream());

			r = serializer.read(Retroaction.class, conn.getInputStream());
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "" +
					"Erro na comunicação com o servidor! O software pode não \n" +
					"Funcionar corretamente");
			e.printStackTrace();
		}
		return r;
	}
}