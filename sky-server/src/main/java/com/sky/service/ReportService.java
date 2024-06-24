package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;

/**
 * 数据统计业务接口
 *
 * @Author itcast
 * @Create 2024/6/24
 **/
public interface ReportService {
    /**
     * 统计指定范围内每天的营业额数据
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
}
