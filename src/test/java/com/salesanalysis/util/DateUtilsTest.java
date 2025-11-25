package com.salesanalysis.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class DateUtilsTest {

    @Test
    void testFormatDate_WithDefaultFormat() {
        // 准备测试数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse("2023-01-15");
        } catch (ParseException e) {
            fail("Failed to parse test date: " + e.getMessage());
            return;
        }

        // 执行测试
        String formattedDate = DateUtils.formatDate(date);

        // 验证结果
        assertEquals("2023-01-15", formattedDate, "Date should be formatted as 'yyyy-MM-dd'");
    }

    @Test
    void testFormatDate_WithCustomFormat() {
        // 准备测试数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdf.parse("2023-01-15");
        } catch (ParseException e) {
            fail("Failed to parse test date: " + e.getMessage());
            return;
        }

        // 执行测试 - 使用默认formatDate方法
        String formattedDate = DateUtils.formatDate(date);

        // 验证结果
        assertEquals("2023-01-15", formattedDate, "Date should be formatted as 'yyyy-MM-dd'");
    }

    @Test
    void testFormatDate_WithNullDate() {
        // 执行测试 - 传入null日期
        String formattedDate = DateUtils.formatDate(null);

        // 验证结果 - 应该返回null
        assertNull(formattedDate, "Formatting null date should return null");
    }

    @Test
    void testParseDate_WithDefaultFormat() throws ParseException {
        // 执行测试
        Date date = DateUtils.parseDate("2023-01-15");

        // 验证结果
        assertNotNull(date, "Parsed date should not be null");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("2023-01-15", sdf.format(date), "Parsed date should be 2023-01-15");
    }

    @Test
    void testParseDate_WithCustomFormat() throws ParseException {
        // 执行测试 - 使用默认parseDate方法
        Date date = DateUtils.parseDate("2023-01-15");

        // 验证结果
        assertNotNull(date, "Parsed date should not be null");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);
        assertEquals("2023-01-15", formattedDate, "Parsed date should be 2023-01-15");
    }

    @Test
    void testParseDate_WithInvalidDateString() {
        // 执行测试 - 传入无效日期字符串
        try {
            Date date = DateUtils.parseDate("invalid-date");
            // 如果没有抛出异常，这是一个问题，因为DateUtils.parseDate方法会抛出ParseException
            fail("Expected ParseException to be thrown for invalid date string");
        } catch (ParseException e) {
            // 预期会抛出ParseException异常
        }
    }

    @Test
    void testParseDate_WithNullString() throws ParseException {
        // 执行测试 - 传入null字符串
        Date date = DateUtils.parseDate(null);

        // 验证结果 - 应该返回null
        assertNull(date, "Parsing null date string should return null");
    }

    @Test
    void testParseDate_WithInvalidFormat() {
        // 执行测试 - 传入不匹配格式的日期字符串
        try {
            Date date = DateUtils.parseDate("15/01/2023");
            // 如果没有抛出异常，这是一个问题，因为格式不匹配
            fail("Expected ParseException to be thrown for invalid date format");
        } catch (ParseException e) {
            // 预期会抛出ParseException异常
        }
    }

    @Test
    void testIsValidDate_WithValidDate() {
        // 执行测试 - 有效日期
        boolean isValid = DateUtils.isValidDate("2023-01-15");

        // 验证结果 - 应该返回true
        assertTrue(isValid, "Valid date should be recognized as valid");
    }

    @Test
    void testIsValidDate_WithValidDateAndCustomFormat() {
        // 执行测试 - 有效日期和默认格式
        boolean isValid = DateUtils.isValidDate("2023-01-15");

        // 验证结果 - 应该返回true
        assertTrue(isValid, "Valid date should be recognized as valid");
    }

    @Test
    void testIsValidDate_WithInvalidDate() {
        // 执行测试 - 无效日期
        boolean isValid = DateUtils.isValidDate("2023-02-30");

        // 验证结果 - 应该返回false
        assertFalse(isValid, "Invalid date should be recognized as invalid");
    }

    @Test
    void testIsValidDate_WithInvalidFormat() {
        // 执行测试 - 格式不匹配
        boolean isValid = DateUtils.isValidDate("15/01/2023");

        // 验证结果 - 应该返回false
        assertFalse(isValid, "Date with mismatched format should be recognized as invalid");
    }

    @Test
    void testIsValidDate_WithNullDateString() {
        // 执行测试 - null日期字符串
        boolean isValid = DateUtils.isValidDate(null);

        // 验证结果 - 应该返回false
        assertFalse(isValid, "Null date string should be recognized as invalid");
    }

    @Test
    void testIsValidDate_WithEmptyDateString() {
        // 执行测试 - 空日期字符串
        boolean isValid = DateUtils.isValidDate("");

        // 验证结果 - 应该返回false
        assertFalse(isValid, "Empty date string should be recognized as invalid");
    }

    @Test
    void testIsValidDate_WithLeapYear() {
        // 执行测试 - 闰年
        boolean isValid = DateUtils.isValidDate("2024-02-29");

        // 验证结果 - 应该返回true
        assertTrue(isValid, "Leap year date should be recognized as valid");
    }

    @Test
    void testIsValidDate_WithNonLeapYear() {
        // 执行测试 - 非闰年
        boolean isValid = DateUtils.isValidDate("2023-02-29");

        // 验证结果 - 应该返回false
        assertFalse(isValid, "Non-leap year with February 29 should be recognized as invalid");
    }
}
