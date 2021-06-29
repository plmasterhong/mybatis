package ksmart39.mybatis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ksmart39.mybatis.dao.MemberMapper;
import ksmart39.mybatis.domain.Member;

@Service
@Transactional
public class MemberService {
	
	private static final Logger log = LoggerFactory.getLogger(MemberService.class);

	
	/**
	 * 필드 주입방식 (DI)
	 * MemberMapper memberMapper = new MemberMapper();
	 * setter 메소드 memberMapper
	 * 생성자메소드 memberMapper
	 */
	@Autowired
	private MemberMapper memberMapper;

	@PostConstruct
	public void memberServiceInit() {
		log.info("========================================");
		log.info("MemberService 객체 생성");
		log.info("========================================");
		//System.out.println("====================================");
		//System.out.println("MemberService 객체 생성");
		//System.out.println("====================================");
	}
	
	// 페이징 (로그인이력조회)
	public Map<String, Object> getLoginHistoryList(int currentPage){
		
		//보여줄 행의 갯수
		int rowPerPage = 10;
		
		//table에서 보여질 행의 시작점 초기화
		int rowStart = 0;
		
		//페이지 번호 초기화
		int pageStartNum = 1;
		int pageEndNum = 10;
				
		//rowStart = 페이지알고리즘(현재페이지 - 1) * 보여줄 행의 갯수
		rowStart = (currentPage - 1) * rowPerPage;

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("rowStart", rowStart);
		paramMap.put("rowPerPage", rowPerPage);
		
		List<Map<String, Object>> loginHistoryList = memberMapper.getLoginHistoryList(paramMap);
		
		//테이블(로그인이력)의 총 행의 갯수
		double rowCount = memberMapper.getLoginHistoryCount();
		
		// 마지막페이지 (전체 행의 갯수에서 보여줄 행의 갯수를 나눈 몫에 올림)
		int lastPage = (int) Math.ceil(rowCount/rowPerPage); 
		
		if(currentPage > 6) {
			pageStartNum = currentPage - 5;
			pageEndNum = currentPage + 4;
			
			if(pageEndNum >= lastPage) {
				pageStartNum = lastPage - 9;
				pageEndNum = lastPage;
			}
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("loginHistoryList", loginHistoryList);
		resultMap.put("lastPage", lastPage);
		resultMap.put("pageStartNum", pageStartNum);
		resultMap.put("pageEndNum", pageEndNum);
		
		return resultMap;
	}
	
	public Map<String, Object> loginMember(String memberId, String memberPw) {
		//로그인 여부
		boolean loginCheck = false;
		
		//로그인 결과 
		Map<String, Object> memberInfoMap = new HashMap<String, Object>();
		
		//로그인 처리
		Member member = memberMapper.getMemberInfoById(memberId);
		
		if(member != null && memberPw.equals(member.getMemberPw())) {
			loginCheck = true;
			memberInfoMap.put("loginMember", member);
		}
		
		memberInfoMap.put("loginCheck", loginCheck);
		
		return memberInfoMap;
	}
	
	public boolean removeMember(String memberId, String memberPw) {
		//삭제여부
		boolean removeCheck = false;
		
		//비밀번호 맞으면 삭제 
		Member member = memberMapper.getMemberInfoById(memberId);
		
		if(member != null && memberPw.equals(member.getMemberPw())) {
			//삭제프로세스
			//판매자
			if("2".equals(member.getMemberLevel())) {
				//1. 주문, 상품	
				memberMapper.removeOrderBySellerId(memberId);
				memberMapper.removeGoodsById(memberId);
			}

			//구매자
			if("3".equals(member.getMemberLevel())) {
				//1. 주문	
				memberMapper.removeOrderById(memberId);				
			}
			
			memberMapper.removeLoginById(memberId);
			memberMapper.removeMemberById(memberId);
			
			removeCheck = true;
		}
		
		return removeCheck;
	}
	
	
	public int modifyMember(Member member) {
		
		return memberMapper.modifyMember(member);
	}
	
	public Member getMemberInfoById(String memberId) {
		
		return memberMapper.getMemberInfoById(memberId);
	}
	
	
	public List<Member> getMemberList(Map<String, Object> paramMap){
		
		List<Member> memberList = memberMapper.getMemberList(paramMap);
		
		return memberList;
	}
	
	
	public int addMember(Member member) {
		
		int result = memberMapper.addMember(member);
		
		return result;
	}
	
	
	
	
	
	
	
	
	
}
