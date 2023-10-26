package com.bookshop.admin.member.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.bookshop.member.vo.MemberVO;
@Repository("adminMemberDAO")
public class AdminMemberDAOImpl implements AdminMemberDAO{
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public ArrayList<MemberVO> listMember(HashMap condMap) throws DataAccessException {
		ArrayList<MemberVO> memberList = (ArrayList)sqlSession.selectList("mapper.admin.member.listMember",condMap);
		return memberList;
	}
	@Override
	public MemberVO memberDetail(String member_id) throws DataAccessException {
		MemberVO memberVO = sqlSession.selectOne("mapper.admin.member.memberDetail",member_id);
		return memberVO;
	}
	@Override
	public void modifyMemberInfo(HashMap memberMap) throws DataAccessException {
		sqlSession.update("mapper.admin.member.modifyMemberInfo",memberMap);
	}
}
