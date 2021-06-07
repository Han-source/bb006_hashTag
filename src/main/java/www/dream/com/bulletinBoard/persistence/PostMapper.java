package www.dream.com.bulletinBoard.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import www.dream.com.bulletinBoard.model.BoardVO;
import www.dream.com.bulletinBoard.model.PostVO;
import www.dream.com.common.dto.Criteria;

public interface PostMapper {
	//LRCUD
	public long getTotalCount(@Param("boardId") int boardId);
	
	public long getSearchTotalCount(@Param("boardId") int boardId, @Param("cri") Criteria cri);
	
	/* mapper 함수의 인자 개수가 여러개 일때는 필수적으로 @Param을 넣어야 한다.
	 * 이를 실수하지 않기 위하여 한개여도 그냥 명시적으로 넣어 주세요
	 * 초기화면 띄울때 활용 : 최종적으로는 개인화 서비스에서 초기화면으로 활용하기.
	 * */
	public List<PostVO> getList(@Param("boardId") int boardId,@Param("cri") Criteria cri);

	public List<PostVO> getListByHashTag(@Param("boardId") int boardId,@Param("cri") Criteria cri);
	/** id 값으로 Post 객체 조회*/
	public PostVO findPostById(String id);
	
	public int insert(@Param("board") BoardVO board, @Param("post") PostVO post);
	
	/** 게시글 수정 처리*/
	public int updatePost(PostVO post);
	
	/** id 값으로 Post 객체 삭제*/
	public int deletePostById(String id);
	
	
}
