package com.bookshop.common.base;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop.goods.vo.ImageFileVO;

public abstract class BaseController {
	//파일업로드 저장소
	private static final String CURR_IMAGE_REPO_PATH = "C:\\shopping\\file_repo";
	
	protected List<ImageFileVO> upload(MultipartRequest multipartRequest) throws Exception{
		List<ImageFileVO> fileList = new ArrayList<ImageFileVO>();
		Iterator<String> fileNames = multipartRequest.getFileNames();
		while(fileNames.hasNext()) {
			ImageFileVO imageFileVO = new ImageFileVO();
			String fileName = fileNames.next();
			imageFileVO.setFileType(fileName);
			//String을 MultiparFile 타입으로 리턴 
			MultipartFile mFile = multipartRequest.getFile(fileName);
			//originalfileName을 리턴
			String originalFileName=mFile.getOriginalFilename();
			imageFileVO.setFileName(originalFileName);
			fileList.add(imageFileVO);
			
			File file = new File(CURR_IMAGE_REPO_PATH+"\\"+fileName);
			if(mFile.getSize()!=0) {
				if(!file.exists()) {
					if(file.getParentFile().mkdirs()) {
						file.createNewFile();
					}
				}
				mFile.transferTo(new File(CURR_IMAGE_REPO_PATH+"\\"+"temp"+"\\"+originalFileName));
			}
		}
		
		return fileList;
	}
	//저자가 사용자가 직접 url을 치고 들어올거란 생각을 안해서 이렇게 작성함
	//없는 url을 치고 올 경우 예외처리를 해주어야 함
	@RequestMapping(value="/*.do", method = {RequestMethod.POST,RequestMethod.GET})
	protected ModelAndView viewForm(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		return mav;
	}
	
	protected String calcSearchPeriod(String fixedSearchPeriod) {
		String beginDate = null;
		String endDate = null;
		String endYear = null;
		String endMonth = null;
		String endDay = null;
		String beginYear = null;
		String beginMonth = null;
		String beginDay = null;
		//빈 자리수는 0으로 채워줌
		DecimalFormat df = new DecimalFormat("00");
		Calendar cal = Calendar.getInstance();
		//현재 날짜까지
		endYear = Integer.toString(cal.get(Calendar.YEAR));
		endMonth = df.format(cal.get(Calendar.MONTH)+1);
		endDay = df.format(cal.get(Calendar.DATE));
		endDate = endYear+"-"+endMonth+"-"+endDay;
		
		//현재날짜에서 원하는 이전 날짜 구하기
		if(fixedSearchPeriod == null) {
			cal.add(cal.MONTH, -4);
		}else if(fixedSearchPeriod.equals("one_week")) {
			cal.add(Calendar.DAY_OF_YEAR, -7);
		}else if(fixedSearchPeriod.equals("two_week")) {
			cal.add(Calendar.DAY_OF_YEAR, -14);
		}else if(fixedSearchPeriod.equals("one_month")) {
			cal.add(cal.MONTH, -1);
		}else if(fixedSearchPeriod.equals("two_month")) {
			cal.add(cal.MONTH, -2);
		}else if(fixedSearchPeriod.equals("three_month")) {
			cal.add(cal.MONTH, -3);
		}else if(fixedSearchPeriod.equals("four_month")) {
			cal.add(cal.MONTH, -4);
		}
		//당일날짜(today)
		//if조건을 거쳐서 나오면 해당 월 계산
		beginYear = Integer.toString(cal.get(Calendar.YEAR));
		beginMonth = df.format(cal.get(Calendar.MONTH)+1);
		beginDay = df.format(cal.get(Calendar.DATE));
		beginDate = beginYear + "-" + beginMonth + "-" + beginDay;
		
			
		return beginDate+","+endDate;
	}
	
	protected String calcInitPeriod() {
		String date = null;
		String beginYear = null;
		String beginMonth = null;
		String beginDay = null;
		String beginDate = null;
		
		String endYear = null;
		String endMonth = null;
		String endDay = null;
		String endDate = null;
		
		DecimalFormat df = new DecimalFormat("00");
		Calendar cal = Calendar.getInstance();
		
		beginYear = Integer.toString(cal.get(Calendar.YEAR));
		beginMonth = df.format(cal.get(Calendar.MONTH)-3);
		beginDay = df.format(cal.get(Calendar.DATE));
		beginDate = beginYear+"-"+beginMonth+"-"+beginDay;
		
		endYear = Integer.toString(cal.get(Calendar.YEAR));
		endMonth = df.format(cal.get(Calendar.MONTH)+1);
		endDay = df.format(cal.get(Calendar.DATE));
		endDate = endYear+"-"+endMonth+"-"+endDay;
		
		return beginDate+","+endDate;
	}
}
