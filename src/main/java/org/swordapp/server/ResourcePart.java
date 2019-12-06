package org.swordapp.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ResourcePart {
    protected String uri;
    protected String mediaType;
    protected Map<String, String> properties = new HashMap<String, String>();
    protected List<String> selfLinks = new ArrayList<String>();

    public ResourcePart(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public void addProperty(String predicate, String object) {
        this.properties.put(predicate, object);
    }

    public List<String> getSelfLinks() {
        return selfLinks;
    }

    public void addSelfLink(String link) {
        selfLinks.add(link);
    }

}
