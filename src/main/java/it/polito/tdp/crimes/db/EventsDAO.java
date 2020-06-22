package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Event;


public class EventsDAO {
	
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
	
	public List<String> listaCategorie(){
		String sql= "select distinct offense_category_id " + 
				"from events " + 
				"order by offense_category_id " ;
		
		List<String> categorie = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				categorie.add(res.getString("offense_category_id"));
			}
			
			conn.close();
			return categorie ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> listaAnni(){
		String sql = "select distinct year(reported_date) " + 
				"from events " + 
				"order by year(reported_date)";
		List<Integer> anni = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				anni.add(res.getInt("year(reported_date)"));
			}
			
			conn.close();
			return anni ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> vertici(String categoria, Integer anno){
		String sql ="select distinct offense_type_id " + 
				"from events " + 
				"where offense_category_id = ? " + 
				"and year(reported_date)=? " + 
				"order by offense_type_id";
		
		List<String> vertici = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, categoria);
			st.setInt(2, anno);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				vertici.add(res.getString("offense_type_id"));
			}
			
			conn.close();
			return vertici ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
		
	}

	public Integer getPeso(String s1, String s2) {
		String sql = "select  count(distinct e1.district_id) as c " + 
				"from events as e1, events as e2 " + 
				"where e1.`offense_type_id`=? " + 
				"and e2.`offense_type_id` =? " + 
				"and e1.`district_id` = e2.`district_id` " ;
		Integer peso = null;
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, s1);
			st.setString(2, s2);
			
			ResultSet res = st.executeQuery() ;
			
			
			if(res.next()) {
				peso = res.getInt("c");	
			}
			conn.close();
			return peso;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
		
	}

}
