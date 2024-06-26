package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据统计业务实现
 *
 * @Author itcast
 * @Create 2024/6/24
 **/
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 统计指定范围内每天的营业额数据
     * @param begin
     * @param end
     * @return
     */
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {// 4.1  4.7

        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);

        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        List<Double> turnoverList = new ArrayList<>();

        for (LocalDate date : dateList) {//年月日
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("status", Orders.COMPLETED);
            map.put("begin",beginTime);
            map.put("end",endTime);

            //根据动态条件查询营业额数据
            Double turnover = orderMapper.sumByMap(map);
            turnoverList.add(turnover == null ? 0.0 : turnover);
        }

        TurnoverReportVO turnoverReportVO = TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .turnoverList(StringUtils.join(turnoverList,","))
                .build();

        return turnoverReportVO;
    }

    /**
     * 统计指定范围内每天的总用户量和新增用户量
     * @param begin
     * @param end
     * @return
     */
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);

        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();

        for (LocalDate date : dateList) {//4.1
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap<>();
            map.put("endTime",endTime);

            //统计截止到某天总用户数量
            Integer totalUserCount = userMapper.countByMap(map);
            totalUserList.add(totalUserCount);

            map.put("beginTime",beginTime);
            //统计指定日期新增用户数量
            Integer newUserCount = userMapper.countByMap(map);
            newUserList.add(newUserCount);
        }

        //封装返回结果
        UserReportVO userReportVO = UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();

        return userReportVO;
    }

    /**
     * 统计指定范围内每天的订单数量
     * @param begin
     * @param end
     * @return
     */
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);

        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        List<Integer> totalOrderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();

        Integer totalOrderCountResult = 0;
        Integer validOrderCountResult = 0;

        for (LocalDate date : dateList) {//查询每天的总订单数和有效订单数
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            //select count(id) from orders where order_time > ? and order_time < ?
            Map map = new HashMap();
            map.put("begin",beginTime);
            map.put("end",endTime);

            //查询指定时间范围内的总订单数
            Integer totalOrderCount = orderMapper.countByMap(map);
            totalOrderCountResult += totalOrderCount;
            totalOrderCountList.add(totalOrderCount);

            //select count(id) from orders where order_time > ? and order_time < ? and status = 5
            map.put("status",Orders.COMPLETED);
            //查询指定时间范围内的有效订单数
            Integer validOrderCount = orderMapper.countByMap(map);
            validOrderCountResult += validOrderCount;
            validOrderCountList.add(validOrderCount);
        }

        //Integer integer = totalOrderCountList.stream().reduce(Integer::sum).get();

        Double orderCompletionRate = 0.0;
        if(totalOrderCountResult != 0){
            orderCompletionRate = validOrderCountResult.doubleValue() / totalOrderCountResult;
        }

        OrderReportVO orderReportVO = OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(totalOrderCountList,","))//总订单数
                .validOrderCountList(StringUtils.join(validOrderCountList,","))//有效订单数
                .totalOrderCount(totalOrderCountResult)
                .validOrderCount(validOrderCountResult)
                .orderCompletionRate(orderCompletionRate)
                .build();

        return orderReportVO;
    }

    /**
     * 统计商品销量排名
     * @param begin
     * @param end
     * @return
     */
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        //SELECT od.NAME,SUM(od.NUMBER) num FROM order_detail od ,orders o WHERE od.order_id = o.id AND o.status = 5 GROUP BY od.NAME ORDER BY num desc

        List<GoodsSalesDTO> salesDTOList = orderMapper.getSalesTop10();

        /*String nameList = "";
        for (GoodsSalesDTO goodsSalesDTO : salesDTOList) {
            String name = goodsSalesDTO.getName();
            nameList += name + ",";
            Integer number = goodsSalesDTO.getNumber();
        }*/

        List<String> nameList = salesDTOList.stream().map(dto -> {
            return dto.getName();
        }).collect(Collectors.toList());

        List<Integer> numberList = salesDTOList.stream().map(dto -> {
            return dto.getNumber();
        }).collect(Collectors.toList());

        SalesTop10ReportVO top10ReportVO = SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList,","))//鱼香肉丝,宫保鸡丁,水煮鱼
                .numberList(StringUtils.join(numberList,","))//260,215,200
                .build();

        return top10ReportVO;
    }

    /**
     * 导出运营数据报表
     */
    public void exportXlsx(HttpServletResponse response) throws Exception{
        LocalDate now = LocalDate.now();//获得今天的日期
        LocalDate beginDate = now.minusDays(30);//开始日期
        LocalDate endDate = now.minusDays(1);//结束日期

        LocalDateTime beginTime = LocalDateTime.of(beginDate, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(endDate, LocalTime.MAX);

        //查询最近30天的运营数据（概览数据）
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(beginTime, endTime);

        //使用一个输入流读取到指定的模版文件，动态获取文件的位置
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        //将查询到的数据写到Excel表格中
        XSSFWorkbook excel = new XSSFWorkbook(in);

        //读取到第一个Sheet页
        XSSFSheet sheet = excel.getSheetAt(0);

        //找到第2行，第2个单元格
        sheet.getRow(1).getCell(1).setCellValue(beginDate.toString() + "至" + endDate.toString());

        //营业额数据填充
        sheet.getRow(3).getCell(2).setCellValue(businessDataVO.getTurnover());
        //订单完成率填充
        sheet.getRow(3).getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
        //新增用户数据填充
        sheet.getRow(3).getCell(6).setCellValue(businessDataVO.getNewUsers());
        //有效订单填充
        sheet.getRow(4).getCell(2).setCellValue(businessDataVO.getValidOrderCount());
        //平均客单价填充
        sheet.getRow(4).getCell(4).setCellValue(businessDataVO.getUnitPrice());

        //填充明细数据，一共30行
        for (int i = 0; i < 30; i++) {
            LocalDate date = beginDate.plusDays(i);

            XSSFRow row = sheet.getRow(7 + i);
            BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
            row.getCell(1).setCellValue(date.toString());//日期
            row.getCell(2).setCellValue(businessData.getTurnover());//营业额
            row.getCell(3).setCellValue(businessData.getValidOrderCount());//有效订单数
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());//订单完成率
            row.getCell(5).setCellValue(businessData.getUnitPrice());//评价客单价
            row.getCell(6).setCellValue(businessData.getNewUsers());//新增用户数
        }

        //通过响应对象来获取输出流，这个输出流对准的是客户端浏览器
        ServletOutputStream out = response.getOutputStream();
        excel.write(out);

        //关闭资源
        out.close();
        excel.close();
    }
}
