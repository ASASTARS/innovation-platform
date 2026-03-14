package com.abajin.innovation.common;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 分页结果测试
 */
class PageResultTest {

    @Test
    void defaultConstructor_createsEmptyPageResult() {
        // Act
        PageResult<String> pageResult = new PageResult<>();

        // Assert
        assertNull(pageResult.getPageNum());
        assertNull(pageResult.getPageSize());
        assertNull(pageResult.getTotal());
        assertNull(pageResult.getTotalPages());
        assertNull(pageResult.getList());
    }

    @Test
    void parameterizedConstructor_createsPageResultWithValues() {
        // Arrange
        List<String> data = new ArrayList<>();
        data.add("item1");
        data.add("item2");
        data.add("item3");

        // Act
        PageResult<String> pageResult = new PageResult<>(1, 10, 25L, data);

        // Assert
        assertEquals(1, pageResult.getPageNum());
        assertEquals(10, pageResult.getPageSize());
        assertEquals(25L, pageResult.getTotal());
        assertEquals(3, pageResult.getTotalPages());
        assertEquals(data, pageResult.getList());
    }

    @Test
    void of_createsPageResultWithCalculatedPages() {
        // Arrange
        List<String> data = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            data.add("item" + i);
        }

        // Act
        PageResult<String> pageResult = PageResult.of(2, 5, 12L, data);

        // Assert
        assertEquals(2, pageResult.getPageNum());
        assertEquals(5, pageResult.getPageSize());
        assertEquals(12L, pageResult.getTotal());
        assertEquals(3, pageResult.getTotalPages());
        assertEquals(data, pageResult.getList());
    }

    @Test
    void of_withEmptyList_createsEmptyPageResult() {
        // Act
        PageResult<String> pageResult = PageResult.of(1, 10, 0L, Collections.emptyList());

        // Assert
        assertEquals(1, pageResult.getPageNum());
        assertEquals(10, pageResult.getPageSize());
        assertEquals(0L, pageResult.getTotal());
        assertEquals(0, pageResult.getTotalPages());
        assertTrue(pageResult.getList().isEmpty());
    }

    @Test
    void of_withExactPageSize_createsCorrectPages() {
        // Arrange
        List<String> data = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            data.add(String.valueOf(i));
        }

        // Act
        PageResult<String> pageResult = PageResult.of(1, 10, 20L, data);

        // Assert
        assertEquals(2, pageResult.getTotalPages());
    }

    @Test
    void of_withRemainder_createsCorrectPages() {
        // Arrange
        List<String> data = new ArrayList<>();
        data.add("1");
        data.add("2");
        data.add("3");

        // Act
        PageResult<String> pageResult = PageResult.of(1, 5, 13L, data);

        // Assert
        assertEquals(3, pageResult.getTotalPages());
    }

    @Test
    void of_withSinglePage_createsOnePage() {
        // Arrange
        List<String> data = new ArrayList<>();
        data.add("item");

        // Act
        PageResult<String> pageResult = PageResult.of(1, 10, 1L, data);

        // Assert
        assertEquals(1, pageResult.getTotalPages());
    }

    @Test
    void setters_updateValuesCorrectly() {
        // Arrange
        PageResult<String> pageResult = new PageResult<>();
        List<String> data = new ArrayList<>();
        data.add("item");

        // Act
        pageResult.setPageNum(2);
        pageResult.setPageSize(20);
        pageResult.setTotal(100L);
        pageResult.setTotalPages(5);
        pageResult.setList(data);

        // Assert
        assertEquals(2, pageResult.getPageNum());
        assertEquals(20, pageResult.getPageSize());
        assertEquals(100L, pageResult.getTotal());
        assertEquals(5, pageResult.getTotalPages());
        assertEquals(data, pageResult.getList());
    }

    @Test
    void pageResult_withLargeDataset_handlesCorrectly() {
        // Arrange
        List<Integer> largeList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            largeList.add(i);
        }

        // Act
        PageResult<Integer> pageResult = PageResult.of(1, 100, 10000L, largeList);

        // Assert
        assertEquals(1000, pageResult.getList().size());
        assertEquals(10000L, pageResult.getTotal());
    }

    @Test
    void pageResult_withNullList_handlesNull() {
        // Arrange
        PageResult<String> pageResult = new PageResult<>();

        // Act
        pageResult.setList(null);

        // Assert
        assertNull(pageResult.getList());
    }
}
