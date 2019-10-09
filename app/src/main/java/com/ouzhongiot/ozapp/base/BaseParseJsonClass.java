package com.ouzhongiot.ozapp.base;

import android.content.Context;

public abstract class BaseParseJsonClass {
    public abstract void parseJson(Context context, String content);

    public abstract void finishRequest();

}
