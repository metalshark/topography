package com.bloodnbonesgaming.topography.util;

import com.bloodnbonesgaming.lib.util.JsonHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtil
{
    public static String getJsonStringField(final String json, final String field)
    {
        final JsonElement element = JsonHelper.toJsonTree(json);
        
        if (element != null && element.isJsonObject())
        {
            final JsonObject object = (JsonObject) element;
            
            if (object.has(field))
            {
                return object.get(field).getAsString();
            }
        }
        return null;
    }
}
