package com.bookshop.member.dao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.bookshop.member.vo.MemberVO;
@Repository("memberDAO")
public class MemberDAOImpl implements MemberDAO{
	@Autowired
	private SqlSession session;
	@Override
	public MemberVO login(Map<String, String> loginMap) throws DataAccessException {
		return session.selectOne("mapper.member.login",loginMap);
	}
	@Override
	public void insertNewMember(MemberVO memberVO) throws DataAccessException {
		session.insert("mapper.member.insertNewMember", memberVO);
	}
	@Override
	public String selectOverlappedID(String id) throws DataAccessException {
		return session.selectOne("mapper.member.selectOverlappedID",id);
	}
}
