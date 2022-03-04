package com.hqb.demo.likeanim

import android.animation.*
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import kotlin.math.pow
import kotlin.random.Random

/**
 * 赞飘❤️的动画视图
 */
class LikeAnimView : FrameLayout {

    private val likeImages = mutableListOf<Int>()
    private val likeViewSizeSmall = 89
    private var likeImageIndex = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        likeImages.apply {
            add(R.drawable.heart1)
            add(R.drawable.heart2)
            add(R.drawable.heart3)
            add(R.drawable.heart4)
            add(R.drawable.heart5)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val modeW = MeasureSpec.getMode(widthMeasureSpec)
        val modeH = MeasureSpec.getMode(heightMeasureSpec)
        if (modeW != MeasureSpec.EXACTLY || modeH != MeasureSpec.EXACTLY) {
            // 简单处理，使用的地方一定要指定整个区域的宽高
            throw IllegalStateException("width and height must exactly!")
        }
    }

    fun addLike() {
        val imageView = ImageView(context)
        imageView.layoutParams = ViewGroup.LayoutParams(likeViewSizeSmall, likeViewSizeSmall)
        imageView.setImageResource(likeImages[likeImageIndex++ % likeImages.size])
        // 指定初始位置为底部居中
        imageView.x = ((measuredWidth - imageView.layoutParams.width) / 2).toFloat()
        imageView.y = (measuredHeight - imageView.layoutParams.width).toFloat()
        this.addView(imageView)

        // 开启动画
        startAnim(imageView)
    }

    private fun startAnim(target: View) {
        val enterAnim = generateEnterAnim(target)
        val curveAnim = generateCurveAnim(target)
        val anim = AnimatorSet().apply {
            playSequentially(enterAnim, curveAnim)
        }
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                removeView(target)
            }
        })
        anim.start()
    }

    /**
     * 生成❤️出现动画
     */
    private fun generateEnterAnim(target: View): AnimatorSet {
        val alpha = ObjectAnimator.ofFloat(target, "alpha", 0.2f, 1f)
        val scaleX = ObjectAnimator.ofFloat(target, "scaleX", 0.5f, 1f)
        val scaleY = ObjectAnimator.ofFloat(target, "scaleY", 0.5f, 1f)
        return AnimatorSet().apply {
            playTogether(alpha, scaleX, scaleY)
            duration = 150
        }
    }

    /**
     * 生成❤️上浮轨迹动画
     */
    private fun generateCurveAnim(target: View): ValueAnimator {
        // 起始点，以target的初始位置为准
        val startPointF = PointF(target.x, target.y)
        // 结束点，顶部，给个随机范围
        val endPointF = PointF(
            (measuredWidth / 2 + (if (Random.nextBoolean()) 1 else -1) * (0..(measuredWidth / 3)).random()).toFloat(),
            0f
        )
        // 塞贝尔曲线第一个控制点
        val pointF1 = PointF(
            Random.nextInt(measuredWidth - 100).toFloat(),
            Random.nextInt(measuredHeight / 2).toFloat()
        )
        // 塞贝尔曲线第二个控制点
        val pointF2 = PointF(
            Random.nextInt(measuredWidth - 100).toFloat(),
            (Random.nextInt(measuredHeight / 2) + measuredHeight / 2).toFloat()
        )
        // 估值器
        val evaluator = CurveEvaluator(pointF2, pointF1)
        val valueAnimator = ValueAnimator.ofObject(evaluator, startPointF, endPointF).apply {
            duration = 2000
            interpolator = LinearInterpolator() // 线性插值器
        }
        valueAnimator.setTarget(target)
        valueAnimator.addUpdateListener { animator ->
            val pointF = animator.animatedValue as PointF
            target.apply {
                x = pointF.x
                y = pointF.y
                alpha = 1 - animator.animatedFraction
            }
        }
        return valueAnimator
    }

    /**
     * 估值器（根据当前属性值改变的百分比来计算改变后的属性值）
     * 三阶贝塞尔曲线，两个控制点
     */
    class CurveEvaluator(
        private val ctrlPointF1: PointF,
        private val ctrlPointF2: PointF
    ) : TypeEvaluator<PointF> {
        override fun evaluate(fraction: Float, startValue: PointF, endValue: PointF): PointF {
            // 三阶贝塞尔曲线公式 B(t) = P0 * (1-t)^3 + 3 * P1 * t * (1-t)^2 + 3 * P2 * t^2 * (1-t) + P3 * t^3, t ∈ [0,1]
            // P0、P3分别为起始点和终止点，P1、P2为两个控制点，t为曲线长度比例
            val leftFraction = 1.0f - fraction
            return PointF().apply {
                x = leftFraction.pow(3) * startValue.x +
                        3 * leftFraction.pow(2) * fraction * ctrlPointF1.x +
                        3 * leftFraction * fraction.pow(2) * ctrlPointF2.x +
                        fraction.pow(3) * endValue.x
                y = leftFraction.pow(3) * startValue.y +
                        3 * leftFraction.pow(2) * fraction * ctrlPointF1.y +
                        3 * leftFraction * fraction.pow(2) * ctrlPointF2.y +
                        fraction.pow(3) * endValue.y
            }
        }
    }
}