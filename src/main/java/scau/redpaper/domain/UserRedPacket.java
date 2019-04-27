package scau.redpaper.domain;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author kunrong
 * @description
 * @date 2019/4/27 18:48
 */
@Data
public class UserRedPacket implements Serializable {

    private static final long serialVersionUID = 7049215937937620886L;

    // 用户红包id
    private Long id;
    // 红包id
    private Long redPacketId;
    // 抢红包的用户的id
    private Long userId;
    // 抢红包金额
    private Double amount;
    // 抢红包时间
    private Timestamp grabTime;
    // 备注
    private String note;
    // 省略set/get
}
