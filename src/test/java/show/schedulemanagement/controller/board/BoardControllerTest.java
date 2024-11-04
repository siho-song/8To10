package show.schedulemanagement.controller.board;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.dto.board.BoardSaveRequest;
import show.schedulemanagement.dto.board.BoardUpdateRequest;
import show.schedulemanagement.dto.board.SearchCond;
import show.schedulemanagement.dto.board.SortCondition;
import show.schedulemanagement.provider.TokenProvider;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@DisplayName("게시글 CRUD")
class BoardControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    @BeforeEach
    void setAuthenticationToken(){
        token = tokenProvider.generateAccessToken("normal@example.com"); // 토큰 생성
    }

    @Test
    @DisplayName("게시글 등록 엔드포인트")
    void boardAddTest() throws Exception {
        BoardSaveRequest dto = new BoardSaveRequest("게시판 테스트용", "테스트......................");

        mockMvc.perform(MockMvcRequestBuilders.post("/community/board/add")
                        .header("Authorization","Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("게시글 페이지 조회 엔트포인트")
    void boardPageSearch() throws Exception {
        String keyword = "Nick";
        int pageNum = 0;
        int pageSize = 10;
        String searchCond = SearchCond.WRITER.name();
        String sortCond = SortCondition.LIKE.name();

        mockMvc.perform(MockMvcRequestBuilders.get("/community/board")
                        .header("Authorization","Bearer " + token)
                        .param("keyword", keyword)
                        .param("pageNum", String.valueOf(pageNum))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("searchCond", searchCond)
                        .param("sortCond", sortCond))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 단건 조회 엔드포인트")
    void boardSearch() throws Exception {
        Long boardId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/community/board/{id}", boardId)
                        .header("Authorization","Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 삭제 엔드포인트")
    void deleteBoard() throws Exception {
        Long boardId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/community/board/{id}", boardId)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 업데이트 엔드포인트")
    void updateBoard() throws Exception {
        BoardUpdateRequest request = new BoardUpdateRequest();
        request.setId(1L);
        request.setContents("게시글 수정 테스트 내용");
        request.setTitle("게시글 수정 테스트 제목");
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.put("/community/board")
                .header("Authorization","Bearer " + token)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 좋아요 추가 엔드포인트")
    void addHeart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/community/board/{id}/heart", 3L)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("게시글 좋아요 삭제 엔드포인트")
    void deleteHeart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/community/board/{id}/heart", 1L)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 스크랩 등록 엔드포인트")
    void addScrap() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/community/board/{id}/scrap", 2L)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("게시글 스크랩 삭제 엔드포인트")
    void deleteScrap() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/community/board/{id}/scrap", 1L)
                        .header("Authorization","Bearer " + token)
                ).andExpect(status().isOk());
    }
}