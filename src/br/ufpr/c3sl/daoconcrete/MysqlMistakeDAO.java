package br.ufpr.c3sl.daoconcrete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ufpr.c3sl.dao.MistakeDAO;
import br.ufpr.c3sl.daoFactory.MysqlDAOFactory;
import br.ufpr.c3sl.exception.UserException;
import br.ufpr.c3sl.model.Mistake;
import br.ufpr.c3sl.model.User;


public class MysqlMistakeDAO implements MistakeDAO{

	private static final String INSERT = "INSERT INTO mistakes " +
	"(object, exercise, learningObject, description, answer, correctAnswer, title, user_id, created_at) " +
	"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String FIND_BY_USER = "SELECT * FROM mistakes " +
			"WHERE user_id LIKE ? and learningObject LIKE ?";


	public int insert(Mistake mistake) throws UserException {
		Connection c = MysqlDAOFactory.createConnection();
		PreparedStatement pstmt;
		try {
			pstmt = c.prepareStatement(INSERT);
			pstmt.setBytes(1, mistake.getObject());
			pstmt.setString(2, mistake.getExercise());
			pstmt.setString(3, mistake.getLearningObject());
			pstmt.setString(4, mistake.getDescription());
			pstmt.setString(5, mistake.getAnswer());
			pstmt.setString(6, mistake.getCorrectAnswer());
			pstmt.setString(7, mistake.getTitle());
			pstmt.setInt(8, mistake.getUser().getId());
			pstmt.setTimestamp(9, mistake.getCreatedAt());

			int rowAfecteds = pstmt.executeUpdate();
			return rowAfecteds;
		} catch (SQLException e) {
			throw new UserException(e.getMessage());
		}
	}

	public List<Mistake> getAll(User user, String learningObject) {
		ArrayList<Mistake> list = new ArrayList<Mistake>();
		ResultSet rset;

		Connection c = MysqlDAOFactory.createConnection();
		PreparedStatement pstmt;
		try {
			pstmt = c.prepareStatement(FIND_BY_USER);
			pstmt.setInt(1, user.getId());
			pstmt.setString(2, learningObject);
			rset = pstmt.executeQuery();
			
			while (rset.next()){
				Mistake m = createMistake(rset);
				m.setUser(user);
				list.add(m);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list; 
	}

	private Mistake createMistake(ResultSet rset) throws SQLException {
		Mistake mistake = new Mistake();
		mistake.setId(rset.getInt("id"));
		mistake.setObject(rset.getBytes("object"));
		mistake.setExercise(rset.getString("exercise"));
		mistake.setLearningObject(rset.getString("learningObject"));
		mistake.setDescription(rset.getString("description"));
		mistake.setAnswer(rset.getString("answer"));
		mistake.setCorrectAnswer(rset.getString("correctAnswer"));
		mistake.setTitle(rset.getString("title"));
		mistake.setCreatedAt(rset.getTimestamp("created_at").getTime());

		return mistake;
	}
}