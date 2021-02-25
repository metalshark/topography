package com.bloodnbonesgaming.topography.common.util.scripts.java8;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.bloodnbonesgaming.topography.common.util.scripts.NashornHelperBase;

public class NashornHelper8 extends NashornHelperBase {

	@Override
	public ScriptEngine getScriptEngine() {
		final ScriptEngineManager factory = new ScriptEngineManager(null);
		return factory.getEngineByName("nashorn");
	}

}
