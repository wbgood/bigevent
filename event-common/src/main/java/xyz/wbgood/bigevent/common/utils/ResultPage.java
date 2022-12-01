package xyz.wbgood.bigevent.common.utils;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 返回分页结果通用包装
 * </p>
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "分页数据消息体", description = "分页数据统一对象")
@Builder
public class ResultPage<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int status;

    private String msg;

    private Long total;

    private Integer pageSize;

    private Long pages;

    private Integer pageNum;

    private List<T> items;

    public static ResultPage ok(Long total, Integer pageSize, Long pages, Integer pageNum, List items) {
        return ResultPage.builder()
                .status(ResultCode.OK)
                .msg("操作成功")
                .total(total)
                .pageSize(pageSize)
                .pageNum(pageNum)
                .pages(pages)
                .items(items)
                .build();
    }

    public static ResultPage ok(int code, String msg, Long total, Integer pageSize, Long pages, Integer pageNum, List items) {
        return ResultPage.builder()
                .status(code)
                .msg(msg)
                .total(total)
                .pageSize(pageSize)
                .pageNum(pageNum)
                .pages(pages)
                .items(items)
                .build();
    }
}