package com.stockmonitor.dao;

import com.stockmonitor.model.Stock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StockDao {
    
    @Select("SELECT * FROM stock WHERE code = #{code}")
    Stock findByCode(String code);
    
    @Select("SELECT * FROM stock")
    List<Stock> findAll();
    
    @Update("UPDATE stock SET price = #{price}, change = #{change}, volume = #{volume}, " +
            "high = #{high}, low = #{low}, close = #{close}, " +
            "macd = #{macd}, kdj = #{kdj}, rsi = #{rsi}, " +
            "update_time = #{updateTime} WHERE code = #{code}")
    int updateStock(Stock stock);
} 