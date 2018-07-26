## Blinds

 [ ![Download](https://api.bintray.com/packages/lovejjfg/maven/Blinds/images/download.svg) ](https://bintray.com/lovejjfg/maven/Blinds/_latestVersion)


在微信的「聊天信息」中有了「聊天小程序」这一项。最近在 GitHub 首页，你的相关状态比如说 issues 讨论状态会在顶部有个这个效果，鼠标触碰的话，折叠效果会打开。
[Android 百叶窗折叠效果](https://www.jianshu.com/p/2b1cbc6083fa)

![Blinds](https://raw.githubusercontent.com/lovejjfg/Blinds/master/art/art1.png)
![Blinds](https://raw.githubusercontent.com/lovejjfg/Blinds/master/art/art2.png)

## 使用


    implementation 'com.lovejjfg.blinds:blinds:lastedversion'


xml:

        <com.lovejjfg.blinds.BlindsLayout
            xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:tools="http://schemas.android.com/tools"
            android:id="@+id/blinds"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:blindsFold="true"
            app:blindsFraction="60%"
            app:blindsMaxCount="4"
            app:blindsRevertDraw="true"
            tools:context="com.lovejjfg.demo.MainActivity">

code:

        blinds4.orientation = RIGHT
        blinds4.maxCount = 4
        blinds4.extraSpace = 20
        blinds4.isRevertDraw = true
        blinds4.setAnimationDuration(1000)
        blinds4.setInterpolator(BounceInterpolator())

## 版本

* 0.0.1
  * support fold fraction and fold orientation

* 0.0.2
  * add field `isRevertDraw`

