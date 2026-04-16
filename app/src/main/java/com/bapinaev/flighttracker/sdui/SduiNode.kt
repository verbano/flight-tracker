package com.bapinaev.flighttracker.sdui

data class SduiLayout(
    val id: String? = null,
    val size: Int? = null,
    val minHeight: Int? = null,
    val layoutGravity: String? = null,
    val textAlign: String? = null,
    val paddingHorizontal: Int? = null,
    val paddingTop: Int? = null,
    val paddingBottom: Int? = null,
    val marginTop: Int? = null,
    val marginBottom: Int? = null,
    val marginStart: Int? = null
)

sealed interface SduiAction {
    data class Navigate(val target: String) : SduiAction
    data class Toast(val message: String) : SduiAction
    data class Custom(val value: String) : SduiAction
}

sealed interface SduiNode {
    val layout: SduiLayout
}

data class SduiScrollNode(
    override val layout: SduiLayout = SduiLayout(),
    val background: String? = null,
    val child: SduiNode? = null
) : SduiNode

data class SduiFrameNode(
    override val layout: SduiLayout = SduiLayout(),
    val children: List<SduiNode> = emptyList()
) : SduiNode

data class SduiColumnNode(
    override val layout: SduiLayout = SduiLayout(),
    val children: List<SduiNode> = emptyList()
) : SduiNode

data class SduiCardNode(
    override val layout: SduiLayout = SduiLayout(),
    val children: List<SduiNode> = emptyList()
) : SduiNode

data class SduiTextNode(
    override val layout: SduiLayout = SduiLayout(),
    val text: String? = null,
    val style: String? = null
) : SduiNode

data class SduiButtonNode(
    override val layout: SduiLayout = SduiLayout(),
    val text: String? = null,
    val variant: String? = null,
    val action: SduiAction? = null
) : SduiNode

data class SduiImageNode(
    override val layout: SduiLayout = SduiLayout(),
    val src: String? = null
) : SduiNode

data class SduiDividerNode(
    override val layout: SduiLayout = SduiLayout()
) : SduiNode

data class SduiBackButtonNode(
    override val layout: SduiLayout = SduiLayout(),
    val action: SduiAction? = null
) : SduiNode
