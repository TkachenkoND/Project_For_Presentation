package com.online.bk.olimpbet.presentation.view.fragment

import android.content.pm.ActivityInfo
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import com.online.bk.olimpbet.data.helper.EXIT
import com.online.bk.olimpbet.databinding.GameFragmentBinding
import com.online.bk.olimpbet.presentation.view.dialog.CustomDialog
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameFragment : BaseFragment<GameFragmentBinding>() {

    private var isSound = true

    private var pointt = 0
    private var goals = 5
    private var isPoint = true
    private var count = 0
    private var ballSize = 6

    private var job: Job? = null
    private val handler = Handler(Looper.getMainLooper())

    private var isStartGame = true

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        GameFragmentBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBackPressed()
        sharedVm.saveFlagZaglushkaInSharedVm()

        initClickListener()
    }

    private fun initClickListener() {
        startTracking()

        binding.apply {
            pointt = 0

            ball.setOnClickListener {
                it.isEnabled = false
                startFun()
            }

            btnPause.setOnClickListener {
                ball.isEnabled = false
                pauseView.visibility = View.VISIBLE
            }

            btnSound.setOnClickListener {
                if (isSound){
                    isSound = false
                    Toast.makeText(requireContext(),"Sound Off",Toast.LENGTH_SHORT).show()
                } else {
                    isSound = false
                    Toast.makeText(requireContext(),"Sound On",Toast.LENGTH_SHORT).show()
                }
            }

            btnPlay.setOnClickListener {
                pauseView.visibility = View.GONE
                ball.isEnabled = true
            }

            btnBack.setOnClickListener {
                stopTracking()
                job!!.cancel()
                goToNextFragment(MenuGameFragment())
            }

        }

        startGoalkeeperMovement()

    }

    private fun startFun() {
        binding.apply {
            isPoint = true

            count++
            ballSize--

            countBall.text = ballSize.toString()

            if (count < 6){
                moveBallToRandomPosition()
            }
            else if(count == 6) {
                isStartGame = false
                moveBallToRandomPosition()

                stopTracking()
                goalsTxt.text = "You scored goals $goals"
                binding.ball.isClickable = false

                lifecycleScope.launch {
                    delay(1000)
                    goalsTxt.visibility = View.VISIBLE
                    delay(1500)
                    job!!.cancel()
                    goToNextFragment(MenuGameFragment())
                }
            }
        }
    }

    private fun startGoalkeeperMovement() {
        val vorotaWidth = 230f

        job = viewLifecycleOwner.lifecycleScope.launch {
            while (isStartGame) {
                binding.player.animate().apply {
                    translationX(-vorotaWidth)
                    duration = 4000
                }
                delay(4200)
                binding.player.animate().apply {
                    translationX(vorotaWidth)
                    duration = 4000
                }
                delay(4200)
            }
        }

    }

    private val moveRunnable = object : Runnable {
        override fun run() {
            if (checkCollision() && isPoint) {
                goals--
                isPoint = false
            }

            handler.postDelayed(this, 100)
        }
    }

    private fun stopTracking() {
        handler.removeCallbacks(moveRunnable)
    }

    private fun startTracking() {
        handler.post(moveRunnable)
    }

    private fun moveBallToRandomPosition() {
        val vorotaTop = binding.vorota.top + binding.ball.height
        val vorotaBottom = binding.vorota.bottom - binding.ball.height
        val randomX =
            (binding.vorota.left + binding.ball.width..binding.vorota.right - binding.ball.width).random()
                .toFloat()
        val randomY = (vorotaTop..vorotaBottom).random().toFloat()

        binding.ball.animate().apply {

            x(randomX)
            y(randomY)
            duration = 500

            withEndAction {

                binding.ball.animate().apply {

                    translationX(0f)
                    translationY(0f)
                    duration = 0
                }
                binding.ball.isEnabled = true
            }
        }
    }

    private fun checkCollision(): Boolean {
        val goalkeeperRect = Rect()
        binding.player.getGlobalVisibleRect(goalkeeperRect)

        val ballRect = Rect()
        binding.ball.getGlobalVisibleRect(ballRect)

        if (Rect.intersects(goalkeeperRect, ballRect)) {
            return true
        }

        return false
    }

    private fun initBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    stopTracking()
                    job!!.cancel()
                    CustomDialog.instance(EXIT)
                        .show(parentFragmentManager, CustomDialog.TAG)
                }
            })
    }

}