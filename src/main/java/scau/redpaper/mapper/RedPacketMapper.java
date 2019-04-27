package scau.redpaper.mapper;

import org.springframework.stereotype.Repository;
import scau.redpaper.domain.RedPacket;

/**
 * @author kunrong
 * @description
 * @date 2019/4/27 18:49
 */
@Repository
public interface RedPacketMapper {

    /**
     * 获取红包信息.
     * @param id --红包id
     * @return 红包具体信息
     */
    public RedPacket getRedPacket(Long id);

    /**
     * 扣减抢红包数.
     * @param id -- 红包id
     * @return 更新记录条数
     */
    public int decreaseRedPacket(Long id);


}