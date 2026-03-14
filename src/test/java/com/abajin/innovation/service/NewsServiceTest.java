package com.abajin.innovation.service;

import com.abajin.innovation.entity.News;
import com.abajin.innovation.entity.User;
import com.abajin.innovation.enums.ApprovalStatus;
import com.abajin.innovation.enums.NewsStatus;
import com.abajin.innovation.mapper.NewsMapper;
import com.abajin.innovation.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 新闻服务测试
 */
@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock
    private NewsMapper newsMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private NewsService newsService;

    private User author;
    private News news;

    @BeforeEach
    void setUp() {
        author = new User();
        author.setId(1L);
        author.setUsername("author");
        author.setRealName("作者");

        news = new News();
        news.setId(1L);
        news.setTitle("测试新闻");
        news.setContent("新闻内容");
        news.setAuthorId(1L);
        news.setAuthorName("作者");
        news.setStatus(NewsStatus.DRAFT.name());
        news.setApprovalStatus(ApprovalStatus.PENDING.name());
        news.setViewCount(0);
        news.setLikeCount(0);
    }

    @Test
    void createNews_withValidData_returnsNews() {
        // Arrange
        News newNews = new News();
        newNews.setTitle("新新闻");
        newNews.setContent("内容");

        when(userMapper.selectById(1L)).thenReturn(author);
        when(newsMapper.insert(any(News.class))).thenReturn(1);

        // Act
        News result = newsService.createNews(newNews, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getAuthorId());
        assertEquals("作者", result.getAuthorName());
        assertEquals(NewsStatus.DRAFT.name(), result.getStatus());
        assertEquals(0, result.getViewCount());
        assertEquals(0, result.getLikeCount());
        verify(newsMapper).insert(any(News.class));
    }

    @Test
    void createNews_withNonExistentAuthor_throwsException() {
        // Arrange
        when(userMapper.selectById(999L)).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> newsService.createNews(news, 999L));
        assertEquals("作者不存在", exception.getMessage());
    }

    @Test
    void submitNews_withDraftStatus_submitsSuccessfully() {
        // Arrange
        when(newsMapper.selectById(1L)).thenReturn(news);
        when(newsMapper.update(any(News.class))).thenReturn(1);

        // Act
        News result = newsService.submitNews(1L, 1L);

        // Assert
        assertEquals(NewsStatus.PENDING.name(), result.getStatus());
        assertEquals(ApprovalStatus.PENDING.name(), result.getApprovalStatus());
    }

    @Test
    void submitNews_withNonDraftStatus_throwsException() {
        // Arrange
        news.setStatus(NewsStatus.PENDING.name());
        when(newsMapper.selectById(1L)).thenReturn(news);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> newsService.submitNews(1L, 1L));
        assertEquals("只能提交草稿状态的新闻", exception.getMessage());
    }

    @Test
    void submitNews_withUnauthorizedUser_throwsException() {
        // Arrange
        when(newsMapper.selectById(1L)).thenReturn(news);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> newsService.submitNews(1L, 999L));
        assertEquals("无权提交此新闻", exception.getMessage());
    }

    @Test
    void reviewNews_withPendingStatus_approvesSuccessfully() {
        // Arrange
        news.setStatus(NewsStatus.PENDING.name());
        when(newsMapper.selectById(1L)).thenReturn(news);
        when(newsMapper.update(any(News.class))).thenReturn(1);

        // Act
        News result = newsService.reviewNews(1L, ApprovalStatus.APPROVED.name(), "审核通过", 2L);

        // Assert
        assertEquals(NewsStatus.PUBLISHED.name(), result.getStatus());
        assertEquals(ApprovalStatus.APPROVED.name(), result.getApprovalStatus());
        assertNotNull(result.getPublishTime());
    }

    @Test
    void reviewNews_withPendingStatus_rejectsSuccessfully() {
        // Arrange
        news.setStatus(NewsStatus.PENDING.name());
        when(newsMapper.selectById(1L)).thenReturn(news);
        when(newsMapper.update(any(News.class))).thenReturn(1);

        // Act
        News result = newsService.reviewNews(1L, ApprovalStatus.REJECTED.name(), "不符合要求", 2L);

        // Assert
        assertEquals(NewsStatus.REJECTED.name(), result.getStatus());
        assertEquals(ApprovalStatus.REJECTED.name(), result.getApprovalStatus());
    }

    @Test
    void reviewNews_withNonPendingStatus_throwsException() {
        // Arrange
        news.setStatus(NewsStatus.PUBLISHED.name());
        when(newsMapper.selectById(1L)).thenReturn(news);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> newsService.reviewNews(1L, ApprovalStatus.APPROVED.name(), "", 2L));
        assertEquals("只能审核待审核状态的新闻", exception.getMessage());
    }

    @Test
    void reviewNews_withInvalidStatus_throwsException() {
        // Arrange
        news.setStatus(NewsStatus.PENDING.name());
        when(newsMapper.selectById(1L)).thenReturn(news);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> newsService.reviewNews(1L, "INVALID", "", 2L));
        assertEquals("审批状态无效", exception.getMessage());
    }

    @Test
    void getNewsById_withPublishedNews_incrementsViewCount() {
        // Arrange
        news.setStatus(NewsStatus.PUBLISHED.name());
        when(newsMapper.selectById(1L)).thenReturn(news);
        when(newsMapper.incrementViewCount(1L)).thenReturn(1);

        // Act
        News result = newsService.getNewsById(1L);

        // Assert
        verify(newsMapper).incrementViewCount(1L);
    }

    @Test
    void getNewsById_withDraftNews_doesNotIncrementViewCount() {
        // Arrange
        news.setStatus(NewsStatus.DRAFT.name());
        when(newsMapper.selectById(1L)).thenReturn(news);

        // Act
        News result = newsService.getNewsById(1L);

        // Assert
        verify(newsMapper, never()).incrementViewCount(any());
    }

    @Test
    void getNews_withPagination_returnsNewsList() {
        // Arrange
        List<News> newsList = Collections.singletonList(news);
        when(newsMapper.selectPage(0, 10, null, null, null, null, null)).thenReturn(newsList);

        // Act
        List<News> result = newsService.getNews(1, 10, null, null, null, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void countNews_returnsCount() {
        // Arrange
        when(newsMapper.count(null, null, null, null, null)).thenReturn(50);

        // Act
        int count = newsService.countNews(null, null, null, null, null);

        // Assert
        assertEquals(50, count);
    }

    @Test
    void updateNews_withDraftStatus_updatesSuccessfully() {
        // Arrange
        when(newsMapper.selectById(1L)).thenReturn(news);
        when(newsMapper.update(any(News.class))).thenReturn(1);

        News updateNews = new News();
        updateNews.setTitle("更新标题");

        // Act
        News result = newsService.updateNews(1L, updateNews, 1L);

        // Assert
        verify(newsMapper).update(any(News.class));
    }

    @Test
    void updateNews_withNonDraftStatus_throwsException() {
        // Arrange
        news.setStatus(NewsStatus.PENDING.name());
        when(newsMapper.selectById(1L)).thenReturn(news);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> newsService.updateNews(1L, news, 1L));
        assertEquals("只能更新草稿状态的新闻", exception.getMessage());
    }

    @Test
    void updateNews_withUnauthorizedUser_throwsException() {
        // Arrange
        when(newsMapper.selectById(1L)).thenReturn(news);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> newsService.updateNews(1L, news, 999L));
        assertEquals("无权更新此新闻", exception.getMessage());
    }

    @Test
    void deleteNews_withDraftStatus_deletesSuccessfully() {
        // Arrange
        when(newsMapper.selectById(1L)).thenReturn(news);
        when(newsMapper.deleteById(1L)).thenReturn(1);

        // Act
        newsService.deleteNews(1L, 1L);

        // Assert
        verify(newsMapper).deleteById(1L);
    }

    @Test
    void deleteNews_withNonDraftStatus_throwsException() {
        // Arrange
        news.setStatus(NewsStatus.PUBLISHED.name());
        when(newsMapper.selectById(1L)).thenReturn(news);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> newsService.deleteNews(1L, 1L));
        assertEquals("只能删除草稿状态的新闻", exception.getMessage());
    }

    @Test
    void deleteNews_withUnauthorizedUser_throwsException() {
        // Arrange
        when(newsMapper.selectById(1L)).thenReturn(news);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> newsService.deleteNews(1L, 999L));
        assertEquals("无权删除此新闻", exception.getMessage());
    }
}
