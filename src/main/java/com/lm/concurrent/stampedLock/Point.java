package com.lm.concurrent.stampedLock;

import lombok.Data;

import java.util.concurrent.locks.StampedLock;

/**
 * @Auther: limeng
 * @Date: 2020-11-18 22:59
 * @Description:
 */
@Data
public class Point {
    private double x, y;//内部定义表示坐标点
    private final StampedLock s1 = new StampedLock();//定义了StampedLock锁,



    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    void move(double deltaX, double deltaY) {
        long stamp = s1.writeLock();//这里的含义和distanceFormOrigin方法中 s1.readLock()是类似的
        try {
            x += deltaX;
            y += deltaY;

        } finally {
            s1.unlockWrite(stamp);//退出临界区,释放写锁
        }
    }

    double distanceFormOrigin() throws InterruptedException {//只读方法
        long stamp = s1.tryOptimisticRead();  //试图尝试一次乐观读 返回一个类似于时间戳的邮戳整数stamp 这个stamp就可以作为这一个所获取的凭证
        double currentX = x, currentY = y;//读取x和y的值,这时候我们并不确定x和y是否是一致的

        Thread.sleep(1000);

        if (!s1.validate(stamp)) {//判断这个stamp是否在读过程发生期间被修改过,如果stamp没有被修改过,责任无这次读取时有效的,因此就可以直接return了,反之,如果stamp是不可用的,则意味着在读取的过程中,可能被其他线程改写了数据,因此,有可能出现脏读,如果如果出现这种情况,我们可以像CAS操作那样在一个死循环中一直使用乐观锁,知道成功为止
            stamp = s1.readLock();//也可以升级锁的级别,这里我们升级乐观锁的级别,将乐观锁变为悲观锁, 如果当前对象正在被修改,则读锁的申请可能导致线程挂起.
            try {
                currentX = x;
                currentY = y;
                System.out.println("currentX :"+currentX);
                System.out.println("currentY :"+currentY);
            } finally {
                s1.unlockRead(stamp);//退出临界区,释放读锁
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }


    public static void main(String[] args) throws InterruptedException {

        Point point = new Point(10, 10);
        Thread t1 = new Thread(
                ()->{
                    try {
                        System.out.println("测试：" + point.distanceFormOrigin());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );


        Thread t2 = new Thread(()->{
              point.move(20,20);
        });


        t1.start();
        t2.start();
        t2.join();

    }

}
