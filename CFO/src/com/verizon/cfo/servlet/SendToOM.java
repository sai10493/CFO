package com.verizon.cfo.servlet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.verizon.cfo.connection.ConnectionUtil;

public class SendToOM {

	private static final String targetURL = "http://192.168.1.19:8080/OrderManagement/rest/om/financeConnectionStatus/";

	public static void sendToOM() {

		int live, accno;
		try {

			Connection con = ConnectionUtil.getConnection();

			String query = "select account_number, flag from dlqtable";
			//System.out.println("after con");
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			//System.out.println("before str1");

			ResultSet rs = st.executeQuery(query);
			//System.out.println("Before while loop");
			while (rs.next()) {

				accno = rs.getInt("account_number");
				live = rs.getInt("flag");
				if (live == 0) {

					String sendURL = targetURL + accno + "/" + live;
					send(sendURL);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void send(String sendURL) {

		try {

			URL targetUrl = new URL(sendURL);
			//System.out.println("inside send()" + sendURL);
			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl
					.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("Content-Type",
					"application/json");

			if (httpConnection.getResponseCode() != 200) {

				throw new RuntimeException("Failed : HTTP error code : "

				+ httpConnection.getResponseCode());
			}

			httpConnection.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}
		

	}

}
