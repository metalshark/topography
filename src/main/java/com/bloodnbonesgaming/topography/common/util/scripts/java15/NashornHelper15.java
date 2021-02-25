package com.bloodnbonesgaming.topography.common.util.scripts.java15;

import javax.script.ScriptEngine;

import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import com.bloodnbonesgaming.topography.common.util.scripts.NashornHelperBase;

public class NashornHelper15 extends NashornHelperBase {
	
	@Override
	public ScriptEngine getScriptEngine() {
		final NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
		return factory.getScriptEngine();
	}
}
