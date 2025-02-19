package com.yt.message.common.pojo.dto;


import com.yt.message.common.enums.DataSyncOpeType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataSyncMessagePayload {

    private DataSyncOpeType type;
    private Long id;
    private Integer version;
}
