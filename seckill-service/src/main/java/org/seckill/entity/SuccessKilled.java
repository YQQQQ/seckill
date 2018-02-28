package org.seckill.entity;

import java.util.Date;

public class SuccessKilled {
    private Integer seckillId;
    private Integer userId;
    private Integer state;
    private Date createTime;
    private SeckillGoods seckillGoods;

    public Integer getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Integer seckillId) {
        this.seckillId = seckillId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public SeckillGoods getSeckillGoods() {
        return seckillGoods;
    }

    public void setSeckillGoods(SeckillGoods seckillGoods) {
        this.seckillGoods = seckillGoods;
    }

    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckillId=" + seckillId +
                ", userId=" + userId +
                ", state=" + state +
                ", createTime=" + createTime +
                ", seckillGoods=" + seckillGoods +
                '}';
    }
}
