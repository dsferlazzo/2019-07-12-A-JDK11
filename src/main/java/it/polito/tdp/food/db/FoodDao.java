package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.food.model.Collegamento;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	/**
	 * data una quantita di porzioni, la funzione ritorna tutti i foods con almeno quel numero di porzioni
	 * @param nPorzioni
	 * @return
	 */
	public List<Food> getFoodsByPortions(int nPorzioni){
		String sql = "SELECT f.* "
				+ "FROM food f, `portion` p "
				+ "WHERE p.food_code=f.food_code "
				+ "GROUP BY f.food_code, f.display_name "
				+ "HAVING COUNT(*)>?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, nPorzioni);
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Collegamento> getCollegamenti(int nPorzioni){
		String sql = "WITH q1 "
				+ "AS "
				+ "	( "
				+ "		SELECT f.food_code AS fc, f.display_name AS dn "
				+ "		FROM food f, `portion` p "
				+ "		WHERE p.food_code=f.food_code "
				+ "		GROUP BY f.food_code, f.display_name "
				+ "		HAVING COUNT(*)>? "
				+ "	) "
				+ "SELECT f1.fc AS fc1, f1.dn AS dn1, f2.fc AS fc2, f2.dn AS dn2, AVG(c.condiment_calories) AS n "
				+ "FROM q1 f1, q1 f2, food_condiment fc1, food_condiment fc2, condiment c "
				+ "WHERE f1.fc = fc1.food_code AND f2.fc = fc2.food_code AND fc1.condiment_code=fc2.condiment_code AND "
				+ "	fc1.condiment_code=c.condiment_code AND f1.fc>f2.fc "
				+ "GROUP BY f1.fc, f1.dn, f2.fc, f2.dn" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, nPorzioni);
			
			List<Collegamento> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Food f1 = new Food(res.getInt("fc1"), res.getString("dn1"));
					Food f2 = new Food(res.getInt("fc2"), res.getString("dn2"));
					Collegamento c = new Collegamento(f1, f2, res.getDouble("n"));
					list.add(c);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
}
