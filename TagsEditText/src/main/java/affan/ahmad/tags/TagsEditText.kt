package affan.ahmad.tags

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.flexbox.FlexWrap.WRAP
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.JustifyContent
import java.util.*

/* Copyright (C) 2019 Affan Ahmad Fahmi - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the MIT license.
 */

class TagsEditText : FlexboxLayout {

    private var editText: EditText? = null
    private var mContext: Context? = null
    private var maxCount = 4

    private val tagsLinearLayoutList = ArrayList<LinearLayout>()
    private val tagsList = ArrayList<String>()

    private var tagsListener: TagsListener? = null

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    constructor(context: Context) : super(context) {
        init(context)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun init(context: Context) {
        mContext = context
        editText = EditText(context)

        this.apply {
            flexWrap = WRAP
            justifyContent = JustifyContent.FLEX_START
            setBackgroundResource(R.drawable.bg)
            layoutParams = FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.MATCH_PARENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
            )
        }

        editText!!.apply {
            textDirection = View.TEXT_DIRECTION_LTR
            setBackgroundResource(0)
            layoutParams = FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
            )
            minWidth = 100
            gravity = Gravity.START
            //setTextColor(Color.GRAY)
            //typeface = Typeface.createFromAsset(context.assets, "fonts/OpenSans-SemiBold.ttf")
            textSize = 14f
            inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        }

        this.setOnClickListener {
            if (tagsLinearLayoutList.size < maxCount) {
                editText!!.isFocusableInTouchMode = true
                editText!!.requestFocus()

                val inputMethodManager = mContext!!
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)

                editText!!.isActivated = true
                editText!!.isPressed = true
                editText!!.isCursorVisible = true
            }
        }

        this.addView(editText)

        editText!!.setOnKeyListener { _, keyCode, event ->
            if (editText!!.text.toString().length > 1
                && keyCode == 66 && event.action == KeyEvent.ACTION_UP
            ) {
                val tags = arrayOf(editText!!.text.toString())
                createTag(tags)
                editText!!.setText("")
            }
            false
        }

        editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                Log.d("beforeTextChanged", "$count|$s")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (editText!!.text.toString().isEmpty()) {
                    try {
                        this@TagsEditText.removeView(tagsLinearLayoutList[tagsLinearLayoutList.size - 1])
                        editText!!.setText(" " + (tagsLinearLayoutList[tagsLinearLayoutList.size - 1].getChildAt(0) as TextView).text)
                        editText!!.setSelection(editText!!.text.length)
                        tagsLinearLayoutList.removeAt(tagsLinearLayoutList.size - 1)
                        tagsList.removeAt(tagsList.size - 1)
                        if (tagsListener != null)
                            tagsListener!!.onTagRemoved(tagsList.size - 1)
                        if (tagsLinearLayoutList.size < maxCount) {
                            editText!!.isEnabled = true
                            editText!!.requestFocus()
                        }
                    } catch (ignored: Exception) {
                    }
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            override fun afterTextChanged(s: Editable) {
                if (editText!!.text.toString().contains(",")) {
                    val tags =
                        editText!!.text.toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (tags.isNotEmpty()) {
                        createTag(tags)
                    }
                }
            }
        })
    }

    private fun getPX(dip: Float): Int {
        val r = resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            r.displayMetrics
        ).toInt()
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun createTag(tags: Array<String>) {
        for (txt in tags) {

            val tagContainer = LinearLayout(mContext)

            val crossBtn = ImageView(mContext)

            crossBtn.apply {
                setImageResource(R.drawable.ic_clear)
                setPadding(getPX(10f), 0, getPX(10f), 0)
            }

            val tagText = TextView(mContext)
            tagText.apply {
                text = txt.capitalize().trim { it <= ' ' }
                //setTextColor(context.resources.getColor(R.color.formHeadingsBlack))
                gravity = Gravity.CENTER
                //typeface = Typeface.createFromAsset(context.assets, "fonts/OpenSans-SemiBold.ttf")
                layoutParams = FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
                )
                setPadding(getPX(10f), 0, 0, 0)
            }

            val lp = FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
                )
            )
            lp.setMargins(getPX(0f), getPX(6f), getPX(6f), getPX(6f))
            val pad = getPX(4f)

            tagContainer.apply {
                addView(tagText)
                addView(crossBtn)
                setBackgroundResource(R.drawable.tag_bg)
                layoutParams = lp
                setPadding(pad, pad, pad, pad)
                gravity = Gravity.CENTER
            }

            crossBtn.setOnClickListener {
                this@TagsEditText.removeView(tagContainer)
                tagsList.remove(txt)
                tagsLinearLayoutList.remove(tagContainer)
                if (tagsListener != null)
                    this.tagsListener!!.onTagRemoved(tagsLinearLayoutList.indexOf(tagContainer))
                if (tagsLinearLayoutList.size < maxCount) {
                    editText!!.isEnabled = true
                    editText!!.requestFocus()
                }
            }

            //Adding tag to the view
            this.addView(tagContainer, tagsLinearLayoutList.indexOf(tagContainer))
            tagsLinearLayoutList.add(tagContainer)
            tagsList.add(txt)

            //Calling onTagCreated
            if (tagsListener != null)
                tagsListener!!.onTagCreated(txt)

            //Checking maxCount condition
            if (tagsLinearLayoutList.size >= maxCount) {
                editText!!.isEnabled = false
                break
            }
        }

        editText!!.apply {
            setText(" ")
            setSelection(editText!!.text.length)
        }
    }

    fun setMaxTagCount(maxCount: Int) {
        this.maxCount = maxCount
        editText!!.isEnabled = tagsLinearLayoutList.size < maxCount
    }

    fun setOnTagChangeListener(tagsListener: TagsListener) {
        this.tagsListener = tagsListener
    }

    fun removeOnTagChangeListener() {
        this.tagsListener = null
    }

    fun getTags(): ArrayList<String> {
        return tagsList
    }

    fun getTagsCount(): Int {
        return tagsList.size
    }

}