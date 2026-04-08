package com.bapinaev.flighttracker.sdui

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.bapinaev.flighttracker.designsystem.R as DsR

class SduiFactory(private val context: Context) {

    private val density = context.resources.displayMetrics.density

    private fun dp(value: Int): Int = (value * density + 0.5f).toInt()

    fun buildView(node: SduiNode): View {
        val view = when (node.type) {
            "scroll" -> buildScroll(node)
            "frame" -> buildFrame(node)
            "column" -> buildColumn(node)
            "card" -> buildCard(node)
            "text" -> buildText(node)
            "button" -> buildButton(node)
            "image" -> buildImage(node)
            "divider" -> buildDivider(node)
            "back_button" -> buildBackButton(node)
            else -> View(context)
        }
        node.id?.let { view.tag = it }
        return view
    }

    private fun buildScroll(node: SduiNode): ScrollView {
        val sv = ScrollView(context)
        sv.isFillViewport = true
        sv.fitsSystemWindows = true
        if (node.background == "gradient") {
            sv.setBackgroundResource(DsR.drawable.bg_screen_gradient)
        }
        node.child?.let {
            sv.addView(
                buildView(it),
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
            )
        }
        return sv
    }

    private fun buildFrame(node: SduiNode): FrameLayout {
        val frame = FrameLayout(context)
        node.minHeight?.let { frame.minimumHeight = dp(it) }
        node.children?.forEach { child ->
            val childView = buildView(child)
            val w = child.size?.let { dp(it) } ?: FrameLayout.LayoutParams.MATCH_PARENT
            val h = child.size?.let { dp(it) } ?: FrameLayout.LayoutParams.WRAP_CONTENT
            val lp = FrameLayout.LayoutParams(w, h)
            lp.gravity = parseGravity(child.layoutGravity)
            applyMargins(lp, child)
            frame.addView(childView, lp)
        }
        return frame
    }

    private fun buildColumn(node: SduiNode): LinearLayout {
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.VERTICAL
        ll.clipToPadding = false
        applyPadding(ll, node)
        addLinearChildren(ll, node)
        return ll
    }

    private fun buildCard(node: SduiNode): LinearLayout {
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.VERTICAL
        ll.setBackgroundResource(DsR.drawable.bg_profile_card)
        ll.elevation = context.resources.getDimension(DsR.dimen.ds_elevation_profile_card)
        val padH = context.resources.getDimensionPixelSize(DsR.dimen.ds_space_xxl)
        ll.setPadding(padH, dp(26), padH, padH)
        addLinearChildren(ll, node)
        return ll
    }

    private fun addLinearChildren(parent: LinearLayout, node: SduiNode) {
        node.children?.forEach { child ->
            parent.addView(buildView(child), createLinearChildParams(child))
        }
    }

    private fun createLinearChildParams(child: SduiNode): LinearLayout.LayoutParams {
        val lp = when {
            child.type == "divider" -> LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(1)
            )
            child.size != null -> LinearLayout.LayoutParams(dp(child.size), dp(child.size))
            child.layoutGravity?.contains("center_horizontal") == true -> LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            else -> LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        lp.gravity = parseGravity(child.layoutGravity)
        applyMargins(lp, child)
        return lp
    }

    private fun buildText(node: SduiNode): TextView {
        val tv = TextView(context)
        node.text?.let { tv.text = it }
        resolveTextStyle(node.style)?.let { TextViewCompat.setTextAppearance(tv, it) }
        if (node.textAlign == "center") tv.gravity = Gravity.CENTER
        if (node.style == "headline") tv.setLineSpacing(dp(2).toFloat(), 1f)
        return tv
    }

    private fun buildButton(node: SduiNode): Button {
        val btn = Button(context)
        node.text?.let { btn.text = it }
        btn.isAllCaps = false
        btn.letterSpacing = 0.01f
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)

        when (node.variant) {
            "primary", "primaryCompact" -> {
                btn.setBackgroundResource(DsR.drawable.bg_button_blue_round)
                btn.setTextColor(ContextCompat.getColor(context, DsR.color.white))
                btn.setTypeface(btn.typeface, Typeface.BOLD)
            }
            "outlined" -> {
                btn.setBackgroundResource(DsR.drawable.bg_button_outline_round)
                btn.setTextColor(ContextCompat.getColor(context, DsR.color.text_primary))
            }
            "danger" -> {
                btn.setBackgroundResource(DsR.drawable.bg_button_danger_round)
                btn.setTextColor(ContextCompat.getColor(context, DsR.color.danger_button))
            }
        }

        val heightRes = if (node.variant == "primary") {
            DsR.dimen.ds_button_min_height
        } else {
            DsR.dimen.ds_button_min_height_compact
        }
        btn.minimumHeight = context.resources.getDimensionPixelSize(heightRes)

        return btn
    }

    private fun buildImage(node: SduiNode): ImageView {
        val iv = ImageView(context)
        if (node.src == "avatar_placeholder") {
            iv.setBackgroundResource(DsR.drawable.bg_profile_avatar_ring)
            val pad = dp(5)
            iv.setPadding(pad, pad, pad, pad)
            iv.scaleType = ImageView.ScaleType.CENTER_CROP
            iv.setImageResource(android.R.drawable.sym_def_app_icon)
        }
        return iv
    }

    private fun buildDivider(node: SduiNode): View {
        val v = View(context)
        v.setBackgroundColor(ContextCompat.getColor(context, DsR.color.stroke_soft))
        return v
    }

    private fun buildBackButton(node: SduiNode): ImageButton {
        val btn = ImageButton(context)
        val outValue = TypedValue()
        context.theme.resolveAttribute(
            android.R.attr.selectableItemBackgroundBorderless, outValue, true
        )
        btn.setBackgroundResource(outValue.resourceId)
        val pad = context.resources.getDimensionPixelSize(DsR.dimen.ds_space_md)
        btn.setPadding(pad, pad, pad, pad)
        btn.setImageResource(DsR.drawable.ic_back_profile)
        return btn
    }

    private fun resolveTextStyle(style: String?): Int? = when (style) {
        "display" -> DsR.style.TextAppearance_FlightTracker_Display
        "headline" -> DsR.style.TextAppearance_FlightTracker_Headline
        "title" -> DsR.style.TextAppearance_FlightTracker_Title
        "titleSmall" -> DsR.style.TextAppearance_FlightTracker_TitleSmall
        "body" -> DsR.style.TextAppearance_FlightTracker_Body
        "bodySecondary" -> DsR.style.TextAppearance_FlightTracker_BodySecondary
        "subtitle" -> DsR.style.TextAppearance_FlightTracker_Subtitle
        "caption" -> DsR.style.TextAppearance_FlightTracker_Caption
        "overline" -> DsR.style.TextAppearance_FlightTracker_Overline
        "emphasis" -> DsR.style.TextAppearance_FlightTracker_Emphasis
        "emptyState" -> DsR.style.TextAppearance_FlightTracker_EmptyState
        else -> null
    }

    private fun parseGravity(gravity: String?): Int {
        if (gravity == null) return Gravity.NO_GRAVITY
        var result = Gravity.NO_GRAVITY
        gravity.split("|").forEach {
            result = result or when (it.trim()) {
                "center" -> Gravity.CENTER
                "center_horizontal" -> Gravity.CENTER_HORIZONTAL
                "center_vertical" -> Gravity.CENTER_VERTICAL
                "top" -> Gravity.TOP
                "bottom" -> Gravity.BOTTOM
                "start" -> Gravity.START
                "end" -> Gravity.END
                else -> Gravity.NO_GRAVITY
            }
        }
        return result
    }

    private fun applyPadding(view: View, node: SduiNode) {
        val ph = node.paddingHorizontal?.let { dp(it) } ?: 0
        val pt = node.paddingTop?.let { dp(it) } ?: 0
        val pb = node.paddingBottom?.let { dp(it) } ?: 0
        view.setPadding(ph, pt, ph, pb)
    }

    private fun applyMargins(lp: ViewGroup.MarginLayoutParams, node: SduiNode) {
        node.marginTop?.let { lp.topMargin = dp(it) }
        node.marginBottom?.let { lp.bottomMargin = dp(it) }
        node.marginStart?.let { lp.marginStart = dp(it) }
    }
}
