package com.xxxx.crm.enums;

/**
 * 在JDK1.5中引入了一个新的类型—— 枚举。可以简单地理解枚举就是一种特殊的java类，在这个特殊的
 * 类当中定义几个静态变量，每个变量都是这个类的实例。通过关键字 enum 实现，自动继承自Enum 类
 * （枚举类）。
 * 枚举（enum），是指一个经过排序的、被打包成一个单一实体的项列表。一个枚举的实例可以使
 * 用枚举项列表中任意单一项的值。枚举在各个语言当中都有着广泛的应用，通常用来表示诸如颜
 * 色、方式、类别、状态等等数目有限、形式离散、表达又极为明确的量。
 * 在枚举中，每个枚举的值都有一个或多个对应的字段，而且不同的枚举值也会有不同的字段值。同时，
 * 它和普通的类一样，可以声明构造器和各种各样的方法 。
 */
public enum  DevResult {
    // 未开发
    UNDEV(0),
    // 开发中
    DEVING(1),
    // 开发成功
    DEV_SUCCESS(2),
    // 开发失败
    DEV_FAILED(3);

    private  Integer status;

    DevResult(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
