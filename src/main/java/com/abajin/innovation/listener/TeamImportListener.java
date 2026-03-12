package com.abajin.innovation.listener;

import com.abajin.innovation.dto.TeamImportDTO;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 团队导入 Excel 读取监听，收集所有行
 */
public class TeamImportListener implements ReadListener<TeamImportDTO> {

    private final List<TeamImportDTO> list = new ArrayList<>();

    @Override
    public void invoke(TeamImportDTO data, AnalysisContext context) {
        if (data.getName() != null && !data.getName().trim().isEmpty()) {
            list.add(data);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {}

    public List<TeamImportDTO> getList() {
        return list;
    }
}
