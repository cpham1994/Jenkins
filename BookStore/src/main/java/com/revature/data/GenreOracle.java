package com.revature.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.beans.Book;
import com.revature.beans.Genre;
import com.revature.util.ConnectionUtil;
import com.revature.util.LogUtil;

public class GenreOracle implements GenreDAO{
	private static ConnectionUtil cu = ConnectionUtil.getInstance();
	private static Logger log = Logger.getLogger(GenreOracle.class);
	
	@Override
	public Integer addGenre(Genre g) {
		Integer key = 0;
		log.trace("Inserting a genre into the database.");
		Connection conn = cu.getConnection();
		try {
			conn.setAutoCommit(false);
			String sql = "insert into genre(genre) values(?)";
			String [] keys = {"id"};
			PreparedStatement stmt = conn.prepareStatement(sql, keys);
			stmt.setString(1, g.getGenre());
			int number = stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			
			if(number!=1) {
				log.warn("insert failed");
				conn.rollback();
			} else {
				log.trace("Insert successful");
				if(rs.next()) {
					key = rs.getInt(1);
					g.setId(key);
					conn.commit();
				} else {
					log.warn("genre not created.");
					conn.rollback();
				}
			}
		} catch(SQLException e) {
			LogUtil.rollback(e, conn, GenreOracle.class);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				LogUtil.logException(e, GenreOracle.class);
			}
		}
		
		return key;
	}

	@Override
	public Genre getGenre(Integer id) {
		log.trace("Attempting to find genre with id = "+id);
		Genre genre = null;
		String sql = "select id, genre from genre where id =?";
		try(Connection conn = cu.getConnection()){
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				log.trace("Found genre: "+rs.getInt("id")+" "+rs.getString("genre"));
				genre = new Genre();
				genre.setId(rs.getInt("id"));
				genre.setGenre(rs.getString("genre"));
			} else {
				log.trace("Genre not found.");
			}
		} catch(SQLException e) {
			LogUtil.logException(e, GenreOracle.class);
		}
		log.trace("Returning genre: "+genre);
		return genre;
	}

	@Override
	public Genre getGenreByGenre(String genre) {
		log.trace("Attempting to find genre with genre = "+genre);
		Genre g = null;
		String sql = "select id, genre from genre where genre =?";
		try(Connection conn = cu.getConnection()){
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1,genre);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				log.trace("Found genre: "+rs.getInt("id")+" "+rs.getString("genre"));
				g = new Genre();
				g.setId(rs.getInt("id"));
				g.setGenre(rs.getString("genre"));
			} else {
				log.trace("Genre not found.");
			}
		} catch(SQLException e) {
			LogUtil.logException(e, GenreOracle.class);
		}
		log.trace("Returning genre: "+g);
		return g;
	}

	@Override
    public Set<Genre> getGenres() {
        log.trace("Attempting to find all genres");
        Set<Genre> genreSet = new HashSet<Genre>();
        String sql = "select * from genre";
        try(Connection conn = cu.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                log.trace("Found genre: "+rs.getInt("id")+" "+rs.getString("genre"));
                Genre genre = new Genre();
                genre.setId(rs.getInt("id"));
                genre.setGenre(rs.getString("genre"));
                genreSet.add(genre);
            }
        } catch(SQLException e) {
            LogUtil.logException(e, GenreOracle.class);
        }
        log.trace("Returning genres: "+genreSet);
        return genreSet;
    }

    @Override
    public Set<Genre> getGenresByBook(Book b) {
        log.trace("Attempting to find genres by book: "+b);
        Set<Genre> genreSet = new HashSet<Genre>();
        String sql = "select * from genre join book_genre on "
        		+ "genre.id = book_genre.genre_id where book_id = ?";
        
        try(Connection conn = cu.getConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1,b.getId());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                log.trace("Found genre: "+rs.getInt("id")+" "+rs.getString("genre"));
                Genre genre = new Genre();
                genre.setId(rs.getInt("id"));
                genre.setGenre(rs.getString("genre"));
                genreSet.add(genre);
            }
        } catch(SQLException e) {
            LogUtil.logException(e, GenreOracle.class);
        }
        log.trace("Returning genre: "+genreSet);
        return genreSet;
    }

	@Override
	public void updateGenre(Genre g) {
		log.trace("Updating Genre to "+g);
		Connection conn = cu.getConnection();
		try {
			// JDBC automatically commits data.
			conn.setAutoCommit(false);
			String sql = "update genre set genre=? where id=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(2, g.getId());
			stmt.setString(1, g.getGenre());
			int rs = stmt.executeUpdate();
			if(rs!=1) {
				log.warn("Genre update failed.");
				conn.rollback();
			}
			conn.commit();
		} catch(SQLException e) {
			LogUtil.rollback(e, conn, GenreOracle.class);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				LogUtil.logException(e, GenreOracle.class);
			}
		}
		log.trace("Update ending.");
	}

	@Override
	public void deleteGenre(Genre g) {
		log.trace("Deleting Genre to "+g);
		Connection conn = cu.getConnection();
		try {
			// JDBC automatically commits data.
			conn.setAutoCommit(false);
			String sql = "delete from genre where id=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, g.getId());
			int rs = stmt.executeUpdate();
			if(rs!=1) {
				log.warn("Genre delete failed.");
				conn.rollback();
			}
			conn.commit();
		} catch(SQLException e) {
			LogUtil.rollback(e, conn, GenreOracle.class);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				LogUtil.logException(e, GenreOracle.class);
			}
		}
		log.trace("Delete ending.");
	}

}
