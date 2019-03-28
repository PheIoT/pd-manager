package com.pheiot.phecloud.pd.dto;

import com.pheiot.bamboo.common.dto.AbstractValueObject;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ProductDto extends AbstractValueObject {

    private Long id;
    private String displayName;
    private String pkey;
    private String secret;
    private String nodeType;
    private String commType;
    private boolean isEnabled;
    private Timestamp createAt;
    private Timestamp updateAt;
    private String remark;
    private String ukey;
}
