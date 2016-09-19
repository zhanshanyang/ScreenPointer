package com.chehejia.hmi.debugger.steeringwheelsimulator;

/**
 * Created by chj1300 on 2016/6/22.
 *
 * 方向盘：
 * 接收Lin总线数据的思想是：
 * 1，使用（EventBus）来发送Lin的消息
 * 2，当前的界面在onResume和onPause中注册和注销eventbus数据总线
 * 3，当页面可见时，就制定当前界面当前那个控件为初始焦点
 * 4，接收Lin的事件的方法，此方法解析当前信号的触发按钮是哪个  然后发送此事件
 * 5，接收到此事件的界面，触发某动作
 *
 */
public class SteeringWheel {




}
