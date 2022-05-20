package com.prgrms.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.board.controller.dto.PostRequest;
import com.prgrms.board.controller.dto.PostUpdateRequest;
import com.prgrms.board.service.PostService;
import com.prgrms.board.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
class PostApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Post를 저장한다.")
    void savePostTest() throws Exception {
        // Given
        Long userId = userService.registerUser("머쓱이", 25, "soccer");
        PostRequest postRequest = new PostRequest("This is title", "This is content", userId);

        // When // Then
        mockMvc.perform(post("/posts/v1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("save-post",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목(최대 100 글자)"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("본문(최대 5000 글자)"),
                                fieldWithPath("userId").type(JsonFieldType.NUMBER).description("게시글을 생성한  User의 ID")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 게시글의 ID")
                        )
                ));
    }

    @Test
    @DisplayName("잘못된 입력 값으로 인한 Post 저장 실패")
    void saveInvalidValueTest() throws Exception {
        // Given
        Long userId = userService.registerUser("머쓱이", 25, "soccer");
        PostRequest invalidPostRequest = new PostRequest(null, null, userId);

        // When // Then
        mockMvc.perform(post("/posts/v1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(invalidPostRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("전체 Post를 조회한다.")
    void findAllPostTest() throws Exception {
        // Given
        Long userId = userService.registerUser("머쓱이", 20, "Programming");
        postService.addPost(userId, "title", "content");

        // When // Then
        mockMvc.perform(get("/posts/v1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("find-posts",
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").optional().description("페이지 번호(0부터 시작)"),
                                parameterWithName("size").optional().description("페이지당 가져올 Post 개수(기본값 : 20)")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseFields(
                                fieldWithPath("content[]").type(JsonFieldType.ARRAY).description("게시글 리스트"),
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("content[].content").type(JsonFieldType.STRING).description("게시글 본문"),
                                fieldWithPath("content[].user").type(JsonFieldType.OBJECT).description("게시글을 작성한 User"),
                                fieldWithPath("content[].user.id").type(JsonFieldType.NUMBER).description("게시글을 작성한 User의 ID"),
                                fieldWithPath("content[].user.name").type(JsonFieldType.STRING).description("게시글을 작성한 User의 이름"),
                                fieldWithPath("content[].user.age").type(JsonFieldType.NUMBER).description("게시글을 작성한 User의 나이"),
                                fieldWithPath("content[].user.hobby").type(JsonFieldType.STRING).description("게시글을 작성한 User의 취미"),
                                fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("페이징 관련 정보"),
                                fieldWithPath("pageable.sort").type(JsonFieldType.OBJECT).description("페이징 관련 정보"),
                                fieldWithPath("pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("페이징 관련 정보"),
                                fieldWithPath("pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("페이징 관련 정보"),
                                fieldWithPath("pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("페이징 관련 정보"),
                                fieldWithPath("pageable.offset").type(JsonFieldType.NUMBER).description("페이징 관련 정보"),
                                fieldWithPath("pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이징 관련 정보"),
                                fieldWithPath("pageable.pageSize").type(JsonFieldType.NUMBER).description("페이징 관련 정보"),
                                fieldWithPath("pageable.paged").type(JsonFieldType.BOOLEAN).description("페이징 관련 정보"),
                                fieldWithPath("pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이징 관련 정보"),
                                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("페이징 관련 정보"),
                                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("페이징 관련 정보"),
                                fieldWithPath("last").type(JsonFieldType.BOOLEAN).description("페이징 관련 정보"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이징 관련 정보"),
                                fieldWithPath("number").type(JsonFieldType.NUMBER).description("페이징 관련 정보"),
                                fieldWithPath("sort").type(JsonFieldType.OBJECT).description("페이징 관련 정보"),
                                fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("페이징 관련 정보"),
                                fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("페이징 관련 정보"),
                                fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("페이징 관련 정보"),
                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER).description("페이징 관련 정보"),
                                fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("페이징 관련 정보"),
                                fieldWithPath("empty").type(JsonFieldType.BOOLEAN).description("페이징 관련 정보")
                        )
                ));

    }

    @Test
    @DisplayName("ID로 Post를 단건 조회한다.")
    void findByIdTest() throws Exception {
        // Given
        Long userId = userService.registerUser("머쓱이", 20, "Programming");
        Long postId = postService.addPost(userId, "TIL", "Java is...");

        // When // Then
        mockMvc.perform(get("/posts/v1/{postId}", postId)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("find-post",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("조회할 Post ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 본문"),
                                fieldWithPath("user").type(JsonFieldType.OBJECT).description("게시글 작성 User 정보"),
                                fieldWithPath("user.id").type(JsonFieldType.NUMBER).description("게시글 작성 User ID"),
                                fieldWithPath("user.name").type(JsonFieldType.STRING).description("게시글 작성 User 이름"),
                                fieldWithPath("user.age").type(JsonFieldType.NUMBER).description("게시글 작성 User 나이"),
                                fieldWithPath("user.hobby").type(JsonFieldType.STRING).description("게시글 작성 User 취미(없는 경우 Null)")
                        )
                    )
                );
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회시 404를 응답한다.")
    void findByInvalidIdTest() throws Exception {
        // Given
        Long userId = userService.registerUser("머쓱이", 20, "Programming");
        postService.addPost(userId, "TIL", "Java is...");
        Long invalidId = -1L;

        // When // Then
        mockMvc.perform(get("/posts/v1/{postId}", invalidId)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("Post를 업데이트 한다.")
    void updateTest() throws Exception {
        // Given
        Long userId = userService.registerUser("머쓱이", 20, "Programming");
        Long postId = postService.addPost(userId, "TIL", "Java is...");
        String updateTitle = "update title";
        String updateContent = "update content";
        PostUpdateRequest updateRequest = new PostUpdateRequest(updateTitle, updateContent);

        // When // Then
        mockMvc.perform(post("/posts/v1/{postId}", postId)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-post",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("postId").description("업데이트할 Post ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description(MediaType.APPLICATION_JSON_VALUE),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 본문")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("업데이트된 게시글의 ID")
                        )
                    )
                );
    }

    @Test
    @DisplayName("존재하지 않는 ID로 인한 업데이트 실패")
    void updateInvalidIdTest() throws Exception {
        // Given
        Long userId = userService.registerUser("머쓱이", 20, "Programming");
        postService.addPost(userId, "TIL", "Java is...");
        PostUpdateRequest updateRequest = new PostUpdateRequest("update title", "update content");
        Long invalidId = -1L;

        // When // Then
        mockMvc.perform(post("/posts/v1/{postId}", invalidId)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("잘못된 입력 값으로 인한 Post 업데이트 실패")
    void updateInvalidValueTest() throws Exception {
        // Given
        Long userId = userService.registerUser("머쓱이", 20, "Programming");
        Long postId = postService.addPost(userId, "TIL", "Java is...");
        PostUpdateRequest updateRequest = new PostUpdateRequest(null, null);

        // When // Then
        mockMvc.perform(post("/posts/v1/{postId}", postId)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}