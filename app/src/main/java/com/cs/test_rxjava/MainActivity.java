package com.cs.test_rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
/*
使用的时候可以把注释去掉，看LOG就行，建议手动写一下并结合文档会比较好理解
 */

public class MainActivity extends AppCompatActivity {
    private String [] names={"大哥","小弟","大侠"};
    private String [][] seats={{"01","12"},{"22","33"},{"44","55","33"}};
    private String [] number={"0","1","1","2","2","3","3","4","5"};

    private final String TAG="TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getObservabla();//最简单观察者模式 actiona
        //simpleObservable();//just关键字
        //observablejust();//repeat重复发射的消息
        //observablerange();//range 变成for循环发消息
        //observablerepeat();
        //observerableThead()://Schedulers真正的实现线程之间的切换
        //observablemap();//map字符类型转换
        //obserablefrom();//from读取数组list等集合
        //observableflatmap();//flatmap是比较难理解的，
        //observablefilter();//filter过滤的一个效果
        // observabletake();//take选择需要的元素符号,从0开始选择,类似的还有takeLast,takeFirst
        //observabletakelast();//和take差不多
        //observabledistinct();//disdtinct去掉重复的content,有点像set集合
        //observableChange();//看注释吧
        //observablefirst();//first第一个去发送消息，从last同理；
        //observableSkip();//skip和skiplast是跳过，从0开始计数，不解释
        //observabletimmer();//timer的使用 和interval区别下
    }

    private void observabletimmer() {
        Observable.just(22, 23, 22, 22, 24).timer(3000, TimeUnit.MILLISECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Log.d(TAG,"隔了几秒收到："+aLong);//along的参数是0
            }
        });
    }

    private void observableSkip() {
        Observable.just(22,23,22,22,24).skip(2).subscribe(new Action1<Integer>() {
            @Override//当数据变化时会调用，这个方法到是挺好的，不过不是很清楚怎么去调用呢
            public void call(Integer integer) {
                Log.d(TAG,"当前的温度为"+integer);
            }
        });
    }

    private void observablefirst() {
        Observable.just(22,23,22,22,24).first().subscribe(new Action1<Integer>() {
            @Override//当数据变化时会调用，这个方法到是挺好的，不过不是很清楚怎么去调用呢
            public void call(Integer integer) {
                Log.d(TAG,"当前的温度为"+integer);
            }
        });
    }

    private void observableChange() {
        Observable.just(22,23,22,22,24).distinctUntilChanged().subscribe(new Action1<Integer>() {
            @Override//当数据变化时会调用，这个方法到是挺好的，不过不是很清楚怎么去调用呢
            public void call(Integer integer) {
                Log.d(TAG,"当前的温度为"+integer);
            }
        });
    }

    private void observabledistinct() {
        Observable.from(number).distinct().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG,"当前数字"+s);
            }
        });
    }

    private void observabletakelast() {
        Observable.just(0,1,2,3,4,4,5,6).takeLast(4).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG,"当前的数字为："+integer);
            }
        });
    }

    private void observabletake() {
        Observable.just(0,1,2,3,4,4,5,6).take(4).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG,"当前的数字为："+integer);
            }
        });
    }

    private void observablefilter() {
        Observable.just(0,1,1,3,3,4,5,6,4).filter(new Func1<Integer, Boolean>() {
            @Override//FUNcl里面的参数第一个是Integer,第二个是我得命令，可进行拓张
            public Boolean call(Integer integer) {
                return integer>1 && integer>3;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG,"当前数字为"+integer);
            }
        });
    }

    private void observableflatmap() {
        Action1<String> action1 = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG,"当前座位号是"+s);
            }
        };
        Observable.from(seats).flatMap(new Func1<String[], Observable<String >>() {
            @Override//flatMap好难理解，遍历二维数组
            public Observable<String > call(String[] strings) {
                return Observable.from(strings);
            }
        }).subscribe(action1);
    }

    private void obserablefrom() {
        Observable.from(names).map(new Func1<String, String >() {
            @Override
            public String call(String s) {
                return s+"sss";
            }
        }).subscribe(new Action1<String>() {
            @Override//from就是支持数组
            public void call(String s) {
                Log.d(TAG, "我的名字是："+s);
            }
        });
    }

    private void observablemap() {
        Action1<String> action1 = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG, "你被调用了"+s);

            }
        };
        //map是类型转换
        Observable.just(1,2,3,4).repeat(2).map(new Func1<Integer, String >() {
            @Override//funcl第一个数据是前面的，后面的S的
            public String call(Integer integer) {
                return integer.toString()+2;//数据的可拓展性好强啊
            }
        }).subscribe(action1);
    }

    private void observerableThead() {
        Observable<String > observable = Observable.create(new Observable.OnSubscribe<String >() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.d(TAG, "你被调用了"+Thread.currentThread().getName());
                subscriber.onNext("你被调用了"+Thread.currentThread().getName());
            }
        });
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.d(TAG,"你有什么丰富"+Thread.currentThread().getName());
            }

        };
        //Schedulers.newThread()
        // 子线程//Schedulers.immediate()
        //Schedulers.io()读写文件的适合，或者网络,读取文件等耗时操作
        //Schedulers.computation()适合计算操作，与cpu有关

        observable.observeOn(Schedulers.computation()).subscribe(subscriber);
    }

    private void observablerepeat() {
        Observable.just(1,2,3,4,5).repeat(3).subscribe(new Action1<Integer>() {
            @Override//重复执行几次
            public void call(Integer integer) {
                Log.d(TAG, integer + "");
            }
        });
    }

    private void observablerange() {
        Observable.range(0, 10).subscribe(new Action1<Integer>() {
            @Override//变成for循环了
            public void call(Integer integer) {
                Log.d(TAG, integer + "");

            }
        });
    }

    private void observablejust() {
        Observable.just(0,1,2,3,4,5).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG,integer+"");
            }
        });
    }

    private void simpleObservable() {
        //这里应该是用户
        Observable<String> observable = Observable.just("我只要大块的肉");

        //开发者或者我们需要程序去做的事情
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG,s);
            }
        });
    }

    private void getObservabla() {
        //观察者 顾客
        Subscriber<String> subscriber=new Subscriber<String >(){

            @Override
            public void onNext(String s) {
                Log.d(TAG,s);

            }
            @Override
            public void onCompleted() {
                Log.d(TAG,"onCompleted");

            }
            @Override
            public void onError(Throwable t) {

            }
        };
        //Observable 即被观察者 老板娘
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("去拿一大碗米饭");
                subscriber.onCompleted();
            }
        });
        observable.subscribe(subscriber);
    }
}
