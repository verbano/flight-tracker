package com.bapinaev.flighttracker.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bapinaev.flighttracker.MainActivity
import com.bapinaev.flighttracker.R
import com.bapinaev.flighttracker.sdui.SduiAction
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
                val root = SduiFactory(
                    context = this@ProfileActivity,
                    onAction = ::handleSduiAction
                ).buildView(node)
                setContentView(root)
                bindSdui(root)
            } else {
                showNoConnection {
                    showLoading()
                    lifecycleScope.launch {
                        val retry = sduiLoader.loadScreen("bapinaev/profile")
                        if (retry != null) {
                            val root = SduiFactory(
                                context = this@ProfileActivity,
                                onAction = ::handleSduiAction
                            ).buildView(retry)
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
        setContentView(R.layout.sdui_no_connection)
        findViewById<Button>(R.id.btn_sdui_retry).setOnClickListener { onRetry() }
        findViewById<Button>(R.id.btn_sdui_skip).setOnClickListener {
            setContentView(R.layout.profile_screen)
            bindXml()
            observeViewModel()
        }
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
        nameView = root.findViewWithTag("name")
        loginView = root.findViewWithTag("login")
        ageView = root.findViewWithTag("age")
    }

    private fun handleSduiAction(action: SduiAction) {
        when (action) {
            is SduiAction.Navigate -> when (action.target) {
                "back" -> viewModel.onBackClicked()
                "edit_profile" -> viewModel.onEditClicked()
                "logout" -> viewModel.onLogoutClicked()
                "delete_account" -> viewModel.onDeleteClicked()
            }
            is SduiAction.Toast -> Toast.makeText(this, action.message, Toast.LENGTH_SHORT).show()
            is SduiAction.Custom -> Unit
        }
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
