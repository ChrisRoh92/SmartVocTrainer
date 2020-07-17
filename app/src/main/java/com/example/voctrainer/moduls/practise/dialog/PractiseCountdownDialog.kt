package com.example.voctrainer.moduls.practise.dialog


import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

import com.example.voctrainer.R
import kotlinx.coroutines.*

class PractiseCountdownDialog(var duration:Int): DialogFragment()
{
    // Allgemeine Variablen:
    private lateinit var dialogView: View
    private lateinit var mListener:OnReadyListener

    // View Elemente:
    private lateinit var tvCountdown:TextView

    // Coroutine Stuff:
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main+viewModelJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        dialogView = inflater.inflate(R.layout.dialog_practise_countdown, container, false)

        initView()
        initTimer()

        return dialogView
    }

    private fun initView()
    {
        tvCountdown = dialogView.findViewById(R.id.dialog_practise_countdown_tv)
        tvCountdown.text = "$duration"

    }

    private fun initTimer()
    {
        uiScope.launch {
            delay(1000)

            for(i in 0 until duration)
            {
                var before = System.currentTimeMillis()
                var input = if(i < duration-1) "${duration-(i+1)}" else "Engage"
                setNewNumber(input) {
                    // Ensure the Animation is over!
                }
                delay(1000-(System.currentTimeMillis()-before))
            }
            delay(200)
            mListener?.setOnReadyListener()

        }
    }

    private fun setNewNumber(n:String, callback:()->Unit)
    {
        // Am Anfang ist es da
            tvCountdown.clearAnimation()
            tvCountdown.animate()
                .alpha(0f)
                .scaleY(0f)
                .scaleX(0f)
                .setDuration(250).setInterpolator(FastOutSlowInInterpolator())
                .setListener(object:Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        tvCountdown.text = "$n"
                        tvCountdown.animate()
                            .alpha(1f)
                            .scaleY(1f)
                            .scaleX(1f)
                            .setDuration(250).setInterpolator(FastOutSlowInInterpolator())
                            .setListener(object:Animator.AnimatorListener{
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

        }

    interface OnReadyListener
    {
        fun setOnReadyListener()
    }

    fun setOnReadyListener(mListener:OnReadyListener)
    {
        this.mListener = mListener
    }




    }
