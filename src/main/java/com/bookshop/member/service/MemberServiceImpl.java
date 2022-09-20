package com.bookshop.member.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookshop.member.dao.MemberDAO;
import com.bookshop.member.vo.MemberVO;
@Service("memberService")
public class MemberServiceImpl implements MemberService{
	@Autowired
	private MemberDAO memberDAO;
	@Autowired
	private MemberVO memberVO;
	
	@Override
	public MemberVO login(Map<String, String> loginMap) throws Exception {
		memberVO = memberDAO.login(loginMap);
		return memberVO;
	}
	@Override
	public void addMember(MemberVO memberVO) throws Exception {
		memberDAO.insertNewMember(memberVO);
	}
	@Override
	public String overlapped(String id) throws Exception {
		String overlappedID = memberDAO.selectOverlappedID(id);
		return overlappedID;
	}
}
