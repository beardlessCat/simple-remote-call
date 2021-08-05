package com.bigyj.breaker.manager;

import com.bigyj.breaker.state.BreakerState;
import com.bigyj.breaker.state.ClosedState;
import com.bigyj.breaker.state.HalfOpenState;
import com.bigyj.breaker.state.OpenState;
import lombok.Data;

@Data
public class BreakerStateManager {
    /**
     * ʧ�ܴ���
     */
    private int failCount ;

    /**
     * �ɹ�����
     */
    private int successCount ;

    /**
     * �ӿ��۶�ʱ��
     */
    private long closeAt;

    /**
     * ���ʧ�ܴ���
     */
    private int maxFailCount ;

    /**
     * ���ɹ�����
     */
    private int maxSuccessCount;
    /**
     *��ָ����Դ���
     */
    private int openRetryCount ;
    /**
     * ��ָ�����Դ������ﵽ�������󣬱�ΪOPEN״̬
     */
    private int maxOpenRetryCount ;
    /**
     * ��ǰ״̬
     */
    private BreakerState breakerState ;

    public BreakerStateManager(int failCount, int successCount, long closeAt, int maxFailCount, int maxSuccessCount, int openRetryCount, int maxOpenRetryCount) {
        this.failCount = failCount;
        this.successCount = successCount;
        this.closeAt = closeAt;
        this.maxFailCount = maxFailCount;
        this.maxSuccessCount = maxSuccessCount;
        this.openRetryCount = openRetryCount;
        this.toCloseStatus();
    }

    public void toOpenStatus(){
        this.breakerState = new OpenState(this);
        System.out.println("����·����ΪOPEN��");
    }

    /**
     * ��ΪCLOSE״̬
     */

    public void toCloseStatus(){
        this.breakerState = new ClosedState(this);
        System.out.println("����·����ΪCLOSE��");
    }

    /**
     * ��ΪHALFOPEN״̬
     */
    public void toHalfOpenStatus(){
        this.breakerState = new HalfOpenState(this);
        System.out.println("����·����ΪHALF-OPEN��");
    }

    public void clear() {
        this.failCount = 0 ;
        this.successCount = 0;
        this.openRetryCount = 0;
        this.closeAt = 0;
    }

    /**
     * �Ƿ�ر�
     * @return
     */
    public boolean isClosed() {
        return breakerState instanceof ClosedState;
    }

    /**
     * �Ƿ���
     * @return
     */
    public boolean isOpen() {
        return breakerState instanceof OpenState;
    }

    /**
     * �Ƿ�뿪��
     * @return
     */
    public boolean isHalfOpen() {
        return breakerState instanceof HalfOpenState;
    }
    public synchronized void  addRetryCount(){
        this.openRetryCount++;
        if(this.openRetryCount>=maxOpenRetryCount){
            this.toOpenStatus();
            this.openRetryCount = 0;
            long closeAt = System.currentTimeMillis();
            this.closeAt = closeAt;
        }
    }

    public synchronized void  addFailCount(){
        this.failCount++ ;
        //fixme �е�ӷ�ף�����Ѱ�Ҹ��Ӽ��ķ�ʽ����ǰ״̬Ϊcloseʱ�Ŵ򿪣�
        if(this.failCount>=this.maxFailCount){
            if(this.isClosed()){
                this.toOpenStatus();
                //��¼��ǰ��ʼ�۶�ʱ��
                long closeAt = System.currentTimeMillis();
                this.closeAt = closeAt;
            }
        }
    }

    public synchronized void addSuccessCount(){
        this.successCount++ ;
        if(this.successCount>this.maxSuccessCount){
            toCloseStatus();
            this.closeAt = 0;
            this.successCount=0;
            this.failCount=0;
        }
    }
}
