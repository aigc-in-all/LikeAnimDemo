# LikeAnimDemo

点赞飘心 ❤️ 效果，多用于直播场景，使用三阶贝塞尔曲线控制运动轨迹

## 效果预览

<img src="/screenshot/demo.gif" height="600em" />

## 使用方式

### 1.在布局文件中添加View
```kotlin
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">

        <androidx.appcompat.widget.AppCompatImageButton
            android:id="@+id/btnLike"
            android:layout_width="36dp"
            android:layout_height="36dp"
            android:layout_marginBottom="44dp"
            android:background="@drawable/heart"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent" />

        <com.hqb.demo.likeanim.LikeAnimView
            android:id="@+id/vLikeAnim"
            android:layout_width="100dp"
            android:layout_height="350dp"
            app:layout_constraintBottom_toTopOf="@id/btnLike"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent" />

    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```
布局预览

<img src="/screenshot/layout.png" height="400em" />

### 2.点击展示❤️
```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        initEvents()
    }

    private fun initEvents() {
        binding.btnLike.setOnClickListener {
            binding.vLikeAnim.addLike() // <-- 这里
        }
    }
}
```

## 自定义

所有代码均在一个类里 `com.hqb.demo.likeanim.LikeAnimView`

如需调整 ❤️ 飘出轨迹，直接修改 `generateCurveAnim` 方法中两个控制点的坐标即可。
