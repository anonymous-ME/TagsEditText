package affan.ahmad.tagsedittext


import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
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



class TagTextView : FlexboxLayout {


    private var edit_txt: EditText? = null
    private var mContext: Context? = null
    private var max_count = 4

    private val tags_list = ArrayList<LinearLayout>()

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


        this.setFlexWrap(WRAP)
        this.justifyContent = JustifyContent.FLEX_START
        this.setBackgroundResource(R.drawable.bg)
        this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)


        edit_txt = EditText(context)
        edit_txt!!.textDirection = View.TEXT_DIRECTION_LTR
        edit_txt!!.setBackgroundResource(0)
        edit_txt!!.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        edit_txt!!.minWidth = 10
        edit_txt!!.gravity = Gravity.LEFT

        this.setOnClickListener {
            if (tags_list.size >= max_count) {


            } else {
                edit_txt!!.isFocusableInTouchMode = true
                edit_txt!!.requestFocus()

                val inputMethodManager = mContext!!
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(edit_txt, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        this.addView(edit_txt)

        edit_txt!!.setOnKeyListener { v, keyCode, event ->
            if (edit_txt!!.text.toString().length > 1
                && keyCode == 66 && event.action == KeyEvent.ACTION_UP
            ) {
                val tags = arrayOf(edit_txt!!.text.toString())
                createTag(tags)
                edit_txt!!.setText("")
            }

            if (edit_txt!!.text.toString().isEmpty() && tags_list.size > 0
                && keyCode == 67 && event.action == KeyEvent.ACTION_UP
            ) {
                this@TagTextView.removeView(tags_list[tags_list.size - 1])
                edit_txt!!.setText((tags_list[tags_list.size - 1].getChildAt(0) as TextView).text)
                edit_txt!!.setSelection(edit_txt!!.text.length)
                tags_list.removeAt(tags_list.size - 1)
            }
            false
        }

        edit_txt!!.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

        edit_txt!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            override fun afterTextChanged(s: Editable) {
                if (edit_txt!!.text.toString().contains(",")) {
                    val tags =
                        edit_txt!!.text.toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (tags.isNotEmpty()) {
                        createTag(tags)
                        edit_txt!!.setText("")
                    }
                }
            }
        })
    }

    private fun getPX(dip: Float): Int {
        val r = getResources()
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            r.getDisplayMetrics()
        ).toInt()
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun createTag(tags: Array<String>) {
        for (txt in tags) {

            val tag_container = LinearLayout(mContext)

            tag_container.setBackgroundResource(R.drawable.ic_clear)

            val cross_btn = ImageView(mContext)
            cross_btn.setImageResource(R.drawable.ic_clear)
            cross_btn.setPadding(getPX(16f), 0, getPX(16f), 0)
            val tag_text = TextView(mContext)
            //tag_text.setTextSize(20);

            tag_text.text = txt
            tag_text.gravity = Gravity.CENTER
            tag_text.setTextColor(Color.BLACK)
            tag_text.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            tag_text.setPadding(getPX(10f), 0, 0, 0)

            tag_container.addView(tag_text)
            tag_container.addView(cross_btn)
            val layoutParams = LayoutParams(LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
            layoutParams.setMargins(getPX(8f), getPX(8f), getPX(8f), getPX(8f))
            tag_container.layoutParams = layoutParams


            val pad = getPX(4f)
            tag_container.setPadding(pad, pad, pad, pad)
            tag_container.gravity = Gravity.CENTER

            cross_btn.setOnClickListener {
                this@TagTextView.removeView(tag_container)
                tags_list.remove(tag_container)
                if (tags_list.size < max_count)
                    edit_txt!!.isEnabled = true
            }

            tags_list.add(tag_container)

            this.addView(tag_container, tags_list.indexOf(tag_container))

            if (tags_list.size >= max_count) {
                edit_txt!!.isEnabled = false
                break
            }


        }
    }

    fun setMax_count(max_count: Int) {
        this.max_count = max_count
    }
}
