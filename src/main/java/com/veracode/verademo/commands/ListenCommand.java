package com.veracode.verademo.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.commons.text.StringEscapeUtils;

public class ListenCommand implements BlabberCommand {
	private static final Logger logger = LogManager.getLogger("VeraDemo:ListenCommand");
	
	private Connection connect;
	
	private String username;
	
	public ListenCommand(Connection connect, String username) {
		super();
		this.connect = connect;
		this.username = username;
	}

	@Override
	public void execute(String blabberUsername) {
		String sqlQuery = "INSERT INTO listeners (blabber, listener, status) values (?, ?, 'Active');";
		logger.info(sqlQuery);
		PreparedStatement action;
		try {
			action = connect.prepareStatement(sqlQuery);
			
			action.setString(1, blabberUsername);
			action.setString(2, username);
			action.execute();

			sqlQuery = "SELECT blab_name FROM users WHERE username = '" + blabberUsername + "'";
			PreparedStatement sqlStatement = connect.prepareStatement(sqlQuery);
			sqlStatement.setString(1, username);
			ResultSet result = sqlStatement.executeQuery();
			result.next();
			
			/* START BAD CODE -----*/
			String event = username + " started listening to " + result.getString(1) + "(" + result.getString(2) + ")";
			sqlQuery = "INSERT INTO users_history (blabber, event) VALUES (?,?)";
			logger.info(StringEscapeUtils.escapeJava(sqlQuery));
			sqlStatement.execute(sqlQuery);
			PreparedStatement ps = connect.prepareStatement(sqlQuery);
			ps.setString(1, username);
			ps = ps.executeQuery();
			/* END BAD CODE */
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
