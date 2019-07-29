package com.taleb.passfield

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.AppCompatEditText
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.LayoutDirection
import android.util.TypedValue
import android.view.MotionEvent

class PasswordTextField : AppCompatEditText {
    private var showPasswordDrawable: Drawable? = null
    private var hidePasswordDrawable: Drawable? = null
    private var passwordIsVisible = false

    constructor(context: Context) : super(context) {
        initializeView(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initializeView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initializeView(attrs)
    }

    private fun initializeView(attrs: AttributeSet?) {
        inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        transformationMethod = PasswordTransformationMethod.getInstance()
        if (attrs != null) {
            val attributes = context.theme
                .obtainStyledAttributes(attrs, R.styleable.PasswordTextField, 0, 0)
            val drawableTint = attributes
                .getColor(R.styleable.PasswordTextField_drawableTintCompat, WITHOUT_TINT)
            showPasswordDrawable = getDrawable(attributes, R.styleable.PasswordTextField_showDrawable, drawableTint)
            hidePasswordDrawable = getDrawable(attributes, R.styleable.PasswordTextField_hideDrawable, drawableTint)
            if (showPasswordDrawable == null) {
                showPasswordDrawable = getDrawable(
                    resources
                        .getDrawable(R.drawable.ic_password_visible_24dp), drawableTint
                )
            }
            if (hidePasswordDrawable == null) {
                hidePasswordDrawable = getDrawable(
                    resources
                        .getDrawable(R.drawable.ic_password_hidden_24dp), drawableTint
                )
            }

            val fontSrc = attributes.getString(R.styleable.PasswordTextField_fontAssetSrc)
            if (fontSrc != null)
                setFontAssetSrc(fontSrc)

            attributes.recycle()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (showPasswordDrawable != null && hidePasswordDrawable != null) {
            putDrawable(showPasswordDrawable)
            compoundDrawablePadding = dp2px(DRAWABLE_PADDING_IN_DP)
        }
    }

    private fun putDrawable(drawable: Drawable?) {
        if (layoutDirection == LayoutDirection.RTL) {
            setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        }
    }

    fun setFontAssetSrc(fontSrc: String) {
        try {
            typeface = Typeface.createFromAsset(resources.assets, fontSrc)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getDrawable(attributes: TypedArray, attribute: Int, tint: Int): Drawable? {
        val drawable = attributes.getDrawable(attribute)
        return getDrawable(drawable, tint)
    }

    private fun getDrawable(drawable: Drawable?, tint: Int): Drawable? {
        if (drawable != null && tint != WITHOUT_TINT) {
            val drwbl = DrawableCompat.wrap(drawable)
            DrawableCompat.setTint(drwbl, tint)
        }
        return drawable
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (showPasswordDrawable != null && hidePasswordDrawable != null) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                val drawableWidth = showPasswordDrawable!!.intrinsicWidth
                val minimumX = measuredWidth - drawableWidth
                if (layoutDirection == LayoutDirection.LTR) {
                    if (event.x >= minimumX) {
                        changeVisibilityMode()
                    }
                } else {
                    if (event.x <= drawableWidth) {
                        changeVisibilityMode()
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun changeVisibilityMode() {
        requestFocus()
        if (!passwordIsVisible) {
            putDrawable(hidePasswordDrawable)
            transformationMethod = HideReturnsTransformationMethod.getInstance()
            passwordIsVisible = true
        } else {
            putDrawable(showPasswordDrawable)
            transformationMethod = PasswordTransformationMethod.getInstance()
            passwordIsVisible = false
        }
    }

    private fun dp2px(value: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    companion object {
        private const val WITHOUT_TINT = -1
        private const val DRAWABLE_PADDING_IN_DP = 8
    }
}
