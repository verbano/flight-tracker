package com.bapinaev.flighttracker.sdui

data class SduiNode(
    val type: String = "",
    val id: String? = null,
    val text: String? = null,
    val style: String? = null,
    val variant: String? = null,
    val background: String? = null,
    val src: String? = null,
    val size: Int? = null,
    val minHeight: Int? = null,
    val layoutGravity: String? = null,
    val textAlign: String? = null,
    val paddingHorizontal: Int? = null,
    val paddingTop: Int? = null,
    val paddingBottom: Int? = null,
    val marginTop: Int? = null,
    val marginBottom: Int? = null,
    val marginStart: Int? = null,
    val child: SduiNode? = null,
    val children: List<SduiNode>? = null
)
