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

    /**
     *
     *
     * 乐观锁，按时间戳重入
     *
     * @Description: 乐观锁，按时间戳重入
     *
     * @param redPacketId
     * @param userId
     * @return
     *
     * @return: int
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int grapRedPacketForVersion(Long redPacketId, Long userId) {
        // 记录开始时间
        long start = System.currentTimeMillis();
        // 无限循环，等待成功或者时间满100毫秒退出
        while (true) {
            // 获取循环当前时间
            long end = System.currentTimeMillis();
            // 当前时间已经超过100毫秒，返回失败
            if (end - start > 100) {
                return FAILED;
            }
            // 获取红包信息,注意version值
            RedPacket redPacket = redPacketDao.getRedPacket(redPacketId);
            // 当前小红包库存大于0
            if (redPacket.getStock() > 0) {
                // 再次传入线程保存的version旧值给SQL判断，是否有其他线程修改过数据
                int update = redPacketDao.decreaseRedPacketForVersion(redPacketId, redPacket.getVersion());
                // 如果没有数据更新，则说明其他线程已经修改过数据，则重新抢夺
                if (update == 0) {
                    continue;
                }
                // 生成抢红包信息
                UserRedPacket userRedPacket = new UserRedPacket();
                userRedPacket.setRedPacketId(redPacketId);
                userRedPacket.setUserId(userId);
                userRedPacket.setAmount(redPacket.getUnitAmount());
                userRedPacket.setNote("抢红包 " + redPacketId);
                // 插入抢红包信息
                int result = userRedPacketDao.grapRedPacket(userRedPacket);
                return result;
            } else {
                // 一旦没有库存，则马上返回
                return FAILED;
            }
        }
    }

    /**
     *
     *
     * @Title: grapRedPacketForVersion
     *
     * @Description: 乐观锁，按次数重入
     *
     * @param redPacketId
     * @param userId
     *
     * @return: int
     */

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int grapRedPacketForVersionII(Long redPacketId, Long userId) {
        for (int i = 0; i < 3; i++) {
            // 获取红包信息，注意version值
            RedPacket redPacket = redPacketDao.getRedPacket(redPacketId);
            // 当前小红包库存大于0
            if (redPacket.getStock() > 0) {
                // 再次传入线程保存的version旧值给SQL判断，是否有其他线程修改过数据
                int update = redPacketDao.decreaseRedPacketForVersion(redPacketId, redPacket.getVersion());
                // 如果没有数据更新，则说明其他线程已经修改过数据，则重新抢夺
                if (update == 0) {
                    continue;
                }
                // 生成抢红包信息
                UserRedPacket userRedPacket = new UserRedPacket();
                userRedPacket.setRedPacketId(redPacketId);
                userRedPacket.setUserId(userId);
                userRedPacket.setAmount(redPacket.getUnitAmount());
                userRedPacket.setNote("抢红包 " + redPacketId);
                // 插入抢红包信息
                int result = userRedPacketDao.grapRedPacket(userRedPacket);
                return result;
            } else {
                // 一旦没有库存，则马上返回
                return FAILED;
            }
        }
        return FAILED;
    }


}
