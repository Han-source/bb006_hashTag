package www.dream.com.common.dto;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;
import www.dream.com.framework.util.StringUtil;

@Data
public class Criteria {
	//몇 페이지의 몇 개
	
	private static final float PAGENATION_TOTAL = 10;
	
	private String searching; // 이 데이터는 화면에 나타는 검색어의 집합 ex)
	
	
	private int startPage, endPage; // 화면에 출력되는 첫페이지와 마지막 페이지
	private boolean prev, next; // 앞으로 가기 및 뒤로 가기
	private int pageNumber; //현재 쪽 번호
	private int amount; // 쪽 당 보여 줄 데이터 건수

	private long total; // 전체 데이터 건수
	
	public Criteria() {
		this.pageNumber = 1;
		this.amount = 10;
	}
	public void setTotal(long total) {
		this.total = total;
		calc();
	}
	//현재 페이지를 11로 가정하자
	private void calc() {
		endPage = (int) (Math.ceil(pageNumber / PAGENATION_TOTAL) * PAGENATION_TOTAL);
		startPage = endPage - (int)(PAGENATION_TOTAL - 1);
		int realEnd = (int)Math.ceil((float) total / amount); 
		if(endPage > realEnd) {
			endPage = realEnd;
		}
		prev = startPage > 1;
		next = endPage < realEnd;
		
	}
	public boolean hasSearching() {
		//trim : 문자열 공백제거
		return StringUtil.hasInfo(searching);
		
	}
	public String [] getSearchingHashtags() {
		return searching == null ? new String[] {} : searching.split(" ");
	}
	
	/**
	 * Criteria가 가지고 있는 정보를 UriComponentsBuilder에 추가해주기.
	 * @param builder
	 */
	public void appendQueryParam(UriComponentsBuilder builder) {
		 builder.queryParam("pageNumber", pageNumber)
				.queryParam("amount", amount)
				.queryParam("searching", searching);
		
				
	}
}
