package com.abajin.innovation.controller;

import com.abajin.innovation.common.Result;
import com.abajin.innovation.entity.ActivityType;
import com.abajin.innovation.entity.College;
import com.abajin.innovation.entity.FundType;
import com.abajin.innovation.entity.NewsCategory;
import com.abajin.innovation.entity.PersonType;
import com.abajin.innovation.entity.SpaceType;
import com.abajin.innovation.mapper.ActivityTypeMapper;
import com.abajin.innovation.mapper.CollegeMapper;
import com.abajin.innovation.mapper.FundTypeMapper;
import com.abajin.innovation.mapper.NewsCategoryMapper;
import com.abajin.innovation.mapper.PersonTypeMapper;
import com.abajin.innovation.mapper.SpaceTypeMapper;
import com.abajin.innovation.util.RedisUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公共数据接口（新闻分类、空间类型等）
 */
@RestController
@RequestMapping
@Slf4j
public class CommonController {

    @Autowired
    private NewsCategoryMapper newsCategoryMapper;

    @Autowired
    private SpaceTypeMapper spaceTypeMapper;

    @Autowired
    private PersonTypeMapper personTypeMapper;

    @Autowired
    private ActivityTypeMapper activityTypeMapper;

    @Autowired
    private CollegeMapper collegeMapper;

    @Autowired
    private FundTypeMapper fundTypeMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 获取新闻分类列表
     * GET /api/news-categories
     */
    @GetMapping("/news-categories")
    public Result<List<NewsCategory>> getNewsCategories() {
        try {
            String cacheKey = redisUtil.buildKey("common", "newsCategories");
            try {
                if (redisUtil.exist(cacheKey)) {
                    String json = redisUtil.get(cacheKey);
                    if (StringUtils.hasText(json)) {
                        List<NewsCategory> cached = objectMapper.readValue(json, new TypeReference<List<NewsCategory>>() {});
                        return Result.success(cached);
                    }
                }
            } catch (Exception ex) {
                log.warn("Redis unavailable when reading news categories, fallback to DB. msg={}", ex.getMessage());
            }
            List<NewsCategory> list = newsCategoryMapper.selectAll();
            if (list != null) {
                try {
                    String json = objectMapper.writeValueAsString(list);
                    redisUtil.set(cacheKey, json);
                } catch (Exception ex) {
                    log.warn("Redis unavailable when writing news categories cache, ignore. msg={}", ex.getMessage());
                }
            }
            return Result.success(list);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取空间类型列表
     * GET /api/space-types
     */
    @GetMapping("/space-types")
    public Result<List<SpaceType>> getSpaceTypes() {
        try {
            String cacheKey = redisUtil.buildKey("common", "spaceTypes");
            try {
                if (redisUtil.exist(cacheKey)) {
                    String json = redisUtil.get(cacheKey);
                    if (StringUtils.hasText(json)) {
                        List<SpaceType> cached = objectMapper.readValue(json, new TypeReference<List<SpaceType>>() {});
                        return Result.success(cached);
                    }
                }
            } catch (Exception ex) {
                log.warn("Redis unavailable when reading space types, fallback to DB. msg={}", ex.getMessage());
            }
            List<SpaceType> list = spaceTypeMapper.selectAll();
            if (list != null) {
                try {
                    String json = objectMapper.writeValueAsString(list);
                    redisUtil.set(cacheKey, json);
                } catch (Exception ex) {
                    log.warn("Redis unavailable when writing space types cache, ignore. msg={}", ex.getMessage());
                }
            }
            return Result.success(list);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取人员类型列表
     * GET /api/person-types
     */
    @GetMapping("/person-types")
    public Result<List<PersonType>> getPersonTypes() {
        try {
            String cacheKey = redisUtil.buildKey("common", "personTypes");
            try {
                if (redisUtil.exist(cacheKey)) {
                    String json = redisUtil.get(cacheKey);
                    if (StringUtils.hasText(json)) {
                        List<PersonType> cached = objectMapper.readValue(json, new TypeReference<List<PersonType>>() {});
                        return Result.success(cached);
                    }
                }
            } catch (Exception ex) {
                log.warn("Redis unavailable when reading person types, fallback to DB. msg={}", ex.getMessage());
            }
            List<PersonType> list = personTypeMapper.selectAll();
            if (list != null) {
                try {
                    String json = objectMapper.writeValueAsString(list);
                    redisUtil.set(cacheKey, json);
                } catch (Exception ex) {
                    log.warn("Redis unavailable when writing person types cache, ignore. msg={}", ex.getMessage());
                }
            }
            return Result.success(list);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取活动类型列表
     * GET /api/activity-types
     */
    @GetMapping("/activity-types")
    public Result<List<ActivityType>> getActivityTypes() {
        try {
            String cacheKey = redisUtil.buildKey("common", "activityTypes");
            try {
                if (redisUtil.exist(cacheKey)) {
                    String json = redisUtil.get(cacheKey);
                    if (StringUtils.hasText(json)) {
                        List<ActivityType> cached = objectMapper.readValue(json, new TypeReference<List<ActivityType>>() {});
                        return Result.success(cached);
                    }
                }
            } catch (Exception ex) {
                log.warn("Redis unavailable when reading activity types, fallback to DB. msg={}", ex.getMessage());
            }
            List<ActivityType> list = activityTypeMapper.selectAll();
            if (list != null) {
                try {
                    String json = objectMapper.writeValueAsString(list);
                    redisUtil.set(cacheKey, json);
                } catch (Exception ex) {
                    log.warn("Redis unavailable when writing activity types cache, ignore. msg={}", ex.getMessage());
                }
            }
            return Result.success(list);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取基金类型列表
     * GET /api/fund-types
     */
    @GetMapping("/fund-types")
    public Result<List<FundType>> getFundTypes() {
        try {
            String cacheKey = redisUtil.buildKey("common", "fundTypes");
            try {
                if (redisUtil.exist(cacheKey)) {
                    String json = redisUtil.get(cacheKey);
                    if (StringUtils.hasText(json)) {
                        List<FundType> cached = objectMapper.readValue(json, new TypeReference<List<FundType>>() {});
                        return Result.success(cached);
                    }
                }
            } catch (Exception ex) {
                log.warn("Redis unavailable when reading fund types, fallback to DB. msg={}", ex.getMessage());
            }
            List<FundType> list = fundTypeMapper.selectAll();
            if (list != null) {
                try {
                    String json = objectMapper.writeValueAsString(list);
                    redisUtil.set(cacheKey, json);
                } catch (Exception ex) {
                    log.warn("Redis unavailable when writing fund types cache, ignore. msg={}", ex.getMessage());
                }
            }
            return Result.success(list);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取学院列表（用于主办单位、承办单位等下拉）
     * GET /api/colleges
     */
    @GetMapping("/colleges")
    public Result<List<College>> getColleges() {
        try {
            String cacheKey = redisUtil.buildKey("common", "colleges");
            try {
                if (redisUtil.exist(cacheKey)) {
                    String json = redisUtil.get(cacheKey);
                    if (StringUtils.hasText(json)) {
                        List<College> cached = objectMapper.readValue(json, new TypeReference<List<College>>() {});
                        return Result.success(cached);
                    }
                }
            } catch (Exception ex) {
                log.warn("Redis unavailable when reading colleges, fallback to DB. msg={}", ex.getMessage());
            }
            List<College> list = collegeMapper.selectAll();
            if (list != null) {
                try {
                    String json = objectMapper.writeValueAsString(list);
                    redisUtil.set(cacheKey, json);
                } catch (Exception ex) {
                    log.warn("Redis unavailable when writing colleges cache, ignore. msg={}", ex.getMessage());
                }
            }
            return Result.success(list);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
