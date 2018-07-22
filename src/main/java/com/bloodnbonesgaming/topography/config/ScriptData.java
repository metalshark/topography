package com.bloodnbonesgaming.topography.config;

public class ScriptData
{
    private final String script;
    private final String type;
    
    public ScriptData(final String script, final String type)
    {
        this.script = script;
        this.type = type;
    }

    public String getScript()
    {
        return script;
    }

    public String getType()
    {
        return type;
    }
}
