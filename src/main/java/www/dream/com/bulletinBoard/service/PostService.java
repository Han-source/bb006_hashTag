package www.dream.com.bulletinBoard.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import www.dream.com.bulletinBoard.model.BoardVO;
import www.dream.com.bulletinBoard.model.PostVO;
import www.dream.com.bulletinBoard.persistence.PostMapper;
import www.dream.com.common.dto.Criteria;
import www.dream.com.framework.langPosAnalyzer.PosAnalyzer;
import www.dream.com.framework.util.StringUtil;
import www.dream.com.hashTag.model.HashtagVO;
import www.dream.com.hashTag.persistence.HashTagMapper;

@Service
public class PostService {

	@Autowired
	private PostMapper postMapper;

	@Autowired
	private HashTagMapper hashTagMapper;

	// LRCUD
	public long getSearchTotalCount(int boardId, Criteria cri) {
		if(cri.hasSearching()) {
			return postMapper.getSearchTotalCount(boardId, cri);
		}else {
			return postMapper.getTotalCount(boardId);
		}
	}
	/*
	 * mapper 함수의 인자 개수가 여러개 일때는 필수적으로 @Param을 넣어야 한다. 이를 실수하지 않기 위하여 한개여도 그냥 명시적으로
	 * 넣어 주세요
	 */
	

	public List<PostVO> getListByHashTag(int boardId, Criteria cri) {
		if(cri.hasSearching()) {
			return postMapper.getListByHashTag(boardId, cri);			
		}else {
			return postMapper.getList(boardId, cri);
		}
	}

	/** id 값으로 Post 객체 조회 */
	public PostVO findPostById(String id) {
		return postMapper.findPostById(id);
	}

	/**
	 * 
	 * @param board
	 * @param post  화면에서 사용자가 입력한 변수
	 * @return
	 */
	public int insert(BoardVO board, PostVO post) {
		int affectedRows = postMapper.insert(board, post);
		// mapOccur : 단어의 출현빈도구하기
		Map<String, Integer> mapOccur = PosAnalyzer.getHashTags(post);
		// 출현빈도를 바탕으로 단어만 추출
		Set<String> setHashTag = mapOccur.keySet();
		// 단어가 비어있지 않다면
		createHashTagAndMapping(post, mapOccur, setHashTag);
		return affectedRows;
	}

	private void createHashTagAndMapping(PostVO post, Map<String, Integer> mapOccur, Set<String> setHashTag) {
		if (setHashTag.isEmpty()) {
			// 게시글에서 단어가 나타나지 않았으면 처리할 것이 없다.
			return;

		}
		// 기존 사전에 등록되어있는 hashTag(단어)를찾음. setExisting에 출현빈도를 기록시킴
		Set<HashtagVO> setExisting = hashTagMapper.findExisting(setHashTag);
		// 기존에 있는 것들과 짝 지어 주어야한다.
		for (HashtagVO hashtag : setExisting) {
			hashtag.setOccurCnt(mapOccur.get(hashtag.getHashtag()));
		}

		// setHashTag에 남은 것들은 신규 처리해야할 것들.
		for (HashtagVO hashtag : setExisting) {
			setHashTag.remove(hashtag.getHashtag());
		}

		Set<String> setNewHashTag = setHashTag;
		if (!setNewHashTag.isEmpty()) {
			// 새로운 단어를 HashTag 테이블에 등록해주기.
			// 한번에 여러개의 단어를 만들기
			int[] ids = StringUtil.convertCommaSepString2IntArr(hashTagMapper.getIds(setNewHashTag.size()));
			int idx = 0;
			// 새롭게 생성한 단어셋 : setHT
			Set<HashtagVO> setHT = new HashSet<>();
			// 새로운 단어집합에서 id를 꺼내서 id의 몇번째꺼와 연결해주고 목록에 한번에 집어넣는다
			for (String hashTag : setNewHashTag) {
				HashtagVO newHashtag = new HashtagVO(ids[idx++], hashTag);
				// mapOccur에 데이터 등록
				newHashtag.setOccurCnt(mapOccur.get(hashTag));
				setHT.add(newHashtag);
			}
			hashTagMapper.createHashTag(setHT);
			// 새 단어를 단어집에 넣었으니 기존 단어가 되는 것
			setExisting.addAll(setHT);

		}
		if (!setExisting.isEmpty()) {
			// 한번에 insert를 해주는 고성능의 기능
			// 기존 단어와 짝짓기
			hashTagMapper.insertMapBetweenPost(setExisting, post.getId());
		}

	}

	/** 게시글 수정 처리 */
	public boolean updatePost(PostVO post) {
		return postMapper.updatePost(post) == 1;
	}

	/** id 값으로 Post 객체 삭제 */

	public boolean deletePostById(String id) {

		return postMapper.deletePostById(id) == 1;
	}

}