package com.zwstudio.logicgamesandroid.bridges.domain;

import com.zwstudio.logicgamesandroid.logicgames.domain.HintState;

/**
 * Created by zwvista on 2016/09/29.
 */

public class BridgesIslandObject extends BridgesObject {
    public HintState state = HintState.Normal;
    public Integer[] bridges = {0, 0, 0, 0};
}
