package com.bapinaev.flighttracker.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bapinaev.flighttracker.MainActivity
import com.bapinaev.flighttracker.R
import com.bapinaev.flighttracker.sdui.SduiFactory
import com.bapinaev.flighttracker.sdui.SduiLoader
import kotlinx.coroutines.launch
import com.bapinaev.flighttracker.designsystem.R as DsR

class ProfileActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels()
    private val sduiLoader = SduiLoader()

    private var nameView: TextView? = null
    private var loginView: TextView? = null
    private var ageView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLoading()

        lifecycleScope.launch {
            val node = sduiLoader.loadScreen("bapinaev/profile")
            if (node != null) {
                val root = SduiFactory(this@ProfileActivity).buildView(node)
                setContentView(root)
                bindSdui(root)
            } else {
                showNoConnection {
                    showLoading()
                    lifecycleScope.launch {
                        val retry = sduiLoader.loadScreen("bapinaev/profile")
                        if (retry != null) {
                            val root = SduiFactory(this@ProfileActivity).buildView(retry)
                            setContentView(root)
                            bindSdui(root)
                        } else {
                            setContentView(R.layout.profile_screen)
                            bindXml()
                        }
                        observeViewModel()
                    }
                }
                return@launch
            }
            observeViewModel()
        }
    }

    private fun showNoConnection(onRetry: () -> Unit) {
        val density = resources.displayMetrics.density
        fun dp(v: Int) = (v * density + 0.5f).toInt()

        val scroll = ScrollView(this)
        scroll.isFillViewport = true
        scroll.fitsSystemWindows = true
        scroll.setBackgroundResource(DsR.drawable.bg_screen_gradient)

        val column = LinearLayout(this)
        column.orientation = LinearLayout.VERTICAL
        column.gravity = Gravity.CENTER_HORIZONTAL
        column.setPadding(dp(24), 0, dp(24), 0)

        val icon = TextView(this)
        icon.text = "✈"
        icon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 64f)
        icon.gravity = Gravity.CENTER
        column.addView(icon, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { gravity = Gravity.CENTER_HORIZONTAL })

        val title = TextView(this)
        title.text = getString(R.string.sdui_no_connection_title)
        TextViewCompat.setTextAppearance(title, DsR.style.TextAppearance_FlightTracker_Headline)
        title.gravity = Gravity.CENTER
        column.addView(title, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            topMargin = dp(16)
        })

        val subtitle = TextView(this)
        subtitle.text = getString(R.string.sdui_no_connection_subtitle)
        TextViewCompat.setTextAppearance(subtitle, DsR.style.TextAppearance_FlightTracker_BodySecondary)
        subtitle.gravity = Gravity.CENTER
        column.addView(subtitle, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            topMargin = dp(8)
        })

        val retryBtn = Button(this)
        retryBtn.text = getString(R.string.sdui_retry)
        retryBtn.isAllCaps = false
        retryBtn.letterSpacing = 0.01f
        retryBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        retryBtn.setBackgroundResource(DsR.drawable.bg_button_blue_round)
        retryBtn.setTextColor(ContextCompat.getColor(this, DsR.color.white))
        retryBtn.minimumHeight = resources.getDimensionPixelSize(DsR.dimen.ds_button_min_height_compact)
        retryBtn.setOnClickListener { onRetry() }
        column.addView(retryBtn, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { topMargin = dp(24) })

        val skipBtn = Button(this)
        skipBtn.text = getString(R.string.sdui_skip)
        skipBtn.isAllCaps = false
        skipBtn.letterSpacing = 0.01f
        skipBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        skipBtn.setBackgroundResource(DsR.drawable.bg_button_outline_round)
        skipBtn.setTextColor(ContextCompat.getColor(this, DsR.color.text_primary))
        skipBtn.minimumHeight = resources.getDimensionPixelSize(DsR.dimen.ds_button_min_height_compact)
        skipBtn.setOnClickListener {
            setContentView(R.layout.profile_screen)
            bindXml()
            observeViewModel()
        }
        column.addView(skipBtn, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { topMargin = dp(12) })

        val wrapper = FrameLayout(this)
        wrapper.addView(column, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        ))

        scroll.addView(wrapper, FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        ))

        setContentView(scroll)
    }

    private fun showLoading() {
        val container = FrameLayout(this)
        container.setBackgroundResource(DsR.drawable.bg_screen_gradient)
        container.addView(
            ProgressBar(this),
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        )
        setContentView(container)
    }

    private fun bindSdui(root: View) {
        root.findViewWithTag<ImageButton>("backBtn")?.setOnClickListener { viewModel.onBackClicked() }
        root.findViewWithTag<Button>("editBtn")?.setOnClickListener { viewModel.onEditClicked() }
        root.findViewWithTag<Button>("logoutBtn")?.setOnClickListener { viewModel.onLogoutClicked() }
        root.findViewWithTag<Button>("deleteBtn")?.setOnClickListener { viewModel.onDeleteClicked() }
        nameView = root.findViewWithTag("name")
        loginView = root.findViewWithTag("login")
        ageView = root.findViewWithTag("age")
    }

    private fun bindXml() {
        findViewById<ImageButton>(R.id.backBtn).setOnClickListener { viewModel.onBackClicked() }
        findViewById<Button>(R.id.editBtn).setOnClickListener { viewModel.onEditClicked() }
        findViewById<Button>(R.id.logoutBtn).setOnClickListener { viewModel.onLogoutClicked() }
        findViewById<Button>(R.id.deleteBtn).setOnClickListener { viewModel.onDeleteClicked() }
        nameView = findViewById(R.id.name)
        loginView = findViewById(R.id.login)
        ageView = findViewById(R.id.age)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        nameView?.text = getString(
                            R.string.user_full_name,
                            state.user.firstName,
                            state.user.surname
                        )
                        loginView?.text = getString(R.string.user_login, state.user.login)
                        ageView?.text = getString(R.string.user_age, state.user.age)
                    }
                }
                launch {
                    viewModel.events.collect { event ->
                        when (event) {
                            ProfileEvent.ShowEditSoon ->
                                Toast.makeText(this@ProfileActivity, R.string.profile_edit_soon, Toast.LENGTH_SHORT).show()
                            ProfileEvent.ShowDeleteSoon ->
                                Toast.makeText(this@ProfileActivity, R.string.profile_delete_soon, Toast.LENGTH_SHORT).show()
                            ProfileEvent.NavigateToLogin -> {
                                startActivity(Intent(this@ProfileActivity, LoginActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                })
                                finish()
                            }
                            ProfileEvent.NavigateToMain -> {
                                startActivity(Intent(this@ProfileActivity, MainActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                })
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }
}
