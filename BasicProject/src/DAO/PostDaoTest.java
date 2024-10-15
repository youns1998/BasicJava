package DAO;
import DAO.PostDao;
import VO.PostVo;

import java.util.List;

public class PostDaoTest {
    public static void main(String[] args) {
        PostDao postDAO = new PostDao();
      
        // 게시글 추가 테스트
        PostVo newPost = new PostVo();
        newPost.setUser_id("xodn");
        newPost.setCategory_id(101);  // 이 값이 CATEGORY 테이블에 존재해야 합니다.
        newPost.setTitle("첫 번째 게시글");
        newPost.setContent("이것은 첫 번째 게시글의 내용입니다.");
        newPost.setPrice(10000);
        newPost.setCondition("새로운");

        int insertResult = postDAO.insertPost(newPost);
        System.out.println("게시글 추가 결과: " + (insertResult > 0 ? "성공" : "실패"));

        // 게시글 전체 조회 테스트
        List<PostVo> posts = postDAO.getAllPosts();
        System.out.println("전체 게시글:");
        for (PostVo post : posts) {
            System.out.println(post.getPost_id() + ": " + post.getTitle());
        }

        // 게시글 상세 조회 테스트
        if (!posts.isEmpty()) {
            PostVo postDetail = postDAO.getPost(posts.get(0).getPost_id());
            System.out.println("게시글 상세 조회:");
            System.out.println("제목: " + postDetail.getTitle());
            System.out.println("내용: " + postDetail.getContent());
        }

        // 게시글 수정 테스트
        if (!posts.isEmpty()) {
            PostVo postToUpdate = posts.get(0);
            postToUpdate.setTitle("수정된 제목");
            postToUpdate.setContent("수정된 내용입니다.");
            int updateResult = postDAO.updatePost(postToUpdate);
            System.out.println("게시글 수정 결과: " + (updateResult > 0 ? "성공" : "실패"));
        }

        // 게시글 삭제 테스트
//        if (!posts.isEmpty()) {
//            int deleteResult = postDAO.deletePost(posts.get(0).getPost_id());
//            System.out.println("게시글 삭제 결과: " + (deleteResult > 0 ? "성공" : "실패"));
//        }
    }
}