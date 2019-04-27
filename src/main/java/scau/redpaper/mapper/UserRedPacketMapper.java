package scau.redpaper.mapper;

import org.springframework.stereotype.Repository;
import scau.redpaper.domain.UserRedPacket;

/**
 * @author kunrong
 * @description
 * @date 2019/4/27 19:11
 */
@Repository
public interface UserRedPacketMapper {
    /**
     * 插入抢红包信息.
     * @param userRedPacket ——抢红包信息
     * @return 影响记录数.
     */
    public int grapRedPacket(UserRedPacket userRedPacket);
}
