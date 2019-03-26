package com.xuecheng.framework.model.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QueryResponseResult<T> extends ResponseResult {

    QueryResult queryResult;

    public QueryResponseResult(ResultCode resultCode,QueryResult queryResult){
        super(resultCode);
       this.queryResult = queryResult;
    }

    public static QueryResponseResult SUCCESS(QueryResult queryResult){
        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }

    public static QueryResponseResult FAIL(QueryResult queryResult){
        return new QueryResponseResult(CommonCode.FAIL,queryResult);
    }
}
