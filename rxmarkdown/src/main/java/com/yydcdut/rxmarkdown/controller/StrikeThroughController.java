package com.yydcdut.rxmarkdown.controller;

import android.text.Editable;
import android.widget.Toast;

import com.yydcdut.rxmarkdown.RxMDConfiguration;
import com.yydcdut.rxmarkdown.RxMDEditText;

/**
 * Created by yuyidong on 16/7/13.
 */

public class StrikeThroughController {
    private RxMDEditText mRxMDEditText;
    private RxMDConfiguration mRxMDConfiguration;

    public StrikeThroughController(RxMDEditText rxMDEditText, RxMDConfiguration rxMDConfiguration) {
        mRxMDEditText = rxMDEditText;
        mRxMDConfiguration = rxMDConfiguration;
    }

    public void doStrikeThrough() {
        int start = mRxMDEditText.getSelectionStart();
        int end = mRxMDEditText.getSelectionEnd();
        if (start == end) {
            mRxMDEditText.getText().insert(start, "~~~~");
            mRxMDEditText.setSelection(start + 2, end + 2);
        } else if (end - start > 4) {//选中了4个以上
            int position0 = Utils.findBeforeNewLineChar(mRxMDEditText.getText(), start) + 1;
            int position00 = Utils.findBeforeNewLineChar(mRxMDEditText.getText(), end) + 1;
            if (position0 != position00) {
                Toast.makeText(mRxMDEditText.getContext(), "无法操作多行", Toast.LENGTH_SHORT).show();
                return;
            }
            Editable editable = mRxMDEditText.getText();
            if ("~~".equals(editable.subSequence(start, start + "~~".length()).toString()) &&
                    "~~".equals(editable.subSequence(end - "~~".length(), end).toString())) {
                mRxMDEditText.getText().delete(end - "~~".length(), end);
                mRxMDEditText.getText().delete(start, start + "~~".length());
                mRxMDEditText.setSelection(start, end - "~~".length() * 2);
            } else {
                mRxMDEditText.getText().insert(end, "~~");
                mRxMDEditText.getText().insert(start, "~~");
                mRxMDEditText.setSelection(start, end + "~~".length() * 2);
            }
        } else {
            mRxMDEditText.getText().insert(end, "~~");
            mRxMDEditText.getText().insert(start, "~~");
            mRxMDEditText.setSelection(start, end + "~~".length() * 2);
        }
    }
}
