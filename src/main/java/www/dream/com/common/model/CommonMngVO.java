package www.dream.com.common.model;

import java.util.Date;

import lombok.Data;

/**
 * 공통 관리 정보
 * 
 * @author "Wook"
 *
 */
@Data
public abstract class CommonMngVO {
	private Date registrationDate; // 등록시점
	private Date updateDate; // 수정시점
	
	
	@Override
	public String toString() {
		return "등록일=" + registrationDate + ", 수정일=" + updateDate;
	}
	
	
}