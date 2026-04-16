package com.bapinaev.flighttracker.ui.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bapinaev.flighttracker.MainActivity
import com.bapinaev.flighttracker.R
import com.bapinaev.flighttracker.di.AppGraph
import com.bapinaev.flighttracker.sdui.SduiAction
import com.bapinaev.flighttracker.sdui.SduiFactory
import com.bapinaev.flighttracker.sdui.SduiLoader
import kotlinx.coroutines.launch
import com.bapinaev.flighttracker.designsystem.R as DsR

class ProfileActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(
            getUserUseCase = AppGraph.getUserUseCase,
            saveUserUseCase = AppGraph.saveUserUseCase,
            removeUserUseCase = AppGraph.removeUserUseCase,
            logoutUserUseCase = AppGraph.logoutUserUseCase,
            sessionManager = AppGraph.sessionManager
        )
    }
    private val sduiLoader = SduiLoader()

    private var nameView: TextView? = null
    private var loginView: TextView? = null
    private var ageView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppGraph.ensureInitialized(applicationContext)
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
                            is ProfileEvent.RequestEditProfile -> showEditProfileDialog(event.user)
                            ProfileEvent.ConfirmDeleteAccount -> showDeleteConfirmationDialog()
                            ProfileEvent.ProfileUpdated -> {
                                Toast.makeText(
                                    this@ProfileActivity,
                                    R.string.profile_updated,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is ProfileEvent.OperationFailed -> {
                                Toast.makeText(
                                    this@ProfileActivity,
                                    event.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
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

    private fun showEditProfileDialog(user: com.bapinaev.domain.model.User) {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            val pad = (20 * resources.displayMetrics.density + 0.5f).toInt()
            setPadding(pad, pad, pad, 0)
        }

        val firstNameInput = EditText(this).apply {
            hint = getString(R.string.profile_edit_first_name)
            setText(user.firstName)
        }

        val surnameInput = EditText(this).apply {
            hint = getString(R.string.profile_edit_surname)
            setText(user.surname)
        }

        val ageInput = EditText(this).apply {
            hint = getString(R.string.profile_edit_age)
            inputType = InputType.TYPE_CLASS_NUMBER
            setText(user.age.toString())
        }

        val passwordInput = EditText(this).apply {
            hint = getString(R.string.profile_edit_password)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
            }
            setText(user.password)
        }

        container.addView(firstNameInput)
        container.addView(surnameInput)
        container.addView(ageInput)
        container.addView(passwordInput)

        AlertDialog.Builder(this)
            .setTitle(R.string.profile_edit_dialog_title)
            .setView(container)
            .setNegativeButton(R.string.profile_edit_cancel, null)
            .setPositiveButton(R.string.profile_edit_save) { _, _ ->
                viewModel.onProfileEditSubmitted(
                    firstName = firstNameInput.text?.toString().orEmpty(),
                    surname = surnameInput.text?.toString().orEmpty(),
                    ageInput = ageInput.text?.toString().orEmpty(),
                    password = passwordInput.text?.toString().orEmpty()
                )
            }
            .show()
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.profile_delete_dialog_title)
            .setMessage(R.string.profile_delete_dialog_message)
            .setNegativeButton(R.string.profile_delete_cancel, null)
            .setPositiveButton(R.string.profile_delete_confirm) { _, _ ->
                viewModel.onDeleteConfirmed()
            }
            .show()
    }
}
