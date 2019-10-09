package com.ouzhongiot.ozapp.esptouch.protocol;

import android.util.Log;

import com.ouzhongiot.ozapp.esptouch.task.ICodeData;
import com.ouzhongiot.ozapp.esptouch.util.ByteUtil;

public class GuideCode implements ICodeData {

	public static final int GUIDE_CODE_LEN = 10;

	@Override
	public byte[] getBytes() {
		throw new RuntimeException("DataCode don't support getBytes()");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		char[] dataU8s = getU8s();
		for (int i = 0; i < GUIDE_CODE_LEN; i++) {
			String hexString = ByteUtil.convertU8ToHexString(dataU8s[i]);
			sb.append("0x");
			if (hexString.length() == 1) {
				sb.append("0");
			}
			sb.append(hexString).append(" ");
			Log.wtf("这个是打印的String z在GuideCodez中",hexString);
		}
		return sb.toString();
	}

	@Override
	public char[] getU8s() {
		char[] guidesU8s = new char[GUIDE_CODE_LEN ];
		guidesU8s[0] = 515;
		guidesU8s[1] = 514;
		guidesU8s[2] = 513;
		guidesU8s[3] = 512;
//		guidesU8s[0] = 'H';
//		guidesU8s[1] = 'M';
//		guidesU8s[2] = 'C';
//		guidesU8s[3] = 'O';
//		guidesU8s[4] = 'L';
//		guidesU8s[5] = 'D';
//		guidesU8s[6] = 'F';
//		guidesU8s[7] = 'A';
//		guidesU8s[8] = 'N';
//		guidesU8s[9] = 'A';


		return guidesU8s;
	}
}
