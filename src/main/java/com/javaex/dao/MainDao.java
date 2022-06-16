package com.javaex.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javaex.vo.MainVo;

@Repository
public class MainDao {
	@Autowired
	private SqlSession sqlSession;

	@Autowired
	private DataSource dataSource;

	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	/*
	 * private String driver = "oracle.jdbc.driver.OracleDriver"; private String url
	 * = "jdbc:oracle:thin:@localhost:1521:xe"; private String id = "webdb"; private
	 * String pw = "webdb";
	 */
	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			// Class.forName(driver);

			// 2. Connection 얻어오기
			// conn = DriverManager.getConnection(url, id, pw);
			// System.out.println("접속성공");
			conn = dataSource.getConnection();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	private void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// Guestbook 출력
	public List<MainVo> getMainList() {
		System.out.println("MainDao>getMainList");

		List<MainVo> mainList = sqlSession.selectList("guestbook.selectList");

		return mainList;
	}

	// Guestbook추가 => insert
	public int guestInsert(MainVo mainVo) {
		System.out.println("MainDao>getInsert");

		int count = sqlSession.insert("guestbook.guestInsert", mainVo);

		return count;
	}

	// Guestbook삭제 => delete
	public int guestDelete(int no, String password) {
		int count = -1;

		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행 --> 완성된 sql문을 가져와서 작성할것
			// SQL문 준비
			String query = "";
			query += " delete from guestbook ";
			query += " where no = ? ";
			query += " and password = ? ";

			// 바인딩 쿼리로 만들기
			pstmt = conn.prepareStatement(query); // 쿼리로 만들기
			pstmt.setInt(1, no);
			pstmt.setString(2, password);

			// 실행
			count = pstmt.executeUpdate();

			// 결과처리
			System.out.println("[" + count + " 건 삭제되었습니다.]");

		} catch (SQLException e) {
			System.out.println("error: " + e);
		}

		close();

		return count;
	}

	// Guestbook 1명 불러오기
	public MainVo getMainList(int nno) {
		System.out.println("list");

		MainVo mainVo = sqlSession.selectOne("guestbook.getMainList", nno);

		return mainVo;
	}

}