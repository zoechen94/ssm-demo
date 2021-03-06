package com.zoe.demo.entity.httpdo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by 陈亚兰 on 2018/5/23.
 */
@Setter
@Getter
public class HttpDO {
    private String code;
    private String msg;
    private boolean success;
    private List<CompanyInfo> data;
}
