package scau.redpaper.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import scau.redpaper.domain.RedPacket;
import scau.redpaper.domain.UserRedPacket;
import scau.redpaper.mapper.RedPacketMapper;
import scau.redpaper.mapper.UserRedPacketMapper;
import scau.redpaper.service.UserRedPacketService;

/**
 * @author kunrong
 * @description
 * @date 2019/4/27 19:21
 */
@Service
public class UserRedPacketServiceImpl implements UserRedPacketService {

    // private Logger logger =
    // LoggerFactory.getLogger(UserRedPacketServiceImpl.class);

    @Autowired
    private UserRedPacketMapper userRedPacketDao;

    @Autowired
    private RedPacketMapper redPacketDao;

    // 失败
    private static final int FAILED = 0;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int grapRedPacket(Long redPacketId, Long userId) {
        // 获取红包信息
        RedPacket redPacket = redPacketDao.getRedPacketForUpdate(redPacketId);
        int leftRedPacket = redPacket.getStock();
        // 当前小红包库存大于0
        if (leftRedPacket > 0) {
            redPacketDao.decreaseRedPacket(redPacketId);
            // logger.info("剩余Stock数量:{}", leftRedPacket);
            // 生成抢红包信息
            UserRedPacket userRedPacket = new UserRedPacket();
            userRedPacket.setRedPacketId(redPacketId);
            userRedPacket.setUserId(userId);
            userRedPacket.setAmount(redPacket.getUnitAmount());
            userRedPacket.setNote("redpacket- " + redPacketId);
            // 插入抢红包信息
            int result = userRedPacketDao.grapRedPacket(userRedPacket);
            return result;
        }
        // logger.info("没有红包啦.....剩余Stock数量:{}", leftRedPacket);
        // 失败返回
        return FAILED;
    }

}
