package com.stockmonitor.dao.stock;

import com.stockmonitor.model.mysql.StockRanking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StockRankingDao {

    /**
     * 插入股票排行数据
     */
    int insert(StockRanking stockRanking);

    /**
     * 批量插入股票排行数据
     */
    int batchInsert(@Param("list") List<StockRanking> stockRankings);

    /**
     * 根据股票代码查询最新的排行数据
     */
    StockRanking getLatestBySymbol(@Param("symbol") String symbol);

    /**
     * 查询指定时间范围内的排行数据
     */
    List<StockRanking> getByTimeRange(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 查询涨幅排名前N的股票
     */
    List<StockRanking> getTopByChangeRate(@Param("limit") int limit);

    /**
     * 查询成交量排名前N的股票
     */
    List<StockRanking> getTopByVolume(@Param("limit") int limit);

    /**
     * 查询成交额排名前N的股票
     */
    List<StockRanking> getTopByValue(@Param("limit") int limit);
}