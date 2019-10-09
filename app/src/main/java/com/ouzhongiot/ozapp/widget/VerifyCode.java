package com.ouzhongiot.ozapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ouzhongiot.ozapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/3/15.
 */

public class VerifyCode extends LinearLayout implements TextWatcher,View.OnKeyListener {
    //验证码的位数
    private int codeNumber;
    //两个验证码时间的距离
    private int codeSpace;
    //验证码边框的边长
    private int lengthSide;
    //验证码的大小
    private float textSize;
    //字体颜色
    private int textColor = Color.BLACK;
    private int inputType = 2;
    private LinearLayout.LayoutParams mEditparams;
    private LinearLayout.LayoutParams mViewparams;
    private Context mContext;
    private List<EditText> mEditTextList = new ArrayList<>();
    private int currentPosition = 0;

    public VerifyCode(Context context) {
        super(context);
    }

    public VerifyCode(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerifyCode);
        codeNumber = typedArray.getInteger(R.styleable.VerifyCode_codeNumber, 6);
        codeSpace = typedArray.getInteger(R.styleable.VerifyCode_codeSpace, 20);
        lengthSide = typedArray.getInteger(R.styleable.VerifyCode_lengthSide, 50);
        textSize = typedArray.getFloat(R.styleable.VerifyCode_textSize, 20);
        inputType = typedArray.getInteger(R.styleable.VerifyCode_inputType, 2);
        mEditparams = new LinearLayout.LayoutParams(lengthSide, lengthSide);
        mViewparams = new LinearLayout.LayoutParams(codeSpace, lengthSide);
        initView();


    }

    private void initView() {
        for (int i = 0; i < codeNumber; i++) {
            EditText editText = new EditText(mContext);
            editText.setCursorVisible(true);
            editText.setOnKeyListener(this);
            editText.setTextSize(textSize);
            editText.setInputType(inputType);
            editText.setTextColor(textColor);
            editText.setPadding(0, 0, 0, 0);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            editText.addTextChangedListener(this);
            editText.setLayoutParams(mEditparams);
            editText.setGravity(Gravity.CENTER);
            editText.setBackgroundResource(R.drawable.shape_message_code_orange_stroke);
            addView(editText);
            mEditTextList.add(editText);
            if (i != codeNumber - 1) {
                View view = new View(mContext);
                view.setLayoutParams(mViewparams);
                addView(view);
            }
            InputMethodManager inputManager = (InputMethodManager) editText
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, 0);
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (start == 0 && count >= 1 && currentPosition != mEditTextList.size() - 1){
            currentPosition++;
            mEditTextList.get(currentPosition).requestFocus();
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        EditText editText = (EditText) v;
        if (keyCode == KeyEvent.KEYCODE_DEL && editText.getText().length() == 0){
            int action = event.getAction();
            if (currentPosition!=0 && action == KeyEvent.ACTION_DOWN){
                currentPosition--;
                mEditTextList.get(currentPosition).requestFocus();
                mEditTextList.get(currentPosition).setText("");
            }


        }
        return false;
    }
    public String getVerifyCode(){
        StringBuilder stringBuilder = new StringBuilder();
        for (EditText editText:mEditTextList){
            stringBuilder.append(editText.getText().toString());
        }
        return stringBuilder.toString();
    }
}
