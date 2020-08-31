package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<String> getReati(){
		String sql = "SELECT distinct e.offense_category_id AS id " + 
				"FROM EVENTS e " ;
		List<String> reati = new ArrayList<String>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				reati.add(res.getString("id"));
			}
			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return reati;
	}
	
	public List<Integer> getMesi(){
		String sql = "SELECT distinct MONTH(e.reported_date) AS mese " + 
				"FROM EVENTS e " ;
		List<Integer> mesi = new ArrayList<Integer>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				mesi.add(res.getInt("mese"));
			}
			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return mesi;
	}
	
	public List<String> getVertici(String categoria, Integer mese){
		String sql = "SELECT DISTINCT e.offense_type_id AS vertex " + 
				"FROM EVENTS e " + 
				"WHERE MONTH(e.reported_date) = ? " + 
				"AND e.offense_category_id = ? " ;
		List<String> vertici = new ArrayList<String>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			st.setString(2, categoria);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				vertici.add(res.getString("vertex"));
			}
			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return vertici;
	}
	
	public List<Adiacenza> getAdiacenze(String categoria, Integer mese){
		String sql = "SELECT e1.offense_type_id AS id1, e2.offense_type_id AS id2, COUNT(e1.neighborhood_id) AS peso " + 
				"FROM EVENTS e1, EVENTS e2 " + 
				"WHERE e1.offense_type_id > e2.offense_type_id " + 
				"AND e1.offense_category_id = ? " + 
				"AND e2.offense_category_id = ? " + 
				"AND MONTH(e1.reported_date) = ? " + 
				"AND MONTH(e2.reported_date) = ? " + 
				"AND e1.neighborhood_id = e2.neighborhood_id " + 
				"GROUP BY id1, id2 " ;
		List<Adiacenza> adiacenze = new ArrayList<Adiacenza>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, categoria);
			st.setString(2, categoria);
			st.setInt(3, mese);
			st.setInt(4, mese);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				adiacenze.add(new Adiacenza(res.getString("id1"), res.getString("id2"), res.getDouble("peso")));
			}
			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return adiacenze;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
