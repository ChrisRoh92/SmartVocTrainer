package com.example.voctrainer

import android.animation.Animator
import android.widget.TextView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import java.text.SimpleDateFormat
import java.util.*


fun createCurrentTimeStamp(format:Int = 0):String
{
    var date = Date()
    var formatter:SimpleDateFormat? = null
    if(format == 0)
    {
        formatter = SimpleDateFormat("dd.MM.yyyy - hh:mm")
    }
    else if(format == 1)
    {
        formatter = SimpleDateFormat("dd.MM.yyyy")
    }


    return formatter!!.format(date)
}


fun animateVocText(tv: TextView,input:String,color:Int,callback:()->Unit)
{

    tv.animate()
        .alpha(0f)
        .scaleY(0f)
        .scaleX(0f)
        .setDuration(250).setInterpolator(FastOutSlowInInterpolator())
        .setListener(object: Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                tv.text = input
                tv.setTextColor(color)
                tv.animate()
                    .alpha(1f)
                    .scaleY(1f)
                    .scaleX(1f)
                    .setDuration(250).setInterpolator(FastOutSlowInInterpolator())
                    .setListener(object: Animator.AnimatorListener{
                        override fun onAnimationRepeat(animation: Animator?) {

                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            callback()
                        }

                        override fun onAnimationCancel(animation: Animator?) {

                        }

                        override fun onAnimationStart(animation: Animator?) {

                        }

                    })
                    .start()

            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })
        .start()
    tv.clearAnimation()

}

fun checkForLearnStatus(status:Int):Int
{
    var value = 0
    if(status == 0)
    {
        value = 0
    }
    else if(status in 1..4)
    {
        value = 1
    }
    else if(status >= 5)
    {
        value = 2
    }

    return value
}